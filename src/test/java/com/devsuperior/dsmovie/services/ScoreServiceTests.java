package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class ScoreServiceTests {

	@InjectMocks
	private ScoreService service;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	private Long existingMovieId, nonExistingMovieId;
	private UserEntity user;
	private MovieEntity movie;
	private ScoreEntity score;
	private ScoreDTO scoreDTO;

	@BeforeEach
	void setUp() {
		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		user = UserFactory.createUserEntity();
		movie = MovieFactory.createMovieEntity();
		score = ScoreFactory.createScoreEntity();
		scoreDTO = ScoreFactory.createScoreDTO();

		Mockito.when(userService.authenticated()).thenReturn(user);
		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
		Mockito.when(scoreRepository.saveAndFlush(Mockito.any())).thenReturn(score);
		Mockito.when(movieRepository.save(Mockito.any())).thenReturn(movie);
		Mockito.doAnswer(invocation -> {
			ScoreEntity newScore = invocation.getArgument(0);
			movie.getScores().add(newScore);
			return score;
		}).when(scoreRepository).saveAndFlush(Mockito.any());
	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {
		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingMovieId, result.getId());
		Assertions.assertEquals(4.5, result.getScore());
		Assertions.assertEquals(1, result.getCount());
	}

	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		ScoreDTO nonExistingMovieScoreDTO = new ScoreDTO(nonExistingMovieId, 4.0);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(nonExistingMovieScoreDTO);
		});
	}
}
