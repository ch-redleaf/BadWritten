package myself.badwritten.sso.service.feign;

import myself.badwritten.common.base.State;
import myself.badwritten.common.model.User;
import myself.badwritten.sso.service.feign.hystrixfallback.UserHystrixFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @className UserService
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-16 13:47
 * @Version 1.0
 **/
@Component
@FeignClient(name = "myself-badwritten-user", fallback = UserHystrixFallback.class)
public interface UserService {

    @RequestMapping("/user/getUser/{id}")
    public State<User> getUser(@PathVariable("id")String userId);
}
