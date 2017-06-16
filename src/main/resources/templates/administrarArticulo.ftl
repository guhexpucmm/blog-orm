<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Blog Home - Start Bootstrap Template</title>

    <!-- Bootstrap core CSS -->
    <link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/css/blog-home.css" rel="stylesheet">

    <!-- Temporary navbar container fix -->
    <style>
        .navbar-toggler {
            z-index: 1;
        }

        @media (max-width: 576px) {
            nav > .container {
                width: 100%;
            }
        }
    </style>

</head>

<body>

<!-- Navigation -->
<nav class="navbar fixed-top navbar-toggleable-md navbar-inverse bg-inverse">
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarExample" aria-controls="navbarExample" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="container">
        <a class="navbar-brand" href="/home/"> <img width="75" height="75" src="/logopucmm.png"> Blog PUCMM Programacion
            Web 2017</a>
    </div>
</nav>

<!-- Page Content -->
<div class="container">
    <div class="row">
        <!-- Blog Entries Column -->
        <div class="col-md-8">
            <h1 class="my-4">&nbsp <small> </small></h1> <!-- ESPACIO VACIO -->
            <!-- ARTICULO -->
            <div class="card mb-4">
                <div class="card-block">
                    <h2 class="card-title">${articuloSeleccionado.getTitulo()}</h2>
                    <p class="card-text">${articuloSeleccionado.getCuerpo()}</p>
                </div>

                <div class="card-footer text-muted">
                    <label>Etiquetas: </label>
                    <p>
                    <#list etiquetas as etiqueta>
                        <label><a
                                href="/articulosConEtiquetas/?etiqueta=${etiqueta.getId()}">${etiqueta.getEtiqueta()}</a></label>
                    </#list>
                    </p>
                </div>

                <div class="card-footer">

                    <button type="button" class="btn btn-default">
                        <a href="/eliminarArticulo/${articuloSeleccionado.getId()}">Eliminar</a>
                    </button>
                    <button type="button" class="btn btn-default">
                        <a href="/modificarArticulo/${articuloSeleccionado.getId()}">Modificar</a>
                    </button>

                    <label>
                    ${articuloSeleccionado.getFecha()}
                        by <a href="#">${articuloSeleccionado.getAutor().getNombre()}</a>
                    </label>

                <#if estaLogueado == true>
                    <div class="btn-group" role="group" aria-label="...">
                        <button type="button" class="btn btn-success"
                                onclick="${valoracion(articuloSeleccionado.getId(), "true")}">
                            <span class="glyphicon glyphicon-thumbs-up"></span>Me gusta!<span
                                class="badge">${valoracionesPositivas}</span>
                        </button>
                        <button type="button" class="btn btn-danger"
                                onclick="${valoracion(articuloSeleccionado.getId(), "false")}">
                            <span class="glyphicon glyphicon-thumbs-down"></span>No me gusta!<span
                                class="badge">${valoracionesNegativas}</span>
                        </button>
                    </div>
                </#if>

                <#if estaLogueado == false>
                    <div class="btn-group" role="group" aria-label="...">
                        <button type="button" class="btn btn-success"
                                onclick="">
                            <span class="glyphicon glyphicon-thumbs-up"></span>Me gusta!<span
                                class="badge">${valoracionesPositivas}</span>
                        </button>
                        <button type="button" class="btn btn-danger"
                                onclick="">
                            <span class="glyphicon glyphicon-thumbs-down"></span>No me gusta!<span
                                class="badge">${valoracionesNegativas}</span>
                        </button>
                    </div>
                </#if>

                </div>
            </div>

        <div class="card mb-4">
                <div class="card-block">
                    <h2 class="card-title">Comentarios</h2>
                    <p class="card-text">
                    <#list comentarios as comentario>
                        <div class="card mb-4">
                            <div class="card-block">
                                <p class="card-text">${comentario.getComentario()}</p>
                            </div>
                        <!--LOGICA DE ELIMINAR-->
                        <div class="card-footer text-muted">Comentario hecho por: <a
                                href="#">${comentario.getAutor().getNombre()}</a>
                                <!--
                                <button type="button" class="btn btn-default">
                                    <a href="/eliminarComentario/${articuloSeleccionado.getAutor()}/${comentario.getId()}">Eliminar</a>
                                </button>
                                -->
                            </div>
            </div>
                        </#list>

                    </p>
        </div>
        <#if estaLogueado ==true>

        <div class="card mb-4">
            <!--<img class="card-img-top img-fluid" src="http://placehold.it/750x300" alt="Card image cap">-->
            <div class="card-block">
                <form method="post" action="/crearComentario/${articuloSeleccionado.getId()}">
                <h2 class="card-title">Nuevo comentario:</h2>
                <p class="card-text">
                    <input class="form-username form-control" style="width:500px;height:250px" type="text" name="Coment"
                           value="" id="Comentario" placeholder="Inserte aqui su comentario.!">

                </p>
                    <button class="btn btn-success" type="submit">Sumbit</button>
                </form>
            </div>
        </div>

        </#if>

            </div>
            <!-- COMENTARIOS -->

            <!-- Pagination -->
            <ul class="pagination justify-content-center mb-4">
                <li class="page-item"><a class="page-link" href="#">&larr; Older</a></li>
                <li class="page-item disabled"><a class="page-link" href="#">Newer &rarr;</a></li>
            </ul>

        </div>

<!-- Sidebar Widgets Column -->
<div class="col-md-3">

<#if estaLogueado==false>
    <h1 class="my-4">&nbsp
        <small></small>
    </h1> <!-- ESPACIO VACIO -->
    <div class="card my-4">
        <h5 class="card-header">Iniciar sesion</h5>
        <div class="card-block">
            <form action="/home" method="post" class="form-bottom">
                <div class="form-group">
                    <input class="form-username form-control" type="text" name="User" value="" id="Username"
                           placeholder="Username">
                </div>
                <div class="form-group">
                    <input class="form-username form-control" type="password" name="Pass" value="" id="Password"
                           placeholder="Password">
                </div>
                <div class="input-group">
                    <button class="btn btn-primary" type="submit">Ingresar</button>

                </div>
            </form>
        </div>
    </div>
</#if>

<#if estaLogueado==true>
    <h1 class="my-4">&nbsp
        <small></small>
    </h1> <!-- ESPACIO VACIO -->
    <div class="card my-4">
        <h5 class="card-header">Bienvenido: </h5>
        <div class="card-block">
            <div class="input-group">
                <label>${usuarioLogueado.getNombre()}</label>
            </div>
            <div class="input-group">
                <form method="get" action="/cerraSesion">
                    <button class="btn btn-danger" type="submit">Cerrar sesion</button>
                </form>
            </div>
        </div>
    </div>
    <div class="card my-4">
        <div class="card-header">
            <button type="button" class="btn btn-primary"
                    onclick="window.location='/crearArticulo/${usuarioLogueado.getId()}'">Nuevo articulo
            </button>
        </div>
        <div class="card-header">
            <#if usuarioLogueado.getUsername() == "admin">
                <button type="button" class="btn btn-primary" onclick="window.location='/crearUsuario/'">Nuevo usuario
                </button>
            </#if>
        </div>
    </div>
</#if>
</div>
</div>

</div>
    <!-- /.row -->

</div>
<!-- /.container -->

<!-- Footer -->
<footer class="py-5 bg-inverse">
    <div class="container">
        <p class="m-0 text-center text-white">Pucmm &copy; Programacion Web 2017</p>
    </div>
    <!-- /.container -->
</footer>

<!-- Bootstrap core JavaScript -->
<script src="/vendor/jquery/jquery.min.js"></script>
<script src="/vendor/tether/tether.min.js"></script>
<script src="/vendor/bootstrap/js/bootstrap.min.js"></script>

</body>
<script>
    function meGusta() {
        var articulo
    }
</script>
</html>
