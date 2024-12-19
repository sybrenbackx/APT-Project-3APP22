package fact.it.appointmentservice.service;

import fact.it.appointmentservice.dto.AppointmentRequest;
import fact.it.appointmentservice.dto.AppointmentResponse;
import fact.it.appointmentservice.dto.PatientResponse;
import fact.it.appointmentservice.model.Appointment;
import fact.it.appointmentservice.repository.AppointmentRepository;
import fact.it.appointmentservice.dto.DoctorResponse;
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
    private String patientServiceBaseUrl;
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
            appointment1.setDoctorNumber("1");
            appointment1.setPatientNumber("1");

            Appointment appointment2 = new Appointment();
            appointment2.setAppointmentNumber("1");
            appointment2.setDate(LocalDate.of(2021,12,20));
            appointment2.setTime(LocalTime.of(9,30));
            appointment2.setStatus("test2");
            appointment2.setDoctorNumber("2");
            appointment2.setPatientNumber("2");

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
        try{DoctorResponse doctorResponse = webClient.get()
                .uri("http://" + doctorServiceBaseUrl + "/api/doctor/{id}", doctorNumber)
                .retrieve()
                .bodyToMono(DoctorResponse.class)
                .block();}
        catch (Exception e) {
            throw new NoSuchElementException("No doctor found with: " + doctorNumber);
        }
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(appointment -> appointment.getDoctorNumber().equals(doctorNumber))
                .map(this::mapToAppointmentResponse)
                .toList();
    }
    public List<AppointmentResponse> getAppointmentsByPatient(String patientNumber) {
        try{PatientResponse patientResponse = webClient.get()
                .uri("http://" + patientServiceBaseUrl + "/api/patient/{id}", patientNumber)
                .retrieve()
                .bodyToMono(PatientResponse.class)
                .block();}
        catch (Exception e) {
            System.out.println("no patient with number: " + patientNumber);
        }
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(appointment -> appointment.getPatientNumber().equals(patientNumber))
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
                        .doctorNumber(appointmentRequest.getDoctorNumber())
                        .patientNumber(appointmentRequest.getPatientNumber())
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
            existingAppointment.setDoctorNumber(appointmentRequest.getDoctorNumber());
            existingAppointment.setPatientNumber(appointmentRequest.getPatientNumber());
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
                .doctorNumber(appointment.getDoctorNumber())
                .patientNumber(appointment.getPatientNumber())
                .build();
    }
}
