package controller;

import com.cinema.controller.UserController;
import com.cinema.model.User;
import com.cinema.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestRegisterThenGetPage() {
        assertThat(userController.getCreationPage()).isEqualTo("users/register");
    }

    @Test
    public void whenRequestLoginThenGetPage() {
        var model = new ConcurrentModel();
        assertThat(userController.getLoginPage(model)).isEqualTo("users/login");
    }

    @Test
    public void whenPostUserThenRedirectToIndexPage() {
        when(userService.save(any())).thenReturn(Optional.of(new User()));
        var model = new ConcurrentModel();
        assertThat(userController.create(model, new User())).isEqualTo("redirect:/index");
    }

    @Test
    public void whenPostExistingUserThenGetErrorPage() {
        var model = new ConcurrentModel();
        var view = userController.create(model, new User());
        var actualMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Пользователь с такой почтой уже существует");
    }

    @Test
    public void whenPostLoginThenRedirectToIndexPage() {
        var user = new User(1, "email", "name", "password");
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));
        var model = new ConcurrentModel();
        var httpRequest = new MockHttpServletRequest();
        var view = userController.loginUser(new User(), model, httpRequest);
        assertThat(view).isEqualTo("redirect:/index");
        assertThat(Objects.requireNonNull(httpRequest.getSession()).getAttribute("user")).isEqualTo(user);
    }

    @Test
    public void whenRequestLogoutThenGetPage() {
        var session = new MockHttpSession();
        assertThat(userController.logout(session)).isEqualTo("redirect:/users/login");
    }

}
