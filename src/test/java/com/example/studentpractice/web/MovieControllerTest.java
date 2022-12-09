package com.example.studentpractice.web;

import com.example.studentpractice.entities.Movie;
import com.example.studentpractice.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.*;
import org.springframework.web.servlet.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;





@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class MovieControllerTest {
    Movie movie;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    MovieRepository movieRepository;

    @Mock
    View mockView;

    @InjectMocks
    MovieController movieController;

    @BeforeEach
    void setup() throws ParseException {
        movie = new Movie();
        movie.setId(1L);
        movie.setName("Edward");
        String sDate1 = "2012/11/11";
        Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(sDate1);
        movie.setRelease_date(date1);

        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(movieController).setSingleView(mockView).build();
    }

    @Test
    public void findAll_ListView() throws Exception {
        List<Movie> list = new ArrayList<Movie>();
        list.add(movie);
        list.add(movie);

        when(movieRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listMovies", list))
                .andExpect(view().name("movies"))
                .andExpect(model().attribute("listMovies", hasSize(2)));

        verify(movieRepository, times(1)).findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void findById() throws Exception {
        List<Movie> list = new ArrayList<Movie>();
        list.add(movie);

        when(movieRepository.findMovieById(1L)).thenReturn(list);
        mockMvc.perform(get("/").param("keyword", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listMovies", list))
                .andExpect(view().name("movies"))
                .andExpect(model().attribute("listMovies", hasSize(1)));

        verify(movieRepository, times(1)).findMovieById(anyLong());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void movies() {
    }

    @Test
    void delete() {
        ArgumentCaptor<Long> idCapture = ArgumentCaptor.forClass(Long.class);
        doNothing().when(movieRepository).deleteById(idCapture.capture());
        movieRepository.deleteById(1L);
        assertEquals(1L, idCapture.getValue());
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void formMovies() throws Exception {
        mockMvc.perform(get("/formMovies"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movie", new Movie()))
                .andExpect(view().name("formMovies"));
    }

    @Test
    void save() {
        when(movieRepository.save(movie)).thenReturn(movie);
        movieRepository.save(movie);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void editMovies() throws Exception {
        Movie s2 = new Movie();
        s2.setId(1L);
        s2.setName("Edward");
        String sDate1 = "2012/11/11";
        Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(sDate1);
        s2.setRelease_date(date1);

        Long iid = 1l;
        when(movieRepository.findById(iid)).thenReturn(Optional.of(s2));
        mockMvc.perform(get("/editMovies").param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movie", s2))
                .andExpect(view().name("editMovies"));

        verify(movieRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(movieRepository);
    }
}