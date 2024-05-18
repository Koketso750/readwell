package com.webapp.user;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendEmail(String to, String subject, String text) throws IOException {
        Email from = new Email("readwell-library@outlook.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public void sendEmailWithLogo(String to, String subject, String htmlContent) throws IOException, URISyntaxException {
        Email from = new Email("readwell-library@outlook.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        // Get the path to the logo image in the static folder
        String logoPath = "/static/images/logo1.png";

        // Read the logo image file
        byte[] logoBytes = Files.readAllBytes(Paths.get(getClass().getResource(logoPath).toURI()));

        // Encode the logo image bytes to base64
        String logoBase64 = Base64.getEncoder().encodeToString(logoBytes);

        // Attach the logo image
        Attachments attachments = new Attachments();
        attachments.setFilename("logo1.png");
        attachments.setType("image/png");
        attachments.setDisposition("inline");
        attachments.setContentId("logo");
        attachments.setContent(logoBase64);
        mail.addAttachments(attachments);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }

}
