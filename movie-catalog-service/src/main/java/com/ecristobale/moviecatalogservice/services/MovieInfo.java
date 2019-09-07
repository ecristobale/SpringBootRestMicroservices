package com.ecristobale.moviecatalogservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecristobale.moviecatalogservice.models.CatalogItem;
import com.ecristobale.moviecatalogservice.models.Movie;
import com.ecristobale.moviecatalogservice.models.Rating;
import com.ecristobale.moviecatalogservice.resources.MovieCatalogResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MovieInfo {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod="getFallbackCatalogItem",
			threadPoolKey="movieInfoPool",
			threadPoolProperties= {
					@HystrixProperty(name="coreSize",value="20"),
					@HystrixProperty(name="maxQueueSize",value="10")
			})
	public CatalogItem getCatalogItem(Rating rating) {
		//for each movie ID, call movie info service and get details
		Movie movie = restTemplate.getForObject(MovieCatalogResource.HTTP.concat(MovieCatalogResource.EUREKA_MOVIE_INFO_NAME).concat("/movies/").concat(rating.getMovieId()), Movie.class);	
		
		//put them all together
		return new CatalogItem(movie.getName(), movie.getOverview(), rating.getRating());
	}
	
	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie name not found", "This is the Hystrix fallback catalog item method", rating.getRating());
	}
}
