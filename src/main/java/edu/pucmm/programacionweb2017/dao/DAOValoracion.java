package edu.pucmm.programacionweb2017.dao;

import edu.pucmm.programacionweb2017.entidad.Usuario;
import edu.pucmm.programacionweb2017.entidad.Valoracion;

import java.util.List;

/**
 * Created by gusta on 16-Jun-17.
 */
public interface DAOValoracion extends DAO<Valoracion, Long> {
    @Override
    void insertar(Valoracion valoracion);

    @Override
    void actualizar(Valoracion valoracion);

    @Override
    void borrar(Valoracion valoracion);

    @Override
    Valoracion encontrarPorId(Long aLong);

    @Override
    List<Valoracion> encontrarTodos();

    Usuario encontrarUsuarioValoracion(Usuario usuario);
}
