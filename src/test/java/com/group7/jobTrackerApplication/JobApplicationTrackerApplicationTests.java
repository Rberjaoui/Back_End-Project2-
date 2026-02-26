package com.group7.jobTrackerApplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class JobApplicationTrackerApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {
	}

	@Test
	void databaseConnection() {
		assertNotNull(dataSource);
	}
}
