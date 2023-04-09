package com.example.eurekaclient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;

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
		String msg = "IjIwMjMtMDQtMDlUMTQ6MDM6MjUuMDM5ODU4Ig==";
		log.info(new String(Base64.getDecoder().decode(msg),
				StandardCharsets.UTF_8));
		log.info(new JsonDeserializer<String>(String.class).deserialize("date-out-0", Base64.getDecoder().decode(msg)).toString());
	}
}
