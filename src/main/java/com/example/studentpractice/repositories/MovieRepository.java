package com.example.studentpractice.repositories;

import com.example.studentpractice.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findMovieById (long kw);
}
