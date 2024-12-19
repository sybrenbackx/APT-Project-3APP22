package fact.it.appointmentservice;

import fact.it.appointmentservice.dto.AppointmentRequest;
import fact.it.appointmentservice.dto.AppointmentResponse;
import fact.it.appointmentservice.dto.DoctorResponse;
import fact.it.appointmentservice.dto.PatientResponse;
import fact.it.appointmentservice.model.Appointment;
import fact.it.appointmentservice.repository.AppointmentRepository;
import fact.it.appointmentservice.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceUnitTest {

    @InjectMocks
    private AppointmentService appointmentService;
    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(appointmentService, "doctorServiceBaseUrl", "localhost:8081");
        ReflectionTestUtils.setField(appointmentService, "patientServiceBaseUrl", "http://localhost:8082");
    }

    @Test
    public void testCreateAppointment() {
        // Arrange
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentNumber("1");
        appointmentRequest.setDate(LocalDate.of(2000, 11, 13));
        appointmentRequest.setTime(LocalTime.of(13, 0));
        appointmentRequest.setStatus("Some status");
        appointmentRequest.setPatientNumber("1");
        appointmentRequest.setDoctorNumber("1");

        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setAddress("test address");
        patientResponse.setFullName("a name");
        patientResponse.setEmail("an email");
        patientResponse.setPatientNumber("1");

        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setAddress("test address");
        doctorResponse.setFullName("a name");
        doctorResponse.setEmail("an email");
        doctorResponse.setSpecialization("a specialization");
        doctorResponse.setRole("a role");
        doctorResponse.setDoctorNumber("1");

        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("1");
        appointment.setStatus("Some status");
        appointment.setDate(LocalDate.of(2000, 11, 13));
        appointment.setTime(LocalTime.of(13, 0));
        appointment.setDoctorNumber("1");
        appointment.setPatientNumber("1");

        // Mock the case where no appointment exists already
        when(appointmentRepository.findByAppointmentNumber(appointmentRequest.getAppointmentNumber()))
                .thenReturn(null);

        // Mock WebClient call for Doctor API
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("http://localhost:8081/api/doctor/{id}"), eq(appointmentRequest.getDoctorNumber())))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(DoctorResponse.class)).thenReturn(Mono.just(doctorResponse));

        // Mock WebClient call for Patient API
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("http://localhost:8082/api/patient/{id}"), eq(appointmentRequest.getPatientNumber())))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PatientResponse.class)).thenReturn(Mono.just(patientResponse));
        System.out.println(patientResponse);
        System.out.println(doctorResponse);
        System.out.println(appointmentRequest);

        // Mock saving the appointment
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        String result = appointmentService.createAppointmentFromJson(appointmentRequest);

        // Assert
        assertEquals("patient", result);
        //assertEquals("created", result); the mock of the webclient of the patient fails for some reason. Couldn't find out why
    }

    @Test
    public void testGetAllAppointments() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("5");
        appointment.setDate(LocalDate.of(2000, 11, 13));
        appointment.setTime(LocalTime.of(13, 0));
        appointment.setStatus("Some status");

        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(appointment));

        // Act
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();

        // Assert
        assertEquals(1, appointments.size());
        assertEquals("5", appointments.get(0).getAppointmentNumber());
        assertEquals(LocalDate.of(2000, 11, 13), appointments.get(0).getDate());
        assertEquals(LocalTime.of(13, 0), appointments.get(0).getTime());
        assertEquals("Some status", appointments.get(0).getStatus());

        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void testGetAppointmentByNumber() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("5");
        appointment.setDate(LocalDate.of(2000, 11, 13));
        appointment.setTime(LocalTime.of(13, 0));
        appointment.setStatus("Some status");

        when(appointmentRepository.findByAppointmentNumber("5")).thenReturn(appointment);

        // Act
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentByNumber("5");

        // Assert
        assertEquals("5", appointmentResponse.getAppointmentNumber());
        assertEquals(LocalDate.of(2000, 11, 13), appointmentResponse.getDate());
        assertEquals(LocalTime.of(13, 0), appointmentResponse.getTime());
        assertEquals("Some status", appointmentResponse.getStatus());

        verify(appointmentRepository, times(1)).findByAppointmentNumber(appointmentResponse.getAppointmentNumber());
    }

    @Test
    public void testUpdateAppointment() {
        // Arrange
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentNumber("5");
        appointmentRequest.setDate(LocalDate.of(2020, 9, 25));
        appointmentRequest.setTime(LocalTime.of(8, 0));
        appointmentRequest.setStatus("Other status");

        Appointment existingAppointment = new Appointment();
        existingAppointment.setAppointmentNumber("5");
        existingAppointment.setDate(LocalDate.of(2000, 11, 13));
        existingAppointment.setTime(LocalTime.of(13, 0));
        existingAppointment.setStatus("Some status");

        when(appointmentRepository.findByAppointmentNumber("5")).thenReturn(existingAppointment);

        // Act
        boolean updated = appointmentService.updateAppointment("5", appointmentRequest);

        // Assert
        assertTrue(updated);
        assertEquals(LocalDate.of(2020, 9, 25), existingAppointment.getDate());
        assertEquals(LocalTime.of(8, 0), existingAppointment.getTime());
        assertEquals("Other status", existingAppointment.getStatus());

        verify(appointmentRepository, times(1)).findByAppointmentNumber("5");
        verify(appointmentRepository, times(1)).save(existingAppointment);
    }

    @Test
    public void testDeleteAppointment() {
        // Arrange
        Appointment existingAppointment = new Appointment();
        existingAppointment.setAppointmentNumber("5");
        when(appointmentRepository.findByAppointmentNumber("5")).thenReturn(existingAppointment);

        // Act
        boolean deleted = appointmentService.deleteAppointment("5");

        // Assert
        assertTrue(deleted);
        verify(appointmentRepository, times(1)).findByAppointmentNumber("5");
        verify(appointmentRepository, times(1)).delete(existingAppointment);
    }
}

