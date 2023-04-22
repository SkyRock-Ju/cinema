package com.cinema.controller;

import com.cinema.model.FilmSession;
import com.cinema.service.FilmService;
import com.cinema.service.FilmSessionService;
import com.cinema.service.HallService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@ThreadSafe
@Controller
@RequestMapping("/film_sessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;
    private final FilmService filmService;
    private final HallService hallService;

    public FilmSessionController(FilmSessionService filmSessionService,
                                 HallService hallService, FilmService filmService) {
        this.filmSessionService = filmSessionService;
        this.hallService = hallService;
        this.filmService = filmService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("film_sessions", filmSessionService.findAll());
        return "film_sessions/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("halls", hallService.findAll());
        model.addAttribute("films", filmService.findAll());
        return "film_sessions/create";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var filmSessionOptional = filmSessionService.findById(id);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("film_session", filmSessionOptional.orElseThrow());
        model.addAttribute("halls", hallService.findAll());
        model.addAttribute("films", filmService.findAll());
        return "film_sessions/one";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute FilmSession filmSession) {
        filmSessionService.save(filmSession);
        return "redirect:/film_sessions";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute FilmSession filmSession, Model model) {
        var isUpdated = filmSessionService.update(filmSession);
        if (!isUpdated) {
            model.addAttribute("message",
                    "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        return "redirect:/film_sessions";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = filmSessionService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        return "redirect:/film_sessions";
    }
}
