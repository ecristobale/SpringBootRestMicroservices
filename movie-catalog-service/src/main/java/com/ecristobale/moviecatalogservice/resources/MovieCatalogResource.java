package com.ecristobale.moviecatalogservice.resources;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecristobale.moviecatalogservice.models.CatalogItem;
import com.ecristobale.moviecatalogservice.models.CatalogItemWrapper;
import com.ecristobale.moviecatalogservice.models.Movie;
import com.ecristobale.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	private String EUREKA_RATINGS_NAME = "ratings-data-service";
	private String EUREKA_MOVIE_INFO_NAME = "movie-info-service";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public CatalogItemWrapper getCatalog(@PathVariable("userId") String userId){
		CatalogItemWrapper catalogItemWrapper = new CatalogItemWrapper();
		
		//get all rated movie IDs
		UserRating ratings = restTemplate.getForObject("http://".concat(EUREKA_RATINGS_NAME).concat("/ratingsdata/users/foo"), UserRating.class);
		
		catalogItemWrapper.setCatalogItem(ratings.getUserRating().stream().map(rating -> {
			//for each movie ID, call movie info service and get details
			Movie movie = restTemplate.getForObject("http://".concat(EUREKA_MOVIE_INFO_NAME).concat("/movies/").concat(rating.getMovieId()), Movie.class);	
			
			//put them all together
			return new CatalogItem(movie.getName(), movie.getOverview(), rating.getRating());
		})
		.collect(Collectors.toList()));
		return catalogItemWrapper;
	}
	
	// an alternative way to call external API by using asynch WebClient
//	Movie movie = webClientBuilder.build()
//		.get()
//		.uri("http://localhost:8082/movies/".concat(rating.getMovieId()))
//		.retrieve()
//		.bodyToMono(Movie.class)
//		.block();
}
