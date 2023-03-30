package com.cinema.service;

import com.cinema.model.FilmSession;
import com.cinema.model.FilmSessionView;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;

@ThreadSafe
public interface FilmSessionService {

    Collection<FilmSession> findAll();

    FilmSessionView toView(FilmSession filmSession);

}
