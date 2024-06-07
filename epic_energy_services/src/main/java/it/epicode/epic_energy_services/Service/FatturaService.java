package it.epicode.epic_energy_services.Service;

import it.epicode.epic_energy_services.DTO.FatturaDto;
import it.epicode.epic_energy_services.Enums.StatoFattura;
import it.epicode.epic_energy_services.Exception.ClienteNotFoundException;
import it.epicode.epic_energy_services.Exception.FatturaNotFoundException;
import it.epicode.epic_energy_services.entity.Cliente;
import it.epicode.epic_energy_services.entity.Fattura;
import it.epicode.epic_energy_services.repository.ClienteRepository;
import it.epicode.epic_energy_services.repository.FatturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FatturaService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FatturaRepository fatturaRepository;

    public List<Fattura> findFattureByCliente(Cliente cliente){
        return fatturaRepository.findByCliente(cliente);
    }

    public List<Fattura> findFattureByStato(StatoFattura statoFattura){
        return fatturaRepository.findByStatoFattura(statoFattura);
    }

    public List<Fattura> findFattureByDataInserimento(LocalDate data){
        return fatturaRepository.findByDataInserimento(data);
    }

    public List<Fattura> findFattureByAnno(int anno){
       /* LocalDate startDate = LocalDate.of(anno, 1, 1);
        LocalDate endDate = LocalDate.of(anno, 12, 31);*/
        return fatturaRepository.findByAnno(anno);
    }


    public List<Fattura> findFattureByRangeImporti(double importoMin, double importoMax){
        return fatturaRepository.fatturePerRangeDiImporti(importoMin, importoMax);
    }



    public String saveFattura(FatturaDto fatturaDto) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(fatturaDto.getClienteId());
        if(clienteOptional.isPresent()){
         Fattura fattura = new Fattura();
                 fattura.setData(fatturaDto.getData());
                 fattura.setStatoFattura(fatturaDto.getStatoFattura());
                 fattura.setImporto(fatturaDto.getImporto());
                 fattura.setCliente(clienteOptional.get());
//                 clienteOptional.get().getFatture().add(fattura);
//                 clienteRepository.save(c)
                 fatturaRepository.save(fattura);
                 return "Fattura con numero " + fattura.getNumeroFattura() + " creata correttamente.";
        }else{
           throw new  ClienteNotFoundException("Non è stato trovato nessun cliente con id: "+ clienteOptional.get().getId());
        }

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
