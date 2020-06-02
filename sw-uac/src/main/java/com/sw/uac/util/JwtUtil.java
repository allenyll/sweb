package com.sw.uac.util;

import com.sw.uac.entity.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token 相关操作类
 * @Author: yu.leilei
 * @Date: 下午 5:15 2018/5/24 0024
 */
@Component
public class JwtUtil {

    private static final String CLAIM_KEY_USER_ACCOUNT = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    /**
     * 密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 过期时间
     */
    @Value("${jwt.exp}")
    private Long exp;

    /**
     * 从token中获取用户账户/名称
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token){
        String userName;
        try {
            final Claims claims = getClaimsFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            userName = null;
        }
        return userName;
    }

    /**
     * 从token中获取claims
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e){
            claims = null;
        }
        return claims;
    }

    /**
     * 生存token的过期时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + exp * 1000);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取token的过期时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(5);
        claims.put(CLAIM_KEY_USER_ACCOUNT, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        //采用什么算法是可以自己选择的，不一定非要采用HS512
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * token 是否可刷新
     * @param token
     * @return
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordResetDate) {
        final Date created = getCreatedDateFromToken(token);
        Boolean isCreateBeforeReset = isCreatedBeforeLastPasswordReset(created, lastPasswordResetDate);
        return !isTokenExpired(token) && isCreateBeforeReset;
    }

    /**
     * 创建用户是否在最后一次更新密码之前
     * @param created
     * @param lastPasswordReset
     * @return
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * 从token中获取创建时间
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        if(userDetails == null){
            return false;
        }
        JwtUser user = (JwtUser) userDetails;
        final String userName = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        Boolean result = (userName.equals(user.getUsername())
                && !isTokenExpired(token));
                //&& isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())); // 创建时间没有配置对
        return result;
    }
}
