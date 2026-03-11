package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    private String idioma;
    private Double numeroDeDescargas;

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Autor getAutor() {
        return autor;
    }

    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    @Override
    public String toString() {
        return """
           ----- LIBRO -----
           Título: %s
           Autor: %s
           Idioma: %s
           Descargas: %s
           ------------------
           """.formatted(
                this.titulo,
                (this.autor != null ? this.autor.getNombre() : "Autor desconocido"),
                this.idioma,
                this.numeroDeDescargas
        );
    }
}

