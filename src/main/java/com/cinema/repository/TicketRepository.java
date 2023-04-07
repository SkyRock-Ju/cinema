package com.cinema.repository;

import com.cinema.model.Hall;
import com.cinema.model.Ticket;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface TicketRepository {

    Optional<Ticket> save(Ticket ticket);

    Collection<Ticket> findAll();

}
