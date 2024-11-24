import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;
import java.util.Random;
import io.github.cdimascio.dotenv.Dotenv;

public class OTPMailer {
    public void sendOTP(String recipientEmail, String otp) {
        final Dotenv dotenv = Dotenv.load();

        String senderEmail = dotenv.get("EMAIL_ID");
        String senderPassword = dotenv.get("EMAIL_PASSWORD");

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Your OTP for Admin Login");
            message.setText("Your OTP is: " + otp);

            Transport.send(message);
            System.out.println("OTP sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String generateOTP() {
        Random random = new Random();
        int otp = random.nextInt(900000) + 100000; // Generate a 6-digit OTP
        return String.valueOf(otp);
    }
}
