package edu.pucmm.programacionweb2017.entidad;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mt on 06/06/17.
 */
@Entity
@Table(name = "comentario")
@Access(AccessType.FIELD)
public class Comentario {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "comentario")
    private String comentario;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarioId", nullable = false)
    private Usuario autor;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "articuloId", nullable = false)
    private Articulo articulo;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "comentarioUsuarios", joinColumns = {@JoinColumn(name = "comentarioId")}, inverseJoinColumns = {@JoinColumn(name = "usuarioId")})
    private Set<Usuario> listaUsuarios;

    public Comentario() {
        listaUsuarios = new HashSet<>();
    }

    public Comentario(String comentario, Usuario autor, Articulo articulo, Set<Usuario> listaUsuarios) {
        this.comentario = comentario;
        this.autor = autor;
        this.articulo = articulo;
        this.listaUsuarios = listaUsuarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario usuario) {
        this.autor = usuario;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
