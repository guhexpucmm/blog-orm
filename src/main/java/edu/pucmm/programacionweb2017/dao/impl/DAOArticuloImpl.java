package edu.pucmm.programacionweb2017.dao.impl;

import edu.pucmm.programacionweb2017.dao.DAOArticulo;
import edu.pucmm.programacionweb2017.entidad.Articulo;
import edu.pucmm.programacionweb2017.entidad.Etiqueta;
import edu.pucmm.programacionweb2017.entidad.Valoracion;
import edu.pucmm.programacionweb2017.hibernate.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class DAOArticuloImpl extends DAOImpl<Articulo, Long> implements DAOArticulo {
    private static final Logger logger = LoggerFactory.getLogger(DAOArticuloImpl.class);

    public DAOArticuloImpl(Class<Articulo> articuloClass) {
        super(articuloClass);
    }

    @Override
    public void insertar(Articulo articulo) {
        super.insertar(articulo);
    }

    @Override
    public void actualizar(Articulo articulo) {
        super.actualizar(articulo);
    }

    @Override
    public void borrar(Articulo articulo) {
        super.borrar(articulo);
    }

    @Override
    public Articulo encontrarPorId(Long aLong) {
        return super.encontrarPorId(aLong);
    }

    @Override
    public List<Articulo> encontrarTodos() {
        return super.encontrarTodos();
    }

    @Override
    public List<Valoracion> obtenerValoracionesPositivas(Articulo articulo) {
        Session session = null;
        Transaction transaction = null;
        Query query = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            query = session.createQuery("from Valoracion v where v.tipo = true and v.articulo.id = :articulo").setParameter("articulo", articulo.getId());

            if (query.list() != null) {
                return query.list();
            } else {
                return new ArrayList<>();
            }
        } catch (HibernateException e) {
            transaction.rollback();
            logger.debug("Error al ejecutar un select el objeto en la base de datos.", e);
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Valoracion> obtenerValoracionesNegativas(Articulo articulo) {
        Session session = null;
        Transaction transaction = null;
        Query query = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            query = session.createQuery("from Valoracion v where v.tipo = false and v.articulo.id = :articulo").setParameter("articulo", articulo.getId());

            if (query.list() != null) {
                return query.list();
            } else {
                return new ArrayList<>();
            }
        } catch (HibernateException e) {
            transaction.rollback();
            logger.debug("Error al ejecutar un select el objeto en la base de datos.", e);
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Articulo> obtenerArticulosDadaUnaEtiqueta(Etiqueta etiqueta) {
        Session session = null;
        Transaction transaction = null;
        Query query = null;

        try {
            session = HibernateUtil.openSession();
            transaction = session.beginTransaction();

            query = session.createQuery("from Articulo a, Etiqueta e where e.etiqueta = :etiqueta")
                    .setParameter("etiqueta", etiqueta.getEtiqueta());

            if (query.list() != null) {
                return query.list();
            } else {
                return new ArrayList<>();
            }
        } catch (HibernateException e) {
            transaction.rollback();
            logger.debug("Error al ejecutar un select el objeto en la base de datos.", e);
            return null;
        } finally {
            session.close();
        }
    }
}