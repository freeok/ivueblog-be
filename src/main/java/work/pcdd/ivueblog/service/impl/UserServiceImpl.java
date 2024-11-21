package work.pcdd.ivueblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.pcdd.ivueblog.common.lang.Result;
import work.pcdd.ivueblog.entity.User;
import work.pcdd.ivueblog.mapper.UserMapper;
import work.pcdd.ivueblog.service.UserService;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pcdd
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    public Result fun1() {
        List<User> list = userMapper.selectList(null);
        return Result.success(list);
    }


}
