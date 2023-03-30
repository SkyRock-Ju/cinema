package com.cinema.repository;

import com.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {

    Collection<Genre> findAll();

    Optional<Genre> findById(int id);

}
