package com.bbk.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.bbk.demo.entity.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ValidationAndIntegrationTest {
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void validation_test_invalid_user_should_be_rejected() {
		User user = new User();
		user.setFirstName("john");
		ResponseEntity<String> response = restTemplate.postForEntity("/api/user", user, String.class);
		assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void valid_user_must_be_added_and_should_be_available_in_get_all_users() {
		User user = new User();
		user.setFirstName("john");
		user.setLastName("cena");
		user.setEmail("jc@gmail.com");
		user.setAddedDate(new Date());

		// before adding here, the response must have 0 outputs
		ResponseEntity<Object[]> usersResponse = restTemplate.getForEntity("/api/user/all", Object[].class);
		Object[] users = usersResponse.getBody();
		Integer initialSize = users.length;

		ResponseEntity<String> response = restTemplate.postForEntity("/api/user", user, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// after created, there should be one created
		ResponseEntity<Object[]> ur = restTemplate.getForEntity("/api/user/all", Object[].class);
		Object[] usrs = ur.getBody();
		assertThat(usrs.length).isGreaterThan(initialSize);
	}

	@Test
	public void delete_should_delete_the_user_from_the_list() {
		User user = new User();
		user.setFirstName("john");
		user.setLastName("cena");
		user.setEmail("jc@gmail.com");
		user.setAddedDate(new Date());

		ResponseEntity<String> response = restTemplate.postForEntity("/api/user", user, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		User[] users = getAllUsers();
		assertThat(users.length).isEqualTo(1);
		// lets get the user id and delete it
		Integer userToBeDeleted = users[0].getId();
		assertThat(userToBeDeleted).isNotNull();

		restTemplate.delete("/api/user/" + userToBeDeleted);

		User[] currentUsers = getAllUsers();
		assertThat(currentUsers.length).isEqualTo(0);
	}

	@Test
	public void all_users_must_have_id_associated_with_it() {
		User user = new User();
		user.setFirstName("john");
		user.setLastName("cena");
		user.setEmail("jc@gmail.com");
		user.setAddedDate(new Date());

		ResponseEntity<String> response = restTemplate.postForEntity("/api/user", user, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		user.setFirstName("Peter");
		user.setUrl("www.test.com");
		ResponseEntity<String> _response = restTemplate.postForEntity("/api/user", user, String.class);
		assertThat(_response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		User[] allUsers = getAllUsers();

		// now all users must have id
		for (int i = 0; i < allUsers.length; i++) {
			assertThat(allUsers[i].getId()).isNotNull().isNotNegative();
		}
	}

	private User[] getAllUsers() {
		ResponseEntity<User[]> usersResponse = restTemplate.getForEntity("/api/user/all", User[].class);
		return usersResponse.getBody();
	}
}