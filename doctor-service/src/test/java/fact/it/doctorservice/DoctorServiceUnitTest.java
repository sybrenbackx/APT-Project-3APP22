package fact.it.doctorservice;
import fact.it.doctorservice.dto.DoctorRequest;
import fact.it.doctorservice.dto.DoctorResponse;
import fact.it.doctorservice.model.Doctor;
import fact.it.doctorservice.repository.DoctorRepository;
import fact.it.doctorservice.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceUnitTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @Test
    public void testCreateDoctor() {
        // Arrange
        DoctorRequest doctorRequest = new DoctorRequest();
        doctorRequest.setDoctorNumber("5");
        doctorRequest.setFullName("Mike Mikelson");
        doctorRequest.setEmail("Mike.Mikelson@gmail.com");
        doctorRequest.setRole("surgeon");
        doctorRequest.setAddress("Some address");
        doctorRequest.setSpecialization("Cardiologist");

        // Act
        doctorService.createDoctorFromJson(doctorRequest);

        // Assert
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    public void testGetAllDoctors() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorNumber("5");
        doctor.setFullName("Mike Mikelson");
        doctor.setEmail("Mike.Mikelson@gmail.com");
        doctor.setRole("surgeon");
        doctor.setAddress("Some address");
        doctor.setSpecialization("Cardiologist");

        when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor));

        // Act
        List<DoctorResponse> doctors = doctorService.getAllDoctors();

        // Assert
        assertEquals(1, doctors.size());
        assertEquals("5", doctors.get(0).getDoctorNumber());
        assertEquals("Mike Mikelson", doctors.get(0).getFullName());
        assertEquals("Mike.Mikelson@gmail.com", doctors.get(0).getEmail());
        assertEquals("surgeon", doctors.get(0).getRole());
        assertEquals("Some address", doctors.get(0).getAddress());
        assertEquals("Cardiologist", doctors.get(0).getSpecialization());

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    public void testGetDoctorByNumber() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorNumber("5");
        doctor.setFullName("Mike Mikelson");
        doctor.setEmail("Mike.Mikelson@gmail.com");
        doctor.setRole("surgeon");
        doctor.setAddress("Some address");
        doctor.setSpecialization("Cardiologist");

        when(doctorRepository.findByDoctorNumber("5")).thenReturn(doctor);

        // Act
        DoctorResponse doctorResponse = doctorService.getDoctorByNumber("5");

        // Assert
        assertEquals("5", doctorResponse.getDoctorNumber());
        assertEquals("Mike Mikelson", doctorResponse.getFullName());
        assertEquals("Mike.Mikelson@gmail.com", doctorResponse.getEmail());
        assertEquals("surgeon", doctorResponse.getRole());
        assertEquals("Some address", doctorResponse.getAddress());
        assertEquals("Cardiologist", doctorResponse.getSpecialization());

        verify(doctorRepository, times(1)).findByDoctorNumber(doctorResponse.getDoctorNumber());
    }
    @Test
    public void testUpdateDoctor() {
        // Arrange
        DoctorRequest doctorRequest = new DoctorRequest();
        doctorRequest.setDoctorNumber("5");
        doctorRequest.setFullName("Lars Larson");
        doctorRequest.setEmail("Lars.Larson@gmail.com");
        doctorRequest.setRole("supervisor");
        doctorRequest.setAddress("different address");
        doctorRequest.setSpecialization("Oncologist");

        Doctor existingDoctor = new Doctor();
        existingDoctor.setDoctorNumber("5");
        existingDoctor.setFullName("Mike Mikelson");
        existingDoctor.setEmail("Mike.Mikelson@gmail.com");
        existingDoctor.setRole("surgeon");
        existingDoctor.setAddress("Some address");
        existingDoctor.setSpecialization("Cardiologist");

        when(doctorRepository.findByDoctorNumber("5")).thenReturn(existingDoctor);

        // Act
        boolean updated = doctorService.updateDoctor("5", doctorRequest);

        // Assert
        assertTrue(updated);
        assertEquals("Lars Larson", existingDoctor.getFullName());
        assertEquals("Lars.Larson@gmail.com", existingDoctor.getEmail());
        assertEquals("supervisor", existingDoctor.getRole());
        assertEquals("different address", existingDoctor.getAddress());
        assertEquals("Oncologist", existingDoctor.getSpecialization());

        verify(doctorRepository, times(1)).findByDoctorNumber("5");
        verify(doctorRepository, times(1)).save(existingDoctor);
    }
    @Test
    public void testDeleteDoctor() {
        // Arrange
        Doctor existingDoctor = new Doctor();
        existingDoctor.setDoctorNumber("5");
        when(doctorRepository.findByDoctorNumber("5")).thenReturn(existingDoctor);

        // Act
        boolean deleted = doctorService.deleteDoctor("5");

        // Assert
        assertTrue(deleted);
        verify(doctorRepository, times(1)).findByDoctorNumber("5");
        verify(doctorRepository, times(1)).delete(existingDoctor);
    }
}
