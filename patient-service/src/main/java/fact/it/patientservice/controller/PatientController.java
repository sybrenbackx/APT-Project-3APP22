package fact.it.patientservice.controller;

import lombok.RequiredArgsConstructor;
import fact.it.patientservice.dto.PatientRequest;
import fact.it.patientservice.dto.PatientResponse;
import fact.it.patientservice.model.Patient;
import fact.it.patientservice.service.PatientService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String placeOrder(@RequestBody PatientRequest patientRequest) {
        boolean result = patientService.createPatient(patientRequest);
        return (result ? "Patient added successfully" : "Failed to add patient");
    }
}
