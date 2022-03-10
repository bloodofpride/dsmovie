package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dtos.MovieDTO;
import com.devsuperior.dsmovie.entities.Movie;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.excepions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public Page<MovieDTO> findAll(Pageable pageable){
        Page<Movie> movies = movieRepository.findAll(pageable);
        Page<MovieDTO> page = movies.map(MovieDTO::new);
        return page;
    }

    public MovieDTO findById(Long id){
        Optional<Movie> movie = movieRepository.findById(id);
        return new MovieDTO(movie.orElseThrow(() -> new ObjectNotFoundException("Movie not found with id: "+id)));
    }
}
