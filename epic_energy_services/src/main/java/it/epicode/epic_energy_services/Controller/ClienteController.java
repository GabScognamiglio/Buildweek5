package it.epicode.epic_energy_services.Controller;

import it.epicode.epic_energy_services.DTO.ClienteDto;
import it.epicode.epic_energy_services.Exception.BadRequestException;
import it.epicode.epic_energy_services.Exception.ClienteNotFoundException;
import it.epicode.epic_energy_services.Service.ClienteService;
import it.epicode.epic_energy_services.entity.Cliente;
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
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/clienti")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')") //solo chi è ADMIN è autorizzato!!
    public String saveCliente(@RequestBody @Validated ClienteDto clienteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, a) -> s + a));
        }
        return clienteService.saveCliente(clienteDto);
    }

    @GetMapping("/clienti")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Cliente> getLaptops(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return clienteService.getClientiConPaginazione(page, size, sortBy);
    }

    @GetMapping("/clienti/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Cliente getClienteById(@PathVariable int id) {
        Optional<Cliente> clienteOptional = clienteService.getClienteById(id);
        if (clienteOptional.isPresent()) {
            return clienteOptional.get();
        } else {
            throw new ClienteNotFoundException("Cliente con id " + id +" non trovato");
        }
    }

    @PutMapping("/clienti/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente updateCliente(@PathVariable int id, @RequestBody @Validated ClienteDto clienteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", ((s, s2) -> s + s2)));
        }

        return clienteService.updateCliente(id, clienteDto);
    }

    @DeleteMapping("/clienti/{id}")
    @PreAuthorize("hasAuthority('ADMIN')") //solo chi è ADMIN è autorizzato
    public String deleteCliente(@PathVariable int id) {
        return clienteService.deleteCliente(id);


    }
}
