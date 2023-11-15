package dev.dreiling.ForumAPI.service;

import dev.dreiling.ForumAPI.dto.RegisterRequest;
import dev.dreiling.ForumAPI.exceptions.ForumException;
import dev.dreiling.ForumAPI.model.NotificationEmail;
import dev.dreiling.ForumAPI.model.User;
import dev.dreiling.ForumAPI.model.VerificationToken;
import dev.dreiling.ForumAPI.repository.UserRepository;
import dev.dreiling.ForumAPI.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword( passwordEncoder.encode(registerRequest.getPassword()) );
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);

        try {
            mailService.sendMail(new NotificationEmail("Please activate your Account", user.getEmail(),
                    "Thank you for signing up!<br><br>Please click on the link below ti activate your account: " +
                    "http://localhost:8080/api/auth/accountVerification/" + token));
        } catch (Exception e) {
            throw new ForumException(e.getMessage());
        }
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new ForumException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ForumException("User not found with name: " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

}
