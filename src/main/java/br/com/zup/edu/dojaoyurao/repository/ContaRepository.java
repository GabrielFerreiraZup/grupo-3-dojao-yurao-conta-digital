package br.com.zup.edu.dojaoyurao.repository;

import br.com.zup.edu.dojaoyurao.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta,Long> {

    Optional<Conta> findByNumeroConta(String numeroConta);

}
