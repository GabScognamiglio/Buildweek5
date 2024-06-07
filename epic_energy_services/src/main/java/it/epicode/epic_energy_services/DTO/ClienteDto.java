package it.epicode.epic_energy_services.DTO;

import it.epicode.epic_energy_services.Enums.TipoCliente;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class ClienteDto {

    @NotBlank(message = "La ragione sociale non può essere vuota, mancante o composta da soli spazi")
    private String ragioneSociale;

    @NotBlank(message = "La partita iva non può essere vuota, mancante o composta da soli spazi")
    private String partitaIva;

    @Email
    @NotBlank(message = "L'email non può essere vuota, mancante o composta da soli spazi")
    private String email;


    private TipoCliente tipoCliente;

    @Min(value = 0)
    private double fatturatoAnnuale;

    @Email
    @NotBlank(message = "La pec non può essere vuota, mancante o composta da soli spazi")
    private String pec;

    @NotBlank(message = "Il telefono non può essere vuoto, mancante o composta da soli spazi")
    private String telefono;

    @Email
    @NotBlank(message = "L'email contatto non può essere vuota, mancante o composta da soli spazi")
    private String emailContatto;

    @NotBlank(message = "Nome contatto non può essere vuoto, mancante o composta da soli spazi")
    private String nomeContatto;

    @NotBlank(message = "Cognome contatto non può essere vuoto, mancante o composta da soli spazi")
    private String cognomeContatto;

    @NotNull(message ="Telefono contatto non può essere vuoto, mancante o composta da soli spazi")
    private String telefonoContatto;

    private String logoAziendale;

    @NotNull
    private List<IndirizzoDto> indirizzi;
}
