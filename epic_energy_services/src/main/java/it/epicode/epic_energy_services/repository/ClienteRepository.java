package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
