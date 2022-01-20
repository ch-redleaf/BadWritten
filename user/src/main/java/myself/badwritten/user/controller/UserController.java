package myself.badwritten.user.controller;


import myself.badwritten.common.base.State;
import myself.badwritten.common.base.cloud.stream.StreamTemplate;
import myself.badwritten.common.model.User;
import myself.badwritten.common.util.GsonUtils;
import myself.badwritten.user.service.UserService;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import myself.badwritten.common.base.BaseController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author RedLeaf
 * @since 2022-01-19
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    UserService userService;

    @Resource
    StreamTemplate streamTemplate;

    @RequestMapping("/getUser/{id}")
    public State<User> getUser(@PathVariable("id")String userId){
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        User user = userService.selectById(userId);
        String userJson = GsonUtils.toJson(null, user);
        boolean send = streamTemplate.send(userJson);
        return State.success(user);
    }
}

