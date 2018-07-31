package com.bbk.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bbk.demo.entity.User;

@Service
public class UserService {
	private List<User> users = new ArrayList<>();

	public Integer addUser(User user) {
		user.setId(users.size());
		users.add(user);
		return users.size() - 1;
	}

	public Boolean userExists(Integer id) {
		return users.stream().filter(u -> u.getId().equals(id)).collect(Collectors.toList()).size() > 0;
	}

	public void deleteUser(Integer id) {
		// now to delete, we will just find the user
		User user = users.stream().filter(u -> u.getId().equals(id)).findFirst().get();
		users.remove(user);
	}

	public List<User> getAllUsers() {
		return users;
	}
}
