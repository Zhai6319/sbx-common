package com.sbx.common.auth.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * <p>说明：</p>
 *
 * @author Z.jc
 * @version 1.0.0
 * @since 2020/12/28
 */
@Data
public class TokenBody implements Serializable {
    private static final long serialVersionUID = 6779003816509259434L;

    private String header;

    private String payload;

    private String signature;

    public TokenBody(String token){
        if (StringUtils.isBlank(token)) {
            throw new SecurityException("token is null");
        }
        String[] tokens = token.split("\\.");
        if (tokens.length !=3 ) {
            throw new SecurityException("the token format is error");
        }
        this.header = tokens[0];
        this.payload = tokens[1];
        this.signature = tokens[2];
    }
}
