package edu.pucmm.programacionweb2017.dao.impl;

import edu.pucmm.programacionweb2017.dao.DAOValoracion;
import edu.pucmm.programacionweb2017.entidad.Usuario;
import edu.pucmm.programacionweb2017.entidad.Valoracion;
import edu.pucmm.programacionweb2017.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gusta on 16-Jun-17.
 */
public class DAOValoracionImpl extends DAOImpl<Valoracion, Long> implements DAOValoracion {
    private static final Logger logger = LoggerFactory.getLogger(DAOImpl.class);

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

    @Override
    public Usuario encontrarUsuarioValoracion(Usuario usuario) {
        Session session = null;
        Transaction transaction = null;
        Query query = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            query = session.createQuery("from Valoracion v where v.usuario = :usuario").setParameter("usuario", usuario);

            Valoracion valoracion = (Valoracion) query.uniqueResult();

            return valoracion.getUsuario();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.debug("Error al ejecutar un select el objeto en la base de datos.", e);
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public Valoracion encontrarValoracion(Usuario usuario, Long id) {
        Session session = null;
        Transaction transaction = null;
        Query query = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            query = session.createQuery("from Valoracion v where v.usuario.id = :uid and articulo.id = :id").setParameter("uid", usuario.getId()).setParameter("id", id);

            return (Valoracion) query.uniqueResult();
        } catch (HibernateException e) {
            transaction.rollback();
            logger.debug("Error al ejecutar un select el objeto en la base de datos.", e);
            return null;
        } finally {
            session.close();
        }
    }
}
