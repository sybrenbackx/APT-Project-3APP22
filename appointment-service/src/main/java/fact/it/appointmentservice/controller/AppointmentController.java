package fact.it.appointmentservice.controller;

import fact.it.appointmentservice.dto.AppointmentResponse;
import fact.it.appointmentservice.dto.AppointmentRequest;
import fact.it.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
    @GetMapping("/{appointmentNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAppointmentByNumber(@PathVariable String appointmentNumber) {
        AppointmentResponse appointment = appointmentService.getAppointmentByNumber(appointmentNumber);
        if (appointment != null) {
            return ResponseEntity.status(HttpStatus.OK).body(appointment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found with number: " + appointmentNumber);
        }
    }
    @GetMapping("/{doctorNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAppointmentsByDoctor(@PathVariable String doctorNumber) {
        List<AppointmentResponse> appointment = appointmentService.getAppointmentsByDoctor(doctorNumber);
        if (appointment != null) {
            return ResponseEntity.status(HttpStatus.OK).body(appointment);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No appointments found for this doctor");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addAppointments(@RequestBody AppointmentRequest appointmentRequest) {
        boolean created = appointmentService.createAppointmentFromJson(appointmentRequest);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Appointments added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A appointment you tried to add already exists.");
        }
    }
    @PutMapping("/{appointmentNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateAppointment(
            @PathVariable String appointmentNumber,
            @RequestBody AppointmentRequest appointmentRequest) {
        boolean updated = appointmentService.updateAppointment(appointmentNumber, appointmentRequest);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body("Appointment updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
    }

    @DeleteMapping("/{appointmentNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteAppointment(@PathVariable String appointmentNumber) {
        boolean deleted = appointmentService.deleteAppointment(appointmentNumber);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Appointment deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        }
    }
}
