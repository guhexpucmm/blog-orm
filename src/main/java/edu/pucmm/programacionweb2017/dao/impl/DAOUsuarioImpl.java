package edu.pucmm.programacionweb2017.dao.impl;

import edu.pucmm.programacionweb2017.dao.DAOUsuario;
import edu.pucmm.programacionweb2017.entidad.Usuario;
import edu.pucmm.programacionweb2017.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class DAOUsuarioImpl extends DAOImpl<Usuario, Long> implements DAOUsuario {
    private static final Logger logger = LoggerFactory.getLogger(DAOUsuarioImpl.class);

    public DAOUsuarioImpl(Class<Usuario> usuarioClass) {
        super(usuarioClass);
    }

    @Override
    public void insertar(Usuario usuario) {
        super.insertar(usuario);
    }

    @Override
    public void actualizar(Usuario usuario) {
        super.actualizar(usuario);
    }

    @Override
    public void borrar(Usuario usuario) {
        super.borrar(usuario);
    }

    @Override
    public Usuario encontrarPorId(Long aLong) {
        return super.encontrarPorId(aLong);
    }

    @Override
    public List<Usuario> encontrarTodos() {
        return super.encontrarTodos();
    }

    @Override
    public Usuario encontrarPorNombreUsuario(String nombreUsuario) {
        Session session = null;
        Transaction transaction = null;
        Query query = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            query = session.createQuery("from Usuario where nombre = :nombre").setParameter("nombre", nombreUsuario);

            return (Usuario) query.uniqueResult();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.debug("Error al ejecutar un select el objeto en la base de datos.", e);
            return null;
        } finally {
            session.close();
        }
    }
}
