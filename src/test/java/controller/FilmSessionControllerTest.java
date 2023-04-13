package controller;

import com.cinema.controller.FilmSessionController;
import com.cinema.model.FilmSession;
import com.cinema.service.FilmService;
import com.cinema.service.FilmSessionService;
import com.cinema.service.HallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilmSessionControllerTest {

    private FilmSessionService filmSessionService;
    private FilmSessionController filmSessionController;

    @BeforeEach
    public void initTests() {
        filmSessionService = mock(FilmSessionService.class);
        HallService hallService = mock(HallService.class);
        FilmService filmService = mock(FilmService.class);
        filmSessionController = new FilmSessionController(filmSessionService, hallService, filmService);
    }

    @Test
    public void whenRequestFilmSessionListPageThenGetPageWithFilmSessions() {
        var filmSession1 = new FilmSession(1, 1, 1,
                LocalDateTime.now(), LocalDateTime.now(), 1);
        var filmSession2 = new FilmSession(2, 2, 2,
                LocalDateTime.now(), LocalDateTime.now(), 2);
        var filmSessions = List.of(filmSession1, filmSession2);
        when(filmSessionService.findAll()).thenReturn(filmSessions);
        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualFilmSessions = model.getAttribute("film_sessions");
        var expectedFilmSessionsView = filmSessions.stream().map(filmSessionService::toView).collect(Collectors.toList());
        assertThat(view).isEqualTo("film_sessions/list");
        assertThat(actualFilmSessions).isEqualTo(expectedFilmSessionsView);
    }

    @Test
    public void whenRequestCreationPageThenGetPage() {
        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        assertThat(view).isEqualTo("film_sessions/list");
    }

    @Test
    public void whenPostFilmSessionThenSameDataAndRedirectToFilmSessionPage() {
        var filmSession = new FilmSession(1, 1, 1,
                LocalDateTime.now(), LocalDateTime.now(), 1);
        var filmSessionArgumentCaptor = ArgumentCaptor.forClass(FilmSession.class);
        when(filmSessionService.save(filmSessionArgumentCaptor.capture())).thenReturn(filmSession);
        var model = new ConcurrentModel();
        var view = filmSessionController.create(filmSession, model);
        var actualFilmSession = filmSessionArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/film_sessions");
        assertThat(actualFilmSession).isEqualTo(filmSession);
    }

    @Test
    public void whenRequestFilmSessionByIdThenGetCorrespondingFilmSession() {
        var filmSession = new FilmSession(1, 1, 1,
                LocalDateTime.now(), LocalDateTime.now(), 1);
        when(filmSessionService.findById(filmSession.getId())).thenReturn(Optional.of(filmSession));
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, filmSession.getId());
        var actualFilmSession = model.getAttribute("film_session");
        assertThat(view).isEqualTo("film_sessions/one");
        assertThat(actualFilmSession).isEqualTo(filmSession);
    }

    @Test
    public void whenRequestFilmSessionByNotExistingIdThenGetError() {
        when(filmSessionService.findById(1)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);
        var actualMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Сеанс с указанным идентификатором не найден");
    }

    @Test
    public void whenUpdateFilmSessionThenSameDataAndRedirectToFilmSessionsPage() {
        var filmSession = new FilmSession(1, 1, 1,
                LocalDateTime.now(), LocalDateTime.now(), 1);
        var filmSessionArgumentCaptor = ArgumentCaptor.forClass(FilmSession.class);
        when(filmSessionService.update(filmSessionArgumentCaptor.capture())).thenReturn(true);
        var model = new ConcurrentModel();
        var view = filmSessionController.update(filmSession, model);
        var actualFilmSession = filmSessionArgumentCaptor.getValue();
        assertThat(view).isEqualTo("redirect:/film_sessions");
        assertThat(actualFilmSession).isEqualTo(filmSession);
    }

    @Test
    public void whenDeleteFilmSessionThenGetPage() {
        when(filmSessionService.deleteById(1)).thenReturn(true);
        var model = new ConcurrentModel();
        var view = filmSessionController.delete(model, 1);
        assertThat(view).isEqualTo("redirect:/film_sessions");
    }

    @Test
    public void whenDeleteFilmSessionThenGetError() {
        when(filmSessionService.deleteById(1)).thenReturn(false);
        var model = new ConcurrentModel();
        var view = filmSessionController.delete(model, 1);
        var actualMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Сеанс с указанным идентификатором не найден");
    }
}
