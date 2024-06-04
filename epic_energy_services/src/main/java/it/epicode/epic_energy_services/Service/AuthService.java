package it.epicode.epic_energy_services.Service;

import it.epicode.epic_energy_services.DTO.UserLoginDTO;
import it.epicode.epic_energy_services.Exception.UnauthorizedException;
import it.epicode.epic_energy_services.Security.JwtTool;
import it.epicode.epic_energy_services.entity.Cliente;
import it.epicode.epic_energy_services.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTool jwtTool;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public String authenticateUserAndCreateToken(UserLoginDTO userLoginDTO){
     User user = userService.getUserByEmail(userLoginDTO.getEmail());

        if (passwordEncoder.matches(userLoginDTO.getPassword() ,user.getPassword())) {
        return jwtTool.createToken(user); // metodo createToken vuole l'utente per creare il token
        }else{
            throw  new UnauthorizedException("Dati errati, controllare i dati inseriti e prova nuovamente, se riscontri ancora problemi contatta l'assistenza di TicketGenius");
        }
    }


}
