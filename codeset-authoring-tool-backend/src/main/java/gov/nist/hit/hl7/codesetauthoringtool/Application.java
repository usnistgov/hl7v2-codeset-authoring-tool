package gov.nist.hit.hl7.codesetauthoringtool;

import gov.nist.hit.hl7.codesetauthoringtool.service.AdminUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication(scanBasePackages = { "gov.nist.hit.hl7.codesetauthoringtool" })
public class Application {

	@Value("${emailFrom}")
	private String emailFrom;
	@Value("${emailSubject}")
	private String emailSubject;
	@Value("${emailProtocol}")
	private String emailProtocol;
	@Value("${emailPort}")
	private String emailPort;
	@Value("${emailHost}")
	private String emailHost;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}
