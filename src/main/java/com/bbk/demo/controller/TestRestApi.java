package com.bbk.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bbk.demo.entity.CreatedResponse;
import com.bbk.demo.entity.Response;
import com.bbk.demo.entity.User;
import com.bbk.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class TestRestApi {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ResponseEntity<String> welcome() {
		return new ResponseEntity<String>("welcome to springboot", HttpStatus.OK);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
		return new ResponseEntity<CreatedResponse>(new CreatedResponse("success", userService.addUser(user)),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
		if (!userService.userExists(id)) {
			return new ResponseEntity<Response>(new Response("not found"), HttpStatus.NOT_FOUND);
		}
		userService.deleteUser(id);
		return new ResponseEntity<Response>(new Response("success"), HttpStatus.OK);
	}

	@RequestMapping(value = "/user/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsers() {
		return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
	}
}
