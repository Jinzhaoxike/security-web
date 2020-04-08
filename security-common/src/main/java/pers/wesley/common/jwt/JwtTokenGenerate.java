package pers.wesley.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pers.wesley.common.util.DesUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @Description : jwt token生成器
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 17:15
 */
public class JwtTokenGenerate {

    private JwtProperties jwtProperties;

    public JwtTokenGenerate(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 生成token
     * @param userId
     * @param username
     * @param nickname
     * @param permissions
     * @return
     */
    public String getToken(String userId, String username, String nickname, List<String> permissions) {

        Claims claims = Jwts.claims();
        claims.put("userId", DesUtils.encrypt(userId, jwtProperties.getDesKey()));

        LocalDateTime currentTime = LocalDateTime.now();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setAudience(nickname)
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plus(jwtProperties.getExpireTime(), ChronoUnit.SECONDS)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSignKey())
                .compact();
        return token;
    }
}
