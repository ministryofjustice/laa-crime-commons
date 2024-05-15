package uk.gov.justice.laa.crime.enums.orchestration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoredProcedure {

    PRE_UPDATE_CHECKS(Package.APPLICATION, "pre_update_checks"),
    ASSESSMENT_POST_PROCESSING_PART_1(Package.ASSESSMENTS, "post_assessment_processing_part_1"),
    ASSESSMENT_POST_PROCESSING_PART_2(Package.ASSESSMENTS, "post_assessment_processing_part_2"),
    ASSESSMENT_POST_PROCESSING_PART_1_C3(Package.ASSESSMENTS, "post_assessment_processing_part_1_c3"),
    PROCESS_ACTIVITY_AND_GET_CORRESPONDENCE(Package.CROWN_COURT, "xx_process_activity_and_get_correspondence"),
    PROCESS_ACTIVITY(Package.MATRIX_ACTIVITY, "process_activity"),
    DETERMINE_MAGS_REP_DECISION(Package.ASSESSMENTS, "determine_mags_rep_decision"),
    PRE_UPDATE_CC_APPLICATION(Package.APPLICATION, "pre_update_cc_application"),
    GET_APPLICATION_CORRESPONDENCE(Package.CORRESPONDENCE, "get_application_correspondence"),
    UPDATE_CC_APPLICANT_AND_APPLICATION(Package.CROWN_COURT, "update_cc_applicant_and_application"),
    UPDATE_DBMS_TRANSACTION_ID(Package.APPLICATION, "update_dbms_transaction_id");

    final String packageName;
    final String procedureName;

    private static class Package {
        public static final String ASSESSMENTS = "assessments";
        public static final String APPLICATION = "application";
        public static final String CROWN_COURT = "crown_court";
        public static final String MATRIX_ACTIVITY = "matrix_activity";
        public static final String CORRESPONDENCE = "correspondence_pkg";

    }
}
