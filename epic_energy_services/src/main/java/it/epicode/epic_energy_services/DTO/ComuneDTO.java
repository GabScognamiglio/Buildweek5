package it.epicode.epic_energy_services.DTO;

import jakarta.validation.constraints.NotBlank;

public class ComuneDTO {
@NotBlank(message = "Inserire codice provincia")
    private String codiceProvincia;
    @NotBlank(message = "Inserire progressivo comune")
    private String progressivoComune;
    @NotBlank(message = "Inserire denominazione comune")
    private String denominazione;
}
