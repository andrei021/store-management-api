package io.github.andrei021.store.common.aspect;

import io.github.andrei021.store.common.property.LoggingProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class RepositoryMonitoringAspect {

    private final LoggingProperties loggingProperties;

    public RepositoryMonitoringAspect(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    @Around("execution(public * io.github.andrei021.store.persistence..*Repository.*(..))")
    public Object logRepositoryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object[] args = joinPoint.getArgs();
        log.debug("Entering [{}] with args={}", joinPoint.getSignature(), Arrays.toString(args));

        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        String resultInfo = "null";

        if (result instanceof Collection<?> collection) {
            resultInfo = "Collection size=[" + collection.size() + "], preview=[" +
                    collection.stream().limit(loggingProperties.previewLimit())
                            .map(Object::toString)
                            .toList() + "]";
        } else if (result instanceof Optional<?> optional) {
            resultInfo = optional.map(Object::toString).orElse("empty");
        } else if (result != null) {
            resultInfo = result.toString();
        }

        log.info("Executed [{}] in [{}] ms, result=[{}]", joinPoint.getSignature(), duration, resultInfo);
        return result;
    }
}
