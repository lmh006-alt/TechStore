
package vn.iotstar.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailUtil {

    public static void sendOTP(String recipient, String otp)
            throws MessagingException {

        String sender = System.getenv("MAIL_USERNAME");
        String appPassword = System.getenv("MAIL_APP_PASSWORD");

        if (sender == null || appPassword == null) {
            throw new IllegalStateException(
                "Chưa cấu hình Gmail gửi OTP");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication
                        getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        sender, appPassword);
                }
            });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(recipient)
        );
        message.setSubject("Ma OTP xac nhan tai khoan");
        message.setText(
            "Ma OTP cua ban la: " + otp +
            "\nMa co hieu luc trong 5 phut."
        );

        Transport.send(message);
    }
}
