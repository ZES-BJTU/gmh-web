package com.zes.squad.gmh.web.entity.vo;

import lombok.Data;

@Data
public class StaffVo {

    private Long   id;

    private String email;

    private String password;

    private String salt;

}
