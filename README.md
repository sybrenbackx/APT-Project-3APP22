# APT-Project
## Description:
A system for scheduling doctor appointments, managing patient and doctor records.
## Deployment List
- Appointment Service (MongoDB)   
- Patient Service (SQL DB)  
- Doctor Service (SQL DB)  
- Api-Gateway  
- Google Cloud Platform Oauth2  
- Prometheus  
- Mongo-Appointment  
- MySql-Doctor
- MySql-Patient  
## Deployment Schema
![Screenshot 2024-12-20 172423](https://github.com/user-attachments/assets/d7b39cce-c259-4cfe-88e2-2f5f5f418dd9)
# Postman
## Appointments:
GET /appointments -> Get all appointments  
![image](https://github.com/user-attachments/assets/b6ea0944-5082-48e4-a800-4268181b4d42)  
GET /appointments/{appointmentNumber} -> Get appointment by number  
![Screenshot 2024-12-20 180350](https://github.com/user-attachments/assets/3d661379-8100-4db6-9425-fc51369c943a)  
GET /appointments/patient/{patientNumber} -> Get all appointments of a patient  
![image](https://github.com/user-attachments/assets/47f32928-96e3-4274-b5ac-89371a872814)  
GET /appointments/doctor/{doctorNumber} -> Get all appointments of a doctor  
![image](https://github.com/user-attachments/assets/fb464378-e649-4f2e-9043-7209d9bbcf76)  
POST /appointments (AUTH) - Create an appointment  
![image](https://github.com/user-attachments/assets/d1b4cdd3-a11f-4e8f-9ffa-5053b3a9541d)  
PUT /appointments (AUTH) - Update an appointment  
![image](https://github.com/user-attachments/assets/20db6aa1-4743-47cc-a4f4-994425a88fc7)
DELETE /appointments (AUTH) - Delete an appointment  
![image](https://github.com/user-attachments/assets/10e2a3ad-416e-4584-bdaa-ebe531fef1b2)  
## Doctor:
GET /doctors -> Get all appointments  
![image](https://github.com/user-attachments/assets/e1496f37-0e58-44cb-984f-f5c79bc977ef)  
GET /doctors/{doctorNumber} -> Get doctor by number  
![image](https://github.com/user-attachments/assets/143f04f7-8265-46d0-bbe7-a95b1770c33e)  
POST /doctors (AUTH) - Create a doctor  
![image](https://github.com/user-attachments/assets/6c57e72a-efd8-467c-b595-9fd0f8a6eef2)  
PUT /doctors (AUTH) - Update a doctor  
![image](https://github.com/user-attachments/assets/8b117646-f54b-4e64-9679-577c2f98f3a7)  
DELETE /doctors (AUTH) - Delete a doctor  
![image](https://github.com/user-attachments/assets/abecbfff-3f83-4551-979a-d8e536b8b702)  

## Patient:
GET /patients -> Get all appointments  
![image](https://github.com/user-attachments/assets/a68e1519-8a49-428f-9b0b-2c175266dfe4)  
GET /patients/{patientNumber} -> Get patient by number  
![image](https://github.com/user-attachments/assets/20ea69dc-3441-402c-aca7-7ebfb5b186fc)  
POST /patients (AUTH) - Create a patient  
![image](https://github.com/user-attachments/assets/b7241115-4c5e-411c-ada2-1af9118fc1a7)  
PUT /patients (AUTH) - Update a patient  
![image](https://github.com/user-attachments/assets/21d03b3d-3fa5-4b4b-a90c-cb50f10401f9)  
DELETE /patients (AUTH) - Delete a patient  
![image](https://github.com/user-attachments/assets/18fb565f-4414-4aed-bd10-b270900828a6)  

### Prometheus
Cpu usage  
![Screenshot 2024-12-19 191619](https://github.com/user-attachments/assets/de72f5c0-b1ab-4d94-a749-0993efe0cae8)  
Number of requests of an endpoint  
![Screenshot 2024-12-19 191643](https://github.com/user-attachments/assets/507222ee-1b1e-4356-8472-0640d6429552)  
Connected targets  
![Screenshot 2024-12-19 191945](https://github.com/user-attachments/assets/a4e51ec2-00c1-4f0c-b7e4-4879a2361305)  
Request speed  
![Screenshot 2024-12-19 192250](https://github.com/user-attachments/assets/138b8060-a097-4c19-82c2-a7e9ad1940f4)  

