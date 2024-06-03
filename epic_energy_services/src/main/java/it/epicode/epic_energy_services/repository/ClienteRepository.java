package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Cliente;
import it.epicode.epic_energy_services.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    public Optional<Cliente> findByEmail(String email);
}
