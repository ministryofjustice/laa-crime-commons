package uk.gov.justice.laa.crime.orchestration.enums.cat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionType {

    GET("get"),
    SAVE_APPLICATION("Save_Application"),
    SAVE_MEANS_FULL("Save_Means_Full"),
    SAVE_MEANS_INIT("Save_Means_Init"),
    SAVE_PASSPORT("Save_Passport");

    private final String code;
}
