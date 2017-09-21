package com.til.prime.timesSubscription.service.impl;

import com.google.common.collect.ImmutableSet;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.service.CommunicationService;
import com.til.prime.timesSubscription.service.ExecutorService;
import com.til.prime.timesSubscription.util.ExceptionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CommunicationServiceImpl implements CommunicationService {
    protected static final Logger LOG = Logger.getLogger(CommunicationServiceImpl.class);
    @Resource(name = "config_properties")
    private Properties properties;
    @Autowired
    private ExecutorService executorService;
    private JavaMailSender javaMailSender;
    private static final AtomicBoolean init = new AtomicBoolean(false);
    private static final Set<String> defaultBccAddresses = ImmutableSet.of("timesprime.tech@timesinternet.in");
    private final String FROM_EMAIL_ADDRESS;
    private final String DEFAULT_SENDER_NAME;

    @Autowired
    public CommunicationServiceImpl(@Value("${senders.email}") String senderEmail, @Value("${default.sender.name}") String defaultSenderName) {
        this.FROM_EMAIL_ADDRESS=senderEmail;
        this.DEFAULT_SENDER_NAME=defaultSenderName;
    }

    private MailSender getMailSender() {
        if (init.compareAndSet(false, true)) {
            final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setProtocol(properties.getProperty(GlobalConstants.EMAIL_PROTOCOL));
            javaMailSender.setHost(properties.getProperty(GlobalConstants.EMAIL_HOST));
            javaMailSender.setPort(Integer.parseInt(properties.getProperty(GlobalConstants.EMAIL_PORT)));
            javaMailSender.setUsername(properties.getProperty(GlobalConstants.EMAIL_USERNAME));
            javaMailSender.setPassword(properties.getProperty(GlobalConstants.EMAIL_PASSWORD));
            javaMailSender.setDefaultEncoding(GlobalConstants.UTF_8);
            this.javaMailSender = javaMailSender;
        }
        return javaMailSender;
    }

    public void sendMail(final String subject, final String body, final Collection<String> toAddresses, String attachmentName, File file) {
        if (!init.get()) {
            return;
        }
        try {
            executorService.getExecutorService().submit(new Callable<Object>() {
                public Object call() throws Exception {
                    _sendMail(subject, body, toAddresses, StringUtils.EMPTY, false, attachmentName, file);
                    return null;
                }
            });
        } catch (Exception ex) {
            throw ExceptionUtils.wrapInRuntimeExceptionIfNecessary(ex);
        }

    }

    public void sendMail(final String subject, final String body, final Collection<String> toAddresses) {
        sendMail(subject, body, toAddresses, StringUtils.EMPTY, null);
    }

    @Override
    public void sendMail(final String subject, final String body, final Collection<String> toAddresses, final String replyToEmail) {
        try {
            _sendMail(subject, body, toAddresses, replyToEmail, false, StringUtils.EMPTY, null);
        } catch (Exception ex) {
            LOG.error("Exception in sendMail", ex);
            throw ExceptionUtils.wrapInRuntimeExceptionIfNecessary(ex);
        }
    }

    @Override
    public void sendHtmlMail(String subject, String body, Collection<String> toAddresses) {
        try {
            _sendMail(subject, body, toAddresses, defaultBccAddresses, StringUtils.EMPTY, true, StringUtils.EMPTY, null);
        } catch (Exception ex) {
            LOG.error("Exception in sendMail", ex);
            throw ExceptionUtils.wrapInRuntimeExceptionIfNecessary(ex);
        }
    }

    @Override
    public void sendHtmlMail(String subject, String body, Collection<String> toAddresses, Collection<String> bccAddresses) {
        try {
            bccAddresses.addAll(defaultBccAddresses);
            _sendMail(subject, body, toAddresses, bccAddresses, StringUtils.EMPTY, true, StringUtils.EMPTY, null);
        } catch (Exception ex) {
            LOG.error("Exception in sendMail", ex);
            throw ExceptionUtils.wrapInRuntimeExceptionIfNecessary(ex);
        }
    }

//    private void _sendMail(String subject, String body, Collection<String> toAddresses) throws MessagingException, UnsupportedEncodingException {
//        _sendMail(subject, body, toAddresses, StringUtils.EMPTY, false, StringUtils.EMPTY, null);
//    }

    private void _sendMail(String subject, String body, Collection<String> toAddresses, String replyToEmail, boolean html, String attachmentName, File file) throws MessagingException, UnsupportedEncodingException {
        _sendMail(subject, body, toAddresses, Collections.emptyList(), replyToEmail, html, attachmentName, file);
    }

    private void _sendMail(String subject, String body, Collection<String> toAddresses, Collection<String> bccAddresses, String replyToEmail, boolean html, String attachmentName, File file) throws MessagingException, UnsupportedEncodingException {
        if (CollectionUtils.isEmpty(toAddresses)) {
            // No receivers
            return;
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(FROM_EMAIL_ADDRESS, DEFAULT_SENDER_NAME);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, html);
        if (StringUtils.isNotBlank(replyToEmail)) {
            mimeMessageHelper.setReplyTo(replyToEmail);
        }
        if (CollectionUtils.isNotEmpty(toAddresses)) {
            mimeMessageHelper.setTo(toAddresses.toArray(new String[toAddresses.size()]));
        }
        if (CollectionUtils.isNotEmpty(bccAddresses)) {
            mimeMessageHelper.setBcc(bccAddresses.toArray(new String[bccAddresses.size()]));
        }
        if (StringUtils.isNotBlank(attachmentName) && file != null) {
            mimeMessageHelper.addAttachment(attachmentName, file);
        }
        javaMailSender.send(mimeMessageHelper.getMimeMessage());
    }
}
