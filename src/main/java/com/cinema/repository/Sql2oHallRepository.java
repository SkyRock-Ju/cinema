package com.cinema.repository;

import com.cinema.model.Hall;
import com.cinema.model.User;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Hall> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM halls");
            return query.executeAndFetch(Hall.class);
        }
    }

    @Override
    public Optional<Hall> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM halls WHERE id = :id");
            query.addParameter("id", id);
            var result = Optional.ofNullable(query.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetch(Hall.class));
            if (result.get().isEmpty()) {
                return Optional.empty();
            }
            return result.map(halls -> halls.get(0));
        }
    }
}
