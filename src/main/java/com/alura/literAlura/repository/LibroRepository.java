package com.alura.literAlura.repository;

import com.alura.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Libro findByIdLibro(Integer idLibro);

    @Query("select distinct l.languages from Libro l")
    List<String> findAllLanguages();

    @Query("select l from Libro l where l.languages=:languages")
    List<Libro> findByLanguagesWhereOpcion(String languages);
}