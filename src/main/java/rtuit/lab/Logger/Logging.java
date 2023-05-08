package rtuit.lab.Logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class Logging {


    @Pointcut("@annotation(Loggable)")
    public void executeLogging(){}



    @Before("executeLogging()")
    public void logMethodCallBefore(JoinPoint joinPoint){
        StringBuilder message = new StringBuilder("Method: ");
        message.append(joinPoint.getSignature().getName()).append(" !");
        Arrays.stream(joinPoint.getArgs()).forEach(arg ->
                message.append("args: ").append(arg).append(" !"));

        log.info(message.toString());
    }


    @AfterReturning(pointcut = "executeLogging()",returning = "returnValue")
    public void logMethodCallAfter(JoinPoint joinPoint,Object returnValue){
        String message = "Method: " + joinPoint.getSignature().getName() + " !" +
                "return: " + returnValue;

        log.info(message);
    }


    @AfterThrowing(pointcut = "@annotation(Loggable)", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }
}