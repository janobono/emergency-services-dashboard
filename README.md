# emergency-services-dashboard

## Assignment: Emergency Services Dashboard

**Objective:**

Develop a Java application using Spring Boot that acts as an emergency services
dashboard.

The application should log, query, and delete real-time emergency incidents.

Additionally, implement a simple UI using Angular and containerize the application using Docker.

Specifications / Rules:

- General:
    - Spring Boot Setup: Use Spring Boot for setting up the application.
    - Data Persistence: Use Hibernate for data persistence.
    - Unit Testing: Implement unit tests for at least three core functionalities: incident logging, incident search, and
      incident deletion.
    - Documentation: Use Swagger for API documentation.
    - Containerization: Use Docker to containerize the application.
- Incidents:
    - Create and Log Incidents:
        - Implement a RESTful API endpoint to log emergency incidents.
        - Incidents should have attributes like incidentType (e.g., fire, medical), location (latitude and longitude),
          timestamp, and severityLevel (e.g., low, medium, high).
        - Validate the input data using Spring Boot’s validation framework.
    - Search Incidents:
        - Create another API endpoint to perform searches based on parameters such as incidentType, location, and
          timestamp.
    - Delete Incidents:
        - Implement a RESTful API endpoint to delete emergency incidents by their unique identifier.
        - Ensure that the deletion is reflected in the database.
- UI-Implementation:
    - Angular Setup: Use Angular to create a simple user interface.
    - Incident Logging UI:
        - Create a form to log new incidents.
        - Validate the input data on the client side.
    - Incident Search UI:
        - Create a search form to query incidents based on parameters such as incidentType, location, and timestamp.
    - Incident Deletion UI:
        - Add functionality to delete incidents from the search results table.
- Containerization:
    - Docker Setup:
        - Create Compose files for the Spring Boot application and the Angular frontend.

## Realization

- [Java application](./incident-service/README.md)
- [Simple UI](./dashboard-ui/README.md)
- [Docker](./docker/README.md)
