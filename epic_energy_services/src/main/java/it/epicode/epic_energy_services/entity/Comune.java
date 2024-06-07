package it.epicode.epic_energy_services.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Comune {
    @Id
    @GeneratedValue
    private int id;

    private String codiceProvincia;
    private String progressivoComune;
    private String denominazione;

    @OneToMany(mappedBy = "comune")
    @JsonIgnore
    private List<Indirizzo> indirizzi;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;
}
