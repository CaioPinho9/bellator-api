package com.github.CaioPinho9.bellatorapi.email;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    /**
     * <p>String <strong>from</strong> email that will send</p>
     * <p>String <strong>password</strong> to login the email</p>
     */
    @Value("${spring.mail.username}")
    String from;
    @Value("${spring.mail.password}")
    String password;

    /**
     * <p>Properties needed to send email</p>
     * @param email Who will receive the email
     * @param content Content of the email
     */
    @Override
    @Async
    public void send(String email, String content) {

        String subject = "Confirm your email";

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getDefaultInstance(props,
        new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,password);
            }
        });

        try {
            Transport transport = session.getTransport();
            InternetAddress addressFrom = new InternetAddress(from);

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(addressFrom);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(content, true);
            helper.setTo(email);
            helper.setSubject(subject);

            transport.connect();
            Transport.send(mimeMessage);
            transport.close();
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }
}
