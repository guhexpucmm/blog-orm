package edu.pucmm.programacionweb2017;

import edu.pucmm.programacionweb2017.entidad.*;
import edu.pucmm.programacionweb2017.hibernate.HibernateUtil;
import edu.pucmm.programacionweb2017.service.*;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.SQLException;
import java.util.*;

import static edu.pucmm.programacionweb2017.database.BootstrapServices.startDb;
import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

/**
 * Created by mt on 04/06/17.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final ServiceArticulo serviceArticulo = new ServiceArticulo();
    private static final ServiceComentario serviceComentario = new ServiceComentario();
    private static final ServiceEtiqueta serviceEtiqueta = new ServiceEtiqueta();
    private static final ServiceUsuario serviceUsuario = new ServiceUsuario();
    private static final ServiceValoracion serviceValoracion = new ServiceValoracion();

    static int numeroPagina = 1;
    static boolean estaLogueado = estaLogueado(false);
    static Usuario usuarioLogueado = new Usuario();
    static String idSesion = "";

    public static void main(String[] args) throws SQLException {
        logger.info("Iniciando aplicacion");

        logger.info("Iniciando pantalla de debugueo");
        enableDebugScreen();//REMOVER PARA PRODUCCION

        logger.info("Creando el folder estatico");
        staticFileLocation("/public/");
        logger.info("Comenzando la base de datos");
        startDb(); //ABRE EL PUERTO DE LA BASE DE DATOS

        HibernateUtil.openSession().close();

        logger.info("Crear el usuario admin por defecto");
        crearUsuarioAdmin();

        logger.info("Creando la configuracion del template de freemarker");
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(Main.class, "/templates/");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/", (request, response) -> {
            numeroPagina = 1;
            response.redirect("/home/1");
            return "Bienvenido";
        });

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/crearUsuario/", (request, response) -> {
            if (invalidarSesion(request.session())) {
                return new ModelAndView(null, "/sesionInvalida.ftl");
            }

            Map<String, Object> attributes = new HashMap<>();


            return new ModelAndView(null, "/crearUsuario.ftl");
        }, freeMarkerEngine);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        post("/crearUsuario/", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(request.queryMap().get("Nom").value());
            nuevoUsuario.setUsername(request.queryMap().get("User").value());
            nuevoUsuario.setPassword(request.queryMap().get("Pass").value());
            if (request.queryParams("Adm").equals("true")) {
                nuevoUsuario.setAdministrador(true);

            } else if (request.queryParams("Adm").equals("false")) {
                nuevoUsuario.setAdministrador(false);
            }
            if (request.queryParams("Aut").equals("true")) {
                nuevoUsuario.setAutor(true);
            } else {
                nuevoUsuario.setAutor(false);
            }

            serviceUsuario.insertar(nuevoUsuario);

            attributes.put("articulos", serviceArticulo.encontrarTodos());
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("estaLogueado", estaLogueado);

            response.redirect("/home");


            return new ModelAndView(null, "/crearUsuario.ftl");
        }, freeMarkerEngine);

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        get("/home", (request, response) -> {
            numeroPagina = 1;
            response.redirect("/home/1");
            return "Bienvenido";
        });

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/home/:numeroPagina", (request, response) -> {
            if (invalidarSesion(request.session()) && estaLogueado) {

                return new ModelAndView(null, "/sesionInvalida.ftl");
            }
            numeroPagina = Integer.parseInt(request.params("numeroPagina"));


            int listaFin = numeroPagina * 5;
            int listaInicio = listaFin - 5;

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("numeroPagina", numeroPagina);
            attributes.put("articulos", serviceArticulo.obtenerArticulosPaginacion(listaInicio, listaFin));
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("estaLogueado", estaLogueado);

            return new ModelAndView(attributes, "/index.ftl");
        }, freeMarkerEngine);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        post("/home", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            boolean existe = false;
            List<Usuario> listadoUsuarioBD = serviceUsuario.encontrarTodos();
            usuarioLogueado.setUsername(request.queryParams("User"));
            usuarioLogueado.setPassword(request.queryParams("Pass"));

            for (int i = 0; i < listadoUsuarioBD.size(); i++) {
                if (usuarioLogueado.getUsername().equals(listadoUsuarioBD.get(i).getUsername()) && usuarioLogueado.getPassword().equals(listadoUsuarioBD.get(i).getPassword())) {
                    existe = true;
                    if (existe) {
                        estaLogueado = estaLogueado(true);
                        usuarioLogueado = listadoUsuarioBD.get(i);
                        idSesion = request.session().id();
                    }
                    //FALTA CREAR CONDICION SINO EXISTE*************************************
                }
            }
            attributes.put("numeroPagina", numeroPagina);
            attributes.put("articulos", serviceArticulo.encontrarTodos());
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("estaLogueado", estaLogueado);


            response.redirect("/home");
            return new ModelAndView(attributes, "index.ftl");
        }, freeMarkerEngine);

///!////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //RUTA PARA CERRAR SESION
        get("/cerraSesion", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            estaLogueado = false;
            usuarioLogueado = new Usuario();
            idSesion = "";


            response.redirect("/home");

            return new ModelAndView(null, "/cerraSesion.ftl");
        }, freeMarkerEngine);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //FILTRO PARA DETENER USUARIOS QUE NO TIENEN PERMISOS PARA CREAR USUARIO
        before("/crearArticulo/:idUsuario", (request, response) -> {
            Usuario usuarioLogueado = serviceUsuario.encontrarPorId(Long.parseLong(request.params("idUsuario")));

            if (!(usuarioLogueado.isAdministrador() || usuarioLogueado.isAutor())) {
                response.redirect("/home");
                halt(401, "No tiene permisos para crear articulo!");
            }


        });

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/crearArticulo/:idUsuario", (request, response) -> {
            if (invalidarSesion(request.session()) && estaLogueado) {
                return new ModelAndView(null, "/sesionInvalida.ftl");
            }
            usuarioLogueado = serviceUsuario.encontrarPorId(Long.parseLong(request.params("idUsuario")));

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("usuario", usuarioLogueado);
            return new ModelAndView(attributes, "/crearArticulo.ftl");
        }, freeMarkerEngine);

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        post("/crearArticulo/:idUsuario", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            Set<Etiqueta> etiquetas = new HashSet<>();

            for (String string : Arrays.asList(request.queryMap().get("Eti").value().split(","))) {
                Etiqueta etiqueta = serviceEtiqueta.encontrarPorEtiqueta(string);
                if (etiqueta != null) {
                    etiquetas.add(etiqueta);
                } else {
                    etiquetas.add(new Etiqueta(string));
                }
            }

            Articulo nuevoArticulo = new Articulo();
            nuevoArticulo.setTitulo(request.queryParams("Titu"));
            nuevoArticulo.setCuerpo(request.queryParams("Cuer"));
            nuevoArticulo.setFecha(new Date());
            nuevoArticulo.setAutor(serviceUsuario.encontrarPorId(Long.parseLong(request.params().get(":idusuario"))));
            nuevoArticulo.setListaEtiquetas(etiquetas);

            attributes.put("usuario", usuarioLogueado);
            serviceArticulo.insertar(nuevoArticulo);

            response.redirect("/home/1");

            return new ModelAndView(attributes, "/crearArticulo.ftl");
        }, freeMarkerEngine);

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/administrarArticulo/:idArticulo", (request, response) -> {
            if (invalidarSesion(request.session()) && estaLogueado) {
                return new ModelAndView(null, "/sesionInvalida.ftl");
            }
            Long articuloSeleccionId = Long.parseLong(request.params("idArticulo"));
            Articulo articuloSeleccionado = serviceArticulo.encontrarPorId(articuloSeleccionId);

            Map<String, Object> attributes = new HashMap<>();

            attributes.put("estaLogueado", estaLogueado);
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("articuloSeleccionado", articuloSeleccionado);
            attributes.put("valoracionesPositivas", serviceArticulo.obtenerValoracionesPositivas(articuloSeleccionado).size());
            attributes.put("valoracionesNegativas", serviceArticulo.obtenerValoracionesNegativas(articuloSeleccionado).size());
            attributes.put("etiquetas", articuloSeleccionado.getListaEtiquetas());
            attributes.put("comentarios", articuloSeleccionado.getListaComentarios());

            return new ModelAndView(attributes, "/administrarArticulo.ftl");
        }, freeMarkerEngine);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        post("/crearComentario/:idArticulo", (request, response) -> {
            Articulo articulo = serviceArticulo.encontrarPorId(Long.parseLong(request.params("idArticulo")));
            Map<String, Object> attributes = new HashMap<>();
            Comentario nuevoComentario = new Comentario();

            nuevoComentario.setComentario(request.queryParams("Coment"));
            nuevoComentario.setArticulo(articulo);
            nuevoComentario.setAutor(usuarioLogueado);

            articulo.getListaComentarios().add(nuevoComentario);

            serviceArticulo.actualizar(articulo);
            serviceComentario.insertar(nuevoComentario);

            response.redirect("/home/1"); //ARREGLAR QUE AL CREAR MANDA A INDEX **********************************************

            return new ModelAndView(attributes, "/crearComentario.ftl");
        }, freeMarkerEngine);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/eliminarArticulo/:idArticulo", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            Long ArticuloId = Long.parseLong(request.params("idArticulo"));

            serviceArticulo.borrar(serviceArticulo.encontrarPorId(ArticuloId));
            //Comentario.eliminarComentario(ComentarioId);
            response.redirect("/home/1");//ARREGLAR QUE AL ELIMINAR MANDA A INDEX **********************************************

            return new ModelAndView(attributes, "/eliminarArticulo.ftl");
        }, freeMarkerEngine);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //FILTRO PARA DETENER USUARIO QUE NO SEA AUTOR PARA ELIMINAR O ADMINISTRADOR
        before("/eliminarArticulo/:idArticulo", (request, response) -> {
            Long articuloSeleccionId = Long.parseLong(request.params("idArticulo"));
            System.out.println(articuloSeleccionId);
            Articulo articuloSeleccionado = serviceArticulo.encontrarPorId(articuloSeleccionId);

            Map<String, Object> attributes = new HashMap<>();

            StringBuilder etiquetas = new StringBuilder();

            for (Etiqueta etiqueta : articuloSeleccionado.getListaEtiquetas()) {
                etiquetas.append(etiqueta.getEtiqueta() + ",");
            }

            if (etiquetas.charAt(etiquetas.length() - 1) == ',')
                etiquetas.deleteCharAt(etiquetas.length() - 1);

            attributes.put("estaLogueado", estaLogueado);
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("articuloSeleccionado", articuloSeleccionado);
            attributes.put("valoracionesPositivas", serviceArticulo.obtenerValoracionesPositivas(articuloSeleccionado).size());
            attributes.put("valoracionesNegativas", serviceArticulo.obtenerValoracionesNegativas(articuloSeleccionado).size());
            attributes.put("etiquetas", etiquetas.toString());
            attributes.put("comentarios", articuloSeleccionado.getListaComentarios());

            if (!(usuarioLogueado.getId().equals(articuloSeleccionado.getAutor()) || usuarioLogueado.isAdministrador())) {
                response.redirect("/administrarArticulo/" + articuloSeleccionId);
                halt(401, "No tiene permisos para eliminar articulo!");
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/modificarArticulo/:idArticulo", (request, response) -> {
            if (invalidarSesion(request.session()) && estaLogueado) {
                return new ModelAndView(null, "/sesionInvalida.ftl");
            }
            Long idArticulo = Long.parseLong(request.params("idArticulo"));
            Articulo articulo = serviceArticulo.encontrarPorId(idArticulo);

            Map<String, Object> attributes = new HashMap<>();
            StringBuilder etiquetas = new StringBuilder();

            for (Etiqueta etiqueta : articulo.getListaEtiquetas()) {
                etiquetas.append(etiqueta.getEtiqueta() + ",");
            }

            if (etiquetas.charAt(etiquetas.length() - 1) == ',')
                etiquetas.deleteCharAt(etiquetas.length() - 1);

            attributes.put("articulo", articulo);
            attributes.put("etiquetas", etiquetas);

            return new ModelAndView(attributes, "/modificarArticulo.ftl");
        }, freeMarkerEngine);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        post("/modificarArticulo/:idArticulo", (request, response) -> {
            Articulo articulo = serviceArticulo.encontrarPorId(Long.parseLong(request.params("idArticulo")));

            Map<String, Object> attributes = new HashMap<>();

            List<Etiqueta> etiquetas = new ArrayList<>();

            for (String string : Arrays.asList(request.queryMap().get("Eti").value().split(","))) {
                etiquetas.add(new Etiqueta(string));
            }

            articulo.setTitulo(request.queryMap().get("Titu").value());
            articulo.setCuerpo(request.queryMap().get("Cuer").value());
            articulo.getListaEtiquetas().clear();
            articulo.getListaEtiquetas().addAll(etiquetas);

            serviceArticulo.actualizar(articulo);

            attributes.put("articulo", articulo);
            response.redirect("/home/1");

            return new ModelAndView(attributes, "/modificarArticulo.ftl");
        }, freeMarkerEngine);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //FILTRO PARA DETENER USUARIO QUE NO SEA AUTOR PARA MODIFICAR O ADMINISTRADOR
        before("/modificarArticulo/:idArticulo", (request, response) -> {
            Long articuloSeleccionId = Long.parseLong(request.params("idArticulo"));
            System.out.println(articuloSeleccionId);
            Articulo articuloSeleccionado = serviceArticulo.encontrarPorId(articuloSeleccionId);

            Map<String, Object> attributes = new HashMap<>();

            StringBuilder etiquetas = new StringBuilder();

            for (Etiqueta etiqueta : articuloSeleccionado.getListaEtiquetas()) {
                etiquetas.append(etiqueta.getEtiqueta() + ",");
            }

            if (etiquetas.charAt(etiquetas.length() - 1) == ',')
                etiquetas.deleteCharAt(etiquetas.length() - 1);

            attributes.put("estaLogueado", estaLogueado);
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("articuloSeleccionado", articuloSeleccionado);
            attributes.put("valoracionesPositivas", serviceArticulo.obtenerValoracionesPositivas(articuloSeleccionado).size());
            attributes.put("valoracionesNegativas", serviceArticulo.obtenerValoracionesNegativas(articuloSeleccionado).size());
            attributes.put("etiquetas", etiquetas.toString());
            attributes.put("comentarios", articuloSeleccionado.getListaComentarios());
            if (!(usuarioLogueado.getId() == articuloSeleccionado.getAutor().getId() || usuarioLogueado.isAdministrador())) {

                response.redirect("/administrarArticulo/" + articuloSeleccionId);
                halt(401, "No tiene permisos para modificar articulo!");
            }


        });

        get("/articulosConEtiquetas/", (request, response) -> {
            Etiqueta etiqueta = serviceEtiqueta.encontrarPorId(Long.parseLong(request.queryParams("etiqueta")));
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("articulos", serviceArticulo.obtenerArticulosDadaUnaEtiqueta(etiqueta));

            return new ModelAndView(attributes, "/articulosConEtiquetas.ftl");
        }, freeMarkerEngine);

        get("/valoracion/", (request, response) -> {
            Usuario usuario = serviceUsuario.encontrarPorId(Long.valueOf(request.queryParams("usuario")));
            Articulo articulo = serviceArticulo.encontrarPorId(Long.parseLong(request.queryParams("articulo")));
            boolean like = Boolean.valueOf(String.valueOf(request.queryParams("valoracion")));

            Valoracion v = serviceValoracion.encontrarValoracion(usuario, articulo.getId());

            if (v == null) {
                v = new Valoracion(like, usuario, articulo);

                serviceValoracion.insertar(v);

                response.redirect("/administrarArticulo/" + articulo.getId());
            } else {
                response.redirect("/administrarArticulo/" + articulo.getId());
            }
            return new ModelAndView(null, "/valoracion.ftl");
        }, freeMarkerEngine);
    }

    private static boolean estaLogueado(boolean estaLogueado) {
        return estaLogueado;
    }

    private static void crearUsuarioAdmin() {
        Usuario usuario = new Usuario();
        usuario.setId(new Long(1));
        usuario.setNombre("Administrador");
        usuario.setUsername("admin");
        usuario.setPassword("admin");
        usuario.setAdministrador(true);
        usuario.setAutor(true);

        logger.info("Verificando si el usuario ya se introdujo. El usuario por defecto");
        if (serviceUsuario.encontrarPorId(new Long(1)) == null)
            serviceUsuario.insertar(usuario);
    }

    private static boolean invalidarSesion(Session session) {
        if (idSesion.equals("")) {
            idSesion = session.id();

        } else if (!(session.id().equals(idSesion))) {
            session.invalidate();
            return true;


        }
        return false;

    }
}
