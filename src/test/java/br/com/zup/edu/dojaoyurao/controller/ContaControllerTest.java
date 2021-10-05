package br.com.zup.edu.dojaoyurao.controller;

import br.com.zup.edu.dojaoyurao.model.Conta;
import br.com.zup.edu.dojaoyurao.model.dto.DtoTransacaoEntrada;
import br.com.zup.edu.dojaoyurao.repository.ContaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.math.BigDecimal;

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
    @DisplayName("Deve creditar o valor especificado")
    void creditaComSucesso() throws Exception {
        Conta conta = new Conta("123", 1L, new BigDecimal("200.00"));
        contaRepository.save(conta);

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(new BigDecimal("20.00"), 1L);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/credita", body);

        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Não deve creditar caso a conta não exista")
    void naoDeveDepositarEmContaInexistente() throws Exception {

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(new BigDecimal("20.00"), 1L);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/credita", body);

        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Não deve creditar caso valores da requisição sejam nulos")
    void naoDeveDepositarComValorNulo() throws Exception {

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(null,null);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/credita", body);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("Deve debitar o valor especificado")
    void debitaComSucesso() throws Exception {
        Conta conta = new Conta("123", 1L, new BigDecimal("200.00"));
        contaRepository.save(conta);

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(new BigDecimal("20.00"), 1L);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/debita", body);

        mvc.perform(request)
                .andExpect(status().isOk());

    }

    @Test @DisplayName("Não Deve debitar o valor maior que o saldo da conta")
    void naoDeveDebitarMaisQueOSaldoDaConta() throws Exception {
        Conta conta = new Conta("123", 1L, new BigDecimal("200.00"));
        contaRepository.save(conta);

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(new BigDecimal("250.00"), 1L);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/debita", body);

        mvc.perform(request)
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @DisplayName("Não deve debitar caso valores da requisição sejam nulos")
    void naoDeveDebitarComValorNulo() throws Exception{
        DtoTransacaoEntrada body = new DtoTransacaoEntrada(null,null);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/debita", body);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Não deve debitar de caso a conta não exista")
    void naoDeveDebitarEmContaInexistenteTest() throws Exception {

        DtoTransacaoEntrada body = new DtoTransacaoEntrada(new BigDecimal("20.00"), 1L);

        MockHttpServletRequestBuilder request = putRequest("/contas/123/debita", body);

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