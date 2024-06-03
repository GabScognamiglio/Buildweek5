package it.epicode.epic_energy_services.Exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(String message){
        super(message);
    }
}
