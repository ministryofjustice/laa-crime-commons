package uk.gov.justice.laa.crime.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Hooks;

/**
 * Automatically wrap Reactor internal mechanisms to propagate context between operators, threads etc. on application startup.
 */
@Order(2)
@Component
@Slf4j
public class ContextPropagationInitializingSingleton implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("********** Context Propagation Initializer **************");
        Hooks.enableAutomaticContextPropagation();
    }
}
