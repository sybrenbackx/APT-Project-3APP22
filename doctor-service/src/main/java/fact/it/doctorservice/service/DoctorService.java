package fact.it.doctorservice.service;

import fact.it.doctorservice.dto.DoctorRequest;
import fact.it.doctorservice.dto.DoctorResponse;
import fact.it.doctorservice.model.Doctor;
import fact.it.doctorservice.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    @PostConstruct
    public void loadData() {
        System.out.println(doctorRepository.count());
        if(doctorRepository.count() <= 0){
            Doctor doctor1 = new Doctor();
            doctor1.setDoctorNumber("1");
            doctor1.setFullName("mister smith");
            doctor1.setEmail("test.test@test.com");
            doctor1.setAddress("test1");
            doctor1.setRole("test1");
            doctor1.setSpecialization("test1");

            Doctor doctor2 = new Doctor();
            doctor1.setDoctorNumber("2");
            doctor1.setFullName("miss smith");
            doctor1.setEmail("test.test@test.com");
            doctor1.setAddress("test2");
            doctor1.setRole("test2");
            doctor1.setSpecialization("test2");

            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);
        }
    }
    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream().map(this::mapToDoctorResponse).toList();
    }

    public DoctorResponse getDoctorByNumber(String doctorNumber) {
        try {
            Doctor doctor = doctorRepository.findByDoctorNumber(doctorNumber);
            if (doctor == null) {
                throw new NoSuchElementException("Doctor not found with number: " + doctorNumber);
            }
            return mapToDoctorResponse(doctor);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching doctor: " + e.getMessage());
            return null;
        }
    }

    public boolean createDoctorsFromJson(List<DoctorRequest> doctorRequests) {
        for (DoctorRequest doctorRequest : doctorRequests) {
            Doctor existingDoctor = doctorRepository.findByDoctorNumber(doctorRequest.getDoctorNumber());
            if (existingDoctor == null) {
                Doctor doctor = Doctor.builder()
                        .doctorNumber(doctorRequest.getDoctorNumber())
                        .fullName(doctorRequest.getFullName())
                        .email(doctorRequest.getEmail())
                        .address(doctorRequest.getAddress())
                        .role(doctorRequest.getRole())
                        .specialization(doctorRequest.getSpecialization())
                        .build();
                doctorRepository.save(doctor);
            } else {
                System.out.println("Doctor with number " + doctorRequest.getDoctorNumber() + " already exists.");
                return false;
            }
        }
        return true;
    }

    public boolean updateDoctor(String doctorNumber, DoctorRequest doctorRequest) {
        Doctor existingDoctor = doctorRepository.findByDoctorNumber(doctorNumber);
        if (existingDoctor != null) {
            existingDoctor.setFullName(doctorRequest.getFullName());
            existingDoctor.setEmail(doctorRequest.getEmail());
            existingDoctor.setAddress(doctorRequest.getAddress());
            existingDoctor.setRole(doctorRequest.getRole());
            existingDoctor.setSpecialization(doctorRequest.getSpecialization());
            existingDoctor.setAddress(doctorRequest.getAddress());
            doctorRepository.save(existingDoctor);
            return true;
        }
        return false;
    }

    public boolean deleteDoctor(String doctorNumber) {
        Doctor existingDoctor = doctorRepository.findByDoctorNumber(doctorNumber);
        if (existingDoctor != null) {
            doctorRepository.delete(existingDoctor);
            return true;
        }
        return false;
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .doctorNumber(doctor.getDoctorNumber())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .address(doctor.getAddress())
                .role(doctor.getRole())
                .specialization(doctor.getSpecialization())
                .build();
    }
}
