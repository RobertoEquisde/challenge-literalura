package com.alura.literAlura.repository;

import com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByName(String name);

    @Query("select a from Autor a WHERE a.type = 'author'")
    List<Autor> findByTypeEqualsAuthor();

    @Query("select a from Autor a WHERE a.birthYear<:livingYear AND a.deathYear>:livingYear AND a.type='author'")
    List<Autor> findByLivingAuthorGivenYear(Integer livingYear);
}