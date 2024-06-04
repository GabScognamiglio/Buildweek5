package it.epicode.epic_energy_services.Controller;

import it.epicode.epic_energy_services.DTO.ClienteDto;
import it.epicode.epic_energy_services.DTO.FatturaDto;
import it.epicode.epic_energy_services.Exception.BadRequestException;
import it.epicode.epic_energy_services.Exception.ClienteNotFoundException;
import it.epicode.epic_energy_services.Exception.FatturaNotFoundException;
import it.epicode.epic_energy_services.Service.FatturaService;
import it.epicode.epic_energy_services.entity.Cliente;
import it.epicode.epic_energy_services.entity.Fattura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FatturaController {
    @Autowired
    private FatturaService fatturaService;

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
    public Page<Fattura> getLaptops(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return fatturaService.getFatture(page, size, sortBy);
    }

    @GetMapping("/fatture/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Fattura getFatturaById(@PathVariable int id) {
        Optional<Fattura> fatturaOptional = fatturaService.getFatturaByNumeroFattura(id);
        if (fatturaOptional.isPresent()) {
            return fatturaOptional.get();
        } else {
            throw new FatturaNotFoundException("Fattura con id " + id +" non trovatAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAashhhh");
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
}
