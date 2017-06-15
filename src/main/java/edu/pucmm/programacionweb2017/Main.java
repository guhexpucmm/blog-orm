package edu.pucmm.programacionweb2017;

import edu.pucmm.programacionweb2017.entidad.Articulo;
import edu.pucmm.programacionweb2017.entidad.Comentario;
import edu.pucmm.programacionweb2017.entidad.Etiqueta;
import edu.pucmm.programacionweb2017.entidad.Usuario;
import edu.pucmm.programacionweb2017.hibernate.HibernateUtil;
import edu.pucmm.programacionweb2017.service.ServiceArticulo;
import edu.pucmm.programacionweb2017.service.ServiceComentario;
import edu.pucmm.programacionweb2017.service.ServiceEtiqueta;
import edu.pucmm.programacionweb2017.service.ServiceUsuario;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.SQLException;
import java.time.LocalDate;
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

    static int ultimoIdArticulo = 0;
    static int numeroPagina = 1;
    static boolean estaLogueado = estaLogueado(false);
    static Usuario usuarioLogueado = new Usuario();

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
        FreeMarkerEngine freeMarkerEngine= new FreeMarkerEngine(configuration);


        get("/", (request,response)-> {

            Map<String, Object> attributes = new HashMap<>();
            response.redirect("/home/");
            return new ModelAndView(null, "/index.ftl");
        }, freeMarkerEngine );

        get("/crearUsuario/", (request,response)-> {

            Map<String, Object> attributes = new HashMap<>();


            return new ModelAndView(null, "/crearUsuario.ftl");
        }, freeMarkerEngine );

        post("/crearUsuario/", (request,response)-> {

            Map<String, Object> attributes = new HashMap<>();
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(request.queryMap().get("Nom").value());
            nuevoUsuario.setUsername(request.queryMap().get("User").value());
            nuevoUsuario.setPassword(request.queryMap().get("Pass").value());
            nuevoUsuario.setAdministrador(false);
            nuevoUsuario.setAutor(true);

            serviceUsuario.insertar(nuevoUsuario);

            attributes.put("articulos",serviceArticulo.encontrarTodos());
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("estaLogueado", estaLogueado);

            response.redirect("/home/");



            return new ModelAndView(null, "/crearUsuario.ftl");
        }, freeMarkerEngine );

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        get("/home", (request,response)-> {


            response.redirect("/home/1");

            return new ModelAndView(null, "/index.ftl");
        }, freeMarkerEngine );

        get("/home/:numeroPagina", (request,response)-> {
            List<Articulo> articulosPorPagina = serviceArticulo.encontrarTodos();
            numeroPagina=Integer.parseInt(request.params("numeroPagina"));
            int listaFin = numeroPagina *5;
            int listaInicio = listaFin - 5;

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("numeroPagina",numeroPagina);
            if(serviceArticulo.encontrarTodos().size() < listaFin){
                attributes.put("articulos",articulosPorPagina.subList(listaInicio,serviceArticulo.encontrarTodos().size()));
            } else {
                attributes.put("articulos",articulosPorPagina.subList(listaInicio,listaFin));
            }
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("estaLogueado", estaLogueado);

            return new ModelAndView(attributes, "/index.ftl");
        }, freeMarkerEngine );

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        post("/home/", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            boolean existe = false;
            List<Usuario> listadoUsuarioBD = serviceUsuario.encontrarTodos();
            usuarioLogueado.setUsername(request.queryParams("User"));
            usuarioLogueado.setPassword(request.queryParams("Pass"));

            for(int i=0;i<listadoUsuarioBD.size();i++){
                if(usuarioLogueado.getUsername().equals(listadoUsuarioBD.get(i).getUsername()) && usuarioLogueado.getPassword().equals(listadoUsuarioBD.get(i).getPassword())){
                    existe=true;
                    if(existe){
                        estaLogueado=estaLogueado(true);
                        usuarioLogueado = listadoUsuarioBD.get(i);
                    }

                    //FALTA CREAR CONDICION SINO EXISTE*************************************

                }
            }
            attributes.put("numeroPagina",numeroPagina);
            attributes.put("articulos",serviceArticulo.encontrarTodos());
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("estaLogueado", estaLogueado);

            return new ModelAndView(attributes, "index.ftl");
        }, freeMarkerEngine);

///!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //RUTA PARA CERRAR SESION
        get("/home/:cerrarSesion", (request,response)-> {

            Map<String, Object> attributes = new HashMap<>();
            estaLogueado=false;
            usuarioLogueado=new Usuario();


            response.redirect("/home/1");

            return new ModelAndView(null, "/index.ftl");
        }, freeMarkerEngine );

        //!////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //FILTRO PARA DETENER USUARIOS QUE NO TIENEN PERMISOS PARA CREAR USUARIO
        before("/crearArticulo/:idUsuario", (request, response) -> {
            Usuario usuarioLogueado = serviceUsuario.encontrarPorId(Long.parseLong(request.params("idUsuario")));

            if (!(usuarioLogueado.isAdministrador() || usuarioLogueado.isAutor())) {
                halt(401, "No tiene permisos para crear articulo!");
            }


        });

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        get("/crearArticulo/:idUsuario", (request,response)-> {
            usuarioLogueado = serviceUsuario.encontrarPorId(Long.parseLong(request.params("idUsuario")));

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("usuario",usuarioLogueado);
            return new ModelAndView(attributes, "/crearArticulo.ftl");
        }, freeMarkerEngine );

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        post("/crearArticulo/:idUsuario", (request,response)-> {

            Map<String, Object> attributes = new HashMap<>();
            List<Etiqueta> etiquetas = new ArrayList<>();

            for (String string : Arrays.asList(request.queryMap().get("Eti").value().split(","))) {
                etiquetas.add(new Etiqueta(string));
            }

            Articulo nuevoArticulo = new Articulo();
            nuevoArticulo.setTitulo(request.queryParams("Titu"));
            nuevoArticulo.setCuerpo(request.queryParams("Cuer"));
            nuevoArticulo.setFecha(LocalDate.now());
            nuevoArticulo.setAutor(serviceUsuario.encontrarPorId(Long.parseLong(request.params().get(":idusuario"))));
            nuevoArticulo.getListaEtiquetas().addAll(etiquetas);

            attributes.put("usuario",usuarioLogueado);
            serviceArticulo.insertar(nuevoArticulo);

            response.redirect("/home/1");

            return new ModelAndView(attributes, "/crearArticulo.ftl");
        }, freeMarkerEngine );

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/administrarArticulo/:idArticulo", (request,response)-> {
            Long articuloSeleccionId = Long.parseLong(request.params("idArticulo"));
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

            return new ModelAndView(attributes, "/administrarArticulo.ftl");
        }, freeMarkerEngine );

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        post("/crearComentario/:idArticulo", (request, response) -> {
            Articulo articulo = serviceArticulo.encontrarPorId(Long.parseLong(request.params("idArticulo")));
            Map<String, Object> attributes = new HashMap<>();
            Comentario nuevoComentario=new Comentario();

            nuevoComentario.setComentario(request.queryParams("Coment"));
            nuevoComentario.setArticulo(articulo);
            nuevoComentario.setAutor(usuarioLogueado);
            serviceComentario.insertar(nuevoComentario);

            response.redirect("/home/1"); //ARREGLAR QUE AL CREAR MANDA A INDEX **********************************************

            return new ModelAndView(attributes, "/crearComentario.ftl");
        }, freeMarkerEngine);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/eliminarArticulo/:idArticulo", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            Long ArticuloId=Long.parseLong(request.params("idArticulo"));

            serviceArticulo.borrar(serviceArticulo.encontrarPorId(ArticuloId));
            //Comentario.eliminarComentario(ComentarioId);
            response.redirect("/home/1");//ARREGLAR QUE AL ELIMINAR MANDA A INDEX **********************************************

            return new ModelAndView(attributes, "/eliminarArticulo.ftl");
        }, freeMarkerEngine);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //FILTRO PARA DETENER USUARIO QUE NO SEA AUTOR PARA ELIMINAR O ADMINISTRADOR
        before("/eliminarArticulo/:idArticulo", (request, response) ->{
            Long articuloId=Long.parseLong(request.params("idArticulo"));
            Articulo articuloBuscado = serviceArticulo.encontrarPorId(articuloId);

            if(!(usuarioLogueado.getId().equals(articuloBuscado.getAutor()) || usuarioLogueado.isAdministrador())){
                halt(401,"No tiene permisos para eliminar articulo!");
            }
        } );
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/modificarArticulo/:idArticulo", (request, response) -> {
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
        before("/modificarArticulo/:idArticulo", (request, response) ->{
            Articulo articuloBuscado = serviceArticulo.encontrarPorId(Long.parseLong(request.params("idArticulo")));

            if(!(usuarioLogueado.getId() == articuloBuscado.getAutor().getId() || usuarioLogueado.isAdministrador())){
                halt(401,"No tiene permisos para modificar articulo!");
            }
        } );
    }

    private static boolean estaLogueado(boolean estaLogueado){
        return estaLogueado;
    }


    private static void crearUsuarioAdmin() {
        Usuario usuario = new Usuario();
        usuario.setId(new Long(1));
        usuario.setNombre("Gustavo");
        usuario.setUsername("admin");
        usuario.setPassword("admin");
        usuario.setAdministrador(true);
        usuario.setAutor(true);

        logger.info("Verificando si el usuario ya se introdujo. El usuario por defecto");
        if (serviceUsuario.encontrarPorId(new Long(1)) == null)
            serviceUsuario.insertar(usuario);
    }
}

