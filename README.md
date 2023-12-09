## Project Description
This project is a Java application that provides functionality for generating PDF statements and sending them via email based on banking transactions data stored in a CSV file. It utilizes Spring Boot framework for building the application. The application also includes endpoints for retrieving banking statements within a specified date range. 

## Technology Stack
- **Language**: Java
- **Framework**: Spring Boot
- **Libraries**: Apache Commons CSV, iText PDF, JavaMail API
- **Build Tool**: Maven

## Why Spring Boot
Spring Boot was chosen for this project due to its ease of use, powerful features, and rapid development capabilities. It simplifies the setup and configuration of the application, allowing for quick development of RESTful APIs and web applications. Additionally, its integration with other Spring modules such as Spring Data and Spring Security provides a robust architecture for the application.

## Endpoints for Testing
- POST /api/statements/generate
  - Generates a PDF statement report based on user email, start date, and end date.
  - Payload: 
    ```
    {
      "user_email": "user@example.com",
      "start_date": "2022-01-01",
      "end_date": "2022-12-31"
    }
    ```
  - Response: PDF file as attachment
  
- POST /api/statements/sendPdf
  - Sends the generated PDF statement report to the user's email.
  - Payload: 
    ```
    {
      "user_email": "user@example.com",
      "start_date": "2022-01-01",
      "end_date": "2022-12-31"
    }
    ```
  - Response: Success message or error message
  
- GET /api/statements
  - Retrieves banking statements for a user within a specified date range.
  - Query Parameters: userEmail, startDate, endDate

## Notes for Testing
- Make sure to modify the CSV file path in CsvStatementService and PDFGenerationService to point to the actual location of the banking_statements.csv file on your system.
- Use appropriate tools, such as Postman or curl, to test the endpoints and verify their functionality.

## Authentication 

-Dependency Configuration: Add the necessary Spring Security dependencies to the pom.xml file.

-Configuration: Configure Spring Security by creating a class that extends WebSecurityConfigurerAdapter and override the configure method to set up authentication and authorization rules.

-User Authentication: Define a user authentication mechanism, either through an in-memory user store, JDBC authentication, or user details service.

-Endpoint Security: Configure the security rules for each endpoint, specifying who can access them and what roles are required.

-Token-Based Authentication (Optional): For more advanced scenarios, consider implementing token-based authentication using JSON Web Tokens (JWT) or OAuth.

-Testing: Thoroughly test the secured endpoints to ensure that only authenticated and authorized users can access them.