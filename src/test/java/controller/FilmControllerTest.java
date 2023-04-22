package controller;

import com.cinema.controller.FilmController;
import com.cinema.model.dto.FileDto;
import com.cinema.model.Film;
import com.cinema.model.dto.FilmDto;
import com.cinema.service.FilmService;
import com.cinema.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmControllerTest {

    private FilmService filmService;
    private FilmController filmController;
    private MultipartFile testFile;

    @BeforeEach
    public void initTests() {
        filmService = mock(FilmService.class);
        GenreService genreService = mock(GenreService.class);
        filmController = new FilmController(filmService, genreService);
        testFile = new MockMultipartFile("testFile.img", new byte[] {1, 2, 3});
    }

    @Test
    public void whenRequestFilmListPageThenGetPageWithFilms() {
        var film1 = new Film(1, "film1", "desc1",
                2002, 1, 18, 120, 2);
        var film2 = new Film(2, "film2", "desc2",
                2003, 1, 18, 121, 4);
        var expectedFilms = List.of(
                new FilmDto(film1.getId(), film1.getName(), film1.getDescription(), film1.getYear(),
                        film1.getMinimalAge(), film1.getDurationInMinutes(), "action",
                        film1.getGenreId(), film1.getFileId()),
                new FilmDto(film2.getId(), film2.getName(), film2.getDescription(), film2.getYear(),
                        film2.getMinimalAge(), film2.getDurationInMinutes(), "action",
                        film2.getGenreId(), film2.getFileId()));
        when(filmService.findAll()).thenReturn(expectedFilms);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilms);
    }

    @Test
    public void whenRequestCreationPageThenGetPage() {
        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        assertThat(view).isEqualTo("films/list");
    }

    @Test
    public void whenPostFilmWithFileThenSameDataAndRedirectToFilmsPage() throws Exception {
        var film = new Film(1, "film1", "desc1",
                2002, 1, 18, 120, 2);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var filmArgumentCaptor = ArgumentCaptor.forClass(Film.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(filmService.save(filmArgumentCaptor.capture(),
                fileDtoArgumentCaptor.capture())).thenReturn(Optional.of(film));

        var model = new ConcurrentModel();
        var view = filmController.create(film, testFile, model);
        var actualFilm = filmArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/films");
        assertThat(actualFilm).isEqualTo(film);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    public void whenPostFilmWithFileThenGetError() {
        var expectedException = new RuntimeException("Failed to write file");
        when(filmService.save(any(), any())).thenThrow(expectedException);
        var model = new ConcurrentModel();
        var view = filmController.create(new Film(), testFile, model);
        var actualExceptionMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestFilmByIdThenGetCorrespondingFilm() {
        var filmDto = new FilmDto(1, "film1", "desc1",
                2002, 1, 18, "action", 2, 2);
        when(filmService.findById(filmDto.getId())).thenReturn(Optional.of(filmDto));
        var model = new ConcurrentModel();
        var view = filmController.getById(model, filmDto.getId());
        var actualFilm = model.getAttribute("film");
        assertThat(view).isEqualTo("films/one");
        assertThat(actualFilm).isEqualTo(filmDto);
    }

    @Test
    public void whenRequestFilmByNotExistingIdThenGetError() {
        when(filmService.findById(1)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = filmController.getById(model, 1);
        var actualMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Фильм с указанным идентификатором не найден");
    }

    @Test
    public void whenUpdateFilmWithFileThenSameDataAndRedirectToFilmsPage() throws Exception {
        var film = new Film(1, "film1", "desc1",
                2002, 1, 18, 120, 2);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var filmArgumentCaptor = ArgumentCaptor.forClass(Film.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(filmService.update(filmArgumentCaptor.capture(),
                fileDtoArgumentCaptor.capture())).thenReturn(true);
        var model = new ConcurrentModel();
        var view = filmController.update(film, testFile, model);
        var actualFilm = filmArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();
        assertThat(view).isEqualTo("redirect:/films");
        assertThat(actualFilm).isEqualTo(film);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    public void whenUpdateFilmWithFileThenGetError() {
        var expectedException = new RuntimeException("Failed to write file");
        when(filmService.update(any(), any())).thenThrow(expectedException);
        var model = new ConcurrentModel();
        var view = filmController.update(new Film(), testFile, model);
        var actualExceptionMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenDeleteFilmThenGetPage() {
        when(filmService.deleteById(1)).thenReturn(true);
        var model = new ConcurrentModel();
        var view = filmController.delete(model, 1);
        assertThat(view).isEqualTo("redirect:/films");
    }

    @Test
    public void whenDeleteFilmThenGetError() {
        when(filmService.deleteById(1)).thenReturn(false);
        var model = new ConcurrentModel();
        var view = filmController.delete(model, 1);
        var actualMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Фильм с указанным идентификатором не найден");
    }
}
