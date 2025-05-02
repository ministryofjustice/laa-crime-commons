package uk.gov.justice.laa.crime.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    private static final String NULL_VALUE = "null";

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws
            IOException {

        String timestamp = jsonParser.getValueAsString();
        try {
            if (StringUtils.isNotBlank(timestamp) && !timestamp.trim().equals(NULL_VALUE)) {
                return ZonedDateTime.parse(timestamp);
            }
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid timestamp value: " + timestamp, e);
        }
        return null;
    }
}
