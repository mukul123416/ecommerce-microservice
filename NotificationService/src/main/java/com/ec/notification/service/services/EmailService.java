package com.ec.notification.service.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
public class EmailService {

    public boolean sendEmail(String subject,String message,String to){

        boolean f = false;

        String from="ms2224850@gmail.com";

        log.info("Preparing to send email to: {}", to);

        String host="smtp.gmail.com";

        Properties properties = System.getProperties();
        System.out.println("PROPERTIES "+properties);

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ms2224850@gmail.com", "dyoi tusu ofwh sabp");
            }

        });

        session.setDebug(true);

        MimeMessage m = new MimeMessage(session);

        try {

            m.setFrom(from);

            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            m.setSubject(subject);

            m.setContent(message,"text/html");

            Transport.send(m);

            log.info("Email sent successfully to {}", to);
            
            f=true;

        }catch (Exception e) {
            log.error("Error occurred while sending email to {}: {}", to, e.getMessage());
        }
        return f;

    }

}
