package com.spark.springboot2.controller;

import com.spark.springboot2.pojo.Tweet;
import com.spark.springboot2.repos.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class AppController {

    @Autowired
    private TweetRepository tweetRepository;

    @GetMapping("/tweets")
    public Flux<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }

    @PostMapping("/tweets")
    public Mono<Tweet> createTweet(@Valid @RequestBody Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @GetMapping("/tweets/{id}")
    public Mono<ResponseEntity<Tweet>> getTweetById(@PathVariable("id") String tweetId) {
        return tweetRepository.findById(tweetId).map(tweetObject -> ResponseEntity.ok(tweetObject)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/tweets/{id}")
    public Mono<ResponseEntity<Void>> deleteTweet(@PathVariable(value = "id") String tweetId) {

        return tweetRepository.findById(tweetId).flatMap(existingTweet -> tweetRepository.delete(existingTweet).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
