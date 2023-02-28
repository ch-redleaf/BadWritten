package myself.badwritten.common.exception;

import lombok.extern.slf4j.Slf4j;
import myself.badwritten.common.base.State;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @className GlobalExceptionHandler
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-20 21:03
 * @Version 1.0
 **/

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public State<String> globalException(HttpServletResponse response, Exception ex){
        log.info("GlobalExceptionHandler...");
        log.info("错误代码："  + response.getStatus());
        log.info("错误信息："  + ex.getMessage());
        return State.failed(String.valueOf(response.getStatus()), "服务器异常");
    }
}
