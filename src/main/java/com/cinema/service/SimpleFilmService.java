package com.cinema.service;

import com.cinema.model.dto.FileDto;
import com.cinema.model.Film;
import com.cinema.model.dto.FilmDto;
import com.cinema.repository.FilmRepository;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@ThreadSafe
@Service
public class SimpleFilmService implements FilmService {

    FilmRepository filmRepository;
    private final FileService fileService;
    private final GenreService genreService;

    public SimpleFilmService(FilmRepository sql2oFilmRepository, FileService fileService, GenreService genreService) {
        this.filmRepository = sql2oFilmRepository;
        this.fileService = fileService;
        this.genreService = genreService;
    }

    @Override
    public Optional<Film> save(Film film, FileDto fileDto) {
        saveNewFile(film, fileDto);
        return filmRepository.save(film);
    }

    private void saveNewFile(Film film, FileDto image) {
        var file = fileService.save(image);
        film.setFileId(file.getId());
    }

    @Override
    public boolean update(Film film, FileDto fileDto) {
        var isNewFileEmpty = fileDto.getContent().length == 0;
        if (isNewFileEmpty) {
            return filmRepository.update(film);
        }
        var oldFileId = film.getFileId();
        saveNewFile(film, fileDto);
        var isUpdated = filmRepository.update(film);
        fileService.deleteById(oldFileId);
        return isUpdated;
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.findAll().stream().map(film ->
                new FilmDto(
                        film.getId(),
                        film.getName(),
                        film.getDescription(),
                        film.getYear(),
                        film.getMinimalAge(),
                        film.getDurationInMinutes(),
                        genreService.findById(film.getGenreId()).orElseThrow().getName(),
                        film.getGenreId(),
                        film.getFileId()
                )).collect(Collectors.toList());
    }

    @Override
    public FilmDto findById(int id) {
        if (filmRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Фильм с указанным идентификатором не найден");
        }
        var film = filmRepository.findById(id).orElseThrow();
        var genre = genreService.findById(film.getGenreId()).orElseThrow();
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getYear(),
                film.getMinimalAge(),
                film.getDurationInMinutes(),
                genre.getName(),
                film.getGenreId(),
                film.getFileId()
        );
    }

    @Override
    public boolean deleteById(int id) {
        return filmRepository.deleteById(id);
    }
}
