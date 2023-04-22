package com.cinema.controller;

import com.cinema.model.FileDto;
import com.cinema.model.Film;
import com.cinema.service.FilmService;
import com.cinema.service.GenreService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@ThreadSafe
@Controller
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    private final GenreService genreService;

    public FilmController(FilmService filmService, GenreService genreService) {
        this.filmService = filmService;
        this.genreService = genreService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("genres", genreService.findAll());
        return "films/create";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        try {
            var filmDto = filmService.findById(id);
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("film", filmDto);
        } catch (NoSuchElementException exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
        return "films/one";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Film film, @RequestParam MultipartFile file, Model model) {
        try {
            filmService.save(film, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/films";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Film film, @RequestParam MultipartFile file, Model model) {
        try {
            var isUpdated = filmService.update(film, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message",
                        "Фильм с указанным идентификатором не найден");
                return "errors/404";
            }
            return "redirect:/films";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = filmService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Фильм с указанным идентификатором не найден");
            return "errors/404";
        }
        return "redirect:/films";
    }
}
