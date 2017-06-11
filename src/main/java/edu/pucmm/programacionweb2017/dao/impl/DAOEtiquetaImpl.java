package edu.pucmm.programacionweb2017.dao.impl;

import edu.pucmm.programacionweb2017.dao.DAOEtiqueta;
import edu.pucmm.programacionweb2017.entidad.Etiqueta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class DAOEtiquetaImpl extends DAOImpl<Etiqueta, Long> implements DAOEtiqueta {
    private static final Logger logger = LoggerFactory.getLogger(DAOEtiquetaImpl.class);

    public DAOEtiquetaImpl(Class<Etiqueta> etiquetaClass) {
        super(etiquetaClass);
    }

    @Override
    public void insertar(Etiqueta etiqueta) {
        super.insertar(etiqueta);
    }

    @Override
    public void actualizar(Etiqueta etiqueta) {
        super.actualizar(etiqueta);
    }

    @Override
    public void borrar(Etiqueta etiqueta) {
        super.borrar(etiqueta);
    }

    @Override
    public Etiqueta encontrarPorId(Long aLong) {
        return super.encontrarPorId(aLong);
    }

    @Override
    public List<Etiqueta> encontrarTodos() {
        return super.encontrarTodos();
    }
}
