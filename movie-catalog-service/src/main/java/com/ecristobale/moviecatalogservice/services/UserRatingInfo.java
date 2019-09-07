package com.ecristobale.moviecatalogservice.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.ecristobale.moviecatalogservice.models.Rating;
import com.ecristobale.moviecatalogservice.models.UserRating;
import com.ecristobale.moviecatalogservice.resources.MovieCatalogResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserRatingInfo {

	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod="getFallbackUserRating")
	public UserRating getUserRating(@PathVariable("userId") String userId) {
		return restTemplate.getForObject(MovieCatalogResource.HTTP.concat(MovieCatalogResource.EUREKA_RATINGS_NAME).concat("/ratingsdata/users/").concat(userId), UserRating.class);
	}
	
	public UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
		UserRating userRating = new UserRating();
		userRating.setUserRating(Arrays.asList(new Rating("0",0)));
		return userRating;
	}
}
