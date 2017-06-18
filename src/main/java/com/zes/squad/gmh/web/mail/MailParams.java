package com.zes.squad.gmh.web.mail;

import java.util.Map;

import lombok.Data;

@Data
public class MailParams {

    private Map<String, Object> props;
    private String[]            receiversTO;
    private String[]            receiversCC;
    private String[]            receiversBCC;
    private String[]            replys;
    private String              subject;
    private String              content;
    private String              contentType;

}
