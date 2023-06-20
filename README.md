# Loan Application System

The Loan Application System is a web-based application that allows loan officers and customers to manage and track loan applications. It provides a user-friendly interface for loan officers to review and manage loan applications, and for customers to apply for loans and track their application status. The system incorporates Spring Security JWT for user authentication and authorization.

## Features

- User Roles:
  - Loan Officer: Responsible for managing loan applications and reviewing them for approval.
  - Customer: Applies for a loan and tracks loan application status.
- Loan Officer Tasks:
  - Login: Loan officers can log in to the system using their credentials.
  - View Loan Applications: Loan officers can view a list of loan applications with relevant details.
  - Review Loan Application: Loan officers can review the details of a loan application, including customer information, loan amount, and requested terms.
  - Approve/Reject Loan Application: Loan officers can approve or reject a loan application based on their assessment. If rejected, a reason can be provided.
  - Generate Loan Agreement: Loan officers can generate a loan agreement for approved applications, including loan terms and repayment schedule.
  - Update Loan Status: Loan officers can update the status of a loan application (e.g., approved, rejected, in progress, closed).
- Customer Tasks:
  - Register: Customers can register an account by providing their personal and contact information.
  - Login: Customers can log in to the system using their credentials.
  - Apply for a Loan: Customers can fill out a loan application form, providing details such as loan amount, purpose, and repayment preferences.
  - View Loan Application Status: Customers can check the status of their loan application (e.g., approved, rejected, in progress, closed).
  - View Loan Agreement: If a loan application is approved, customers can view and download the loan agreement.

## Technologies Used

- Java
- Spring Boot
- Spring Security JWT
- Maven
- Database (e.g., MySQL, PostgreSQL, or H2)

## Getting Started

### Prerequisites

- Java 8 or above
- Maven
- Database (e.g., MySQL, PostgreSQL, or H2)

### Installation

1. Clone the repository:

   ```shell
   git clone https://github.com/your-username/loan-application-system.git
   ```

2. Navigate to the project directory:

   ```shell
   cd loan-application-system
   ```

3. Configure the database:
   - Open the `application.properties` file located in the `src/main/resources` directory.
   - Update the database connection properties (e.g., URL, username, password) according to your database configuration.

4. Build the project using Maven:

   ```shell
   mvn clean install
   ```

5. Run the application:

   ```shell
   mvn spring-boot:run
   ```

   The application will start running on `http://localhost:8085`.

## Usage

1. Loan Officer Tasks:
   - Log in to the system using the provided credentials.
   - View the list of loan applications.
   - Review the details of a loan application and provide an assessment.
   - Approve or reject a loan application.
   - Generate a loan agreement for approved applications.
   - Update the status of a loan application.

2. Customer Tasks:
   - Register an account by providing personal and contact information.
   - Log in to the system using the created credentials.
   - Fill out a loan application form, providing loan details such as amount, purpose, and repayment preferences.
   - Check the status of the loan application.
   - If the loan application is approved, view and download the loan agreement.

## Configuration

The application can be configured using the `application.properties` file located in the
