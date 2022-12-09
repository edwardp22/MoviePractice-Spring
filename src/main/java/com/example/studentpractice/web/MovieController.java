package com.example.studentpractice.web;

import com.example.studentpractice.entities.Movie;
import com.example.studentpractice.repositories.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@SessionAttributes({"a","e"})
@Controller
@AllArgsConstructor
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    static int num = 0;

    @GetMapping(path = "/")
    public String movies(Model model, @RequestParam(name="keyword",defaultValue = "") String keyword) {
        List<Movie> movies;

        if (keyword.isEmpty()) {
            movies = movieRepository.findAll();
        } else {
            long key = Long.parseLong(keyword);
            movies = movieRepository.findMovieById(key);
        }
        model.addAttribute("listMovies", movies);

        return "movies";
    }

    @GetMapping("/delete")
    public String delete(Long id){
        movieRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/formMovies")
    public String formMovies(Model model){
        model.addAttribute("movie", new Movie());
        return "formMovies";
    }

    @PostMapping(path="/save")
    public String save(Model model, Movie movie, BindingResult bindingResult, ModelMap mm, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "formMovies";
        } else {
            movieRepository.save(movie);
            if (num == 2) {
                mm.put("e", 2);
                mm.put("a", 0);
            } else {
                mm.put("a", 1);
                mm.put("e", 0);
            }
            return "redirect:/";
        }
    }

    @GetMapping("/editMovies")
    public String editMovies(Model model, Long id, HttpSession session){
        num = 2;
        session.setAttribute("info", 0);
        Movie movie = movieRepository.findById(id).orElse(null);
        if(movie ==null) throw new RuntimeException("Movie does not exist");
        model.addAttribute("movie", movie);
        return "editMovies";
    }
}
