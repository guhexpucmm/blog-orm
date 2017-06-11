package edu.pucmm.programacionweb2017.service;

import edu.pucmm.programacionweb2017.dao.DAOEtiqueta;
import edu.pucmm.programacionweb2017.dao.impl.DAOEtiquetaImpl;
import edu.pucmm.programacionweb2017.entidad.Etiqueta;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class ServiceEtiqueta {
    DAOEtiquetaImpl daoEtiqueta;

    public ServiceEtiqueta() {
        daoEtiqueta = new DAOEtiquetaImpl(Etiqueta.class);
    }

    public void insertar(Etiqueta etiqueta) {
        daoEtiqueta.insertar(etiqueta);
    }

    public void actualizar(Etiqueta etiqueta) {
        daoEtiqueta.actualizar(etiqueta);
    }

    public void borrar(Etiqueta etiqueta) {
        daoEtiqueta.borrar(etiqueta);
    }

    public Etiqueta encontrarPorId(Long aLong) {
        return daoEtiqueta.encontrarPorId(aLong);
    }

    public List<Etiqueta> encontrarTodos() {
        return daoEtiqueta.encontrarTodos();
    }
}
