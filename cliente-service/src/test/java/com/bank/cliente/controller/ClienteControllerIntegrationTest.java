package com.bank.cliente.controller;

import com.bank.cliente.dto.ClienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClienteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAndGetCliente() {
        // Given
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("1234567890");
        clienteDTO.setDireccion("Otavalo sn y principal");
        clienteDTO.setTelefono("098254785");
        clienteDTO.setClienteId("JOSE001");
        clienteDTO.setContrasena("1234");
        clienteDTO.setEstado(true);

        String baseUrl = "http://localhost:" + port + "/clientes";

        HttpEntity<ClienteDTO> request = new HttpEntity<>(clienteDTO);
        ResponseEntity<ClienteDTO> createResponse = restTemplate.postForEntity(baseUrl, request, ClienteDTO.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals("Jose Lema", createResponse.getBody().getNombre());
        assertEquals("JOSE001", createResponse.getBody().getClienteId());

        Long clienteId = createResponse.getBody().getId();
        ResponseEntity<ClienteDTO> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + clienteId, ClienteDTO.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Jose Lema", getResponse.getBody().getNombre());
        assertEquals("JOSE001", getResponse.getBody().getClienteId());
    }

    @Test
    void testCreateClienteWithDuplicateClienteId() {
        ClienteDTO clienteDTO1 = new ClienteDTO();
        clienteDTO1.setNombre("Jose Lema");
        clienteDTO1.setGenero("Masculino");
        clienteDTO1.setEdad(30);
        clienteDTO1.setIdentificacion("1111111111");
        clienteDTO1.setDireccion("Otavalo sn y principal");
        clienteDTO1.setTelefono("098254785");
        clienteDTO1.setClienteId("DUPLICATE001");
        clienteDTO1.setContrasena("1234");
        clienteDTO1.setEstado(true);

        ClienteDTO clienteDTO2 = new ClienteDTO();
        clienteDTO2.setNombre("Marianela Montalvo");
        clienteDTO2.setGenero("Femenino");
        clienteDTO2.setEdad(25);
        clienteDTO2.setIdentificacion("2222222222");
        clienteDTO2.setDireccion("Amazonas y NNUU");
        clienteDTO2.setTelefono("097548965");
        clienteDTO2.setClienteId("DUPLICATE001");
        clienteDTO2.setContrasena("5678");
        clienteDTO2.setEstado(true);

        String baseUrl = "http://localhost:" + port + "/clientes";

        HttpEntity<ClienteDTO> request1 = new HttpEntity<>(clienteDTO1);
        ResponseEntity<ClienteDTO> response1 = restTemplate.postForEntity(baseUrl, request1, ClienteDTO.class);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        HttpEntity<ClienteDTO> request2 = new HttpEntity<>(clienteDTO2);
        ResponseEntity<String> response2 = restTemplate.postForEntity(baseUrl, request2, String.class);
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    }

    @Test
    void testUpdateCliente() {
        // Given
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan Osorio");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(35);
        clienteDTO.setIdentificacion("1122334455");
        clienteDTO.setDireccion("13 junio y Equinoccial");
        clienteDTO.setTelefono("098874587");
        clienteDTO.setClienteId("JUAN001");
        clienteDTO.setContrasena("1245");
        clienteDTO.setEstado(true);

        String baseUrl = "http://localhost:" + port + "/clientes";

        HttpEntity<ClienteDTO> createRequest = new HttpEntity<>(clienteDTO);
        ResponseEntity<ClienteDTO> createResponse = restTemplate.postForEntity(baseUrl, createRequest, ClienteDTO.class);
        Long clienteId = createResponse.getBody().getId();

        clienteDTO.setNombre("Juan Osorio Updated");
        clienteDTO.setTelefono("099999999");

        HttpEntity<ClienteDTO> updateRequest = new HttpEntity<>(clienteDTO);
        ResponseEntity<ClienteDTO> updateResponse = restTemplate.exchange(
                baseUrl + "/" + clienteId, HttpMethod.PUT, updateRequest, ClienteDTO.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("Juan Osorio Updated", updateResponse.getBody().getNombre());
        assertEquals("099999999", updateResponse.getBody().getTelefono());
    }

    @Test
    void testDeleteCliente() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Cliente To Delete");
        clienteDTO.setGenero("Masculino");
        clienteDTO.setEdad(40);
        clienteDTO.setIdentificacion("9988776655");
        clienteDTO.setDireccion("Address");
        clienteDTO.setTelefono("099887766");
        clienteDTO.setClienteId("DELETE001");
        clienteDTO.setContrasena("password");
        clienteDTO.setEstado(true);

        String baseUrl = "http://localhost:" + port + "/clientes";

        HttpEntity<ClienteDTO> createRequest = new HttpEntity<>(clienteDTO);
        ResponseEntity<ClienteDTO> createResponse = restTemplate.postForEntity(baseUrl, createRequest, ClienteDTO.class);
        Long clienteId = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + clienteId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + clienteId, String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
