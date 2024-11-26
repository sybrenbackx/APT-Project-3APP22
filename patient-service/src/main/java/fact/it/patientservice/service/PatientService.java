package fact.it.patientservice.service;

import fact.it.patientservice.dto.PatientRequest;
import fact.it.patientservice.dto.PatientResponse;
import fact.it.patientservice.model.Patient;
import fact.it.patientservice.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    @Value("${appointmentservice.baseurl}")
    private String appointmentServiceBaseUrl;
    @Value("${doctorservice.baseurl}")
    private String doctorServiceBaseUrl;

    private final PatientRepository patientRepository;

    @PostConstruct
    public void loadData() {
        System.out.println(patientRepository.count());
        if(patientRepository.count() <= 0){
            Patient patient1 = new Patient();
            patient1.setPatientNumber("1");
            patient1.setFullName("mister smith");
            patient1.setEmail("test.test@test.com");
            patient1.setAddress("test1");

            Patient patient2 = new Patient();
            patient2.setPatientNumber("1");
            patient2.setFullName("mister smith");
            patient2.setEmail("test.test@test.com");
            patient2.setAddress("test1");


            patientRepository.save(patient1);
            patientRepository.save(patient2);
        }
    }
    public boolean createPatient(PatientRequest patientRequest){
        Patient patientInSystem = patientRepository.findByPatientNumber(patientRequest.getPatientNumber());
        if (patientInSystem != null){
            return false;
        }
        Patient patient = Patient.builder()
                .patientNumber(patientRequest.getPatientNumber())
                .fullName(patientRequest.getFullName())
                .email(patientRequest.getEmail())
                .address(patientRequest.getAddress())
                .build();
        patientRepository.save(patient);
        return true;
    }

    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(this::mapToPatientResponse).toList();
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
