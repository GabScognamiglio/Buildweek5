package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Indirizzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo,Integer> {
}
