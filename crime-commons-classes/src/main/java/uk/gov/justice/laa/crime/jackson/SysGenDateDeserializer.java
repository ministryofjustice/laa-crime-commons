package uk.gov.justice.laa.crime.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import uk.gov.justice.laa.crime.dto.maat.SysGenDate;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class SysGenDateDeserializer extends JsonDeserializer<SysGenDate> {

    private static final String NULL_VALUE = "null";

    @Override
    public SysGenDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode node = jsonParser
                .getCodec()
                .readTree(jsonParser);

        String value = node.get("value").asText();
        try {
            if (StringUtils.isNotBlank(value) && !value.trim().equals(NULL_VALUE)) {
                return new SysGenDate(Date.from(Instant.parse(value)));
            }
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid date value: " + value, e);
        }
        return null;
    }
}
