package fact.it.patientservice.service;

import fact.it.patientservice.dto.PatientRequest;
import fact.it.patientservice.dto.PatientResponse;
import fact.it.patientservice.model.Patient;
import fact.it.patientservice.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    @PostConstruct
    public void loadData() {
        System.out.println(patientRepository.count());
        if (patientRepository.count() <= 0) {
            Patient patient1 = new Patient();
            patient1.setPatientNumber("1");
            patient1.setFullName("mister smith");
            patient1.setEmail("test.test@test.com");
            patient1.setAddress("test1");

            Patient patient2 = new Patient();
            patient2.setPatientNumber("2");
            patient2.setFullName("mister smith");
            patient2.setEmail("test.test@test.com");
            patient2.setAddress("test1");


            patientRepository.save(patient1);
            patientRepository.save(patient2);
        }
    }

    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(this::mapToPatientResponse).toList();
    }

    public PatientResponse getPatientByNumber(String patientNumber) {
        return mapToPatientResponse(patientRepository.findByPatientNumber(patientNumber));
    }

    public boolean createPatientsFromJson(List<PatientRequest> patientRequests) {
        for (PatientRequest patientRequest : patientRequests) {
            Patient existingPatient = patientRepository.findByPatientNumber(patientRequest.getPatientNumber());
            if (existingPatient == null) {
                Patient patient = Patient.builder()
                        .patientNumber(patientRequest.getPatientNumber())
                        .fullName(patientRequest.getFullName())
                        .email(patientRequest.getEmail())
                        .address(patientRequest.getAddress())
                        .build();
                patientRepository.save(patient);
            } else {
                System.out.println("Patient with number " + patientRequest.getPatientNumber() + " already exists.");
                return false;
            }
        }
        return true;
    }

    public boolean updatePatient(String patientNumber, PatientRequest patientRequest) {
        Patient existingPatient = patientRepository.findByPatientNumber(patientNumber);
        if (existingPatient != null) {
            existingPatient.setFullName(patientRequest.getFullName());
            existingPatient.setEmail(patientRequest.getEmail());
            existingPatient.setAddress(patientRequest.getAddress());
            patientRepository.save(existingPatient);
            return true;
        }
        return false;
    }

    public boolean deletePatient(String patientNumber) {
        Patient existingPatient = patientRepository.findByPatientNumber(patientNumber);
        if (existingPatient != null) {
            patientRepository.delete(existingPatient);
            return true;
        }
        return false;
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .patientNumber(patient.getPatientNumber())
                .fullName(patient.getFullName())
                .address(patient.getAddress())
                .email(patient.getEmail())
                .build();
    }
}
