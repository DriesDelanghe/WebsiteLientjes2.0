package be.thomasmore.be.websitelientjes.controllers;

import be.thomasmore.be.websitelientjes.models.ContactForm;
import be.thomasmore.be.websitelientjes.models.RedirectEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class JavaMailSender {

    Logger logger = LoggerFactory.getLogger(JavaMailSender.class);

    public JavaMailSender() {
    }

    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("out.nameweb.biz");
        mailSender.setPort(2525);

        mailSender.setUsername("info@lientjes.be");
        mailSender.setPassword("L13ntjes@2021");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    @Async
    public void sendmail(List<RedirectEmail> emails, ContactForm contactForm) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "out" +
                ".nameweb.biz");
        props.put("mail.smtp.port", "2525");
        logger.info("Setting up mail client properties");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("info@lientjes.be", "L13ntjes@2021");
                    }
                });
        logger.info("setting up session");

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("info@lientjes.be", false));

        logger.info("instatiating email, setting from");

        String recipients = "";
        for (RedirectEmail mail : emails) {
            if (emails.indexOf(mail) != 0) {
                recipients += ", ";
            }
            recipients += mail.getEmail();
        }
        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(recipients));
        logger.info("setting recipients, recipients found: " + recipients);

        msg.setSubject("nieuw bericht op lientjes");

        String HtmlTemplate = "<h1>Nieuw bericht ontvangen</h1>" +
                "<p>Er is een nieuw bericht binnengekomen op lientjes van het type " + contactForm.getContactType().getQuestionType() + "</p>" +
                "<p>Klik <a href=\"www.lientjes.be/admin/inbox/message/" + contactForm.getId() + "\" target=\"_blank\">hier</a> om het bericht te bekijken </p>";

        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(HtmlTemplate, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        msg.setContent(multipart);
        logger.info("message contructed, now sending...");
        Transport.send(msg);
        logger.info("Message send!");
    }
}
