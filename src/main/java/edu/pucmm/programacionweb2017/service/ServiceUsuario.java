package edu.pucmm.programacionweb2017.service;

import edu.pucmm.programacionweb2017.dao.DAOUsuario;
import edu.pucmm.programacionweb2017.dao.impl.DAOUsuarioImpl;
import edu.pucmm.programacionweb2017.entidad.Usuario;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class ServiceUsuario {
    DAOUsuarioImpl daoUsuario;

    public ServiceUsuario() {
        daoUsuario = new DAOUsuarioImpl(Usuario.class);
    }

    public void insertar(Usuario usuario) {
        daoUsuario.insertar(usuario);
    }

    public void actualizar(Usuario usuario) {
        daoUsuario.actualizar(usuario);
    }

    public void borrar(Usuario usuario) {
        daoUsuario.borrar(usuario);
    }

    public Usuario encontrarPorId(Long aLong) {
        return daoUsuario.encontrarPorId(aLong);
    }

    public List<Usuario> encontrarTodos() {
        return daoUsuario.encontrarTodos();
    }

    public Usuario encontrarPorNombreUsuario(String nombreUsuario) {
        return daoUsuario.encontrarPorNombreUsuario(nombreUsuario);
    }
}
