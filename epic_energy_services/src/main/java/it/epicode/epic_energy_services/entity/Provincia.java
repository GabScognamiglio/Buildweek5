package it.epicode.epic_energy_services.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sigla;
    private String provincia;
    private String regione;

    @OneToMany(mappedBy = "provincia")
    private List<Comune> comuni;


}
