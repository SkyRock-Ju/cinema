package repository;

import com.cinema.configuration.DataSourceConfiguration;
import com.cinema.model.File;
import com.cinema.model.Film;
import com.cinema.model.FilmSession;
import com.cinema.repository.Sql2oFileRepository;
import com.cinema.repository.Sql2oFilmRepository;
import com.cinema.repository.Sql2oFilmSessionRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oFilmSessionRepositoryTest {

    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;
    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oFileRepository sql2oFileRepository;
    private static File file;
    private static Optional<Film> film;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DataSourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oFileRepository = new Sql2oFileRepository(sql2o);
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        file = sql2oFileRepository.save(new File("test", "test"));
        film = sql2oFilmRepository.save(new Film(0, "film", "desc",
                2000, 1, 18, 120, file.getId()));
    }

    @AfterEach
    public void clearFilmSessions() {
        var filmSessions = sql2oFilmSessionRepository.findAll();
        for (var filmSession : filmSessions) {
            sql2oFilmSessionRepository.deleteById(filmSession.getId());
        }
    }

    @AfterAll
    public static void clear() {
        sql2oFilmRepository.deleteById(film.orElseThrow().getId());
        sql2oFileRepository.deleteById(file.getId());
    }

    @Test
    public void whenSaveThenGetSame() {
        var startAndEndDate = now().truncatedTo(ChronoUnit.MINUTES);
        var filmSession = sql2oFilmSessionRepository.save(new FilmSession(0, film.orElseThrow().getId(),
                1, startAndEndDate, startAndEndDate, 120));
        var savedFilmSession = sql2oFilmSessionRepository.findById(filmSession.getId());
        assertThat(savedFilmSession.orElseThrow()).usingRecursiveComparison().isEqualTo(filmSession);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var startAndEndDate = now().truncatedTo(ChronoUnit.MINUTES);
        var filmSession1 = sql2oFilmSessionRepository.save(new FilmSession(0, film.orElseThrow().getId(),
                1, startAndEndDate, startAndEndDate, 121));
        var filmSession2 = sql2oFilmSessionRepository.save(new FilmSession(0, film.orElseThrow().getId(),
                1, startAndEndDate, startAndEndDate, 122));
        var filmSession3 = sql2oFilmSessionRepository.save(new FilmSession(0, film.orElseThrow().getId(),
                1, startAndEndDate, startAndEndDate, 123));
        var result = sql2oFilmSessionRepository.findAll();
        assertThat(result).isEqualTo(List.of(filmSession1, filmSession2, filmSession3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFilmSessionRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oFilmSessionRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var startAndEndDate = now().truncatedTo(ChronoUnit.MINUTES);
        var filmSession = sql2oFilmSessionRepository.save(new FilmSession(0, film.orElseThrow().getId(),
                1, startAndEndDate, startAndEndDate, 120));
        var isDeleted = sql2oFilmSessionRepository.deleteById(filmSession.getId());
        var savedFilmSession = sql2oFilmSessionRepository.findById(filmSession.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedFilmSession).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oFilmSessionRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var startAndEndDate = now().truncatedTo(ChronoUnit.MINUTES);
        var filmSession = sql2oFilmSessionRepository.save(new FilmSession(0, film.orElseThrow().getId(),
                1, startAndEndDate, startAndEndDate, 120));
        var startAndEndDateUpdated = now().truncatedTo(ChronoUnit.MINUTES);
        var updatedFilmSession = new FilmSession(filmSession.getId(), film.orElseThrow().getId(),
                1, startAndEndDateUpdated, startAndEndDateUpdated, 333);
        var isUpdated = sql2oFilmSessionRepository.update(updatedFilmSession);
        var savedFilmSession = sql2oFilmSessionRepository.findById(updatedFilmSession.getId());
        assertThat(isUpdated).isTrue();
        assertThat(savedFilmSession.orElseThrow()).usingRecursiveComparison().isEqualTo(updatedFilmSession);
    }

    @Test
    public void whenUpdateNotExistingFilmThenGetFalse() {
        var startAndEndDate = now().truncatedTo(ChronoUnit.MINUTES);
        var filmSession = new FilmSession(0, film.orElseThrow().getId(),
                1, startAndEndDate, startAndEndDate, 120);
        var isUpdated = sql2oFilmSessionRepository.update(filmSession);
        assertThat(isUpdated).isFalse();
    }
}