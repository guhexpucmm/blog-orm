package edu.pucmm.programacionweb2017.entidad;

import javax.persistence.*;

/**
 * Created by mt on 06/06/17.
 */
@Entity
@Table(name = "ETIQUETA")
@Access(AccessType.FIELD)
public class Etiqueta {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "ETIQUETA")
    private String etiqueta;

    public Etiqueta() {
    }

    public Etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
