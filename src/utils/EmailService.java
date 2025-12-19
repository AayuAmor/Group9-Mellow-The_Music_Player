/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 *
 * @author Nitro V 16
 */
public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "aayuamor@gmail.com";
    private static final String EMAIL_PASSWORD = "kysv uqhx ogoh qyxg";   
    
    public static boolean sendOTPEmail(String recipientEmail, String otpCode) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        
        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, 
                InternetAddress.parse(recipientEmail));
            message.setSubject("Password Reset OTP - Your App Name");
            
            String emailBody = 
                "<h3>Password Reset Request</h3>" +
                "<p>Your OTP for password reset is: <strong>" + otpCode + "</strong></p>" +
                "<p>This OTP is valid for 10 minutes.</p>" +
                "<p>If you didn't request this, please ignore this email.</p>" +
                "<br><p>Thank you,<br>Your App Team</p>";
            
            message.setContent(emailBody, "text/html");
            
            Transport.send(message);
            System.out.println("OTP email sent to: " + recipientEmail);
            return true;
            
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convenience wrapper for controllers that expect an instance method.
     */
    public boolean sendOtp(String recipientEmail, String otpCode) {
        return sendOTPEmail(recipientEmail, otpCode);
    }
}
