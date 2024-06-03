package it.epicode.epic_energy_services.DTO;

import it.epicode.epic_energy_services.Enums.StatoFattura;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;


@Data
public class FatturaDto {

    private LocalDate data;

    @Min(value = 0)
    private double importo;

    private StatoFattura statoFattura;

}
