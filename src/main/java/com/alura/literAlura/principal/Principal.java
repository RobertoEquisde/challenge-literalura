package com.alura.literAlura.principal;

import com.alura.literAlura.model.*;
import com.alura.literAlura.repository.AutorRepository;
import com.alura.literAlura.repository.LibroRepository;
import com.alura.literAlura.services.ConsumoAPI;
import com.alura.literAlura.services.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    Scanner sc = new Scanner(System.in);
    ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://gutendex.com/books/?search=";
    ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    Opcion: 
                    """;
            System.out.print(menu);
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:listarAutoresVivosEnUnDeterminadoAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }

    }

    private void listarLibrosPorIdioma() {
        System.out.println("Seleccione el idioma del libro, de los siguientes idiomas disponibles: ");
        List<String> languages = libroRepository.findAllLanguages();
        languages.stream()
                .forEach(System.out::println);
        System.out.println("Ingrese el idioma del libro: ");
        String idioma = sc.nextLine();

        libros = libroRepository.findByLanguagesWhereOpcion(idioma);

        if (!libros.isEmpty()) {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getIdLibro))
                    .forEach(System.out::println);
        }else{
            System.out.println("No se encontraron libros con el idioma ingresado");
        }
    }

    private void listarAutoresVivosEnUnDeterminadoAnio() {
        System.out.println("Ingrese un año para buscar autores vivos en un determinado año: ");
        Integer livingYear = sc.nextInt();
        sc.nextLine();
        autores = autorRepository.findByLivingAuthorGivenYear(livingYear);
        if (!autores.isEmpty()) {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getBirthYear))
                    .forEach(System.out::println);
        }else{
            System.out.println("No se encontraron autores vivos en el año: "+livingYear);
        }

    }

    private void listarAutoresRegistrados() {
        autores = autorRepository.findByTypeEqualsAuthor();
        if (!autores.isEmpty()) {
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getBirthYear))
                    .forEach(System.out::println);
        }else{
            System.out.println("No hay autores registrados");
        }

    }

    private void listarLibrosRegistrados() {
        libros = libroRepository.findAll();
        if (!libros.isEmpty()) {
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getIdLibro))
                    .forEach(System.out::println);
        }
        System.out.println("No hay libros registrados");
    }

    private void buscarLibroPorTitulo() {
        DatosResults datosResults = getDatosLibro();
        if (datosResults != null) {
            List<DatosLibro> datosLibroList = datosResults.results();
            for (DatosLibro datosLibro : datosLibroList) {
                List<Autor> autores = datosLibro.authors().stream()
                        .map(da -> {
                            Autor autor = autorRepository.findByName(da.name());
                            if (autor == null) {
                                autor = new Autor(da.name(),
                                        da.birthYear() != null ? da.birthYear().toString() : "N/A",
                                        da.deathYear() != null ? da.deathYear().toString() : "N/A",
                                        "author");
                                autorRepository.save(autor);
                            }
                            return autor;
                        })
                        .collect(Collectors.toList());

                List<Autor> translators = datosLibro.translators().stream()
                        .map(da -> {
                            Autor autor = autorRepository.findByName(da.name());
                            if (autor == null) {
                                autor = new Autor(da.name(),
                                        da.birthYear() != null ? da.birthYear().toString() : "N/A",
                                        da.deathYear() != null ? da.deathYear().toString() : "N/A",
                                        "translator");
                                autorRepository.save(autor);
                            }
                            return autor;
                        })
                        .collect(Collectors.toList());

                Libro libro = libroRepository.findByIdLibro(datosLibro.idLibro());
                if (libro == null) {
                    libro = new Libro(datosLibro);
                    if (!autores.isEmpty()) {
                        libro.setAutores(new HashSet<>(autores));
                    }
                    libroRepository.save(libro);
                } else {
                    System.out.println("El libro " + libro.getTitle() + " ya existe en la base de datos.");
                }
            }
        }else{
            System.out.println("No se encontraron datos con los registros ingresados");
        }
    }

    private DatosResults getDatosLibro() {
        System.out.println("Ingrese el titulo: ");
        String titulo = sc.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE+titulo.replace(" ","+"));
        return convierteDatos.obtenerDatos(json,DatosResults.class);
    }
}
