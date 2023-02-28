package myself.badwritten.base.config.stream;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @className StreamReceiver
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-18 23:47
 * @Version 1.0
 **/
@Component
@EnableBinding(value = {StreamClient.class})
public class StreamReceiver {

    @StreamListener(StreamClient.INPUT)
    public void receive(String message){
        System.out.println("-------------"+message);
    }
}
