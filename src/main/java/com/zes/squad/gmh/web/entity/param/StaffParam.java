package com.zes.squad.gmh.web.entity.param;

import lombok.Data;

@Data
public class StaffParam {

	private Long   id;

    private String email;

    private String password;

    private String salt;

}
