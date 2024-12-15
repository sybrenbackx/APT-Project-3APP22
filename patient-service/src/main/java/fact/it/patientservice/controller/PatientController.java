package fact.it.patientservice.controller;

import lombok.RequiredArgsConstructor;
import fact.it.patientservice.dto.PatientRequest;
import fact.it.patientservice.dto.PatientResponse;
import fact.it.patientservice.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PatientResponse> getAllPatients() {
        return patientService.getAllPatients();
    }
    @GetMapping("/{patientNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getPatientByNumber(@PathVariable String patientNumber) {
        PatientResponse patient = patientService.getPatientByNumber(patientNumber);
        if (patient != null) {
            return ResponseEntity.status(HttpStatus.OK).body(patient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found with number: " + patientNumber);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addPatients(@RequestBody PatientRequest patientRequest) {
        boolean created = patientService.createPatientFromJson(patientRequest);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Patients added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A patient you tried to add already exists.");
        }


    }
    @PutMapping("/{patientNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updatePatient(
            @PathVariable String patientNumber,
            @RequestBody PatientRequest patientRequest) {
        boolean updated = patientService.updatePatient(patientNumber, patientRequest);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body("Patient updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }

    @DeleteMapping("/{patientNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deletePatient(@PathVariable String patientNumber) {
        boolean deleted = patientService.deletePatient(patientNumber);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Patient deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }
}
