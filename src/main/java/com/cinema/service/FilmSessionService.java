package com.cinema.service;

import com.cinema.model.FilmSession;
import com.cinema.model.FilmSessionView;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface FilmSessionService {

    Collection<FilmSession> findAll();

    Optional<FilmSession> findById(int id);

    FilmSessionView toView(FilmSession filmSession);

    FilmSession save(FilmSession filmSession);

    boolean update(FilmSession filmSession);

    boolean deleteById(int id);
}
