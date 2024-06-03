package it.epicode.epic_energy_services.Exception;

public class FatturaNotFoundException extends RuntimeException{
    public FatturaNotFoundException(String message){
        super(message);
    }
}
