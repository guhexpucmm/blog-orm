package edu.pucmm.programacionweb2017.service;

import edu.pucmm.programacionweb2017.dao.impl.DAOValoracionImpl;
import edu.pucmm.programacionweb2017.entidad.Valoracion;

import java.util.List;

/**
 * Created by gusta on 16-Jun-17.
 */
public class ServiceValoracion {
    private DAOValoracionImpl daoValoracion;

    public ServiceValoracion() {
        daoValoracion = new DAOValoracionImpl(Valoracion.class);
    }

    public void insertar(Valoracion valoracion) {
        daoValoracion.insertar(valoracion);
    }

    public void actualizar(Valoracion valoracion) {
        daoValoracion.actualizar(valoracion);
    }

    public void borrar(Valoracion valoracion) {
        daoValoracion.borrar(valoracion);
    }

    public Valoracion encontrarPorId(Long aLong) {
        return daoValoracion.encontrarPorId(aLong);
    }

    public List<Valoracion> encontrarTodos() {
        return daoValoracion.encontrarTodos();
    }
}
