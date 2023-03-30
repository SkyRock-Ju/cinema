package com.cinema.repository;

import com.cinema.model.Film;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface FilmRepository {

    Optional<Film> save(Film film);

    boolean update(Film film);

    Collection<Film> findAll();

    Optional<Film> findById(int id);

    boolean deleteById(int id);

}
