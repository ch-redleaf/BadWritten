package myself.badwritten.user.config.stream;

import myself.badwritten.common.base.cloud.stream.StreamTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @className StreamTemplateImpl
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-20 0:22
 * @Version 1.0
 **/
@Component
public class StreamTemplateImpl implements StreamTemplate {

    @Resource
    StreamClient streamClient;

    @Override
    public boolean send(String json){
        boolean send = streamClient.output().send(MessageBuilder.withPayload(json).build());
        return send;
    }


}
