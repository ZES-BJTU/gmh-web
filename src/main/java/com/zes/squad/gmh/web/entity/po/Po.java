package com.zes.squad.gmh.web.entity.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 数据库Po对象
 * 
 * @author zhouyuhuan 2017年6月17日 下午6:01:44
 */
@Data
public class Po implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long              id;

    private Date              createdTime;

    private Date              modifiedTime;

}
