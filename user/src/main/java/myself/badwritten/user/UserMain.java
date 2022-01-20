package myself.badwritten.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


/**
 * @className UserMain
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-12 23:45
 * @Version 1.0
 **/
@EnableHystrix
@EnableDiscoveryClient
@ComponentScans(value = {@ComponentScan(basePackages = "myself.badwritten.common")})
@MapperScan("myself.badwritten.user.dao")
@SpringBootApplication
public class UserMain {
    public static void main(String[] args) {
        SpringApplication.run(UserMain.class, args);
    }
}
