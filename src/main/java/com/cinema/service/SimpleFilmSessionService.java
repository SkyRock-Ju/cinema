package com.cinema.service;

import com.cinema.model.FilmSession;
import com.cinema.model.dto.FilmSessionDto;
import com.cinema.repository.FilmRepository;
import com.cinema.repository.FilmSessionRepository;
import com.cinema.repository.HallRepository;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ThreadSafe
@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SimpleFilmSessionService(FilmSessionRepository sql2oFilmSessionRepository,
                                    FilmRepository sql2oFilmRepository,
                                    HallRepository sql2oHallRepository) {
        this.filmSessionRepository = sql2oFilmSessionRepository;
        this.filmRepository = sql2oFilmRepository;
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public List<FilmSessionDto> findAll() {
        return filmSessionRepository.findAll().stream().map(this::toView).collect(Collectors.toList());
    }

    public Optional<FilmSession> findById(int id) {
        return filmSessionRepository.findById(id);
    }

    @Override
    public FilmSessionDto toView(FilmSession filmSession) {
        return new FilmSessionDto(
                filmSession.getId(),
                filmSession.getFilmId(),
                filmRepository.findById(filmSession.getFilmId()).orElseThrow().getName(),
                hallRepository.findById(filmSession.getHallsId()).orElseThrow().getName(),
                filmSession.getStartTime(),
                filmSession.getEndTime(),
                filmSession.getPrice());
    }

    @Override
    public FilmSession save(FilmSession filmSession) {
        return filmSessionRepository.save(filmSession);
    }

    @Override
    public boolean update(FilmSession filmSession) {
        return filmSessionRepository.update(filmSession);
    }

    @Override
    public boolean deleteById(int id) {
        return filmSessionRepository.deleteById(id);
    }
}
