package it.epicode.epic_energy_services.Controller;

import it.epicode.epic_energy_services.DTO.UserDTO;
import it.epicode.epic_energy_services.DTO.UserLoginDTO;
import it.epicode.epic_energy_services.Exception.BadRequestException;
import it.epicode.epic_energy_services.Security.AuthenticationResponse;
import it.epicode.epic_energy_services.Service.AuthService;
import it.epicode.epic_energy_services.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;
    @PostMapping("/signup")
    @CrossOrigin(origins = "*")
    public String signup(@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).reduce("",(s, s2) -> s+s2 ));
        }

        return userService.saveUser(userDTO);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public AuthenticationResponse login(@RequestBody @Validated UserLoginDTO userLoginDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).reduce("",(s, s2) -> s+s2 ));
        }

        return authService.authenticateUserAndCreateToken(userLoginDTO);
    }
}
