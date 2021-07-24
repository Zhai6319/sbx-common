package com.sbx.common.auth.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>说明：</p>
 *
 * @author Z.jc
 * @version 1.0.0
 * @since 2020/12/28
 */
@Data
public class AuthUser implements Serializable {

    private static final long serialVersionUID = -1102425730785499916L;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 客户端类型 1货主 2司机 3驿站 4合伙人 5运营管理系统
     */
    private Integer clientType;
    /**
     * 登陆令牌
     */
    private String token;

    private LocalDateTime tokenTime;

    private Long expiredSeconds;
}
