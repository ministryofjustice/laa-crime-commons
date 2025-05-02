package uk.gov.justice.laa.crime.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZonedDateTimeDeserializerTest {

    private final JsonFactory factory = new JsonFactory();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ZonedDateTimeDeserializer deserializer = new ZonedDateTimeDeserializer();
    private static final String TIME_STAMP = "2011-12-03T10:15:30.342+01:00";

    @Test
    void givenValidDateWithNanoSeconds_whenDeserializeIsInvoked_thenLocalDateTimeIsDeserialized() throws IOException {
        String content = String.format("\"%s\"", TIME_STAMP);
        JsonParser parser = factory.createParser(content);
        parser.setCodec(mapper);
        parser.nextToken();

        ZonedDateTime expected = ZonedDateTime.parse(TIME_STAMP);
        ZonedDateTime result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void givenNullDate_whenDeserializeIsInvoked_thenNullIsReturned() throws IOException {
        String content = "\"null\"";
        JsonParser parser = factory.createParser(content);
        parser.setCodec(mapper);
        parser.nextToken();

        ZonedDateTime result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertThat(result).isNull();
    }

    @Test
    void givenInvalidDate_whenDeserializeIsInvoked_thenExceptionIsThrown() throws IOException {
        String content = "\"invalid-date\"";
        JsonParser parser = factory.createParser(content);
        parser.setCodec(mapper);
        parser.nextToken();

        assertThatThrownBy(() -> deserializer.deserialize(parser, mapper.getDeserializationContext()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid timestamp value: invalid-date");
    }
}
