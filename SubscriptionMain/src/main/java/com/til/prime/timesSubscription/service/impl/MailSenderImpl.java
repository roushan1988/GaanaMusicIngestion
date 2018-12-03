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
    public void sendMail(String subject, String text, String[] toEmailIds, String[] attachFiles){
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
            if(toEmailIds!=null && toEmailIds.length>0){
                for(String toEmailId: toEmailIds){
                    internetAddresses.add(new InternetAddress(toEmailId));
                }
            }
            InternetAddress[] array = internetAddresses.toArray(new InternetAddress[internetAddresses.size()]);
            message.addRecipients(Message.RecipientType.TO, array);
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
//    private static final Logger LOGGER = org.apache.log4j.Logger.getLogger(MailSenderImpl.class);
//
//    @Value("${mx.gaana.s3.bucket}")
//    private String s3Bucket;
//
//    @Value("${mx.gaana.excel.path.s3}")
//    private String s3Folder;
//
//    @Value("${mx.gaana.excel.path.local}")
//    private String localFolder;
//
//    @Autowired
//    private S3FileOperations s3FileOperations;
//
//    @Value("${mx.gaana.email.sendTo}")
//    private String sendTo;
//
//    @Value("${mx.gaana.email.subject}")
//    private String subject;
//
//    private static JavaMailSender mailSender = new JavaMailSenderImpl();
//
//    @Override
//    public void sendMail(String filePath) {
//        File file = new File(filePath);
//        if (file != null) {
//            sendMail(file);
//            uploadToS3Bucket(file);
//            file.delete();
//        }
//    }
//
//    public void call(MimeMessage mimeMessage) throws MailException {
//        try {
//            LOGGER.debug("Sending email: " + mimeMessage);
//            mailSender.send(mimeMessage);
//        } catch (MailException e) {
//            LOGGER.error("Error while sending email: " + e);
//            throw e;
//        }
//
//    }
//
//    private void sendMail(File file) {
//        try {
//            MimeMessage message = createMimeMessage(file);
//            call(message);
//            LOGGER.info("Excel sheet report generated is mailed successfully!");
//        } catch (Exception e) {
//            LOGGER.error("Exception", e);
//        }
//    }
//
//    private void uploadToS3Bucket(File file) {
//        String s3ExcelPath = s3FileOperations.uploadFile(file, s3Bucket, s3Folder);
//        LOGGER.info("Excel sheet report generated is uploaded to S3: " + s3ExcelPath);
//    }
//
//    private MimeMessage createMimeMessage(File file) {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper;
//        try {
//            helper = new MimeMessageHelper(mimeMessage, true);
//            helper.setTo(InternetAddress.parse(sendTo));
//            helper.setText(subject);
//            helper.setSubject(subject);
//            helper.addAttachment(file.getName(), file);
//        } catch (MessagingException e) {
//            LOGGER.error("Error while creating email mime message: " ,e);
//            throw new RuntimeException(e);
//        }
//        return mimeMessage;
//    }
}
