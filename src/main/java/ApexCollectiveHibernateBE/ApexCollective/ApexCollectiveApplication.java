package ApexCollectiveHibernateBE.ApexCollective;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ApexCollectiveApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApexCollectiveApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApexCollectiveApplication.class, args);
	}


}