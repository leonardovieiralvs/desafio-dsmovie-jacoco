package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private CustomUserUtil userUtil;

	private String existingUsername, nonExistingUsername;
	private UserEntity user;
	private List<UserDetailsProjection> userDetails;

	@BeforeEach
	void setUp() {
		existingUsername = "maria@gmail.com";
		nonExistingUsername = "bob@gmail.com";

		user = UserFactory.createUserEntity();
		userDetails = UserDetailsFactory.createCustomAdminClientUser(existingUsername);

		Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingUsername);
		Mockito.when(repository.findByUsername(existingUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

		Mockito.when(repository.searchUserAndRolesByUsername(existingUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByUsername(nonExistingUsername)).thenReturn(new ArrayList<>());
	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		UserEntity result = service.authenticated();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingUsername, result.getUsername());
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(nonExistingUsername);

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result = service.loadUserByUsername(existingUsername);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingUsername, result.getUsername());
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingUsername);
		});
	}
}
