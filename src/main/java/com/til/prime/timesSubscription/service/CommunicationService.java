package com.til.prime.timesSubscription.service;

import java.io.File;
import java.util.Collection;

public interface CommunicationService {
    void sendMail(String subject, String body, Collection<String> toAddresses);
    void sendMail(String subject, String body, Collection<String> toAddresses, String attachmentFileName, File file);
    void sendMail(String subject, String body, Collection<String> toAddresses, String replyToEmail);
    void sendHtmlMail(String subject, String body, Collection<String> toAddresses);
    void sendHtmlMail(String subject, String body, Collection<String> toAddresses, Collection<String> bccAddresses);
}
