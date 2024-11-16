package fact.it.appointmentservice.service;

import fact.it.appointmentservice.dto.AppointmentRequest;
import fact.it.appointmentservice.dto.AppointmentResponse;
import fact.it.appointmentservice.model.Appointment;
import fact.it.appointmentservice.repository.AppointmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

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
    public void createAppointment(AppointmentRequest appointmentRequest){
        Appointment appointment = Appointment.builder()
                .appointmentNumber(appointmentRequest.getAppointmentNumber())
                .status(appointmentRequest.getStatus())
                .time(appointmentRequest.getTime())
                .date(appointmentRequest.getDate())
                .build();

        appointmentRepository.save(appointment);
    }

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream().map(this::mapToAppointmentResponse).toList();
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
