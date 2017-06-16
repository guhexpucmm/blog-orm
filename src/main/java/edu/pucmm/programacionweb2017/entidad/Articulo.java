package edu.pucmm.programacionweb2017.entidad;

import javax.persistence.*;
import java.util.*;


/**
 * Created by mt on 06/06/17.
 */
@Entity
@Table(name = "articulo")
@Access(AccessType.FIELD)
public class Articulo {

    @Id
    @GeneratedValue()
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(name = "titulo", nullable = false)
    private String titulo;
    @Column(name = "cuerpo", nullable = false)
    private String cuerpo;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "autorId")
    private Usuario autor;
    @Column(name = "fecha", nullable = false)
    private Date fecha;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "articuloComentarios", joinColumns = {@JoinColumn(name = "articuloId")}, inverseJoinColumns = {@JoinColumn(name = "comentarioId")})
    private Set<Comentario> listaComentarios;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "articuloEtiquetas", joinColumns = {@JoinColumn(name = "articuloId", unique = true)}, inverseJoinColumns = {@JoinColumn(name = "etiquetaId")}, uniqueConstraints = @UniqueConstraint(columnNames = {"articuloId", "etiquetaId"}))
    private List<Etiqueta> listaEtiquetas;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "articuloValoraciones", joinColumns = {@JoinColumn(name = "articuloId")}, inverseJoinColumns = {@JoinColumn(name = "valoracionId")})
    private Set<Valoracion> listaValoraciones;
    @Transient
    private String resumen;

    public Articulo() {
        listaComentarios = new HashSet<>();
        listaEtiquetas = new ArrayList<>();
        listaValoraciones = new HashSet<>();
    }

    public Articulo(String titulo, String cuerpo, Usuario autor, Date fecha, Set<Comentario> listaComentarios, List<Etiqueta> listaEtiquetas, Set<Valoracion> listaValoraciones) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.listaComentarios = listaComentarios;
        this.listaEtiquetas = listaEtiquetas;
        this.listaValoraciones = listaValoraciones;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Set<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(Set<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public List<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(List<Etiqueta> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }

    public Set<Valoracion> getListaValoraciones() {
        return listaValoraciones;
    }

    public void setListaValoraciones(Set<Valoracion> listaValoraciones) {
        this.listaValoraciones = listaValoraciones;
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