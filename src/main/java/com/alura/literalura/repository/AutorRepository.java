package com.alura.literalura.repository;

import com.alura.literalura.models.Autor;
import com.alura.literalura.models.Idioma;
import com.alura.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    //Buscar libro por nombre en internet
    @Query("SELECT DISTINCT l FROM Libro l JOIN l.autor a WHERE l.titulo LIKE %:nombre%")
    Optional<Libro> buscarLibroPorNombre(@Param("nombre") String nombre);

    //Buscar todos los libros registrados en la base de datos
    @Query("SELECT DISTINCT l FROM Autor a JOIN a.libros l")
    List<Libro> buscarTodosLosLibros();

    //Buscar autor por nombre en la base de datos
    @Query("SELECT DISTINCT a FROM Libro l JOIN l.autor a WHERE a.nombre LIKE %:nombre%")
    Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);

    //Buscar autores vivos mediante la fecha
    @Query("SELECT a FROM Autor a WHERE a.fallecimiento IS NULL OR a.fallecimiento > :fecha")
    List<Autor> buscarAutoresVivos(@Param("fecha") Integer fecha);

    //Buscar libros por idioma
    @Query("SELECT DISTINCT l FROM Autor a JOIN a.libros l WHERE l.idioma = :idioma")
    List<Libro> buscarLibrosPorIdioma(@Param("idioma") Idioma idioma);

}
