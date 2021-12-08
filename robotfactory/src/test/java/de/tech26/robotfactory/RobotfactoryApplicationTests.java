package de.tech26.robotfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tech26.robotfactory.factory.RobotFactory;
import de.tech26.robotfactory.model.OrderRequest;
import de.tech26.robotfactory.model.OrderResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class RobotfactoryApplicationTests {

	ObjectMapper om = new ObjectMapper();
	@Autowired
	MockMvc mockMvc;

	@Test
	public void shouldOrderRobotSuccessTestCase() throws Exception {
		int expected = RobotFactory.getRoboPartByCode("A").getAvailable()-1;
		mockMvc.perform(post("/orders")
				.contentType("application/json")
				.content(om.writeValueAsString(new OrderRequest(Arrays.asList("I","A","D","F")))))
				.andExpect(jsonPath("$.order_id", notNullValue()))
				.andExpect(jsonPath("$.total", equalTo(160.11)))
				.andExpect(status().isCreated());
		Assert.assertEquals(expected, RobotFactory.getRoboPartByCode("A").getAvailable());
	}


	@Test
	public void shouldNotAllowInvalidBody() throws Exception {
		mockMvc.perform(post("/orders")
				.contentType("application/json")
				.content(om.writeValueAsString(new OrderRequest(Arrays.asList("BENDER")))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldNotAllowInvalidRobotConfiguration() throws Exception {
		mockMvc.perform(post("/orders")
				.contentType("application/json")
				.content(om.writeValueAsString(new OrderRequest(Arrays.asList("A","C","I","D")))))
				.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void noStockAvailableTestCase() throws Exception {
		mockMvc.perform(post("/orders")
				.contentType("application/json")
				.content(om.writeValueAsString(new OrderRequest(Arrays.asList("I","C","D","F")))))
				.andExpect(status().isImUsed());
	}
	@Test
	public void shouldThrowUnavailableTestCase() throws Exception {
		mockMvc.perform(post("/orders")
				.contentType("application/json")
				.content(om.writeValueAsString(new OrderRequest(Arrays.asList("I","A","D","F")))));
		mockMvc.perform(post("/orders")
				.contentType("application/json")
				.content(om.writeValueAsString(new OrderRequest(Arrays.asList("I","A","D","F")))))
				.andExpect(status().isImUsed());
	}
}
