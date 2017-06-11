package edu.pucmm.programacionweb2017.dao;

import edu.pucmm.programacionweb2017.entidad.Articulo;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public interface DAOArticulo extends DAO<Articulo, Long> {
    @Override
    void actualizar(Articulo articulo);

    @Override
    void borrar(Articulo articulo);

    @Override
    Articulo encontrarPorId(Long aLong);

    @Override
    List<Articulo> encontrarTodos();
}
