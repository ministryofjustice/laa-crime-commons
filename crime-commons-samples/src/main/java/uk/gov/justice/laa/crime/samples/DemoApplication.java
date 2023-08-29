package uk.gov.justice.laa.crime.samples;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.samples.model.FinancialAssessment;
import uk.gov.justice.laa.crime.samples.model.RepOrderCCOutcome;
import uk.gov.justice.laa.crime.samples.service.DemoClientService;

import java.util.List;
import java.util.UUID;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {

	private final DemoClientService demoClientService;

	@GetMapping("/financial-assessments/{id}")
	public FinancialAssessment financialAssessment(@PathVariable int id) {
		return demoClientService.getFinancialAssessment(id);
	}

	@GetMapping("/outcomes/{repId}")
	public List<RepOrderCCOutcome> outcomes(@PathVariable int repId) {
		return demoClientService.getRepOrderCCOutcomeByRepId(repId);
	}

	@GetMapping("/test")
	public List<RepOrderCCOutcome> test() {
		return demoClientService.getRepOrderCCOutcomeByRepId(5635503);
	}

	@GetMapping("/cda")
	public void cda() {
		demoClientService.triggerHearingProcessing(UUID.randomUUID(), UUID.randomUUID().toString());
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
