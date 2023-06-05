package uk.gov.justice.laa.crime.samples.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.justice.laa.crime.commons.client.RestAPIClient;
import uk.gov.justice.laa.crime.samples.model.FinancialAssessment;
import uk.gov.justice.laa.crime.samples.model.RepOrderCCOutcome;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemoClientService {

    @Qualifier("maatApiClient")
    private final RestAPIClient maatApiClient;

    @Qualifier("cdaApiClient")
    private final RestAPIClient cdaApiClient;

    @Value("${cda.base-url}")
    private String cdaBaseUrl;

    @Value("${maat-api.base-url}")
    private String maatApiBaseUrl;

    public static final String RESPONSE_STRING = "Response from Court Data API: %s";

    public FinancialAssessment getFinancialAssessment(int financialAssessmentId) {
        FinancialAssessment response = maatApiClient.get(
                new ParameterizedTypeReference<FinancialAssessment>() {},
                maatApiBaseUrl + "/financial-assessments/{financialAssessmentId}",
                Map.of("LAA_TRANSACTION_ID", UUID.randomUUID().toString()),
                financialAssessmentId
        );
        log.info(String.format(RESPONSE_STRING, response));
        return response;
    }

    public List<RepOrderCCOutcome> getRepOrderCCOutcomeByRepId(int repId) {
        List<RepOrderCCOutcome> response = maatApiClient.get(
                new ParameterizedTypeReference<List<RepOrderCCOutcome>>() {},
                maatApiBaseUrl + "/rep-orders/cc-outcome/reporder/{repId}",
                Map.of("LAA_TRANSACTION_ID", UUID.randomUUID().toString()),
                repId
        );
        log.info(String.format(RESPONSE_STRING, response));
        return response;
    }

    public void triggerHearingProcessing(UUID hearingId, String laaTransactionId) {

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("publish_to_queue", "true");

        cdaApiClient.get(
                new ParameterizedTypeReference<Void>() {},
                cdaBaseUrl + "/api/internal/v2/hearing_results/{hearingId}",
                Map.of("X-Request-ID", laaTransactionId),
                queryParams,
                hearingId
        );
    }
}
