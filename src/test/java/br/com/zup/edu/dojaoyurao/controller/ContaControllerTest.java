package br.com.zup.edu.dojaoyurao.controller;

import br.com.zup.edu.dojaoyurao.model.Conta;
import br.com.zup.edu.dojaoyurao.model.dto.DtoTransacaoEntrada;
import br.com.zup.edu.dojaoyurao.repository.ContaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ContaControllerTest {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        contaRepository.deleteAll();
    }

    @AfterEach
    void after() {
        contaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve depositar o valor especificado")
    void depositoTest() throws Exception {
        Conta conta = new Conta("123", 1L, new BigDecimal("200.00"));
        contaRepository.save(conta);

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(new BigDecimal("20.00"), 1L);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/deposita", body);

        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Não deve depositar caso a conta não exista")
    void depositoEmContaInexistenteTest() throws Exception {

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(new BigDecimal("20.00"), 1L);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/deposita", body);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    public static MockHttpServletRequestBuilder putRequest(
            String url,
            DtoTransacaoEntrada body
    ) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body));

    }
}