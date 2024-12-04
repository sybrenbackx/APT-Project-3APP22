package fact.it.doctorservice.controller;

import fact.it.doctorservice.dto.DoctorRequest;
import fact.it.doctorservice.dto.DoctorResponse;
import fact.it.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorResponse> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
    @GetMapping("/{doctorNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getDoctorByNumber(@PathVariable String doctorNumber) {
        DoctorResponse doctor = doctorService.getDoctorByNumber(doctorNumber);
        if (doctor != null) {
            return ResponseEntity.status(HttpStatus.OK).body(doctor);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found with number: " + doctorNumber);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addDoctors(@RequestBody List<DoctorRequest> doctorRequests) {
        boolean created = doctorService.createDoctorsFromJson(doctorRequests);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Doctors added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A doctor you tried to add already exists.");
        }


    }
    @PutMapping("/{doctorNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateDoctor(
            @PathVariable String doctorNumber,
            @RequestBody DoctorRequest doctorRequest) {
        boolean updated = doctorService.updateDoctor(doctorNumber, doctorRequest);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body("Doctor updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }

    @DeleteMapping("/{doctorNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteDoctor(@PathVariable String doctorNumber) {
        boolean deleted = doctorService.deleteDoctor(doctorNumber);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Doctor deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }
}
