package edu.pucmm.programacionweb2017.entidad;

import javax.persistence.*;

/**
 * Created by gusta on 15-Jun-17.
 */
@Entity
@Table(name = "valoracion")
@Access(AccessType.FIELD)
public class Valoracion {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "tipo")
    private boolean tipo;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarioId")
    private Usuario usuario;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "articuloId")
    private Articulo articulo;

    public Valoracion() {
    }

    public Valoracion(boolean tipo, Usuario usuario, Articulo articulo) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.articulo = articulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
