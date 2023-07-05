package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MpaServiceTest {
    private final MpaService mpaService;

    @Test
    public void testGetMpa() {
        assertEquals(mpaService.getMpa(1), new Mpa(1, "G"));
    }

    @Test
    public void testGetAllMpa() {
        List<Mpa> mpas = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            mpas.add(mpaService.getMpa(i));
        }
        assertEquals(mpaService.getAllMpa(), mpas);
    }

}