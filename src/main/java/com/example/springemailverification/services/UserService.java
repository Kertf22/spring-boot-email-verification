package com.example.springemailverification.services;

import com.example.springemailverification.model.Confirmation;
import com.example.springemailverification.model.User;
import com.example.springemailverification.repository.ConfirmationRepository;
import com.example.springemailverification.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private  final EmailService emailService;

    public UserService(UserRepository userRepository, ConfirmationRepository confirmationRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.confirmationRepository = confirmationRepository;
        this.emailService = emailService;
    }

    public User createUser(String name, String email, String password) {
        var existWithEmail = this.userRepository.findByEmail(email);

        if (existWithEmail != null) {
            throw new RuntimeException("Email already been taken");
        }
        User user = this.userRepository.save(new User(name, email, password));
        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        /* TODO: Send email with confirmation code to user */
//        emailService.sendSimpleMessage(user.getName(),user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithAttchaments(user.getName(),user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithEmbeddedFiles(user.getName(),user.getEmail(), confirmation.getToken());
//        emailService.sendHTMLEmail(user.getName(),user.getEmail(), confirmation.getToken());
        emailService.sendHTMLEmailWithEmbeddedFiles(user.getName(),user.getEmail(), confirmation.getToken());
        return user;
    }

    public void verifyUser(String token) {
        Confirmation confirmation = this.confirmationRepository.findByToken(token);

        if (confirmation == null) {
            throw new RuntimeException("Confirmation not found");
        }

        User user = this.userRepository.findByEmail(confirmation.getUser().getEmail());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setEnabled(true);
        this.userRepository.save(user);
        this.confirmationRepository.delete(confirmation);

    }

    public String resendVerificationCode(String email) {
        var user = this.userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Confirmation existWithUser = this.confirmationRepository.findByUser(user);


        if(existWithUser != null) {
            if(existWithUser.getExpiresAt().isAfter(java.time.LocalDateTime.now())) {
                this.confirmationRepository.delete(existWithUser);
            } else {
                throw new RuntimeException("You can only get a new verification code every 15 minutes");
            }
        }

        Confirmation confirmation = new Confirmation(user);

        confirmationRepository.save(confirmation);

        return confirmation.getToken();
    }

    public User login(String email, String password) {
        var user = this.userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Password is incorrect");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User is not verified");
        }

        return user;
    }

    ;
}
