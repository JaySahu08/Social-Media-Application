package com.jay.SocialMedia;

import com.jay.SocialMedia.DTO.CreatePostRequest;
import com.jay.SocialMedia.DTO.CreateUserRequest;
import com.jay.SocialMedia.DTO.UserDTO;
import com.jay.SocialMedia.Service.PostService;
import com.jay.SocialMedia.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SoicalMediaApplication {
	public static void main(String[] args) {
		SpringApplication.run(SoicalMediaApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(UserService userService, PostService postService) {
		return args -> {
			// Create Users
			CreateUserRequest user1 = new CreateUserRequest();
			user1.setName("Alice Johnson");
			user1.setEmail("alice@example.com");
			user1.setPassword("password123");
			UserDTO alice = userService.saveUser(user1);

			CreateUserRequest user2 = new CreateUserRequest();
			user2.setName("Bob Smith");
			user2.setEmail("bob@example.com");
			user2.setPassword("password123");
			UserDTO bob = userService.saveUser(user2);

			CreateUserRequest user3 = new CreateUserRequest();
			user3.setName("Charlie Brown");
			user3.setEmail("charlie@example.com");
			user3.setPassword("password123");
			UserDTO charlie = userService.saveUser(user3);

			// Create Posts
			// 1. Text only
			CreatePostRequest post1 = new CreatePostRequest();
			post1.setContent("Hello everyone! Just joined this amazing platform.");
			post1.setUserId(alice.getId());
			postService.createPost(post1);

			// 2. With Image
			CreatePostRequest post2 = new CreatePostRequest();
			post2.setContent("Look at this beautiful sunset I captured today! 🌅");
			post2.setUserId(bob.getId());
			post2.setImageUrl("https://images.unsplash.com/photo-1501179691627-eeaa65ea017c?auto=format&fit=crop&w=1350&q=80");
			postService.createPost(post2);

			// 3. With Video
			CreatePostRequest post3 = new CreatePostRequest();
			post3.setContent("Checkout this cool coding timelapse!");
			post3.setUserId(charlie.getId());
			post3.setVideoUrl("https://www.w3schools.com/html/mov_bbb.mp4");
			postService.createPost(post3);

			// 4. Another Image
			CreatePostRequest post4 = new CreatePostRequest();
			post4.setContent("Weekend vibes in the mountains 🏔️");
			post4.setUserId(alice.getId());
			post4.setImageUrl("https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=1350&q=80");
			postService.createPost(post4);

			// 5. Another Video
			CreatePostRequest post5 = new CreatePostRequest();
			post5.setContent("Rainy days are for cozy vibes 🌧️");
			post5.setUserId(bob.getId());
			post5.setVideoUrl("https://www.w3schools.com/html/horse.mp4");
			postService.createPost(post5);
		};
	}
}