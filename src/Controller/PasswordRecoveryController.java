package Controller;

import Dao.PasswordResetDao;
import Dao.userDao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import view.AccountRecovery;
import view.OTP_Verification_Interface;
import view.ChangeForgottenPassword_Interface;

public class PasswordRecoveryController {
    private final userDao userDao = new userDao();
    private final PasswordResetDao resetDao = new PasswordResetDao();
    private final EmailService emailService = new EmailService();

    private final AccountRecovery accountRecoveryView;
    private OTP_Verification_Interface otpView;
    private ChangeForgottenPassword_Interface resetView;

    private String targetEmail;

    public PasswordRecoveryController(AccountRecovery accountRecoveryView) {
        this.accountRecoveryView = accountRecoveryView;
        this.accountRecoveryView.addSearchListener(new SearchListener());
    }

    public void open() {
        accountRecoveryView.setVisible(true);
    }

    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = accountRecoveryView.getEmailField().getText().trim();
            if (email.isEmpty() || email.equalsIgnoreCase("Enter Your Email")) {
                JOptionPane.showMessageDialog(accountRecoveryView, "Please enter your email.");
                return;
            }
            if (!userDao.existsByEmail(email)) {
                JOptionPane.showMessageDialog(accountRecoveryView, "No account found with this email.");
                return;
            }
            targetEmail = email;
            String otp = generateOtp();
            Instant expiresAt = Instant.now().plus(10, ChronoUnit.MINUTES);
            boolean saved = resetDao.createToken(email, otp, expiresAt);
            if (!saved) {
                JOptionPane.showMessageDialog(accountRecoveryView, "Could not initiate reset. Please try again.");
                return;
            }
            boolean sent = emailService.sendOtp(email, otp);
            if (!sent) {
                JOptionPane.showMessageDialog(accountRecoveryView, "Failed to send email. Check SMTP config.");
                return;
            }
            JOptionPane.showMessageDialog(accountRecoveryView, "OTP sent to your email. Enter it to continue.");
            openOtpView();
        }
    }

    private void openOtpView() {
        otpView = new OTP_Verification_Interface();
        otpView.addNextListener(new VerifyOtpListener());
        otpView.setVisible(true);
        accountRecoveryView.dispose();
    }

    class VerifyOtpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String code = otpView.getOtp().trim();
            if (code.length() < 4) { // allow 4-6 digits depending on UI filled
                JOptionPane.showMessageDialog(otpView, "Please enter the full OTP code.");
                return;
            }
            boolean ok = resetDao.verifyToken(targetEmail, code);
            if (!ok) {
                JOptionPane.showMessageDialog(otpView, "Invalid or expired OTP. Please try again.");
                return;
            }
            openResetView();
        }
    }

    private void openResetView() {
        resetView = new ChangeForgottenPassword_Interface();
        resetView.addResetListener(new ResetPasswordListener());
        resetView.setVisible(true);
        otpView.dispose();
    }

    class ResetPasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String p1 = resetView.getNewPassword();
            String p2 = resetView.getConfirmPassword();
            if (p1 == null || p2 == null || p1.isEmpty() || p2.isEmpty()) {
                JOptionPane.showMessageDialog(resetView, "Please enter and confirm your new password.");
                return;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(resetView, "Passwords do not match.");
                return;
            }
            if (p1.length() < 6) {
                JOptionPane.showMessageDialog(resetView, "Password must be at least 6 characters.");
                return;
            }
            boolean updated = userDao.updatePasswordByEmail(targetEmail, p1);
            if (!updated) {
                JOptionPane.showMessageDialog(resetView, "Failed to update password. Please try again.");
                return;
            }
            JOptionPane.showMessageDialog(resetView, "Password updated successfully. You can now log in.");
            view.Login loginView = new view.Login();
            new LoginController(loginView).open();
            resetView.dispose();
        }
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // 6 digits
        return String.valueOf(code);
    }
}
