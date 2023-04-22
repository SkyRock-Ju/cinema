package com.cinema.controller;

import com.cinema.model.FilmSession;
import com.cinema.model.Ticket;
import com.cinema.model.dto.TicketDto;
import com.cinema.model.User;
import com.cinema.service.FilmSessionService;
import com.cinema.service.HallService;
import com.cinema.service.TicketService;
import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Controller
@RequestMapping("/purchase")
public class TicketPurchaseController {

    private final TicketService ticketService;
    private final FilmSessionService filmSessionService;
    private final HallService hallService;

    public TicketPurchaseController(TicketService ticketService, FilmSessionService filmSessionService,
                                    HallService hallService) {
        this.ticketService = ticketService;
        this.filmSessionService = filmSessionService;
        this.hallService = hallService;
    }

    @GetMapping("/{id}")
    public String getPurchasePage(Model model, @PathVariable int id, HttpSession httpSession) {
        var filmSession = filmSessionService.findById(id).orElseThrow();
        var halls = hallService.findById(filmSession.getHallsId());
        List<Integer> rows = new ArrayList<>();
        for (int i = 1; i <= halls.orElseThrow().getRowCount(); i++) {
            rows.add(i);
        }
        List<Integer> places = new ArrayList<>();
        for (int i = 1; i <= halls.orElseThrow().getPlaceCount(); i++) {
            places.add(i);
        }
        model.addAttribute("film_session", filmSessionService.toView(filmSession));
        model.addAttribute("halls", halls);
        model.addAttribute("rows", rows);
        model.addAttribute("places", places);
        httpSession.setAttribute("film_session", filmSession);
        return "tickets/purchase";
    }

    @PostMapping
    public String create(@ModelAttribute TicketDto ticket, Model model, HttpSession request) {
        try {
            FilmSession filmSession = (FilmSession) request.getAttribute("film_session");
            User user = (User) request.getAttribute("user");
            var result = ticketService.save(new Ticket(filmSession.getId(),
                    ticket.getRowNumber(), ticket.getPlaceNumber(), user.getId()));
            if (result.isEmpty()) {
                model.addAttribute("message",
                        "Не удалось приобрести билет на заданное место. "
                                + "Вероятно оно уже занято. "
                                + "Перейдите на страницу бронирования билетов и попробуйте снова.");
                return "errors/404";
            }
            model.addAttribute("message",
                    String.format("Вы успешно приобрели билет на [%s] ряд [%s] место.",
                            ticket.getRowNumber(), ticket.getPlaceNumber()));
            return "tickets/success";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }
}
