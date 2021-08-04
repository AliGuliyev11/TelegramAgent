package com.mycode.telegramagent.services.email;

import com.mycode.telegramagent.dto.MailDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.TemplateEngine;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Service class for sending message
 */

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender emailSender;

    @Value("${email.username}")
    String emailUsername;

    Configuration templateEngine;

    public EmailServiceImpl(JavaMailSender emailSender, Configuration templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendSimpleMessage(MailDTO mail) {
        SimpleMailMessage message = new SimpleMailMessage();

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
                mimeMessage.setFrom(new InternetAddress(emailUsername));
                mimeMessage.setSubject(mail.getSubject());

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


                Template template=templateEngine.getTemplate(mail.getTemplateName());
                String html=FreeMarkerTemplateUtils.processTemplateIntoString(template,mail);


                helper.setText(html, true);

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
