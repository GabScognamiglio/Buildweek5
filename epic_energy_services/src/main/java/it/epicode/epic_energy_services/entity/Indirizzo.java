package it.epicode.epic_energy_services.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.epic_energy_services.Enums.TipoIndirizzo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Indirizzo {

    @Id
    @GeneratedValue
    private int id;

    private String via;

    private int civico;

    private String localita;

    private String cap;

    @ManyToOne
    @JoinColumn(name = "comune_id")
    private Comune comune;

    @Enumerated(EnumType.STRING)
    private TipoIndirizzo tipoIndirizzo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;


}
