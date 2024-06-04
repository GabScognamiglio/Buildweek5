package it.epicode.epic_energy_services.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Provincia {
    @Id
    @GeneratedValue
    private int id;

    private String sigla;
    private String provincia;
    private String regione;

    @OneToMany(mappedBy = "provincia", fetch = FetchType.EAGER)
    private List<Comune> comuni;


}
