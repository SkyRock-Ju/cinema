package com.cinema.service;

import com.cinema.model.Ticket;
import com.cinema.repository.Sql2oTicketRepository;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleTicketService implements TicketService {

    private final Sql2oTicketRepository sql2oTicketRepository;

    public SimpleTicketService(Sql2oTicketRepository sql2oTicketRepository) {
        this.sql2oTicketRepository = sql2oTicketRepository;
    }

    @Override
    public Collection<Ticket> findAll() {
        return sql2oTicketRepository.findAll();
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return sql2oTicketRepository.save(ticket);
    }
}
