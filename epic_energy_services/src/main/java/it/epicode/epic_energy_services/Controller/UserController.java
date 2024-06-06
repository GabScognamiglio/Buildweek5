package it.epicode.epic_energy_services.Controller;

import it.epicode.epic_energy_services.DTO.UserDTO;
import it.epicode.epic_energy_services.Exception.BadRequestException;
import it.epicode.epic_energy_services.Service.UserService;
import it.epicode.epic_energy_services.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy){
        return userService.getUserConPaginazione(page,size,sortBy);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Optional<User> getUser(@PathVariable int id){
       return userService.getUserById(id);
    }



    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser( @PathVariable int id,@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + s2));
        }
        return userService.updateUser(userDTO, id);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser( @PathVariable int id){
        return userService.deleteUser(id);
    }


}
