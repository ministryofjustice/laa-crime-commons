package uk.gov.justice.laa.crime.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum IojAppealDecisionReason {

    LOSS_OF_LIBERTY("LOSLIBTY", "Loss of liberty"),
    SUSPENDED_SENTENCE("SUSPSENT", "Subject to a suspended sentence"),
    LOSS_OF_LIVELIHOOD("LOSSLIVEHD", "Loss of livelihood"),
    DAMAGE_TO_REPUTATION("SEDAMTOREP", "Serious damage to reputation"),
    QUESTION_OF_LAW ("SUBQUELAW", "Substantial question of law"),
    UNDERSTAND_PROCEEDINGS("NOTUNDPROC", "Would not understand the proceedings"),
    WITNESS_TRACE  ("WITTRACE", "Witnesses to be traced"),
    SKILL_EXAM ("SKILLEXAM", "Skillful exam of prosecution witness"),
    INTERESTS_PERSON("INTANOPERS", "In the interests of another person"),
    OTHER("OTHER", "Other");

    @JsonPropertyDescription("IoJ decision reason code")
    @JsonValue
    private final String code;
    private final String description;

    public static IojAppealDecisionReason getFrom(String code) {
        if (StringUtils.isBlank(code)) { return null; }

        return Stream.of(IojAppealDecisionReason.values())
            .filter(reason -> reason.code.equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("IoJ appeal decision reason code: %s does not exist.", code)));
    }
}
