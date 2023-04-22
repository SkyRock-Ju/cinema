package com.cinema.service;

import com.cinema.model.dto.FileDto;
import com.cinema.model.Film;
import com.cinema.model.dto.FilmDto;

import java.util.Collection;
import java.util.Optional;

public interface FilmService {

    Optional<Film> save(Film film, FileDto fileDto);

    boolean update(Film film, FileDto fileDto);

    Collection<FilmDto> findAll();

    FilmDto findById(int id);

    boolean deleteById(int id);

}
