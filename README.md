# University FAQ Chatbot (Basic Implementation)

## Project Overview
This project is a basic implementation of an intelligent FAQ chatbot designed to assist university students with commonly asked questions. It uses Spring Boot for the backend infrastructure and MongoDB for storing FAQs. 
## Current Features
- Basic FAQ Management System
- Web Crawling Service for gathering university information
- Simple Admin Interface for managing FAQs
- MongoDB Integration for data storage
- API documentation with Swagger

## Technologies Used
- Java
- Spring Boot
- Maven
- MongoDB
- JSoup (for web crawling)
- Swagger (for API documentation)

## Prerequisites
- JDK 17 or higher
- Maven 3.6.0 or higher
- MongoDB 4.4 or higher

## Setup Instructions

1. Clone the repository:
```sh
[git clone https://github.com/yourusername/university-faq-chatbot.git](https://github.com/zmehta2/university-chatbot.git)
cd university-faq-chatbot
```

2. Configure MongoDB:
Create an `application.properties` file in `src/main/resources/` with the following content:
```properties
spring.data.mongodb.uri=mongodb+srv://username:password@your-cluster-url/universitychatbot?retryWrites=true&w=majority
```
Replace `username`, `password`, and `your-cluster-url` with your actual MongoDB credentials.

3. Build and run the service:
```sh
mvn clean install 
java -jar target/university-faq-chatbot-0.0.1-SNAPSHOT.jar
```

4. Access the Swagger UI:
Once the application is running, access the Swagger UI at:
```
http://localhost:9090/swagger-ui.html
```
Use the following credentials for admin endpoints:
- Username: admin
- Password: adminpassword

## API Documentation
The API is documented using Swagger. To access protected endpoints:
1. Click the "Authorize" button in Swagger UI
2. Enter the admin credentials
3. Click "Authorize"

## Original Project Requirements
The full project aim is to develop a comprehensive FAQ chatbot with the following features:
- Intelligent chat interface using OpenAI for natural language processing
- Usage analytics and reporting
- Role-based access for customizable content
- Admin interface for knowledge base expansion

## Future Development
To meet the original project requirements, future development will focus on:
1. Implementing usage analytics and reporting features
2. Developing role-based access control
3. Enhancing the admin interface for comprehensive knowledge base management
4. Creating a user-friendly chat interface for students
5. Integrating OpenAI API for natural language processing if feasible within timeline
