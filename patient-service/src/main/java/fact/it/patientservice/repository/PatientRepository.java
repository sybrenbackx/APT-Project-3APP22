package fact.it.patientservice.repository;

import fact.it.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByPatientNumberIn(List<String> patientNumber);
    Patient findByPatientNumber(String patientNumber);

}
