package it.epicode.epic_energy_services.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Comune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codiceProvincia;
    private String progressivoComune;
    private String denominazione;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;
}
