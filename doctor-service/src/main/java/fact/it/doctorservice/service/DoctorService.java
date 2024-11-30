package fact.it.doctorservice.service;

import fact.it.doctorservice.dto.DoctorRequest;
import fact.it.doctorservice.dto.DoctorResponse;
import fact.it.doctorservice.model.Doctor;
import fact.it.doctorservice.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
            doctor1.setPassword("test1");

            Doctor doctor2 = new Doctor();
            doctor1.setDoctorNumber("2");
            doctor1.setFullName("miss smith");
            doctor1.setEmail("test.test@test.com");
            doctor1.setAddress("test2");
            doctor1.setRole("test2");
            doctor1.setSpecialization("test2");
            doctor1.setPassword("test2");

            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);
        }
    }
    public void createDoctor(DoctorRequest doctorRequest){
        Doctor doctor = Doctor.builder()
                .doctorNumber(doctorRequest.getDoctorNumber())
                .fullName(doctorRequest.getFullName())
                .email(doctorRequest.getEmail())
                .address(doctorRequest.getAddress())
                .role(doctorRequest.getRole())
                .specialization(doctorRequest.getSpecialization())
                .password(doctorRequest.getPassword())
                .build();

        doctorRepository.save(doctor);
    }

    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream().map(this::mapToDoctorResponse).toList();
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .doctorNumber(doctor.getDoctorNumber())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .address(doctor.getAddress())
                .role(doctor.getRole())
                .specialization(doctor.getSpecialization())
                .password(doctor.getPassword())
                .build();
    }
}
