package it.epicode.epic_energy_services.DTO;

import it.epicode.epic_energy_services.Enums.TipoIndirizzo;
import it.epicode.epic_energy_services.entity.Comune;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IndirizzoDto {
    @NotBlank(message = "La via non può essere vuota o mancante o composta da soli spazi")
    private String via;

    @NotBlank(message = "Il comune non può essere vuota, mancante o composta da soli spazi")
    private String comuneDenominazione;

    @NotBlank(message = "Il CAP non può essere vuoto, mancante o composta da soli spazi")
    private String cap;

    @NotBlank(message = "La località non può essere vuota, mancante o composta da soli spazi")
    private String localita;

    @Min(value = 1)
    private int civico;

    @NotBlank(message = "il tipo indirizzo non può essere vuoto, mancante o composto da soli spazi")
    private TipoIndirizzo tipoIndirizzo;
}
