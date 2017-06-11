package edu.pucmm.programacionweb2017.dao.impl;

import edu.pucmm.programacionweb2017.dao.DAOComentario;
import edu.pucmm.programacionweb2017.entidad.Comentario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class DAOComentarioImpl extends DAOImpl<Comentario,Long> implements DAOComentario {
    private static final Logger logger = LoggerFactory.getLogger(DAOComentarioImpl.class);

    public DAOComentarioImpl(Class<Comentario> comentarioClass) {
        super(comentarioClass);
    }

    @Override
    public void insertar(Comentario comentario) {
        super.insertar(comentario);
    }

    @Override
    public void actualizar(Comentario comentario) {
        super.actualizar(comentario);
    }

    @Override
    public void borrar(Comentario comentario) {
        super.borrar(comentario);
    }

    @Override
    public Comentario encontrarPorId(Long aLong) {
        return super.encontrarPorId(aLong);
    }

    @Override
    public List<Comentario> encontrarTodos() {
        return super.encontrarTodos();
    }
}
