package com.feedify.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.feedify.commands.PasswordCommand;
import com.feedify.commands.UserProfileUpdateCommand;
import com.feedify.converters.UserProfileUpdateCommandToUser;
import com.feedify.converters.UserToUserProfileUpdateCommand;
import com.feedify.database.entity.User;
import com.feedify.database.repository.UserRepository;
import com.feedify.exceptions.UserNotFoundException;
import com.feedify.rest.service.email.TokenService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private FeedsService feedsService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private static final String USER_NOT_FOUND = "We couldn't find any user with the username provided";
    private static final String NO_USER_WITH_GIVEN_EMAIL = "No user with given email was found.";

    /**
     *
     * @param username
     * @return
     * @throws UserNotFoundException
     */
    public Optional<User> findUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }

    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        return user;
    }

    public User saveUser(User user) {
        user = userRepository.save(user);
        return user;
    }

    /**
     *
     * @param userProfileUpdateCommand
     * @throws UserNotFoundException
     */
    public void updateProfileInfo(UserProfileUpdateCommand userProfileUpdateCommand) throws UserNotFoundException {

        UserProfileUpdateCommandToUser up = new UserProfileUpdateCommandToUser();
        User convertedUser = up.convert(userProfileUpdateCommand);
        Optional<User> optionalUser = userRepository.findByUsername(convertedUser.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(convertedUser.getEmail());
            user.setFirstName(convertedUser.getFirstName());
            user.setLastName(convertedUser.getLastName());
            user.setCountry(convertedUser.getCountry());
            // user.setJob(usconvertedUserer.getJob());
            // user.setAge(convertedUser.getAge());
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(NO_USER_WITH_GIVEN_EMAIL);
        }

    }

    /**
     *
     * @param user
     * @param password
     */
    public void changePassword(PasswordCommand PasswordCommand) {
        Optional<User> optionalUser = userRepository.findByUsername(PasswordCommand.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encodedPassword = passwordEncoder.encode(PasswordCommand.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }

    /**
     *
     * @param user
     * @param interests
     */
    public void saveUserInterests(User user, List<String> interests) {
        interests = interests.stream().map(String::toLowerCase).collect(Collectors.toList());
        user.setUserInterests(interests);
        userRepository.save(user);
    }

    public void delete(User user) {
    	tokenService.deleteConfirmationTokenByUser(user);
    	tokenService.deletePasswordTokenByUser(user);
        feedsService.deleteAllFeeItemUserByUser(user);
        channelService.deleteAllChannelUserByUser(user);
        userRepository.delete(user);
    }

    public UserProfileUpdateCommand convertToUserProfileUpdateCommand(User user) {
        UserToUserProfileUpdateCommand userToUserProfileUpdateCommand = new UserToUserProfileUpdateCommand();
        return userToUserProfileUpdateCommand.convert(user);
    }

    public void changeLanguage() {
        // TODO

    }
}
