package com.cinema.service;

import com.cinema.model.Ticket;
import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
public interface TicketService {

    Collection<Ticket> findAll();

    Optional<Ticket> save(Ticket ticket);
}
