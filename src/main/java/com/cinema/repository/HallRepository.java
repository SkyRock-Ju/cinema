package com.cinema.repository;

import com.cinema.model.Hall;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface HallRepository {

    Collection<Hall> findAll();

    Optional<Hall> findById(int id);

}
