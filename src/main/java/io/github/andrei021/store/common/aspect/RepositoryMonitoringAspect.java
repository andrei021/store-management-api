package io.github.andrei021.store.common.aspect;

import jakarta.annotation.PostConstruct;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
public class RepositoryMonitoringAspect {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryMonitoringAspect.class);

    @Value("${app.logging.preview-limit}")
    private Integer previewLimit;
    private int defaultPreviewLimit = 5;

    @PostConstruct
    public void postConstruct() {
        if (previewLimit == null) {
            previewLimit = defaultPreviewLimit;
        }
    }

    @Around("execution(public * io.github.andrei021.store.persistence..*Repository.*(..))")
    public Object logRepositoryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object[] args = joinPoint.getArgs();
        logger.debug("Entering [{}] with args={}", joinPoint.getSignature(), Arrays.toString(args));

        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        String resultInfo = "null";

        if (result instanceof Collection<?> collection) {
            resultInfo = "Collection size=[" + collection.size() + "], preview=[" +
                    collection.stream().limit(previewLimit)
                            .map(Object::toString)
                            .toList() + "]";
        } else if (result instanceof Optional<?> optional) {
            resultInfo = optional.map(Object::toString).orElse("empty");
        } else if (result != null) {
            resultInfo = result.toString();
        }

        logger.info("Executed [{}] in [{}] ms, result=[{}]", joinPoint.getSignature(), duration, resultInfo);
        return result;
    }
}
