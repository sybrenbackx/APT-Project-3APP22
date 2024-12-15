package fact.it.appointmentservice;
import fact.it.appointmentservice.dto.AppointmentRequest;
import fact.it.appointmentservice.dto.AppointmentResponse;
import fact.it.appointmentservice.model.Appointment;
import fact.it.appointmentservice.repository.AppointmentRepository;
import fact.it.appointmentservice.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceUnitTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    public void testCreateAppointment() {
        // Arrange
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentNumber("5");
        appointmentRequest.setDate(LocalDate.of(2000,11,13));
        appointmentRequest.setTime(LocalTime.of(13, 0));
        appointmentRequest.setStatus("Some status");

        // Act
        appointmentService.createAppointmentFromJson(appointmentRequest);

        // Assert
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testGetAllAppointments() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("5");
        appointment.setDate(LocalDate.of(2000,11,13));
        appointment.setTime(LocalTime.of(13, 0));
        appointment.setStatus("Some status");

        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(appointment));

        // Act
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments();

        // Assert
        assertEquals(1, appointments.size());
        assertEquals("5", appointments.get(0).getAppointmentNumber());
        assertEquals(LocalDate.of(2000,11,13), appointments.get(0).getDate());
        assertEquals(LocalTime.of(13, 0), appointments.get(0).getTime());
        assertEquals("Some status", appointments.get(0).getStatus());

        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    public void testGetAppointmentByNumber() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber("5");
        appointment.setDate(LocalDate.of(2000,11,13));
        appointment.setTime(LocalTime.of(13, 0));
        appointment.setStatus("Some status");

        when(appointmentRepository.findByAppointmentNumber("5")).thenReturn(appointment);

        // Act
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentByNumber("5");

        // Assert
        assertEquals("5", appointmentResponse.getAppointmentNumber());
        assertEquals(LocalDate.of(2000,11,13), appointmentResponse.getDate());
        assertEquals(LocalTime.of(13, 0), appointmentResponse.getTime());
        assertEquals("Some status", appointmentResponse.getStatus());

        verify(appointmentRepository, times(1)).findByAppointmentNumber(appointmentResponse.getAppointmentNumber());
    }
    @Test
    public void testUpdateAppointment() {
        // Arrange
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentNumber("5");
        appointmentRequest.setDate(LocalDate.of(2020,9,25));
        appointmentRequest.setTime(LocalTime.of(8, 0));
        appointmentRequest.setStatus("Other status");

        Appointment existingAppointment = new Appointment();
        existingAppointment.setAppointmentNumber("5");
        existingAppointment.setDate(LocalDate.of(2000,11,13));
        existingAppointment.setTime(LocalTime.of(13, 0));
        existingAppointment.setStatus("Some status");

        when(appointmentRepository.findByAppointmentNumber("5")).thenReturn(existingAppointment);

        // Act
        boolean updated = appointmentService.updateAppointment("5", appointmentRequest);

        // Assert
        assertTrue(updated);
        assertEquals(LocalDate.of(2020,9,25), existingAppointment.getDate());
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

