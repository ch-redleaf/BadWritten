package myself.badwritten.base.service.impl;

import myself.badwritten.common.model.User;
import myself.badwritten.base.dao.UserMapper;
import myself.badwritten.base.service.UserService;
import myself.badwritten.common.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author RedLeaf
 * @since 2022-01-19
 */
@Service
public class UserServiceImpl extends BaseService<UserMapper, User> implements UserService {

}
