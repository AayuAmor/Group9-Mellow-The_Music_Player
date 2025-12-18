package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
    private final Properties props = new Properties();

    public EmailService() {
        loadConfig();
    }

    private void loadConfig() {
        // Defaults
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.from", "no-reply@example.com");
        props.put("mail.user", System.getenv().getOrDefault("SMTP_USERNAME", ""));
        props.put("mail.password", System.getenv().getOrDefault("SMTP_PASSWORD", ""));

        try (InputStream in = getClass().getResourceAsStream("/Config/email.properties")) {
            if (in != null) {
                Properties fileProps = new Properties();
                fileProps.load(in);
                fileProps.forEach((k, v) -> props.put(String.valueOf(k), String.valueOf(v)));
            }
        } catch (IOException ignored) {}
    }

    public boolean sendOtp(String toEmail, String otp) {
        final String username = props.getProperty("mail.user", "");
        final String password = props.getProperty("mail.password", "");
        final String from = props.getProperty("mail.from", username);

        Properties sessionProps = new Properties();
        sessionProps.put("mail.smtp.auth", props.getProperty("mail.smtp.auth", "true"));
        sessionProps.put("mail.smtp.starttls.enable", props.getProperty("mail.smtp.starttls.enable", "true"));
        sessionProps.put("mail.smtp.host", props.getProperty("mail.smtp.host", "smtp.gmail.com"));
        sessionProps.put("mail.smtp.port", props.getProperty("mail.smtp.port", "587"));

        Session session = Session.getInstance(sessionProps, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your Mellow OTP Code");
            message.setText("Your OTP code is: " + otp + "\nThis code will expire in 10 minutes.");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println(e);
            return false;
        }
    }
}
