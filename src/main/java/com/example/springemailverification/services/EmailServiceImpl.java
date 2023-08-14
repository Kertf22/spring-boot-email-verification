package com.example.springemailverification.services;

import com.example.springemailverification.util.EmailUtils;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

import com.example.springemailverification.util.EmailUtils;
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String EMAIL_VERIFICATION = "Email Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "email";
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    @Override
    @Async
    public void sendSimpleMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(EMAIL_VERIFICATION);
            message.setFrom(fromEmail);

            message.setTo(to);
            message.setText(EmailUtils.getEmailMessage(name, host, token));
            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttchaments(String name, String to, String token) {
        try {
            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

            helper.setPriority(1);
            helper.setSubject(EMAIL_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, host, token));

            FileSystemResource resume = new FileSystemResource(new File("attchaments/resume.pdf"));
            helper.addAttachment(resume.getFilename(), resume);

            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

            helper.setPriority(1);
            helper.setSubject(EMAIL_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(EmailUtils.getEmailMessage(name, host, token));

            FileSystemResource profile_photo = new FileSystemResource(new File("attchaments/profile.jpg"));
//            FileSystemResource resume = new FileSystemResource(new File("attchaments/resume.pdf"));


            helper.addInline(getContentId(profile_photo.getFilename()), profile_photo);
//            helper.addInline(getContentId(resume.getFilename()), resume);

            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Async
    public void sendHTMLEmail(String name, String to, String token) {
        try {
            // Add HTMl content
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", EmailUtils.getVerificationUrl(host, token));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

            helper.setPriority(1);
            helper.setSubject(EMAIL_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);

            helper.setText(text,true);
            FileSystemResource resume = new FileSystemResource(new File("attchaments/resume.pdf"));
            helper.addAttachment(resume.getFilename(), resume);

            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Async
    public void sendHTMLEmailWithEmbeddedFiles(String name, String to, String token) {
        try {

            MimeMessage message = getMineMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

            helper.setPriority(1);
            helper.setSubject(EMAIL_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            FileSystemResource resume = new FileSystemResource(new File("attchaments/resume.pdf"));
            helper.addAttachment(resume.getFilename(), resume);

            // Add HTMl content
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", EmailUtils.getVerificationUrl(host, token));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Add Image to body
            messageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(new File("attchaments/profile.jpg"));
            messageBodyPart.setDataHandler(new jakarta.activation.DataHandler(dataSource));
            messageBodyPart.setHeader("Content-ID","image");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private MimeMessage getMineMessage() {
        return emailSender.createMimeMessage();
    }
    private String getContentId(String name) {
        return "<" + name + ">";
    }
}
