package br.com.zup.edu.dojaoyurao.controller;


import br.com.zup.edu.dojaoyurao.model.Conta;
import br.com.zup.edu.dojaoyurao.model.dto.DtoTransacaoEntrada;
import br.com.zup.edu.dojaoyurao.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    ContaRepository contaRepository;

    @PostMapping("/create/")
    @Transactional
    public ResponseEntity<?> criar(@RequestBody @Valid DtoTransacaoEntrada request){
        Conta novaConta = new Conta("1",request.getIdCliente(),request.getValor());
        contaRepository.save(novaConta);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{numeroConta}/credita")
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public ResponseEntity<?> credita(@PathVariable @NotNull String numeroConta, @RequestBody @Valid DtoTransacaoEntrada request){
        Optional<Conta> contaOptional = contaRepository.findByNumeroConta(numeroConta);
        if(contaOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Conta contaAtt = contaOptional.get();
        contaAtt.credita(request.getValor());
        contaRepository.save(contaAtt);
        return ResponseEntity.ok(contaAtt);
    }

    @PutMapping("/{numeroConta}/debita")
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public ResponseEntity<Conta> debita(@PathVariable String numeroConta, @RequestBody @Valid DtoTransacaoEntrada request){
        Optional<Conta> contaOptional = contaRepository.findByNumeroConta(numeroConta);
        if(contaOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Conta contaAtt = contaOptional.get();
        if (contaAtt.getSaldo().compareTo(request.getValor()) < 0 ){
            return ResponseEntity.unprocessableEntity().build();
        }
        contaAtt.debita(request.getValor());
        contaRepository.save(contaAtt);
        return ResponseEntity.ok(contaAtt);

    }

}
