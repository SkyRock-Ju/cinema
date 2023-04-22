package com.cinema.service;

import com.cinema.model.FilmSession;
import com.cinema.model.dto.FilmSessionDto;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface FilmSessionService {

    Collection<FilmSessionDto> findAll();

    Optional<FilmSession> findById(int id);

    FilmSessionDto toView(FilmSession filmSession);

    FilmSession save(FilmSession filmSession);

    boolean update(FilmSession filmSession);

    boolean deleteById(int id);
}
