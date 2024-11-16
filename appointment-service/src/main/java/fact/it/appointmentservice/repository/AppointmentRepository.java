package fact.it.appointmentservice.repository;

import fact.it.appointmentservice.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByAppointmentNumberIn(List<String> appointmentNumber);
}
