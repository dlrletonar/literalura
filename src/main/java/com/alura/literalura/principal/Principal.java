package com.alura.literalura.principal;
import com.alura.literalura.models.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.sevice.ConsumoAPI;
import com.alura.literalura.sevice.Conversor;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private ConsumoAPI consumoAPI=new ConsumoAPI();
    private String URL_BASE = "https://gutendex.com/books/";
    private Scanner teclado = new Scanner(System.in);
    private Conversor conversor = new Conversor();
    private AutorRepository repository;


    public Principal(AutorRepository repository){
        this.repository =repository;
    }

    public Principal() {
    }

    public void principalMenu() {
        var opcion = -1;
        while (opcion != 0) {
            menu();
            try {
                opcion = Integer.valueOf(teclado.nextLine());
                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivos();
                    case 5 -> listarLibrosPorIdioma();
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción no válida: " + e.getMessage());
            }
        }
    }
    public void menu(){
        var menu = """
            Elija la opción a través de su número:
            1 - Buscar libros por titulo
            2 - Listar libros registrados
            3 - Listar autores registrados
            4 - Listar autores vivos en un determinado año
            5 - Listar libros por idioma
            0 - Salir
            Elija una opción:
            """;
        System.out.println(menu);
    }
    public void buscarLibroPorTitulo(){
        System.out.println("Buscar libro por título ");
        System.out.println("Introduzca el nombre del libro que desea buscar:");
        String nombre = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombre.replace(" ", "+").toLowerCase());

        //para verificar Json vacio, nulo, sin contenido
        if (json.isEmpty() || json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {
            System.out.println("Libro no encontrado!");
            return;
        }
        //conversion del Json a objetos java
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        //busqueda del primer libro en los datos obtenidos
        Optional<DatosLibros> libroBuscado = datos.libros().stream().findFirst();
        //verificación y mostrando el libro encontrado
        if (libroBuscado.isPresent()) {
            mostrarDatosLibro(libroBuscado.get());
        } else {
            System.out.println("Libro no encontrado!");
        }
    }

    public void mostrarDatosLibro(DatosLibros libro){
        System.out.println(
                "\n************ Datos del Libro ************" +
                        "\nTítulo: " + libro.titulo() +
                        "\nAutor: " + obtenerNombreAutor(libro) +
                        "\nIdioma: " + libro.idiomas().stream().collect(Collectors.joining()) +
                        "\nNúmero de descargas: " + libro.descagas() +
                        "\n************************************\n"
        );
        try {
        //verificación si el libbro ya esta guardado en la base de datos
            Optional<Libro> libroOptional = repository.buscarLibroPorNombre(libro.titulo());
            if (libroOptional.isPresent()) {
                System.out.println("El libro ya está guardado en la BD.");
                return;
            }
            //verificación si el autor ya esta guardado en la base de datos
            Optional<Autor> autorBD = repository.buscarAutorPorNombre(obtenerNombreAutor(libro));
            //guardar el autor en la base de datos, si no existe
            Autor autor = autorBD.orElseGet(() -> {
                Autor autorAPI = libro.autor().stream().findFirst().map(Autor::new).orElse(null);
                repository.save(autorAPI);
                return autorAPI;
            });
            //asociar el libro al autor y guardar en la base de datos
            autor.setLibros(List.of(new Libro(libro)));
            repository.save(autor);
        } catch (Exception e) {
            System.out.println("Error al mostrar datos del libro..! " + e.getMessage());
        }
    }

    private String obtenerNombreAutor(DatosLibros libro) {
        return libro.autor().stream()
                .findFirst()
                .map(a -> a.nombre())
                .orElse("Desconocido");
    }

    public void listarLibrosRegistrados () {
        System.out.println("Lista de libros registrado en la DB");
        List<Libro> libros = repository.buscarTodosLosLibros();
        libros.forEach(l -> System.out.println(
                "************ Libro Registrado en DB ************" +
                        "\nTítulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getIdioma().getIdioma() +
                        "\nNúmero de descargas: " + l.getDescargas() +
                        "\n************************************\n"
        ));
    }

    public void listarAutoresRegistrados () {
        System.out.println("Lista de autores registrados en DB");
        List<Autor> autores = repository.findAll();
        System.out.println();
        autores.forEach(l -> System.out.println(
                "Autor: " + l.getNombre() +
                        "\nFecha de Nacimiento: " + l.getNacimiento() +
                        "\nFecha de Muerte: " + l.getMuerte() +
                        "\nLibros: " + l.getLibros().stream()
                        .map(t -> t.getTitulo()).collect(Collectors.toList()) + "\n"
        ));
    }

    public void listarAutoresVivos () {
        System.out.println("Lista de Autores vivos segun año");
        System.out.println("Ingrese el año, para buscar los autor(es) : ");
        try {
            var fecha = Integer.valueOf(teclado.nextLine());
            //busqueda de autores vivos en la base de datos
            List<Autor> autores = repository.buscarAutoresVivos(fecha);
            if (!autores.isEmpty()) {
                System.out.println();
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getNombre() +
                                "\nFecha de Nacimiento: " + a.getNacimiento() +
                                "\nFecha de Muerte: " + a.getMuerte() +
                                "\nLibros: " + a.getLibros().stream()
                                .map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
                ));
            } else {
                System.out.println("No hay autores vivos en ese año...");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingresa un año válido " + e.getMessage());
        }
    }

    public void listarLibrosPorIdioma() {
        System.out.println("Lista de libros por idioma");
        System.out.println(
                "**************************************************\n" +
                        "Seleccione el idioma del libro que desea encontrar:\n" +
                        "**************************************************\n" +
                        "1 - Español\n" +
                        "2 - Francés\n" +
                        "3 - Inglés\n" +
                        "4 - Portugués\n" +
                        "5 - Alemán\n" +
                        "6 - Italiano\n" +
                        "7 - Ruso\n" +
                        "8 - Japonés\n" +
                        "9 - Chino\n" +
                "**************************************************\n"
        );
        try {
            int opcion = Integer.parseInt(teclado.nextLine());
            String idioma;
            switch (opcion) {
                case 1 -> idioma = "es";
                case 2 -> idioma = "fr";
                case 3 -> idioma = "en";
                case 4 -> idioma = "pt";
                case 5 -> idioma = "de";
                case 6 -> idioma = ("it");
                case 7 -> idioma = "ru";
                case 8 -> idioma = "ja";
                case 9 -> idioma = "zh";
                default -> {System.out.println("Opción inválida!");return;}
            }
            buscarLibrosPorIdioma(idioma);
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida: " + e.getMessage());
        }
    }

    private void buscarLibrosPorIdioma(String idioma) {
        try {
            Idioma idiomaEnum = Idioma.fromString(idioma);
            List<Libro> libros = repository.buscarLibrosPorIdioma(idiomaEnum);
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados en ese idioma");
            } else {
                System.out.println();
                libros.forEach(l -> System.out.println(
                        "****************** Libro ******************\n" +
                                "Título: " + l.getTitulo() + "\n" +
                                "Autor: " + l.getAutor().getNombre() + "\n" +
                                "Idioma: " + l.getIdioma().getIdioma() + "\n" +
                                "Número de descargas: " + l.getDescargas() + "\n" +
                                "************************************\n"
                ));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Introduce un idioma válido...");
        }
    }
}


