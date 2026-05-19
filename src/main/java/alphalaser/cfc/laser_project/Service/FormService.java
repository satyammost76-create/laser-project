package alphalaser.cfc.laser_project.Service;

import java.util.List;

import alphalaser.cfc.laser_project.Entity.Data;
import alphalaser.cfc.laser_project.Repository.FormRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormService {

    @Autowired
    private FormRepository repo;

    // =========================================
    // FIND BY EMAIL
    // =========================================

    public Data getByEmail(String email) {

        return repo.findByEmail(email)
                .orElse(null);
    }

    // =========================================
    // SAVE DATA
    // =========================================

    public Data saveData(Data data) {

        return repo.save(data);
    }

    // =========================================
    // GET ALL DATA
    // =========================================

    public List<Data> getAllData() {

        return repo.findAll();
    }

    // =========================================
    // GET BY ID
    // =========================================

    public Data getById(Long id) {

        return repo.findById(id)
                .orElse(null);
    }

    // =========================================
    // DELETE DATA
    // =========================================

    public String deleteData(Long id) {

        repo.deleteById(id);

        return "Data Deleted Successfully with ID : " + id;
    }

    // =========================================
    // UPDATE DATA
    // =========================================

    public Data updateData(Long id, Data newData) {

        Data oldData =
                repo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("User Not Found"));

        // =====================================
        // UPDATE FIELDS
        // =====================================

        oldData.setName(
                newData.getName()
        );

        oldData.setAddress(
                newData.getAddress()
        );

        oldData.setMobile(
                newData.getMobile()
        );

        oldData.setEmail(
                newData.getEmail()
        );

        oldData.setMake(
                newData.getMake()
        );

        oldData.setRemarks(
                newData.getRemarks()
        );

        // =====================================
        // KEEP OLD PASSWORD + ROLE
        // =====================================

        oldData.setPassword(
                oldData.getPassword()
        );

        oldData.setRole(
                oldData.getRole()
        );

        // =====================================
        // FORCE SAVE DATABASE
        // =====================================

        Data savedData =
                repo.saveAndFlush(oldData);

        return savedData;
    }
}