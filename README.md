# Claim Management System

## Installation and Running Steps

### 1. Clone the Repository
Clone the GitHub repository to your local machine:
```sh
git clone https://github.com/vtq2301/FP_G32_ASM2.git
```

### 2. Run the Application

#### Using an IDE (Recommended)
1. Open your IDE (e.g., IntelliJ IDEA, Eclipse).
2. Navigate to the project directory.
3. Open the `all/Main.java` file.
4. Run the `Main.java` file from the IDE.

#### Using the Command Line (Not Recommended)
To run the application from the command line, you need Java JDK 17 or later and JavaFX SDK 22.0.1 installed.

##### Command to Run the JAR File:
```sh
java --module-path "C:\Program Files\javafx-sdk-22.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar demo.jar
```

**Note**: You might run into the following error:
```
Caused by: java.lang.NoClassDefFoundError: javafx/application/Application
```
Hence, running from the command line is not recommended. It is preferable to use an IDE.

## Accounts used for demonstration
- **Policy Holder**:
- **Dependent**
- **Policy Owner**: policy_owner_2 / pw: abc123
- **Manager**
- **Surveyor**
- **Admin**: Admin / 1234

## Technologies Used

- **Java**: JDK 21
- **Database**: PostgreSQL with JDBC (pg-JDBC)
- **Server**: Supabase
- **Testing**: JUnit 5 and Mockito
- **GUI**: JavaFX – ensures a responsive and smooth GUI

## Application Description

This Claim Management System is designed to handle various operations related to insurance claims, utilizing JavaFX for the graphical user interface and JDBC for database interactions. The system supports multiple roles, including Customers, Providers, and System Administrators, each with specific functionalities.

### Functional Requirements

#### System Operations

- Load data from the prepared database when the program starts
- Support real-time updates
- User authentication with secure login and logout functionalities
- Responsive and smooth GUI operations

### User Roles and Operations

#### Customer
- **Policy Holder (PH)**:
    - Cannot log in after being removed from beneficiaries of a Policy Owner
    - Cannot log in without a Policy Owner
    - File, update, and retrieve their claims (CRUD - no remove)
    - File, update, and retrieve their dependents' claims (CRUD - no remove)
    - Update personal information (phone, address, email, password)
    - Get and update dependents' information
    - View their insurance rate and logs of historical actions
    - Upload documents as images for claims

- **Dependent (Dep)**:
    - Retrieve their claims
    - Retrieve and update their information
    - Have an insurance rate of 60% of their Policy Holder’s rate

- **Policy Owner (PO)**:
    - File, update, delete, and retrieve their beneficiaries’ claims (CRUD)
    - Add, update, remove, and get beneficiaries’ information (CRUD)
    - Get and update their information
    - Calculate yearly insurance costs for providers and dependents
    - View logs of historical actions

#### Provider
- **Insurance Surveyor**:
    - Require more information from a claim
    - Propose a claim to their managers (Insurance Manager)
    - Retrieve all claims with different filtering options
    - View logs of historical actions
    - Get and update their information

- **Insurance Manager**:
    - Approve or reject proposed claims
    - Retrieve all claims with different filtering options
    - Retrieve insurance surveyors' information
    - View logs of historical actions
    - Get and update their information

#### System Admin
- **System Administrator**:
    - CRUD operations on Customer and Provider information
    - Retrieve claims’ information
    - Summarize successfully claimed amounts with different parameters/filtering options
    - Get and update their information
    - View logs of historical actions
