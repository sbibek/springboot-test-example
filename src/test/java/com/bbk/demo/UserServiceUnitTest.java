package com.bbk.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bbk.demo.entity.User;
import com.bbk.demo.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceUnitTest {
	@Autowired
	private UserService userService;

	@Test
	public void userservie_must_not_be_null() {
		assertThat(userService).isNotNull();
	}

	@Test
	public void add_fn_should_add_a_user() {
		User user = new User();
		user.setFirstName("a");
		user.setLastName("b");
		user.setEmail("a@b.com");
		user.setAddedDate(new Date());
		user.setUrl("www.g.com");

		Integer id = userService.addUser(user);
		assertThat(id).isNotNegative();

		assertThat(userService.userExists(id)).isTrue();
	}

	@Test
	public void delete_fn_should_delete_the_user() {
		User user = new User();
		user.setFirstName("a");
		user.setLastName("b");
		user.setEmail("a@b.com");
		user.setAddedDate(new Date());
		user.setUrl("www.g.com");
		Integer id = userService.addUser(user);
		userService.deleteUser(id);
		assertThat(userService.userExists(id)).isFalse();
	}

	@Test
	public void all_users_must_have_id() {
		userService.getAllUsers().forEach(user -> {
			assertThat(user.getId()).isNotNull().isNotNegative();
		});
	}
}
