package pl.utp.scrumban;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class ScrumbanApplicationTests {

	@Test
	void contextLoads() {
	}

}
