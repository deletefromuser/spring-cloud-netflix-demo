package com.example.eurekaclient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
// @Import({ TestSupportBinderConfiguration.class })
@Slf4j
class EurekaClientApplicationTests {

	// @Autowired
	// private InputDestination input;

	// @Autowired
	// private OutputDestination output;

	// @Test
	// void contextLoads() {
	// input.send(new GenericMessage<byte[]>("hello".getBytes()));
	// assertThat(output.receive().getPayload()).isEqualTo("HELLO".getBytes());
	// }

	@Test
	public void testMyMethod() {
		log.info(new String(Base64.getDecoder().decode("IjIwMjMtMDQtMDlUMTM6NDk6MzQuNTA2MDg5Ig=="),
				StandardCharsets.UTF_8));
	}
}
