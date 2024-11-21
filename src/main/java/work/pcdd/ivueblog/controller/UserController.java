package work.pcdd.ivueblog.controller;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.pcdd.ivueblog.common.lang.Result;
import work.pcdd.ivueblog.entity.User;
import work.pcdd.ivueblog.service.UserService;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pcdd
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Value("${pcdd.jwt.secret}")
    private String secret;


    /**
     * 获取所有用户信息
     */
    @RequiresAuthentication
    @GetMapping("/list")
    public Result findAll() {
        return Result.success(userService.list());
    }

    /**
     * 用户注册
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {

        Assert.isTrue(userService.getOne(new QueryWrapper<User>()
                .eq("username", user.getUsername())) == null, "该用户名已被注册");

        user.setCreated(LocalDateTime.now());

        user.setPassword(SecureUtil.md5(secret + user.getPassword() + secret));
        Assert.isTrue(userService.save(user), "添加用户失败");
        return Result.success();
    }

    @RequiresRoles("admin")
    @PostMapping("/test")
    public Result test() {
        return Result.success("admin接口调用成功！");
    }


}
