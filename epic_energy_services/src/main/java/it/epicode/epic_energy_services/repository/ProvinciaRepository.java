package it.epicode.epic_energy_services.repository;

import it.epicode.epic_energy_services.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia,Integer> {

    public Optional<Provincia> findByProvincia(String provincia);

}
