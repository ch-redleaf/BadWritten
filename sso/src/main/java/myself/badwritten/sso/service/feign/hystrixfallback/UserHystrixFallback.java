package myself.badwritten.sso.service.feign.hystrixfallback;

import myself.badwritten.common.base.State;
import myself.badwritten.common.model.User;
import myself.badwritten.sso.service.feign.UserService;
import org.springframework.stereotype.Component;

/**
 * @className UserHystrixFallback
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-16 17:30
 * @Version 1.0
 **/
@Component
public class UserHystrixFallback implements UserService {

    @Override
    public State<User> getUser(String userId) {
        User user = new User();
        user.setUserId("visitor");
        user.setUserName("游客");
        return State.success(user);
    }
}
