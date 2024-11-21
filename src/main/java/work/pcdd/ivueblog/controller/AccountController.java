package work.pcdd.ivueblog.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import work.pcdd.ivueblog.common.dto.LoginDto;
import work.pcdd.ivueblog.common.lang.Result;
import work.pcdd.ivueblog.entity.User;
import work.pcdd.ivueblog.service.UserService;
import work.pcdd.ivueblog.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pcdd
 */
@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${pcdd.jwt.secret}")
    private String secret;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        System.out.println(loginDto);
        System.out.println("-------登录开始执行-------");

        // 通过username到数据库查询,如果不存在提示错误
        User user = userService.getOne(new QueryWrapper<User>()
                .eq("username", loginDto.getUsername())
                .eq("password", SecureUtil.md5(secret + loginDto.getPassword() + secret)));


        Assert.notNull(user, "用户名或密码错误");

        String jwt = jwtUtils.createToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        Map<String, Object> map = new HashMap<>(16);
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("avatar", user.getAvatar());
        map.put("email", user.getEmail());

        System.out.println("登录操作已结束-------");
        return Result.success(map);
    }

    /**
     * 退出登录
     */
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

    /**
     * 刷新token，每当用户打开主页时，发送携带token的请求，
     * 后端判断token的合法性，若通过校验，返回新的token以及最新的用户信息
     */
    @RequiresAuthentication
    @GetMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest req, HttpServletResponse resp) {
        String token = req.getHeader("Authorization");
        String userId = jwtUtils.getClaim(token).getSubject();
        User user = userService.getById(userId);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        if (user.getStatus() == -1) {
            throw new LockedAccountException("用户已被锁定");
        }

        String jwt = jwtUtils.createToken(user.getId());
        resp.setHeader("Authorization", jwt);
        resp.setHeader("Access-Control-Expose-Headers", "Authorization");

        Map<String, Object> map = new HashMap<>(16);
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("avatar", user.getAvatar());
        map.put("email", user.getEmail());

        return Result.success(map);
    }
}
