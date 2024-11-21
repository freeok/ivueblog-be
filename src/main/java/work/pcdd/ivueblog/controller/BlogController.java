package work.pcdd.ivueblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work.pcdd.ivueblog.common.lang.Result;
import work.pcdd.ivueblog.entity.Blog;
import work.pcdd.ivueblog.service.BlogService;
import work.pcdd.ivueblog.service.UserService;
import work.pcdd.ivueblog.util.ShiroUtils;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pcdd
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    /**
     * 查询所有文章
     */
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1", name = "current") Integer currentPage
            , @RequestParam(defaultValue = "5", name = "page") Integer pageSize) {

        System.out.println(currentPage);
        System.out.println(pageSize);
        Page<Blog> page = new Page<>(currentPage, pageSize);
        IPage<Blog> data = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.success(data);
    }

    /**
     * 根据id查询文章
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        // 先判断该文章的作者是否存在，需要连接查询，应在mapper中写sql
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "文章不存在");
        return Result.success(blog);
    }

    /**
     * 发布文章
     */
    @RequiresAuthentication
    @PostMapping("/add")
    public Result add(@Validated @RequestBody Blog blog) {
        Assert.isTrue(Objects.equals(blog.getUserId(), ShiroUtils.getProfile().getId()), "没有权限添加");
        blog.setCreated(LocalDateTime.now());
        return Result.success(blogService.save(blog));
    }

    /**
     * 编辑文章
     */
    @RequiresAuthentication
    @PutMapping("/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        System.out.println("======================编辑请求开始执行！============================");
        System.out.println(blog);
        Long id = blog.getId();

        // 判断文章是否存在
        Assert.isTrue(id != null || blogService.getById(id) != null, "文章不存在");

        // 只能编辑自己的文章
        Assert.isTrue(Objects.equals(blog.getUserId(), ShiroUtils.getProfile().getId()), "没有权限编辑");

        Assert.isTrue(blogService.updateById(blog), "文章编辑失败");

        return Result.success();
    }

}
