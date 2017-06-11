package edu.pucmm.programacionweb2017.entidad;

import javax.persistence.*;

/**
 * Created by mt on 06/06/17.
 */
@Entity
@Table(name = "COMENTARIO")
@Access(AccessType.FIELD)
public class Comentario {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "COMENTARIO")
    private String comentario;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario autor;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICULO_ID", nullable = false)
    private Articulo articulo;

    public Comentario() {
    }

    public Comentario(String comentario, Usuario autor, Articulo articulo) {
        this.comentario = comentario;
        this.autor = autor;
        this.articulo = articulo;
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
