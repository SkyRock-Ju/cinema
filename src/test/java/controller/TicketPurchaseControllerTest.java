package controller;

import com.cinema.controller.TicketPurchaseController;
import com.cinema.model.*;
import com.cinema.model.dto.TicketDto;
import com.cinema.service.FilmSessionService;
import com.cinema.service.HallService;
import com.cinema.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketPurchaseControllerTest {

    private TicketService ticketService;
    private TicketPurchaseController ticketPurchaseController;
    private FilmSessionService filmSessionService;
    private HallService hallService;

    @BeforeEach
    public void initTests() {
        ticketService = mock(TicketService.class);
        filmSessionService = mock(FilmSessionService.class);
        hallService = mock(HallService.class);
        ticketPurchaseController = new TicketPurchaseController(ticketService, filmSessionService, hallService);
    }

    @Test
    public void whenRequestTicketPurchasePageThenGetTicketBuyingPage() {
        var filmSession1 = new FilmSession(1, 1, 1,
                LocalDateTime.now(), LocalDateTime.now(), 1);
        var hall = new Hall(1, "hall", 1, 1, "desc");
        when(filmSessionService.findById(filmSession1.getId())).thenReturn(Optional.of(filmSession1));
        when(hallService.findById(hall.getId())).thenReturn(Optional.of(hall));
        var model = new ConcurrentModel();
        var session = new MockHttpSession();
        var view = ticketPurchaseController.getPurchasePage(model, 1, session);
        var actualFilmSessions = model.getAttribute("film_sessions");
        var expectedFilmSessionsView = filmSessionService.toView(filmSession1);
        var actualHalls = model.getAttribute("halls");
        var actualRows = model.getAttribute("rows");
        var expectedRows = new ArrayList<>();
        expectedRows.add(1);
        assertThat(view).isEqualTo("tickets/purchase");
        assertThat(actualFilmSessions).isEqualTo(expectedFilmSessionsView);
        assertThat(actualHalls).isEqualTo(Optional.of(hall));
        assertThat(actualRows).isEqualTo(expectedRows);
    }

    @Test
    public void whenPostTicketThenSameDataAndRedirectToSuccessPage() {
        var ticket = new Ticket(1, 1, 1, 1);
        var ticketRowPlace = new TicketDto(1, 1);
        var ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketArgumentCaptor.capture())).thenReturn(Optional.of(ticket));
        var model = new ConcurrentModel();
        var request = new MockHttpSession();
        request.setAttribute("user", new User(1, "fullName", "email", "password"));
        request.setAttribute("film_session",
                new FilmSession(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 1));
        var view = ticketPurchaseController.create(ticketRowPlace, model, request);
        var actualTicket = ticketArgumentCaptor.getValue();
        var message = model.getAttribute("message");
        assertThat(view).isEqualTo("tickets/success");
        assertThat(actualTicket).isEqualTo(ticket);
        assertThat(message).isEqualTo("Вы успешно приобрели билет на ["
                        + ticket.getRowNumber()
                        + "] ряд "
                        + "["
                        + ticket.getPlaceNumber()
                        + "] место");
    }

    @Test
    public void whenPostTicketThenErrorPage() {
        var ticketRowPlace = new TicketDto(1, 1);
        when(ticketService.save(any())).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var request = new MockHttpSession();
        request.setAttribute("user", new User(1, "fullName", "email", "password"));
        request.setAttribute("film_session",
                new FilmSession(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 1));
        var view = ticketPurchaseController.create(ticketRowPlace, model, request);
        var message = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo("Не удалось приобрести билет на заданное место. "
                + "Вероятно оно уже занято. "
                + "Перейдите на страницу бронирования билетов и попробуйте снова.");
    }
}
