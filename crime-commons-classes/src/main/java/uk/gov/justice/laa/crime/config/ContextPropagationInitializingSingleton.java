package uk.gov.justice.laa.crime.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Hooks;

/**
 * Automatically wrap Reactor internal mechanisms to propagate context between operators, threads etc. on application startup.
 */
@Component
@Slf4j
public class ContextPropagationInitializingSingleton implements SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        Hooks.enableAutomaticContextPropagation();
    }
}
