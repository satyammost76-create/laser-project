package alphalaser.cfc.laser_project.Controller;

import java.security.Principal;
import java.util.List;

import alphalaser.cfc.laser_project.Entity.Data;
import alphalaser.cfc.laser_project.Service.FormService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/rajeshroy")
public class FormController {

    @Autowired
    private FormService service;

    // =====================================================
    // USER : GET OWN PROFILE
    // =====================================================

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(
            Authentication authentication
    ) {

        try {

            if (authentication == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized Access");
            }

            String email =
                    authentication.getName();

            Data user =
                    service.getByEmail(email);

            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User Not Found");
            }

            return ResponseEntity.ok(user);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // SAVE DATA
    // =====================================================

    @PostMapping("/save")
    public ResponseEntity<?> saveData(
            @RequestBody Data data,
            Principal principal
    ) {

        try {

            if (principal != null) {

                System.out.println(
                        "Data saved by : "
                                + principal.getName()
                );
            }

            Data savedData =
                    service.saveData(data);

            return ResponseEntity.ok(savedData);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // ADMIN : GET ALL USERS
    // =====================================================

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllData() {

        try {

            List<Data> list =
                    service.getAllData();

            return ResponseEntity.ok(list);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // GET USER BY ID
    // ADMIN -> CAN SEE EVERYONE
    // USER -> CAN SEE ONLY OWN DATA
    // =====================================================

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(
            @PathVariable long id,
            Authentication authentication
    ) {

        try {

            if (authentication == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized");
            }

            String loggedInEmail =
                    authentication.getName();

            Data data =
                    service.getById(id);

            if (data == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User Not Found");
            }

            // ======================================
            // ADMIN ACCESS
            // ======================================

            boolean isAdmin =
                    authentication
                            .getAuthorities()
                            .stream()
                            .anyMatch(authority ->
                                    authority.getAuthority()
                                            .equals("ROLE_ADMIN"));

            if (isAdmin) {

                return ResponseEntity.ok(data);
            }

            // ======================================
            // USER OWN DATA ACCESS
            // ======================================

            if (data.getEmail()
                    .equals(loggedInEmail)) {

                return ResponseEntity.ok(data);
            }

            // ======================================
            // ACCESS DENIED
            // ======================================

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access Denied");

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // DELETE USER
    // ADMIN ONLY
    // =====================================================

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteData(
            @PathVariable long id
    ) {

        try {

            String result =
                    service.deleteData(id);

            return ResponseEntity.ok(result);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    // =====================================================
    // UPDATE USER
    // ADMIN -> CAN UPDATE ANYONE
    // USER -> CAN UPDATE OWN DATA
    // =====================================================

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateData(
            @PathVariable long id,
            @RequestBody Data data,
            Authentication authentication
    ) {

        try {

            if (authentication == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized");
            }

            String loggedInEmail =
                    authentication.getName();

            Data existingData =
                    service.getById(id);

            if (existingData == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User Not Found");
            }

            // ======================================
            // ADMIN ACCESS
            // ======================================

            boolean isAdmin =
                    authentication
                            .getAuthorities()
                            .stream()
                            .anyMatch(authority ->
                                    authority.getAuthority()
                                            .equals("ROLE_ADMIN"));

            if (isAdmin) {

                Data updated =
                        service.updateData(id, data);

                return ResponseEntity.ok(updated);
            }

            // ======================================
            // USER OWN UPDATE
            // ======================================

            if (existingData.getEmail()
                    .equals(loggedInEmail)) {

                Data updated =
                        service.updateData(id, data);

                return ResponseEntity.ok(updated);
            }

            // ======================================
            // ACCESS DENIED
            // ======================================

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Access Denied");

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}