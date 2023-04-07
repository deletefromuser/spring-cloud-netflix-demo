package com.example.eurekaclient;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.TestSupportBinderConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({ TestSupportBinderConfiguration.class })
class EurekaClientApplicationTests {

	// @Autowired
	// private InputDestination input;

	// @Autowired
	// private OutputDestination output;

	// @Test
	// void contextLoads() {
	// 	input.send(new GenericMessage<byte[]>("hello".getBytes()));
	// 	assertThat(output.receive().getPayload()).isEqualTo("HELLO".getBytes());
	// }

}
