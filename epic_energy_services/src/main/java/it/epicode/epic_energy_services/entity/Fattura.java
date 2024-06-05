package it.epicode.epic_energy_services.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.epic_energy_services.Enums.StatoFattura;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Fattura {

    @Id
    @GeneratedValue
    private int numeroFattura;

    private LocalDate data;
    private double importo;

    @Enumerated(EnumType.STRING)
    private StatoFattura statoFattura;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

}
