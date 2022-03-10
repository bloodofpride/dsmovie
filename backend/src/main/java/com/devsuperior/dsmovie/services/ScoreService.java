package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dtos.MovieDTO;
import com.devsuperior.dsmovie.dtos.ScoreDTO;
import com.devsuperior.dsmovie.entities.Movie;
import com.devsuperior.dsmovie.entities.Score;
import com.devsuperior.dsmovie.entities.User;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.services.excepions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ScoreService {
    @Autowired
    private UserRepository userReposiory;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional
    public MovieDTO saveScore(ScoreDTO scoreDTO){
        User user = userReposiory.findByEmail(scoreDTO.getEmail());
        if(user == null){
            user = new User(null, scoreDTO.getEmail());
            user = userReposiory.saveAndFlush(user);
        }

        Movie movie = movieRepository.findById(scoreDTO.getMovieId())
                .orElseThrow(() -> new ObjectNotFoundException("Movie not found with id: "+scoreDTO.getMovieId()));

        Score score = new Score();
        score.setMovie(movie);
        score.setUser(user);
        score.setValue(scoreDTO.getScore());

        score = scoreRepository.saveAndFlush(score);

        double sum = movie.getScores().stream()
                .map(Score::getValue)
                .reduce(Double::sum)
                .orElse(0.0);

        movie.setScore(sum/movie.getScores().size());
        movie.setCount(movie.getScores().size());
        movie = movieRepository.save(movie);

        return new MovieDTO(movie);
    }
}
