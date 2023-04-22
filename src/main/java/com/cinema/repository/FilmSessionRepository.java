package com.cinema.repository;

import com.cinema.model.FilmSession;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface FilmSessionRepository {

    Collection<FilmSession> findAll();

    Optional<FilmSession> findById(int id);

    FilmSession save(FilmSession filmSession);

    boolean update(FilmSession filmSession);

    boolean deleteById(int id);

}
