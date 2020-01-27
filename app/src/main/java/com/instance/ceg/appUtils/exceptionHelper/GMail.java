package com.instance.ceg.appUtils.exceptionHelper;

import com.instance.ceg.appUtils.AppHelper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GMail {

    private final int emailPort = 587; //gmail's smtp port
    private final String smtpAuth = "true";
    private final String starttls = "true";
    private final String emailHost = "smtp.gmail.com";

    private String fromEmail;
    private String fromPassword;
    private List<String> toEmailList;
    private String emailSubject;
    private String emailBody;

    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    public GMail() {

    }

    GMail(String fromEmail, String fromPassword,
          List<String> toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", String.valueOf(emailPort));
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.host", emailHost);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        AppHelper.printLog("Mail server properties set.");
    }

    void createEmailMessage() throws
            MessagingException, UnsupportedEncodingException {

//        Authenticator authenticator = new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(fromEmail, fromPassword);
//            }
//        };

        mailSession = Session.getDefaultInstance(emailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromPassword));
        for (String toEmail : toEmailList) {
            AppHelper.printLog("GMail - toEmail: " + toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        AppHelper.printLog("GMail - Email Message created.");
    }

    void sendEmail() throws MessagingException {
        Transport transport = mailSession.getTransport("smtp");
        try {
            transport.connect(emailHost, emailPort, fromEmail, fromPassword);
            AppHelper.printLog("GMail - allrecipients: " + Arrays.toString(emailMessage.getAllRecipients()));
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            AppHelper.printLog("GMail - Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
