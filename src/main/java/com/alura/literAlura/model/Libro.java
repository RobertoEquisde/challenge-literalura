package com.alura.literAlura.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer idLibro;
    private String title;
    private String subjects;
    private String bookshelves;
    private String languages;
    private Boolean copyright;
    private String media_type;
    private Integer download_count;

    @ManyToMany
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    public Libro(DatosLibro datosLibro) {
        this.idLibro = datosLibro.idLibro();
        this.title = datosLibro.title().length() > 255 ? datosLibro.title().substring(0, 255) : datosLibro.title();
        this.subjects = !datosLibro.subjects().isEmpty() ? datosLibro.subjects().get(0) : "N/A  ";
        this.bookshelves = !datosLibro.bookshelves().isEmpty() ? datosLibro.bookshelves().get(0) : "N/A  ";
        this.languages = !datosLibro.languages().isEmpty() ? datosLibro.languages().get(0) : "N/A  ";
        this.copyright = datosLibro.copyright();
        this.autores = datosLibro.authors().stream()
                .map(da -> new Autor(da.name(),
                        da.birthYear() != null ? da.birthYear().toString() : "N/A",
                        da.deathYear() != null ? da.deathYear().toString() : "N/A",
                        "author"))
                .collect(Collectors.toSet());
        this.autores.addAll(datosLibro.translators().stream()
                .map(da -> new Autor(da.name(),
                        da.birthYear() != null ? da.birthYear().toString() : "N/A",
                        da.deathYear() != null ? da.deathYear().toString() : "N/A",
                        "translator"))
                .collect(Collectors.toSet()));
        this.media_type = datosLibro.media_type();
        this.download_count = datosLibro.download_count();

    }

    public Libro() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCopyright() {
        return copyright;
    }

    public void setCopyright(Boolean copyright) {
        this.copyright = copyright;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public Integer getDownload_count() {
        return download_count;
    }

    public void setDownload_count(Integer download_count) {
        this.download_count = download_count;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    @Override
    public String toString() {
        return  "IdLibro=" + idLibro +
                ", Title='" + title + '\'' +
                ", Subjects='" + subjects + '\'' +
                ", Bookshelves='" + bookshelves + '\'' +
                ", Languages='" + languages + '\'' +
                ", Copyright=" + copyright +
                ", Media_type='" + media_type + '\'' +
                ", Download_count=" + download_count;
    }
}