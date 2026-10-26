# Employee Management Payroll System

A web-based Employee Management and Payroll System developed using Java and Spring Boot. The application manages employee records, calculates bonuses and deductions, determines net salaries, and exports payroll reports. Docker is used to containerize the application.

## Features

- Add, view, update, and delete employee records.
- Manage employee names, departments, roles, and base salaries.
- Calculate employee bonuses based on roles.
- Calculate salary deductions and net salaries.
- Search employees by ID and department.
- Export employee payroll details to a text report.
- Store employee information in a MySQL database.
- Run the application in a Docker container.

## Technology Stack

- **Backend:** Java 17, Spring Boot
- **Web:** Spring MVC, Thymeleaf, HTML
- **Database:** MySQL
- **ORM:** Spring Data JPA, Hibernate
- **Build Tool:** Maven
- **Containerization:** Docker

## Payroll Calculation

The application calculates employee salaries using the following rules:

- **Manager Bonus:** 25% of base salary
- **Other Roles Bonus:** 15% of base salary
- **Standard Deduction:** 12% of base salary
- **Manager Additional Deduction:** 200

**Net Salary = Base Salary + Bonus − Deductions**

These calculations follow the rules implemented in the application.

## Project Structure

```text
Employee_Management_Payroll/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/employee/
│   │   │       └── Employee_Management_Payroll/
│   │   │           ├── Employee.java
│   │   │           ├── EmployeeController.java
│   │   │           ├── EmployeeRepository.java
│   │   │           ├── EmployeeService.java
│   │   │           ├── EmployeeReportService.java
│   │   │           └── EmployeeManagementPayrollApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
├── Dockerfile
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17 or compatible JDK
- MySQL Server
- Maven, or the included Maven Wrapper
- Docker

## Database Configuration

1. Install and start MySQL.
2. Create a database named `employee_payroll_db`.
3. Configure the database URL, username, and password in `src/main/resources/application.properties`.

Keep database credentials private. Configure them through environment variables or an external configuration file rather than committing passwords to GitHub.

## Run Locally

1. Clone this repository.
2. Configure the MySQL database.
3. Open the project in Eclipse or another Java IDE.
4. Build and run the Spring Boot application using Maven or the IDE.
5. Open the application in your browser.

```text
http://localhost:8080/employees
```

## Run with Docker

Build the Docker image from the project root:

```bash
docker build -t employee-payroll .
```

Run the container:

```bash
docker run -d --name employee-payroll -p 8080:8080 employee-payroll
```

Ensure that MySQL is accessible to the container and that the datasource configuration uses the correct database host and credentials. The default `localhost` database URL must be adjusted when MySQL runs outside the application container.

## Application Status

Completed and pushed to GitHub.


