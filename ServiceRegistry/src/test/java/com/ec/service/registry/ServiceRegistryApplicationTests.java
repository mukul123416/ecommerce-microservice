package com.ec.service.registry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.cloud.config.enabled=false",
		"spring.kafka.bootstrap-servers=localhost:9092"
})
class ServiceRegistryApplicationTests {

	@Test
	void contextLoads() {
	}

}
