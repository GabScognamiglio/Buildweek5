package it.epicode.epic_energy_services.Service;

import it.epicode.epic_energy_services.DTO.FatturaDto;
import it.epicode.epic_energy_services.Exception.FatturaNotFoundException;
import it.epicode.epic_energy_services.entity.Fattura;
import it.epicode.epic_energy_services.repository.FatturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FatturaService {

    @Autowired
    private FatturaRepository fatturaRepository;

    public String saveFattura(FatturaDto fatturaDto) {
        Fattura fattura = new Fattura();
        fattura.setData(fatturaDto.getData());
        fattura.setStatoFattura(fatturaDto.getStatoFattura());
        fattura.setImporto(fatturaDto.getImporto());

        fatturaRepository.save(fattura);

        return "Fattura con numero " + fattura.getNumeroFattura() + " creata correttamente.";
    }

    public Page<Fattura> getFatture(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return fatturaRepository.findAll(pageable);
    }

    public Optional<Fattura> getFatturaByNumeroFattura(int numeroFattura) {
        return fatturaRepository.findById(numeroFattura);
    }

    public Fattura updateFattura(int numeroFattura, FatturaDto fatturaDto) {
        Optional<Fattura> fatturaOptional = getFatturaByNumeroFattura(numeroFattura);
        if (fatturaOptional.isPresent()){
            Fattura fattura = fatturaOptional.get();
            fattura.setData(fatturaDto.getData());
            fattura.setStatoFattura(fatturaDto.getStatoFattura());
            fattura.setImporto(fatturaDto.getImporto());

            return fatturaRepository.save(fattura);
        } else {
            throw new FatturaNotFoundException("Fattura con numero fattura " + numeroFattura + " non trovata");
        }
    }

    public String deleteFattura(int numeroFattura) {
        Optional<Fattura> fatturaOptional = getFatturaByNumeroFattura(numeroFattura);

        if (fatturaOptional.isPresent()){
            Fattura fattura = fatturaOptional.get();
            fatturaRepository.delete(fattura);
            return "Fattura numero " + numeroFattura + " cancellata correttamente";
        } else {
            throw new FatturaNotFoundException("Fattura con numero fattura " + numeroFattura + " non trovata");
        }
    }
}
