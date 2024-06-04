package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ComuneRepository extends JpaRepository<Comune,Integer> {

    public Optional<Comune> findByDenominazione(String denominazione);
}
