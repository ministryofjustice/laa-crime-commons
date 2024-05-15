package uk.gov.justice.laa.crime.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws
            IOException {

        long value = jsonParser.getValueAsLong();
        Instant instant = Instant.ofEpochMilli(value);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
