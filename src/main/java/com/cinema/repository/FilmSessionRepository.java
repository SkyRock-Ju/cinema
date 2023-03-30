package com.cinema.repository;

import com.cinema.model.FilmSession;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;

@ThreadSafe
public interface FilmSessionRepository {

    Collection<FilmSession> findAll();

}
