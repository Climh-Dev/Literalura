package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaDeNacimiento;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    private String fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }

    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = String.valueOf(datosAutor.fechaDeNacimiento());
        this.fechaDeFallecimiento = String.valueOf(datosAutor.fechaDeFallecimiento());
    }

    @Override
    public String toString() {
        return """
           Nombre: %s
           Fecha de Nacimiento: %s
           Fecha de Fallecimiento: %s
           """.formatted(
                this.nombre,
                this.fechaDeNacimiento,
                (this.fechaDeFallecimiento != null ? this.fechaDeFallecimiento : "N/A")
        );
    }
}
