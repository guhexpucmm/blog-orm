package edu.pucmm.programacionweb2017.service;

import edu.pucmm.programacionweb2017.dao.DAOComentario;
import edu.pucmm.programacionweb2017.dao.impl.DAOComentarioImpl;
import edu.pucmm.programacionweb2017.entidad.Comentario;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class ServiceComentario {
    DAOComentarioImpl daoComentario;

    public ServiceComentario() {
        daoComentario = new DAOComentarioImpl(Comentario.class);
    }

    public void insertar(Comentario comentario) {
        daoComentario.insertar(comentario);
    }

    public void actualizar(Comentario comentario) {
        daoComentario.actualizar(comentario);
    }

    public void borrar(Comentario comentario) {
        daoComentario.borrar(comentario);
    }

    public Comentario encontrarPorId(Long aLong) {
        return daoComentario.encontrarPorId(aLong);
    }

    public List<Comentario> encontrarTodos() {
        return daoComentario.encontrarTodos();
    }
}
