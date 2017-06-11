package edu.pucmm.programacionweb2017.entidad;

import javax.persistence.*;

/**
 * Created by mt on 06/06/17.
 */
@Entity
@Table(name = "USUARIO")
@Access(AccessType.FIELD)
public class Usuario {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ADMINISTRADOR")
    private boolean administrador;
    @Column(name = "AUTOR")
    private boolean autor;

    public Usuario() {
    }

    public Usuario(String username, String nombre, String password, boolean administrador, boolean autor) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.administrador = administrador;
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public boolean isAutor() {
        return autor;
    }

    public void setAutor(boolean autor) {
        this.autor = autor;
    }
}
