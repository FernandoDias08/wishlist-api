package com.fernandodias.wishlist.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
public class BaseApiTest {

	@Autowired
	public MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

	static {
		mongoDBContainer.start();
	}

	@DynamicPropertySource
	static void mongoDbProperties(DynamicPropertyRegistry registry) {

		mongoDBContainer.start();
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	public ResultActions callPost(String path, Object content) throws Exception {
		String json = mapper.writeValueAsString(content);
		System.out.println(">>>>>>>JSON CONTENT: " + json);

		return mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json));
	}

	public ResultActions callGet(String path) throws Exception {
		return mockMvc.perform(get(path).accept(MediaType.APPLICATION_JSON)).andDo(print());
	}

	public ResultActions callDelete(String path) throws Exception {
		return mockMvc.perform(delete(path).accept(MediaType.APPLICATION_JSON)).andDo(print());
	}

}
