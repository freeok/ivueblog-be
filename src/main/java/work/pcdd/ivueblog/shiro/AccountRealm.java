package work.pcdd.ivueblog.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.pcdd.ivueblog.entity.User;
import work.pcdd.ivueblog.service.UserService;
import work.pcdd.ivueblog.util.JwtUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author pcdd
 */
@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    /**
     * 告诉AuthorizingRealm支持的是JwtToken（因为我们用的是Jwt令牌）
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 加上 @RequiresAuthentication 的方法会执行
     * 登录认证校验(拿到一个token进行一个帐号密码校验，验证成功后就返回一个info含数据的对象回去) 登录就在这里
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        log.info("开始认证");

        // 因为我们是使用JWT生成的令牌，所以这里必须强转为我们自己的token
        JwtToken jwtToken = (JwtToken) token;

        String userId = jwtUtils.getClaim((String) jwtToken.getPrincipal()).getSubject();
        User user = userService.getById(userId);

        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }

        if (user.getStatus() == -1) {
            throw new LockedAccountException("用户已被锁定");
        }

        AccountProfile profile = new AccountProfile();
        BeanUtils.copyProperties(user, profile);

        System.out.println("认证通过");

        // profile消息体、getCredentials获取token、getName()其实就是这个自定义类的名字
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }

    /**
     * 加上 @RequiresRoles 的方法会执行
     * 该方法主要是用于当前登录用户授权(拿到用户之后获取它的权限，把权限信息封装成一个authorizationInfo返回给Shiro
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        log.info("开始授权");

        Object primaryPrincipal = principals.getPrimaryPrincipal();
        AccountProfile accountProfile = (AccountProfile) primaryPrincipal;
        User user = userService.getOne(new QueryWrapper<User>().eq("username", accountProfile.getUsername()));

        if (user != null) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            Set<String> set = new HashSet<>();
            // 这里数据库表没有建roles字段（以后用在建表时把roles字段加上，然后取过来放在这里就能赋予角色了）
            set.add(user.getRole());
            authorizationInfo.setRoles(set);

            System.out.println("授权完毕！");
            return authorizationInfo;
        }

        return null;
    }

}
