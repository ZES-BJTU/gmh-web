package com.zes.squad.gmh.web.mail;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
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
import javax.mail.internet.MimeUtility;

import org.apache.commons.collections.MapUtils;

import com.zes.squad.gmh.web.property.MailProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailHelper {
    public static void sendAttachmentEmail(MailParams params, File[] files) throws IOException {
        Message msg = new MimeMessage(getEmailSession(params.getProps()));
        try {
            InternetAddress sender = new InternetAddress(MailProperties.get("mail.smtp.sender"));
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
            //处理附件
            multipart.addBodyPart(bodyPart);
            MimeBodyPart attachBodyPart = null;
            if (files != null) {
                for (File file : files) {
                    attachBodyPart = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(file);
                    attachBodyPart.setDataHandler(new DataHandler(fds));
                    attachBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
                    multipart.addBodyPart(attachBodyPart);
                }
            }
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (MessagingException e) {
            log.error("构建邮件message出错", e);
        } catch (UnsupportedEncodingException e) {
            log.error("构建邮件编码出错", e);
        }
    }

    public static void sendTextEmail(MailParams params) {
        Message msg = new MimeMessage(getEmailSession(params.getProps()));
        try {
            InternetAddress sender = new InternetAddress(MailProperties.get("mail.smtp.sender"));
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
            e.printStackTrace();
        }
    }

    private static Session getEmailSession(Map<String, Object> props) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", MailProperties.get("mail.smtp.host"));
        properties.put("mail.smtp.auth", "true");
        if (!MapUtils.isEmpty(props)) {
            for (Map.Entry<String, Object> prop : props.entrySet()) {
                properties.put(prop.getKey(), prop.getValue());
            }
        }

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailProperties.get("mail.smtp.sender"),
                        MailProperties.get("mail.smtp.password"));
            }
        });
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
