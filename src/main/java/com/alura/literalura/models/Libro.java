package com.alura.literalura.models;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.stream.Collectors;
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer descargas;
    @ManyToOne
    private Autor autor;

    public Libro() { }

    public Libro(DatosLibros libro) {
        this.id = libro.id();
        this.titulo = libro.titulo();
        this.idioma = Idioma.fromString(libro.idiomas().stream()
                .limit(1).collect(Collectors.joining()));
        this.descargas = libro.descagas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", idioma=" + idioma +
                ", descargas=" + descargas +
                ", autor=" + autor +
                '}';
    }
}
