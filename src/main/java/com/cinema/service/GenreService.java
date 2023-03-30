package com.cinema.service;

import com.cinema.model.Genre;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface GenreService {

    Collection<Genre> findAll();

    Optional<Genre> findById(int id);

}
