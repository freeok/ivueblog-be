package work.pcdd.ivueblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pcdd
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 指哪些接口URL需要增加跨域设置
        registry.addMapping("/**")
                // 前端哪些域名被允许跨域
                .allowedOriginPatterns("*")
                // 允许哪些请求header访问
                .allowedHeaders("*")
                // 允许哪些方法
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                // 意义是允许客户端携带验证信息，例如 cookie 之类的
                .allowCredentials(true)
                .maxAge(3600);
    }

}
