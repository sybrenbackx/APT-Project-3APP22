package fact.it.patientservice;
import fact.it.patientservice.dto.PatientRequest;
import fact.it.patientservice.dto.PatientResponse;
import fact.it.patientservice.model.Patient;
import fact.it.patientservice.repository.PatientRepository;
import fact.it.patientservice.service.PatientService;
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
public class PatientServiceUnitTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Test
    public void testCreatePatient() {
        // Arrange
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setPatientNumber("5");
        patientRequest.setFullName("Mike Mikelson");
        patientRequest.setEmail("Mike.Mikelson@gmail.com");
        patientRequest.setAddress("Some address");

        // Act
        patientService.createPatientFromJson(patientRequest);

        // Assert
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testGetAllPatients() {
        // Arrange
        Patient patient = new Patient();
        patient.setPatientNumber("5");
        patient.setFullName("Mike Mikelson");
        patient.setEmail("Mike.Mikelson@gmail.com");
        patient.setAddress("Some address");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));

        // Act
        List<PatientResponse> patients = patientService.getAllPatients();

        // Assert
        assertEquals(1, patients.size());
        assertEquals("5", patients.get(0).getPatientNumber());
        assertEquals("Mike Mikelson", patients.get(0).getFullName());
        assertEquals("Mike.Mikelson@gmail.com", patients.get(0).getEmail());
        assertEquals("Some address", patients.get(0).getAddress());

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    public void testGetPatientByNumber() {
        // Arrange
        Patient patient = new Patient();
        patient.setPatientNumber("5");
        patient.setFullName("Mike Mikelson");
        patient.setEmail("Mike.Mikelson@gmail.com");
        patient.setAddress("Some address");

        when(patientRepository.findByPatientNumber("5")).thenReturn(patient);

        // Act
        PatientResponse patientResponse = patientService.getPatientByNumber("5");

        // Assert
        assertEquals("5", patientResponse.getPatientNumber());
        assertEquals("Mike Mikelson", patientResponse.getFullName());
        assertEquals("Mike.Mikelson@gmail.com", patientResponse.getEmail());
        assertEquals("Some address", patientResponse.getAddress());

        verify(patientRepository, times(1)).findByPatientNumber(patientResponse.getPatientNumber());
    }
    @Test
    public void testUpdatePatient() {
        // Arrange
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setPatientNumber("5");
        patientRequest.setFullName("Lars Larson");
        patientRequest.setEmail("Lars.Larson@gmail.com");
        patientRequest.setAddress("different address");

        Patient existingPatient = new Patient();
        existingPatient.setPatientNumber("5");
        existingPatient.setFullName("Mike Mikelson");
        existingPatient.setEmail("Mike.Mikelson@gmail.com");
        existingPatient.setAddress("Some address");

        when(patientRepository.findByPatientNumber("5")).thenReturn(existingPatient);

        // Act
        boolean updated = patientService.updatePatient("5", patientRequest);

        // Assert
        assertTrue(updated);
        assertEquals("Lars Larson", existingPatient.getFullName());
        assertEquals("Lars.Larson@gmail.com", existingPatient.getEmail());
        assertEquals("different address", existingPatient.getAddress());

        verify(patientRepository, times(1)).findByPatientNumber("5");
        verify(patientRepository, times(1)).save(existingPatient);
    }
    @Test
    public void testDeletePatient() {
        // Arrange
        Patient existingPatient = new Patient();
        existingPatient.setPatientNumber("5");
        when(patientRepository.findByPatientNumber("5")).thenReturn(existingPatient);

        // Act
        boolean deleted = patientService.deletePatient("5");

        // Assert
        assertTrue(deleted);
        verify(patientRepository, times(1)).findByPatientNumber("5");
        verify(patientRepository, times(1)).delete(existingPatient);
    }
}

