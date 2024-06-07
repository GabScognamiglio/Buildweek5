package it.epicode.epic_energy_services.Controller;

import it.epicode.epic_energy_services.DTO.ClienteDto;
import it.epicode.epic_energy_services.DTO.FatturaDto;
import it.epicode.epic_energy_services.Enums.StatoFattura;
import it.epicode.epic_energy_services.Exception.BadRequestException;
import it.epicode.epic_energy_services.Exception.ClienteNotFoundException;
import it.epicode.epic_energy_services.Exception.FatturaNotFoundException;
import it.epicode.epic_energy_services.Service.ClienteService;
import it.epicode.epic_energy_services.Service.FatturaService;
import it.epicode.epic_energy_services.entity.Cliente;
import it.epicode.epic_energy_services.entity.Fattura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FatturaController {
    @Autowired
    private FatturaService fatturaService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/fatture")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')") //solo chi è ADMIN è autorizzato!!
    public String saveFattura(@RequestBody @Validated FatturaDto fatturaDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, a) -> s + a));
        }
        return fatturaService.saveFattura(fatturaDto);
    }

    @GetMapping("/fatture")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Fattura> getAllFatture(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "numeroFattura") String sortBy) {
        return fatturaService.getFatture(page, size, sortBy);
    }

    @GetMapping("/fatture/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Fattura getFatturaById(@PathVariable int id) {
        Optional<Fattura> fatturaOptional = fatturaService.getFatturaByNumeroFattura(id);
        if (fatturaOptional.isPresent()) {
            return fatturaOptional.get();
        } else {
            throw new FatturaNotFoundException("Fattura con numero " + id +" non trovata");
        }
    }

    @PutMapping("/fatture/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Fattura updateFattura(@PathVariable int id, @RequestBody @Validated FatturaDto fatturaDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", ((s, s2) -> s + s2)));
        }

        return fatturaService.updateFattura(id, fatturaDto);
    }

    @DeleteMapping("/fatture/{id}")
    @PreAuthorize("hasAuthority('ADMIN')") //solo chi è ADMIN è autorizzato
    public String deleteFattura(@PathVariable int id) {
        return fatturaService.deleteFattura(id);
    }

    //LISTA 3
    /*-------Q1------*/
    @GetMapping("/fattureByCliente/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Fattura> getFattureByCliente(@PathVariable int id) {
        Optional<Cliente> clienteOptional = clienteService.getClienteById(id);
        if (clienteOptional.isPresent()) {
            return fatturaService.findFattureByCliente(clienteOptional.get());
        } else {
            throw new ClienteNotFoundException("Cliente con ID: " + id +" non trovato");
        }
    }

    /*-------Q2------*/
    @GetMapping("/fattureByStato/{statoFattura}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Fattura> getFattureByStatoFattura(@PathVariable("statoFattura") StatoFattura statoFattura) {
            return fatturaService.findFattureByStato(statoFattura);
    }

    /*-------Q3------*/

    @GetMapping("/fattureByDataInserimento/{dataInserimento}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Fattura> getFattureDataInserimento(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInserimento) {
        return fatturaService.findFattureByDataInserimento(dataInserimento);
    }

    /*-------Q4------*/

    @GetMapping("/fattureByAnno/{anno}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Fattura> getFattureByAnno (@PathVariable int anno) {
        return fatturaService.findFattureByAnno(anno);
    }

    /*-------Q5------*/

    @GetMapping("/fattureByRangeImporto/{importMin}/{importMax}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Fattura> getFattureByRangeImporti (@PathVariable double importMin,@PathVariable double importMax) {
        return fatturaService.findFattureByRangeImporti(importMin, importMax);
    }
}
