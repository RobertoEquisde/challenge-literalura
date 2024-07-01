package com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("id") Integer idLibro,
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<DatosAutor> authors,
        @JsonAlias("translators") List<DatosAutor> translators,
        @JsonAlias("subjects") List<String> subjects,
        @JsonAlias("bookshelves") List<String> bookshelves,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("copyright") Boolean copyright,
        @JsonAlias("media_type") String media_type,
        @JsonAlias("download_count") Integer download_count
){ }