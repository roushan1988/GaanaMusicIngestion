package com.til.prime.timesSubscription.service;

public interface MailSender {
    void sendMail(String subject, String text, String[] toEmailIds, String[] ccIds, String[] attachFiles);
}
