package it.epicode.epic_energy_services.DTO;

import it.epicode.epic_energy_services.Enums.TipoCliente;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;


@Data
public class ClienteDto {

    private String ragioneSociale;

    private String partivaIva;

    private String email;

    private TipoCliente tipoCliente;

    private double fatturatoAnnuale;

    private String pec;

    private String telefono;

    private String emailContatto;

    private String nomeContatto;

    private String cognomeContatto;

    private String telefonoContatto;

    private String logoAziendale;
}
