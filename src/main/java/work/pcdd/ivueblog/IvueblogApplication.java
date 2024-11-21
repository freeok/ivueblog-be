package work.pcdd.ivueblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author pcdd
 */
@EnableCaching
@MapperScan("work.pcdd.ivueblog.mapper")
@SpringBootApplication
public class IvueblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(IvueblogApplication.class, args);
    }

}
