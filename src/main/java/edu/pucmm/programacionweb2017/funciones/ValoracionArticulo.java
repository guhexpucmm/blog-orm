package edu.pucmm.programacionweb2017.funciones;

import edu.pucmm.programacionweb2017.entidad.Articulo;
import edu.pucmm.programacionweb2017.service.ServiceArticulo;
import edu.pucmm.programacionweb2017.service.ServiceValoracion;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * Created by gusta on 16-Jun-17.
 */
public class ValoracionArticulo implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        Articulo articulo = new ServiceArticulo().encontrarPorId(Long.parseLong(String.valueOf(list.get(0))));
        edu.pucmm.programacionweb2017.entidad.Valoracion valoracion = new edu.pucmm.programacionweb2017.entidad.Valoracion(Boolean.valueOf(String.valueOf(list.get(1))), articulo.getAutor(), articulo);
        new ServiceValoracion().insertar(valoracion);
        return "Le diste like";
    }
}
