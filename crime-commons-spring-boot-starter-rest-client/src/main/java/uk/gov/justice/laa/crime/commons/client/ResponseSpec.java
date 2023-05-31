package uk.gov.justice.laa.crime.commons.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;

/**
 * The interface Response spec.
 *
 * @param <T> the type parameter
 */
public interface ResponseSpec<T> {

    /**
     * Initialises a new DefaultResponseSpec wrapping the provided class
     *
     * @param <T>   the type parameter
     * @param clazz the class that the DefaultResponseSpec should wrap
     * @return the response spec
     */
    static <T> ResponseSpec<T> forClass(Class<T> clazz) {
        return new DefaultResponseSpec<>(clazz);
    }

    /**
     * Initialises a new ParameterizedResponseSpec wrapping the provided type reference
     *
     * @param <T>           the type parameter
     * @param typeReference the type reference that the ParameterizedResponseSpec should wrap
     * @return the response spec
     */
    static <T> ResponseSpec<T> forParameterizedType(ParameterizedTypeReference<T> typeReference) {
        return new ParameterizedResponseSpec<>(typeReference);
    }

    /**
     * Stores class to use when deserializing REST API responses
     *
     * @param <T> the type parameter
     */
    @Getter
    @AllArgsConstructor
    class DefaultResponseSpec<T> implements ResponseSpec<T> {
        private Class<T> elementClass;
    }

    /**
     * Stores type reference to use when deserializing REST API responses
     *
     * @param <T> the type parameter
     */
    @Getter
    @AllArgsConstructor
    class ParameterizedResponseSpec<T> implements ResponseSpec<T> {
        private ParameterizedTypeReference<T> elementTypeRef;
    }
}
