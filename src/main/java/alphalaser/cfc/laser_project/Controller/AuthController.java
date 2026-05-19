package alphalaser.cfc.laser_project.Controller;

import alphalaser.cfc.laser_project.Components.JwtUtils;
import alphalaser.cfc.laser_project.Entity.Data;
import alphalaser.cfc.laser_project.Repository.FormRepository;
import alphalaser.cfc.laser_project.dto.LoginRequest;
import alphalaser.cfc.laser_project.dto.LoginResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE
        }
)
@RequestMapping("/rajeshroy/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private FormRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // ================= REGISTER =================

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Data data) {

        // EMAIL CHECK
        if (repository.findByEmail(data.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email already exists");
        }

        // PASSWORD ENCODE
        data.setPassword(
                passwordEncoder.encode(data.getPassword())
        );

        // ROLE LOGIC
        if (data.getRole() == null ||
                data.getRole().trim().isEmpty()) {

            data.setRole("USER");

        } else {

            String role = data.getRole().trim().toUpperCase();

            // ONLY ADMIN OR USER ALLOWED
            if (role.equals("ADMIN")) {
                data.setRole("ADMIN");
            } else {
                data.setRole("USER");
            }
        }

        // SAVE USER
        Data savedUser = repository.save(data);

        return new ResponseEntity<>(
                savedUser,
                HttpStatus.CREATED
        );
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest req
    ) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    req.getEmail(),
                                    req.getPassword()
                            )
                    );

            Data user = repository
                    .findByEmail(req.getEmail())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User Not Found"
                            )
                    );

            // GENERATE TOKEN
            String token =
                    jwtUtils.generateToken(
                            user.getEmail()
                    );

            // RESPONSE
            LoginResponse response =
                    new LoginResponse(
                            token,
                            user.getRole(),
                            user.getId()
                    );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Email or Password");

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}