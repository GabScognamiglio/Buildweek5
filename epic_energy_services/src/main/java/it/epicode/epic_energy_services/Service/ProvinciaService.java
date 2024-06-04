package it.epicode.epic_energy_services.Service;

import it.epicode.epic_energy_services.Exception.NotFoundException;
import it.epicode.epic_energy_services.entity.Provincia;
import it.epicode.epic_energy_services.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProvinciaService {
    @Autowired
    private ProvinciaRepository provinciaRepository;


    public Provincia findByProvincia(String provincia ){
        Optional<Provincia> provinciaOptional = provinciaRepository.findByProvincia(provincia);
                if(provinciaOptional.isPresent()) {
                    return provinciaOptional.get();

                }
                else {
                    throw new NotFoundException("Provincia non trovata");
                }
    }
}
