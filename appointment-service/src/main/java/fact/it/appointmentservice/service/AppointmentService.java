package fact.it.appointmentservice.service;

import fact.it.appointmentservice.dto.AppointmentRequest;
import fact.it.appointmentservice.dto.AppointmentResponse;
import fact.it.appointmentservice.model.Appointment;
import fact.it.appointmentservice.repository.AppointmentRepository;
import fact.it.doctorservice.dto.DoctorResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    @Value("${patientservice.baseurl}")
    private String appointmentServiceBaseUrl;
    @Value("${doctorservice.baseurl}")
    private String doctorServiceBaseUrl;

    private final WebClient webClient;
    private final AppointmentRepository appointmentRepository;

    @PostConstruct
    public void loadData() {
        System.out.println(appointmentRepository.count());
        if(appointmentRepository.count() <= 0){
            Appointment appointment1 = new Appointment();
            appointment1.setAppointmentNumber("1");
            appointment1.setDate(LocalDate.of(2020,11,13));
            appointment1.setTime(LocalTime.of(13,30));
            appointment1.setStatus("test1");

            Appointment appointment2 = new Appointment();
            appointment1.setAppointmentNumber("1");
            appointment1.setDate(LocalDate.of(2021,12,20));
            appointment1.setTime(LocalTime.of(9,30));
            appointment1.setStatus("test2");

            appointmentRepository.save(appointment1);
            appointmentRepository.save(appointment2);
        }
    }
    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream().map(this::mapToAppointmentResponse).toList();
    }

    public AppointmentResponse getAppointmentByNumber(String appointmentNumber) {
        try {
            Appointment appointment = appointmentRepository.findByAppointmentNumber(appointmentNumber);
            if (appointment == null) {
                throw new NoSuchElementException("Appointment not found with number: " + appointmentNumber);
            }
            return mapToAppointmentResponse(appointment);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching appointment: " + e.getMessage());
            return null;
        }
    }
    public List<AppointmentResponse> getAppointmentsByDoctor(String doctorNumber) {
        DoctorResponse doctorResponse = webClient.get()
                .uri("http://" + doctorServiceBaseUrl + "/api/doctor",
                        uriBuilder -> uriBuilder.queryParam("doctorNumber", doctorNumber).build())
                .retrieve()
                .bodyToMono(DoctorResponse.class)
                .block();
        if (doctorResponse == null) {
            throw new NoSuchElementException("Doctor not found with number: " + doctorNumber);
        }
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(appointment -> appointment.getDoctorNumber().equals(doctorNumber))
                .map(this::mapToAppointmentResponse)
                .toList();
    }


    public boolean createAppointmentFromJson(AppointmentRequest appointmentRequest) {
            Appointment existingAppointment = appointmentRepository.findByAppointmentNumber(appointmentRequest.getAppointmentNumber());
            if (existingAppointment == null) {
                Appointment appointment = Appointment.builder()
                        .appointmentNumber(appointmentRequest.getAppointmentNumber())
                        .status(appointmentRequest.getStatus())
                        .time(appointmentRequest.getTime())
                        .date(appointmentRequest.getDate())
                        .build();
                appointmentRepository.save(appointment);
            } else {
                System.out.println("Appointment with number " + appointmentRequest.getAppointmentNumber() + " already exists.");
                return false;
            }
        return true;
    }

    public boolean updateAppointment(String appointmentNumber, AppointmentRequest appointmentRequest) {
        Appointment existingAppointment = appointmentRepository.findByAppointmentNumber(appointmentNumber);
        if (existingAppointment != null) {
            existingAppointment.setStatus(appointmentRequest.getStatus());
            existingAppointment.setTime(appointmentRequest.getTime());
            existingAppointment.setDate(appointmentRequest.getDate());
            appointmentRepository.save(existingAppointment);
            return true;
        }
        return false;
    }

    public boolean deleteAppointment(String appointmentNumber) {
        Appointment existingAppointment = appointmentRepository.findByAppointmentNumber(appointmentNumber);
        if (existingAppointment != null) {
            appointmentRepository.delete(existingAppointment);
            return true;
        }
        return false;
    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .appointmentNumber(appointment.getAppointmentNumber())
                .status(appointment.getStatus())
                .time(appointment.getTime())
                .date(appointment.getDate())
                .build();
    }
}
