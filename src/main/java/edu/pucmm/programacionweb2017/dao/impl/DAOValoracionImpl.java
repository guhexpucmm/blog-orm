package edu.pucmm.programacionweb2017.dao.impl;

import edu.pucmm.programacionweb2017.dao.DAOValoracion;
import edu.pucmm.programacionweb2017.entidad.Valoracion;

import java.util.List;

/**
 * Created by gusta on 16-Jun-17.
 */
public class DAOValoracionImpl extends DAOImpl<Valoracion, Long> implements DAOValoracion {
    public DAOValoracionImpl(Class<Valoracion> valoracionClass) {
        super(valoracionClass);
    }

    @Override
    public void insertar(Valoracion valoracion) {
        super.insertar(valoracion);
    }

    @Override
    public void actualizar(Valoracion valoracion) {
        super.actualizar(valoracion);
    }

    @Override
    public void borrar(Valoracion valoracion) {
        super.borrar(valoracion);
    }

    @Override
    public Valoracion encontrarPorId(Long aLong) {
        return super.encontrarPorId(aLong);
    }

    @Override
    public List<Valoracion> encontrarTodos() {
        return super.encontrarTodos();
    }
}
