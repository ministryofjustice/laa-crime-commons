package uk.gov.justice.laa.crime.samples;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.samples.model.FinancialAssessment;
import uk.gov.justice.laa.crime.samples.service.DemoClientService;

import java.util.UUID;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {

	private final DemoClientService demoClientService;

	@GetMapping("/")
	public FinancialAssessment home() {
		return demoClientService.getFinancialAssessment(1000);
	}

	@GetMapping("/cda")
	public void cda() {
		demoClientService.triggerHearingProcessing(UUID.randomUUID(), UUID.randomUUID().toString());
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}