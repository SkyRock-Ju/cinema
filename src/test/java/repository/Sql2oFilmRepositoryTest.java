package repository;

import com.cinema.configuration.DataSourceConfiguration;
import com.cinema.model.File;
import com.cinema.model.Film;
import com.cinema.repository.Sql2oFileRepository;
import com.cinema.repository.Sql2oFilmRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oFilmRepositoryTest {

    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oFileRepository sql2oFileRepository;
    private static File file;

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

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = sql2oFileRepository.save(new File("test", "test"));
    }

    @AfterAll
    public static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    public void clearFilms() {
        var films = sql2oFilmRepository.findAll();
        for (var film : films) {
            sql2oFilmRepository.deleteById(film.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var film = sql2oFilmRepository.save(new Film(0, "film", "description",
                2002, 1, 8, 120, file.getId()));
        var savedFilm = sql2oFilmRepository.findById(film.orElseThrow().getId());
        assertThat(savedFilm).usingRecursiveComparison().isEqualTo(film);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var film1 = sql2oFilmRepository.save(new Film(0, "film1", "description1",
                2002, 1, 8, 120, file.getId()));
        var film2 = sql2oFilmRepository.save(new Film(0, "film2", "description2",
                2002, 1, 8, 120, file.getId()));
        var film3 = sql2oFilmRepository.save(new Film(0, "film3", "description3",
                2002, 1, 8, 120, file.getId()));
        var result = sql2oFilmRepository.findAll();
        assertThat(result).isEqualTo(List.of(film1.orElseThrow(), film2.orElseThrow(), film3.orElseThrow()));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFilmRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oFilmRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var film = sql2oFilmRepository.save(new Film(0, "film", "description",
                2002, 1, 8, 120, file.getId()));
        var isDeleted = sql2oFilmRepository.deleteById(film.orElseThrow().getId());
        var savedFilm = sql2oFilmRepository.findById(film.orElseThrow().getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedFilm).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oFilmRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var film = sql2oFilmRepository.save(new Film(0, "film", "description",
                2002, 1, 8, 120, file.getId()));
        var updatedFilm = new Film(film.orElseThrow().getId(), "new film", "new description",
                2002, 1, 8, 120, file.getId());
        var isUpdated = sql2oFilmRepository.update(updatedFilm);
        var savedFilm = sql2oFilmRepository.findById(updatedFilm.getId());
        assertThat(isUpdated).isTrue();
        assertThat(savedFilm.orElseThrow()).usingRecursiveComparison().isEqualTo(updatedFilm);
    }

    @Test
    public void whenUpdateNotExistingFilmThenGetFalse() {
        var film = new Film(0, "film", "description",
                2002, 1, 8, 120, file.getId());
        var isUpdated = sql2oFilmRepository.update(film);
        assertThat(isUpdated).isFalse();
    }
}
