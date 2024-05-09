package gov.nist.hit.hl7.codesetauthoringtool;

import gov.nist.hit.hl7.codesetauthoringtool.service.AdminUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = { "gov.nist.hit.hl7.codesetauthoringtool" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
