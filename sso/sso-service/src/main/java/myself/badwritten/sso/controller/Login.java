package myself.badwritten.sso.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import myself.badwritten.common.base.State;

import myself.badwritten.common.config.redis.Redis;
import myself.badwritten.common.model.User;
import myself.badwritten.sso.service.feign.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @className Login
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-16 14:19
 * @Version 1.0
 **/
@RestController
@RequestMapping("/login")
@DefaultProperties(defaultFallback = "globalFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
})
public class Login {

    @Resource
    Redis redis;

    @Resource
    UserService userService;

    @GetMapping("/login/{id}")
    public State<User> login(@PathVariable("id")String id){
        State<User> user = userService.getUser(id);
        redis.set("user", user.getData());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object user1 = redis.get("user");
        return user;
    }

    public State<String> globalFallBack(){
        return State.failed("500", "调用服务异常");
    }
}
