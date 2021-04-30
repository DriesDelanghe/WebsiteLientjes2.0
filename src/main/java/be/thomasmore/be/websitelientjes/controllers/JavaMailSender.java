package be.thomasmore.be.websitelientjes.controllers;

import be.thomasmore.be.websitelientjes.models.ContactForm;
import be.thomasmore.be.websitelientjes.models.RedirectEmail;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class JavaMailSender {


    public JavaMailSender() {
    }

    @Bean
    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("out.nameweb.biz");
        mailSender.setPort(465);

        mailSender.setUsername("my.gmail@gmail.com");
        mailSender.setPassword("password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendmail(List<RedirectEmail> emails, ContactForm contactForm) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "out.nameweb.biz");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("info@lientjes.be", "L13ntjes@2021");
                    }
                });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("info@lientjes.be", false));

        String recipients = "";
        for (RedirectEmail mail : emails) {
            if (emails.indexOf(mail) != 0) {
                recipients += ", ";
            }
            recipients += mail.getEmail();
        }
        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(recipients));
        msg.setSubject("nieuw bericht op lientjes");

        msg.setContent("this is a test mail", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("this is also a part of the test mail", "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        msg.setContent(multipart);
        Transport.send(msg);

    }
}
