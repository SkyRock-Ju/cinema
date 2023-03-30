package com.cinema.controller;

import com.cinema.service.FilmSessionService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@ThreadSafe
@Controller
@RequestMapping("/film_sessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;

    public FilmSessionController(FilmSessionService filmSessionService) {
        this.filmSessionService = filmSessionService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("film_sessions", filmSessionService.findAll().stream().map(
                filmSessionService::toView).collect(Collectors.toList()));
        return "film_sessions/list";
    }
}
