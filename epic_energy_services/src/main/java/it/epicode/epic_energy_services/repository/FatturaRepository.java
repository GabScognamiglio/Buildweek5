package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FatturaRepository extends JpaRepository<Fattura, Integer> {
}
