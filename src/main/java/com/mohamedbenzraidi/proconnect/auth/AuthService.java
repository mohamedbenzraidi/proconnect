package com.mohamedbenzraidi.proconnect.auth;

import com.mohamedbenzraidi.proconnect.common.exception.ApiException;
import com.mohamedbenzraidi.proconnect.security.JwtService;
import com.mohamedbenzraidi.proconnect.user.Role;
import com.mohamedbenzraidi.proconnect.user.User;
import com.mohamedbenzraidi.proconnect.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Cet email est déjà utilisé");
        }

        // Détermination du rôle (prend le rôle envoyé dans la requête ou STUDENT par défaut)
        Role role = (req.role() != null) ? req.role() : Role.STUDENT;

        // Instanciation directe avec le constructeur
        User user = new User(
                req.firstName().trim(),
                req.lastName().trim(),
                email,
                role,
                passwordEncoder.encode(req.password())

        );

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmailIgnoreCase(req.email().trim())
                .filter(u -> passwordEncoder.matches(req.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect"));
        return toResponse(user);
    }

    private AuthResponse toResponse(User u) {
        return new AuthResponse(jwtService.generateToken(u), u.getId().toString(),
                u.getEmail(), u.getFirstName(), u.getLastName());
    }
}