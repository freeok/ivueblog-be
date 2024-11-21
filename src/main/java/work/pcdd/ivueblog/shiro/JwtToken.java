package work.pcdd.ivueblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author pcdd
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
