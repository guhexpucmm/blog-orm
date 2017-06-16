package edu.pucmm.programacionweb2017.service;

import edu.pucmm.programacionweb2017.dao.impl.DAOArticuloImpl;
import edu.pucmm.programacionweb2017.entidad.Articulo;
import edu.pucmm.programacionweb2017.entidad.Etiqueta;
import edu.pucmm.programacionweb2017.entidad.Valoracion;

import java.util.List;

/**
 * Created by gusta on 10-Jun-17.
 */
public class ServiceArticulo{
    DAOArticuloImpl daoArticulo;

    public ServiceArticulo() {
        daoArticulo = new DAOArticuloImpl(Articulo.class);
    }

    public void insertar(Articulo articulo) {
        daoArticulo.insertar(articulo);
    }

    public void actualizar(Articulo articulo) {
        daoArticulo.actualizar(articulo);
    }

    public void borrar(Articulo articulo) {
        daoArticulo.borrar(articulo);
    }

    public void merge(Articulo articulo) {
        daoArticulo.merge(articulo);
    }

    public Articulo encontrarPorId(Long aLong) {
        return daoArticulo.encontrarPorId(aLong);
    }

    public List<Articulo> encontrarTodos() {
        return daoArticulo.encontrarTodos();
    }

    public List<Valoracion> obtenerValoracionesPositivas(Articulo articulo) {
        return daoArticulo.obtenerValoracionesPositivas(articulo);
    }

    public List<Valoracion> obtenerValoracionesNegativas(Articulo articulo) {
        return daoArticulo.obtenerValoracionesNegativas(articulo);
    }

    public List<Articulo> obtenerArticulosDadaUnaEtiqueta(Etiqueta etiqueta) {
        return daoArticulo.obtenerArticulosDadaUnaEtiqueta(etiqueta);
    }

    public List<Articulo> obtenerArticulosPaginacion(int inicio, int fin) {
        return daoArticulo.obtenerArticulosPaginacion(inicio, fin);
    }
}
