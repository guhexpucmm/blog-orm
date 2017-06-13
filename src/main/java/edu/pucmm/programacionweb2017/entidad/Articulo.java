package edu.pucmm.programacionweb2017.entidad;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by mt on 06/06/17.
 */
@Entity
@Table(name = "ARTICULO")
@Access(AccessType.FIELD)
public class Articulo {

    @Id
    @GeneratedValue()
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(name = "TITULO", nullable = false)
    private String titulo;
    @Column(name = "CUERPO", nullable = false)
    private String cuerpo;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "AUTOR_ID")
    private Usuario autor;
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ARTICULO_COMENTARIOS", joinColumns = { @JoinColumn(name = "ARTICULO_ID") }, inverseJoinColumns = { @JoinColumn(name = "COMENTARIO_ID") })
    private Set<Comentario> listaComentarios;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ARTICULO_ETIQUETAS", joinColumns = { @JoinColumn(name = "ARTICULO_ID") }, inverseJoinColumns = { @JoinColumn(name = "ETIQUETA_ID") })
    private Set<Etiqueta> listaEtiquetas;
    @Transient
    private String resumen;

    public Articulo() {
        listaComentarios = new HashSet<>();
        listaEtiquetas = new HashSet<>();
    }

    public Articulo(String titulo, String cuerpo, Usuario autor, LocalDate fecha, Set<Comentario> listaComentarios, Set<Etiqueta> listaEtiquetas, String resumen) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.listaComentarios = listaComentarios;
        this.listaEtiquetas = listaEtiquetas;
        this.resumen = resumen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Set<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(Set<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public Set<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(Set<Etiqueta> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }

    public String getResumen() {
        if (cuerpo.length() > 70)
            return cuerpo.substring(0, 70);
        else return cuerpo;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
}
