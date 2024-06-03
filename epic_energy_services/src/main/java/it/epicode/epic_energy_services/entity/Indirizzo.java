package it.epicode.epic_energy_services.entity;

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

    private Comune comune;

    private TipoIndirizzo tipoIndirizzo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
