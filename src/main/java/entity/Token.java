package entity;

import java.time.LocalDateTime;


public class Token {
    //Token
    private String token;
    //有效时间：单位：秒
    private Long expire;
    //过期时间
    private LocalDateTime expiration;

    public Token(){}
    public Token(String token, Long expire, LocalDateTime expiration) {
        this.token = token;
        this.expire = expire;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public Long getExpire() {
        return expire;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
