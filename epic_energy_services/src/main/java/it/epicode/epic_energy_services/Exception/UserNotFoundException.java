package it.epicode.epic_energy_services.Exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
    super(message);
    }
}
