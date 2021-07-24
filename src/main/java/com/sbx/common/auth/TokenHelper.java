package com.sbx.common.auth;

import com.alibaba.fastjson.JSONObject;
import com.sbx.common.auth.model.AuthUser;
import com.sbx.common.auth.model.TokenBody;
import com.sbx.core.model.exception.SecurityException;
import com.sbx.core.redis.cache.SbxRedisCache;
import com.sbx.core.tool.util.Base64Util;
import com.sbx.core.tool.util.DigestUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>说明：</p>
 *
 * @author Z.jc
 * @version 1.0.0
 * @since 2020/12/28
 */
@Component
public class TokenHelper {

    @Resource
    private SbxRedisCache sbxRedisCache;

    public static final String TOKEN_USER_ID = "user_id";

    public static final String USER_TYPE = "user_type";

    private static final String TOKEN_SECRET = "67aefa0055d0e762";

    private static final String USER_TOKEN_KEY = "user:token:";

    private static final Long EXPIRED_SECONDS = 1*24*60*60L;


    /**
     * 生成用户登陆令牌
     * @param authUser  授权用户信息
     * @return          返回token
     */
    public String generateUserLoginToken(AuthUser authUser){
        JSONObject tokenHeaderJson = new JSONObject();
        tokenHeaderJson.put("token-type","sbx");
        tokenHeaderJson.put("secret-mode","hs256");
        String tokenHeader = Base64Util.encode(tokenHeaderJson.toJSONString());
        StringBuilder token = new StringBuilder(tokenHeader);
        token.append(".");
        JSONObject tokenInfoJson = new JSONObject();
        tokenInfoJson.put(TOKEN_USER_ID,authUser.getUserId());
        tokenInfoJson.put(USER_TYPE,authUser.getUserType());
        String tokenInfo = Base64Util.encode(tokenInfoJson.toJSONString());
        token.append(tokenInfo);
        token.append(".");
        LocalDateTime now = LocalDateTime.now();
        String secret = DigestUtil.hmacSha256Hex(tokenHeader+tokenInfo+now+TOKEN_SECRET,TOKEN_SECRET);
        token.append(secret);
        authUser.setTokenTime(now);
        authUser.setExpiredSeconds(EXPIRED_SECONDS);
        sbxRedisCache.setEx(USER_TOKEN_KEY+secret,authUser,authUser.getExpiredSeconds());
        return token.toString();
    }

    /**
     * 获取token 身体
     * @param token token令牌
     * @return      返回信息
     */
    private TokenBody getTokenBody(String token){
        return new TokenBody(token);
    }

    /**
     * 返回用户id
     * @param token token
     * @return      返回用户id
     */
    public Long userId(String token){
        AuthUser authUser = verificationToken(token);
        return authUser.getUserId();
    }


    public AuthUser verificationToken(String token){
        //获取token身体部分得到redis key，判断redis是否存在来判断是否可用
        TokenBody tokenBody = getTokenBody(token);
        JSONObject redisObj = sbxRedisCache.get(USER_TOKEN_KEY+tokenBody.getSignature());
        if (Objects.isNull(redisObj)) {
            throw new SecurityException("登录令牌失效");
        }
        //判断用户是否可用
        AuthUser authUser = redisObj.toJavaObject(AuthUser.class);
        JSONObject payload = JSONObject.parseObject(Base64Util.decode(tokenBody.getPayload()));
        if (!Objects.equals(payload.getLong(TOKEN_USER_ID),authUser.getUserId()) || !Objects.equals(payload.getInteger(USER_TYPE),authUser.getUserType())) {
            throw new SecurityException("登录令牌失效");
        }
        LocalDateTime tokenDueTime = authUser.getTokenTime().plusSeconds(authUser.getExpiredSeconds());
        if (tokenDueTime.isBefore(LocalDateTime.now())) {
            throw new SecurityException("登录令牌过期");
        }
        String secret = DigestUtil.hmacSha256Hex(tokenBody.getHeader()+tokenBody.getPayload()+authUser.getTokenTime()+TOKEN_SECRET,TOKEN_SECRET);
        if (!Objects.equals(secret,tokenBody.getSignature())) {
            throw new SecurityException("登录令牌失效");
        }
        return authUser;
    }

    /**
     * 切换用户客户端
     * @return
     */
    public AuthUser switchUserClient(String token,Integer clientType){
        TokenBody tokenBody = getTokenBody(token);
        AuthUser authUser = this.verificationToken(token);
        authUser.setClientType(clientType);
        sbxRedisCache.setEx(USER_TOKEN_KEY+tokenBody.getSignature(),authUser,authUser.getExpiredSeconds());
        return authUser;
    }

    /**
     * 登出，删除redis 缓存
     * @param token 用户token
     * @return      返回结果
     */
    public Boolean logout(String token){
        TokenBody tokenBody = getTokenBody(token);
        return sbxRedisCache.del(USER_TOKEN_KEY+tokenBody.getSignature());
    }




}
