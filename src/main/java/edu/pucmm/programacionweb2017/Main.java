package edu.pucmm.programacionweb2017;

import edu.pucmm.programacionweb2017.entidad.Articulo;
import edu.pucmm.programacionweb2017.entidad.Comentario;
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

/**
 * Created by mt on 04/06/17.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    static boolean estaLogueado=estaLogueado(false);
    static Usuario usuarioLogueado = new Usuario();

    private static final ServiceArticulo serviceArticulo = new ServiceArticulo();
    private static final ServiceComentario serviceComentario = new ServiceComentario();
    private static final ServiceEtiqueta serviceEtiqueta = new ServiceEtiqueta();
    private static final ServiceUsuario serviceUsuario = new ServiceUsuario();

    public static void main(String[] args) throws SQLException {
        logger.info("Iniciando aplicacion");

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

        get("", (request, response) -> {
            response.redirect("/home");
            return null;
        });

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

            response.redirect("/home");



            return new ModelAndView(null, "/crearUsuario.ftl");
        }, freeMarkerEngine );

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        get("/home", (request,response)-> {

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("articulos",serviceArticulo.encontrarTodos());
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("estaLogueado", estaLogueado);

            return new ModelAndView(attributes, "/index.ftl");
        }, freeMarkerEngine );

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        post("/home", (request, response) -> {

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


            response.redirect("/home");

            return new ModelAndView(null, "/index.ftl");
        }, freeMarkerEngine );

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        get("/crearArticulo/:idUsuario", (request,response)-> {
            usuarioLogueado = serviceUsuario.encontrarPorId(Long.parseLong(request.params("idUsuario")));

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("usuario",usuarioLogueado);
            return new ModelAndView(attributes, "/crearArticulo.ftl");
        }, freeMarkerEngine );

//!////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //FILTRO PARA DETENER USUARIOS QUE NO TIENEN PERMISOS PARA CREAR USUARIO
        before("/crearArticulo/:idUsuario", (request, response) ->{
            Usuario usuarioLogueado = serviceUsuario.encontrarPorId(Long.parseLong(request.params("idUsuario")));

            if(!(usuarioLogueado.isAdministrador() || usuarioLogueado.isAutor())){
                halt(401,"No tiene permisos para crear articulo!");
            }


        } );

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        post("/crearArticulo/:idUsuario", (request,response)-> {

            Map<String, Object> attributes = new HashMap<>();
            Articulo nuevoArticulo = new Articulo();
            nuevoArticulo.setTitulo(request.queryParams("Titu"));
            nuevoArticulo.setCuerpo(request.queryParams("Cuer"));
            nuevoArticulo.setFecha(LocalDate.now());


            int usuarioId=Integer.parseInt(request.params("idUsuario"));
            Usuario usuarioLogueado = new Usuario();
            for(int i=0;i<serviceUsuario.encontrarTodos().size();i++){
                if(usuarioId == serviceUsuario.encontrarTodos().get(i).getId()){
                    usuarioLogueado=serviceUsuario.encontrarTodos().get(i);

                }
            }
            nuevoArticulo.setAutor(usuarioLogueado);

            attributes.put("usuario",usuarioLogueado);
            serviceArticulo.insertar(nuevoArticulo);

            response.redirect("/home");

            return new ModelAndView(attributes, "/crearArticulo.ftl");
        }, freeMarkerEngine );

//!/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/administrarArticulo/:idArticulo", (request,response)-> {
            Long articuloSeleccionId = Long.parseLong(request.params("idArticulo"));
            Articulo articuloSeleccionado = serviceArticulo.encontrarPorId(articuloSeleccionId);

            Map<String, Object> attributes = new HashMap<>();

            attributes.put("estaLogueado", estaLogueado);
            attributes.put("usuarioLogueado", usuarioLogueado);
            attributes.put("articuloSeleccionado", articuloSeleccionado);
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

            response.redirect("/home"); //ARREGLAR QUE AL CREAR MANDA A INDEX **********************************************

            return new ModelAndView(attributes, "/crearComentario.ftl");
        }, freeMarkerEngine);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        get("/eliminarArticulo/:idArticulo", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            Long ArticuloId=Long.parseLong(request.params("idArticulo"));

            serviceArticulo.borrar(serviceArticulo.encontrarPorId(ArticuloId));
            //Comentario.eliminarComentario(ComentarioId);
            response.redirect("/home"); //ARREGLAR QUE AL ELIMINAR MANDA A INDEX **********************************************

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
            Map<String, Object> attributes = new HashMap<>();

            attributes.put("articulo",serviceArticulo.encontrarPorId(idArticulo));

            return new ModelAndView(attributes, "/modificarArticulo.ftl");
        }, freeMarkerEngine);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        post("/modificarArticulo/:idArticulo", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();

            Articulo articulo = serviceArticulo.encontrarPorId(Long.parseLong(request.params("idArticulo")));
            articulo.setTitulo(request.queryMap().get("Titu").value());
            articulo.setCuerpo(request.queryMap().get("Cuer").value());

            serviceArticulo.actualizar(articulo);

            attributes.put("articulo", articulo);
            response.redirect("/home");

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

