package com.udea.banco2026v;

import com.udea.banco2026v.dto.CustomerDTO;
import com.udea.banco2026v.service.DataGeneratorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class Banco2026vApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DataGeneratorService dataGeneratorService;

	@Test
	@DisplayName("Context Loads Test")
	void contextLoads() {
	}

	@Test
	@DisplayName("Main Method Test")
	void mainMethodTest() {
		// Se pasan argumentos para usar H2 y puerto aleatorio, permitiendo cubrir la línea del main
		Banco2026vApplication.main(new String[]{"--spring.profiles.active=test", "--server.port=0"});
	}

	@Test
	@DisplayName("Test de Integración de Controlador - Datos Generados")
	void testGenerateDataController() throws Exception {
		mockMvc.perform(get("/api/test/generate")
				.param("count", "5"))
				.andExpect(status().isOk())
				.andExpect(result -> {
					String content = result.getResponse().getContentAsString();
					assertThat(content).isNotNull();
					assertThat(content).contains("accountNumber");
				});
	}

	@Test
	@DisplayName("Test de Lógica de Negocio - Tamaño de Generación")
	void testDataGenerationLogic() {
		int expectedSize = 10;

		List<CustomerDTO> customers = dataGeneratorService.generateCustomers(expectedSize);

		assertThat(customers).hasSize(expectedSize);
	}

	@Test
	@DisplayName("Test de Mapeo - Integridad de DTOs Generales")
	void testDataMapping() {
		List<CustomerDTO> customers = dataGeneratorService.generateCustomers(1);
		CustomerDTO customer = customers.get(0);

		assertThat(customer.getFirstName()).isNotBlank();
		assertThat(customer.getLastName()).isNotBlank();
		assertThat(customer.getAccountNumber()).isNotBlank();
		assertThat(customer.getBalance()).isNotNull();
		assertThat(customer.getBalance()).isPositive();
	}

}
