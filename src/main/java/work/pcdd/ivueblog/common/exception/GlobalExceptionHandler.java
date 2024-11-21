package work.pcdd.ivueblog.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import work.pcdd.ivueblog.common.lang.Result;

/**
 * ResponseStatus注解就是为了改变HTTP响应的状态码
 * 如果不使用@ResponseStatus，在处理方法正确执行的前提下，后台返回HTTP响应的状态码为200
 * 其实就是调用了response.setStatus方法
 *
 * @author pcdd
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获所有shiro异常（ShiroException）
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public Result handler(ShiroException e) {
        // 目的是为了只在控制台显示错误详情
        log.error("shiro异常：", e);
        return Result.failure(401, e.getMessage());
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     * 压根就没登录（例如游客）
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public Result handle(UnauthenticatedException e) {
        return Result.failure(401, "无权访问(Unauthorized):当前Subject是匿名Subject，请先登录(This subject is anonymous.)");
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     * 已登录，但权限不足（例如用户访问管理员的接口）
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public Result handle(UnauthorizedException e) {
        return Result.failure(403, "无权访问(Unauthorized):当前Subject没有此请求所需权限(" + e.getMessage() + ")");
    }

    /**
     * 捕获token失效异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredCredentialsException.class)
    public Result handle(ExpiredCredentialsException e) {
        return Result.failure(400, "token已失效，请重新登录" + e.getMessage());
    }


    /**
     * 捕获实体校验异常（MethodArgumentNotValidException）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        // log.error("实体校验异常：", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.failure(objectError.getDefaultMessage());
    }

    /**
     * 捕获断言异常（IllegalArgumentException）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        // log.error("断言异常：", e);
        return Result.failure(e.getMessage());
    }

    /**
     * 捕捉接口404异常
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handle(NoHandlerFoundException e) {
        return Result.failure(404, "请求的资源不存在");
    }


    /**
     * 捕获运行异常（RuntimeException）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Result handler(RuntimeException e) {
        // log.error("运行异常：", e);
        return Result.failure(e.getMessage());
    }

    /**
     * 捕捉其他所有异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result globalException(Exception ex) {
        return Result.failure(500, ex.getMessage());
    }

}
