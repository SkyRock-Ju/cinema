package controller;

import com.cinema.controller.IndexController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IndexControllerTest {

    private IndexController indexController;

    @BeforeEach
    public void init() {
        indexController = new IndexController();
    }

    @Test
    public void whenRequestIndexThenGetPage() {
        assertThat(indexController.getIndex()).isEqualTo("index");
    }
}
