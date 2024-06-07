package it.epicode.epic_energy_services.Security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import it.epicode.epic_energy_services.entity.User;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String token;

    @JsonIgnoreProperties(value = "password")
    private User user;

    public AuthenticationResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
