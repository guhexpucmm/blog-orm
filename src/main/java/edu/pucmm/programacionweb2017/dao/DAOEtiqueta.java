package edu.pucmm.programacionweb2017.dao;

import edu.pucmm.programacionweb2017.entidad.Etiqueta;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public interface DAOEtiqueta extends DAO<Etiqueta, Long> {
    @Override
    void insertar(Etiqueta etiqueta);

    @Override
    void actualizar(Etiqueta etiqueta);

    @Override
    void borrar(Etiqueta etiqueta);

    @Override
    Etiqueta encontrarPorId(Long aLong);

    @Override
    List<Etiqueta> encontrarTodos();
}
