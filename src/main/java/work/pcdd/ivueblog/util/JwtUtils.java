package work.pcdd.ivueblog.util;

import cn.hutool.crypto.SecureUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author pcdd
 */
@Data
@Component
@ConfigurationProperties(prefix = "pcdd.jwt")
public class JwtUtils {

    private String secret;
    private long expire;
    private String header;

    /**
     * 创建JWT
     * JWT(JSON Web Token)由3部分组成：头部信息.载荷信息.签名信息
     */
    public String createToken(long userId) {
        System.out.println("userId = " + userId);
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        // 返回JWT
        return Jwts.builder()
                //.setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(userId))
                // 生成签名的时间
                .setIssuedAt(nowDate)
                // 签名过期的时间
                .setExpiration(expireDate)
                // 使用HS512构建密钥信息 这里secret不能随便写，否则报错
                .signWith(SignatureAlgorithm.HS512, generalKey())
                .compact();
    }

    public Claims getClaim(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(generalKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * token是否过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    /**
     * 将字符串md5摘要加密生成key
     */
    public String generalKey() {
        return SecureUtil.md5(secret);
    }

}
/*
* Signature 部分是对前两部分的签名，防止数据篡改。
首先，需要指定一个密钥（secret）。这个密钥只有服务器才知道，不能泄露给用户。然后，使用 Header 里面指定的签名算法（默认是 HMAC SHA256）*/
