package com.lss.l101springsecuritydb.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
