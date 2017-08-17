package com.zes.squad.gmh.web.mail;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.collections.MapUtils;

import com.zes.squad.gmh.web.property.MailProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailHelper {

    private static final String HOST     = "mail.smtp.host";
    private static final String SENDER   = "mail.smtp.sender";
    private static final String PASSWORD = "mail.smtp.password";
    private static final String AUTH     = "mail.smtp.auth";

    public static void sendTextEmail(MailParams params) {
        Message msg = new MimeMessage(getEmailSession(params.getProps()));
        try {
            InternetAddress sender = new InternetAddress(MailProperties.get(SENDER));
            msg.setFrom(sender);
            InternetAddress[] recipientsTO = generateAddress(params.getReceiversTO());
            msg.setRecipients(RecipientType.TO, recipientsTO);
            if (params.getReceiversCC() != null && params.getReceiversCC().length != 0) {
                InternetAddress[] recipientsCC = generateAddress(params.getReceiversCC());
                msg.setRecipients(RecipientType.CC, recipientsCC);
            }
            if (params.getReceiversBCC() != null && params.getReceiversBCC().length != 0) {
                InternetAddress[] recipientsBCC = generateAddress(params.getReceiversBCC());
                msg.setRecipients(RecipientType.BCC, recipientsBCC);
            }
            if (params.getReplys() != null && params.getReplys().length != 0) {
                InternetAddress[] replysTo = generateAddress(params.getReplys());
                msg.setReplyTo(replysTo);
            }
            msg.setSentDate(new Date());
            msg.setSubject(params.getSubject());
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(params.getContent(), params.getContentType());
            multipart.addBodyPart(bodyPart);
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (MessagingException e) {
            log.error("发送邮件失败, exception is", e);
        }
    }

    private static Session getEmailSession(Map<String, Object> props) {
        Properties properties = System.getProperties();
        properties.put(HOST, MailProperties.get(HOST));
//        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.port", 465);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put(AUTH, "true");
        if (!MapUtils.isEmpty(props)) {
            for (Map.Entry<String, Object> prop : props.entrySet()) {
                properties.put(prop.getKey(), prop.getValue());
            }
        }

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailProperties.get(SENDER), MailProperties.get(PASSWORD));
            }
        });
        session.setDebug(true);
        return session;
    }

    private static InternetAddress[] generateAddress(String[] inputs) throws AddressException {
        InternetAddress[] results = new InternetAddress[inputs.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = new InternetAddress(inputs[i]);
        }
        return results;
    }

}
