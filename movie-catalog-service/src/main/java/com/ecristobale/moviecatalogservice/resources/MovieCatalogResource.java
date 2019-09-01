package com.ecristobale.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecristobale.moviecatalogservice.models.CatalogItem;
import com.ecristobale.moviecatalogservice.models.Movie;
import com.ecristobale.moviecatalogservice.models.Rating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

		//get all rated movie IDs
		List<Rating> ratings = Arrays.asList(
				new Rating("234", 8),
				new Rating("432", 9));
		
		return ratings.stream().map(rating -> {
			//Movie movie = restTemplate.getForObject("http://localhost:8082/movies/".concat(rating.getMovieId()), Movie.class);	
			
			// an alternative way to call external API by using asynch WebClient
			Movie movie = webClientBuilder.build()
				.get()
				.uri("http://localhost:8082/movies/".concat(rating.getMovieId()))
				.retrieve()
				.bodyToMono(Movie.class)
				.block();
			
			return new CatalogItem(movie.getName(), "Description of the movie", rating.getRating());
		})
				.collect(Collectors.toList());
		
		//for each movie ID, call movie info service and get details
		
		//put them all together
	}
}
