package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvinciaRepository extends JpaRepository<Provincia,Integer> {


}
