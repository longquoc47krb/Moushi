package id.longquoc.messenger.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.senderEmail}")
    private String senderEmail;
    public void sendEmail(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("Moushi - A Chat App by Lonq");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // Use true to enable HTML content

        mailSender.send(message);
    }
    public void sendRegistrationSuccessEmail(String toEmail, String username) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("Moushi - A Chat App by Lonq");
        helper.setTo(toEmail);
        helper.setSubject("Account Registration Successful");

        // Set content using Thymeleaf template
        Context context = new Context();
        context.setVariable("subject", "Account Registration Successful");
        context.setVariable("username", username);
        context.setVariable("message", "Thank you for registering with our platform!");
        String htmlContent = templateEngine.process("registration-success", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
