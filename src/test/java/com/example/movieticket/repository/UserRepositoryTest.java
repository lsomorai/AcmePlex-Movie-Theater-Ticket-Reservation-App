package com.example.movieticket.repository;

import com.example.movieticket.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User adminUser;
    private User guestUser;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        entityManager.clear();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setUserType("REGULAR");
        testUser.setEmail("test@example.com");
        entityManager.persist(testUser);

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin123");
        adminUser.setUserType("ADMIN");
        adminUser.setEmail("admin@example.com");
        entityManager.persist(adminUser);

        guestUser = new User();
        guestUser.setUsername("guest");
        guestUser.setPassword("guest123");
        guestUser.setUserType("GUEST");
        guestUser.setEmail(null); // Guest users might not have email
        entityManager.persist(guestUser);

        entityManager.flush();
    }

    @Test
    @DisplayName("Should find user by username and password")
    void findByUsernamePassword_Success() {
        List<User> users = userRepository.findByUsernamePassword("testuser", "password123");

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("testuser");
        assertThat(users.get(0).getUserType()).isEqualTo("REGULAR");
    }

    @Test
    @DisplayName("Should return empty list for wrong password")
    void findByUsernamePassword_WrongPassword() {
        List<User> users = userRepository.findByUsernamePassword("testuser", "wrongpassword");

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list for non-existent username")
    void findByUsernamePassword_NonExistentUser() {
        List<User> users = userRepository.findByUsernamePassword("nonexistent", "password123");

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("Should find user by username")
    void findByUsername_Success() {
        List<User> users = userRepository.findByUsername("testuser");

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("testuser");
        assertThat(users.get(0).getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return empty list for non-existent username")
    void findByUsername_NonExistent() {
        List<User> users = userRepository.findByUsername("nonexistent");

        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("Should find users by user type with email")
    void findByUserType_WithEmail() {
        List<User> regularUsers = userRepository.findByUserType("REGULAR");

        assertThat(regularUsers).hasSize(1);
        assertThat(regularUsers.get(0).getUsername()).isEqualTo("testuser");
        assertThat(regularUsers.get(0).getEmail()).isNotNull();
    }

    @Test
    @DisplayName("Should not return users without email when finding by user type")
    void findByUserType_ExcludesUsersWithoutEmail() {
        List<User> guestUsers = userRepository.findByUserType("GUEST");

        // Guest user has null email, so should not be returned
        assertThat(guestUsers).isEmpty();
    }

    @Test
    @DisplayName("Should find admin users by user type")
    void findByUserType_Admin() {
        List<User> adminUsers = userRepository.findByUserType("ADMIN");

        assertThat(adminUsers).hasSize(1);
        assertThat(adminUsers.get(0).getUsername()).isEqualTo("admin");
    }

    @Test
    @DisplayName("Should save and retrieve user")
    void saveAndRetrieveUser() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpassword");
        newUser.setUserType("REGULAR");
        newUser.setEmail("new@example.com");

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newuser");

        List<User> retrievedUsers = userRepository.findByUsername("newuser");
        assertThat(retrievedUsers).hasSize(1);
    }

    @Test
    @DisplayName("Should update user password")
    void updateUserPassword() {
        List<User> users = userRepository.findByUsername("testuser");
        User user = users.get(0);
        user.setPassword("newpassword");

        userRepository.save(user);

        List<User> updatedUsers = userRepository.findByUsernamePassword("testuser", "newpassword");
        assertThat(updatedUsers).hasSize(1);
    }

    @Test
    @DisplayName("Should delete user")
    void deleteUser() {
        List<User> users = userRepository.findByUsername("testuser");
        assertThat(users).hasSize(1);

        userRepository.delete(users.get(0));

        List<User> deletedUsers = userRepository.findByUsername("testuser");
        assertThat(deletedUsers).isEmpty();
    }

    @Test
    @DisplayName("Should find all users")
    void findAllUsers() {
        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(3);
    }

    @Test
    @DisplayName("Should find user by ID")
    void findById() {
        User user = userRepository.findById((long) testUser.getId()).orElse(null);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should handle case-sensitive username search")
    void findByUsername_CaseSensitive() {
        // Username search should be case-sensitive
        List<User> lowerCase = userRepository.findByUsername("testuser");
        List<User> upperCase = userRepository.findByUsername("TESTUSER");

        assertThat(lowerCase).hasSize(1);
        assertThat(upperCase).isEmpty();
    }
}
