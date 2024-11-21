package work.pcdd.ivueblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import work.pcdd.ivueblog.entity.Blog;
import work.pcdd.ivueblog.mapper.BlogMapper;
import work.pcdd.ivueblog.service.BlogService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pcdd
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
}
