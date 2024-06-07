package it.epicode.epic_energy_services.Service;

import it.epicode.epic_energy_services.DTO.IndirizzoDto;
import it.epicode.epic_energy_services.Exception.FatturaNotFoundException;
import it.epicode.epic_energy_services.entity.Indirizzo;
import it.epicode.epic_energy_services.repository.ClienteRepository;
import it.epicode.epic_energy_services.repository.IndirizzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IndirizzoService {

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public String saveIndirizzo(IndirizzoDto indirizzoDto) {

        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(indirizzoDto.getVia());
        indirizzo.setCap(indirizzoDto.getCap());
        indirizzo.setLocalita(indirizzoDto.getLocalita());
        indirizzo.setCivico(indirizzoDto.getCivico());
        indirizzo.setTipoIndirizzo(indirizzoDto.getTipoIndirizzo());

        indirizzoRepository.save(indirizzo);

        return "Indirizzo con ID: " + indirizzo.getId() + " creata correttamente.";
    }

}
