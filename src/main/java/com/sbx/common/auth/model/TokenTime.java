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
public class TokenTime implements Serializable {
    private static final long serialVersionUID = 70902694308343804L;

    private LocalDateTime createTime;

    private Long expiredSeconds;
}
