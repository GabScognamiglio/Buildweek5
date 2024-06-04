package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, Integer> {
}
