package com.ecristobale.movieinfoservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ecristobale.movieinfoservice.models.Movie;
import com.ecristobale.movieinfoservice.models.MovieSummary;

@RestController
@RequestMapping("/movies")
public class MovieResource {
	
	@Value("${api.key}")
	private String apiKey;
	
	@Value("${api.url}")
	private String THE_MOVIE_DB_API;
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/{movieId}")
	public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
		MovieSummary movieSummary = restTemplate.getForObject(
				THE_MOVIE_DB_API.concat(movieId).concat("?api_key=").concat(apiKey),
				MovieSummary.class);
		return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
	}
}
