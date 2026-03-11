package com.aluracursos.literalura.principal;


import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String URL_BUSQUEDA = "?search=";
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ----------------------------------
                    1 - Buscar libro por título 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    ----------------------------------
                    Elija la opción a través del número:
                    """;
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
                teclado.nextLine(); // Limpiar el buffer
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
                teclado.nextLine();
                continue;
            }
            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnAnio();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación Literalura... ¡Hasta pronto!");
                    System.exit(0); // Detiene la ejecución de inmediato
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void buscarLibroWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + URL_BUSQUEDA + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, DatosRespuesta.class);

        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado!");
            guardarDatos(libroBuscado.get()); // Llamamos al método de guardado que ya creamos
        } else {
            System.out.println("Libro no encontrado en la API.");
        }
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(a -> System.out.println(
                    "Autor: " + a.getNombre() + " (" + a.getFechaDeNacimiento() + " - " + a.getFechaDeFallecimiento() + ")"
            ));
        }
    }

    private void mostrarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año que desea investigar:");
        try {
            var anio = teclado.nextInt();
            teclado.nextLine();

            List<Autor> autoresVivos = autorRepository.buscarAutoresVivosEnDeterminadoAnio(anio);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio);
            } else {
                System.out.println("--- Autores vivos en el año " + anio + " ---");
                autoresVivos.forEach(a -> System.out.println(
                        "Autor: " + a.getNombre() +
                                " | Nacimiento: " + a.getFechaDeNacimiento() +
                                " | Fallecimiento: " + (a.getFechaDeFallecimiento() != null ? a.getFechaDeFallecimiento() : "N/A")
                ));
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número para el año.");
            teclado.nextLine();
        }
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("""
            Ingrese el idioma para buscar los libros:
            es - Español
            en - Inglés
            fr - Francés
            pt - Portugués
            """);
        var idioma = teclado.nextLine();

        // IMPORTANTE: El nombre del método debe coincidir con tu LibroRepository
        List<Libro> libros = libroRepository.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma seleccionado.");
        } else {
            System.out.println("--- LIBROS ENCONTRADOS ---");
            libros.forEach(System.out::println);
        }
    }

    private void guardarDatos(DatosLibro datosLibro) {
        try {
            DatosAutor datosAutor = datosLibro.autor().get(0); // Tomamos el primer autor del Record
            Autor autor = autorRepository.findByNombreContainsIgnoreCase(datosAutor.nombre())
                    .orElseGet(() -> {
                        Autor nuevoAutor = new Autor(datosAutor);
                        return autorRepository.save(nuevoAutor);
                    });

            if (libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo()).isPresent()) {
                System.out.println("El libro '" + datosLibro.titulo() + "' ya está registrado.");
            } else {
                Libro libro = new Libro(datosLibro);

                libro.setIdioma(String.join(",", datosLibro.idiomas()));

                libro.setAutor(autor);

                libroRepository.save(libro);
                System.out.println("\n¡Libro guardado con éxito!");
                System.out.println(libro);
            }
        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }


}
