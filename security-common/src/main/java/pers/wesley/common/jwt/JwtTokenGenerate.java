package pers.wesley.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.security.core.authority.AuthorityUtils;
import pers.wesley.common.exception.BaseException;
import pers.wesley.common.exception.ErrorCodeEnum;
import pers.wesley.common.util.DesUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description : jwt token生成器
 * @Author : jinzhaoxike91@outlook.com
 * @Create : 2020/04/08 17:15
 */
public class JwtTokenGenerate {

    private JwtProperties jwtProperties;

    private ObjectMapper objectMapper = new ObjectMapper();

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
    public String getToken(String userId, String username, String nickname, List<String> permissions, Map<String, Object> reserveMap) {
        String scopes;
        String reserve;
        try {
            scopes = objectMapper.writeValueAsString(permissions);
            reserve = objectMapper.writeValueAsString(reserveMap);
        } catch (Exception e) {
            throw new BaseException(ErrorCodeEnum.JSON_ERROR);
        }

        Claims claims = Jwts.claims();
        claims.put("userId", DesUtils.encrypt(jwtProperties.getDesKey(), userId));
        claims.put("scopes", DesUtils.encrypt(jwtProperties.getDesKey(), scopes));
        claims.put("reserve", DesUtils.encrypt(jwtProperties.getDesKey(), reserve));
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

    public JwtAuthenticationToken parseToken(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(jwtProperties.getSignKey()).parseClaimsJws(token);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, new String[] {"token已过期"});
        } catch (UnsupportedJwtException e) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, new String[] {"token签名方式不支持"});
        } catch (MalformedJwtException e) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, new String[] {"token格式错误"});
        } catch (SignatureException e) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, new String[] {"token验签错误"});
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorCodeEnum.AUTHORIZATION_ERROR, new String[] {"token验签key非法"});
        }

        Claims claimsJwsBody = claimsJws.getBody();
        String scopes = claimsJwsBody.get("scopes", String.class);
        String reserve = claimsJwsBody.get("reserve", String.class);

        String decryptScopes = DesUtils.decrypt(jwtProperties.getDesKey(), scopes);
        String decryptReserve = DesUtils.decrypt(jwtProperties.getDesKey(), reserve);

        List<String> permissions;
        Map<String, Object> reserveMap;
        try {
            permissions = objectMapper.readValue(decryptScopes, List.class);
            reserveMap = objectMapper.readValue(decryptReserve, Map.class);
        } catch (Exception e) {
            throw new BaseException(ErrorCodeEnum.JSON_ERROR);
        }
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(claimsJwsBody.getSubject(), token, AuthorityUtils.createAuthorityList(permissions.toArray(new String[permissions.size()])));
        jwtAuthenticationToken.setDetails(reserveMap);
        jwtAuthenticationToken.setAuthenticated(true);
        return jwtAuthenticationToken;
    }
}
