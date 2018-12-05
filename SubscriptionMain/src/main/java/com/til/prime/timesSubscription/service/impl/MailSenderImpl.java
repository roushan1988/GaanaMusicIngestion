package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.service.MailSender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MailSenderImpl implements MailSender {

    private static final Logger LOG = Logger.getLogger(MailSenderImpl.class);

    @Value("${spring.mail.host}")
    private String SMTP_HOST_NAME;

    @Value("${spring.mail.username}")
    private String SMTP_AUTH_USER;

    @Value("${spring.mail.password}")
    private String SMTP_AUTH_PWD;

    @Override
    public void sendMail(String subject, String text, String[] toEmailIds, String[] ccIds,  String[] attachFiles){
        LOG.info("Subject:"+subject+". text: "+text);

        //Get the session object
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", SMTP_HOST_NAME);
        properties.setProperty("mail.smtp.user", SMTP_AUTH_USER);
        properties.setProperty("mail.smtp.password", SMTP_AUTH_PWD);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                SMTP_AUTH_USER, SMTP_AUTH_PWD);// Specify the Username and the PassWord
                    }
                });
        //compose the message
        try{
            MimeMessage message = new MimeMessage(session);
            List<InternetAddress> internetAddresses = new ArrayList<>();
            List<InternetAddress> ccAddresses = new ArrayList<>();
            if(toEmailIds!=null && toEmailIds.length>0){
                for(String toEmailId: toEmailIds){
                    internetAddresses.add(new InternetAddress(toEmailId));
                }
            }
            if(ccIds!=null && ccIds.length>0){
                for(String ccEmailId: ccIds){
                    ccAddresses.add(new InternetAddress(ccEmailId));
                }
            }
            InternetAddress[] array = internetAddresses.toArray(new InternetAddress[internetAddresses.size()]);
            InternetAddress[] ccArray = ccAddresses.toArray(new InternetAddress[ccAddresses.size()]);
            message.addRecipients(Message.RecipientType.TO, array);
            message.addRecipients(Message.RecipientType.CC, ccArray);
            message.setSubject(subject);
            message.setText(text);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, "text/html");
            multipart.addBodyPart(messageBodyPart);
            if (attachFiles != null && attachFiles.length > 0) {
                for (String filePath : attachFiles) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    try {
                        attachPart.attachFile(filePath);
                    } catch (IOException ex) {
                        LOG.info("Exception", ex);
                    }
                    multipart.addBodyPart(attachPart);
                }
            }
            message.setContent(multipart);
            Transport.send(message);
            LOG.info(new StringBuilder("Mail sent successfully...."));
        }catch (Exception e) {
            LOG.error("Exception while sending mail", e);
        }
    }
}
