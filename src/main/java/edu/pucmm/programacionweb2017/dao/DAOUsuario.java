package edu.pucmm.programacionweb2017.dao;

import edu.pucmm.programacionweb2017.entidad.Usuario;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public interface DAOUsuario extends DAO<Usuario, Long> {
    @Override
    void insertar(Usuario usuario);

    @Override
    void actualizar(Usuario usuario);

    @Override
    void borrar(Usuario usuario);

    @Override
    Usuario encontrarPorId(Long aLong);

    @Override
    List<Usuario> encontrarTodos();

    Usuario encontrarPorNombreUsuario(String nombreUsuario);
}
