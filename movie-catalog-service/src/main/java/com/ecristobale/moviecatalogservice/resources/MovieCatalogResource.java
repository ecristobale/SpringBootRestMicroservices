package com.ecristobale.moviecatalogservice.resources;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecristobale.moviecatalogservice.models.CatalogItemWrapper;
import com.ecristobale.moviecatalogservice.models.UserRating;
import com.ecristobale.moviecatalogservice.services.MovieInfo;
import com.ecristobale.moviecatalogservice.services.UserRatingInfo;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	public static String EUREKA_RATINGS_NAME = "ratings-data-service";
	public static String EUREKA_MOVIE_INFO_NAME = "movie-info-service";
	public static final String HTTP = "http://";
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private MovieInfo movieInfo;
	
	@Autowired
	private UserRatingInfo userRatingInfo;

	@RequestMapping("/{userId}")
	public CatalogItemWrapper getCatalog(@PathVariable("userId") String userId){
		CatalogItemWrapper catalogItemWrapper = new CatalogItemWrapper();
		
		//get all rated movie IDs
		UserRating ratings = userRatingInfo.getUserRating(userId);
		
		catalogItemWrapper.setCatalogItem(ratings.getUserRating().stream()
				.map(rating -> movieInfo.getCatalogItem(rating))
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
