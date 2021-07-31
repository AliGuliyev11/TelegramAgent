package com.mycode.telegramagent.services.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Service class for sending message
 */

@Service
public class EmailServiceImpl implements EmailService{

    private JavaMailSender emailSender;

    @Value("${email.username}")
    String emailUsername;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(emailUsername));
                mimeMessage.setSubject(subject);

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setText(text, true);
            }
        };

        emailSender.send(preparator);
    }

    @Override
    public void sendMultipleEmailMessage(String to, List<String> recipientList, String subject, String text) throws MessagingException {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(emailUsername);
                String[] myArray = new String[recipientList.size()];
                recipientList.toArray(myArray);
                helper.setTo(myArray);

                helper.setSubject(subject);
                helper.setText(text, true);
            }
        };


        emailSender.send(preparator);
    }
}
