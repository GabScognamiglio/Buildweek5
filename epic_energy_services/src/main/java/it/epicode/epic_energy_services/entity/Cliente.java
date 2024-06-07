package it.epicode.epic_energy_services.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.epic_energy_services.Enums.TipoCliente;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ragioneSociale;

    private String partitaIva;

    private String email;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    private LocalDate dataInserimento;

    private LocalDate dataUltimoContatto;

    private double fatturatoAnnuale;

    private String pec;

    private String telefono;

    private String emailContatto;

    private String nomeContatto;

    private String cognomeContatto;

    private String telefonoContatto;

    private String logoAziendale;

    @OneToMany(mappedBy = "cliente")
    private List<Indirizzo> indirizzi;


    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Fattura> fatture;


}
