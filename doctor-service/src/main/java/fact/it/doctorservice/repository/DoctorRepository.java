package fact.it.doctorservice.repository;

import fact.it.doctorservice.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByDoctorNumber(String doctorNumber);
}
