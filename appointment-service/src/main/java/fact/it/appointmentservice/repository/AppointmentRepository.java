package fact.it.appointmentservice.repository;

import fact.it.appointmentservice.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    Appointment findByAppointmentNumber(String appointmentNumber);
}
