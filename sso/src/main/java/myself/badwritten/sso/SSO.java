package myself.badwritten.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @className sso
 * @Description TODO
 * @Author XiaoHan
 * @Date 2022-1-6 21:43
 * @Version 1.0
 **/
@EnableHystrix
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScans(value = {@ComponentScan(basePackages = "myself.badwritten.common")})
@SpringBootApplication
public class SSO {
    public static void main(String[] args) {
        SpringApplication.run(SSO.class, args);
    }
}


