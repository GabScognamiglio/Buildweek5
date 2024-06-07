package it.epicode.epic_energy_services.DTO;

import it.epicode.epic_energy_services.Enums.StatoFattura;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class FatturaDto {

    private LocalDate data;

    @Min(value = 0)
    private double importo;

    private StatoFattura statoFattura;


    @Min(value = 1)
    private int clienteId;

}
