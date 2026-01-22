# CrimeDataBase
Java-based crime data analysis system that processes and queries Rutgers University crime reports using hash tables, linked lists, and object-oriented design. Includes CSV parsing, category-based filtering, and unit tests.
## Overview
RUCrimeDatabase is a Java-based application that processes and analyzes Rutgers University crime report data. The project ingests crime data from CSV files and stores it using core data structures, enabling efficient querying and organization of incidents by category and other attributes.

This project was developed to reinforce concepts in data structures, object-oriented programming, and file I/O in Java.

---

## Features
- Parses large CSV datasets containing crime incident records
- Stores incidents using hash tables and linked lists
- Supports category-based and incident-level querying
- Clean object-oriented design with separate domain classes
- Includes unit tests to validate functionality

---

## Technologies Used
- Java
- JUnit (for testing)
- CSV file processing
- Core data structures:
  - Hash tables
  - Linked lists

---

## Project Structure
RUCrimeDatabase/
├── src/
│ └── crime/
│ ├── Category.java
│ ├── Incident.java
│ ├── RUCrimeDatabase.java
│ ├── Driver.java
│ ├── StdIn.java
│ └── StdOut.java
├── test/
│ └── RUCrimesDatabaseTest.java
├── NB2025May.csv
├── NB2025June.csv
├── NB2025July.csv
└── NB2025Combined.csv
---

## How to Run
1. Clone the repository:
2. Open the project in an IDE such as VS Code or IntelliJ.
3. Compile and run `Driver.java`.
4. Ensure the CSV files are present in the project root directory.

---

## Testing
Unit tests are provided using JUnit.
- Run `RUCrimesDatabaseTest.java` to validate core functionality and data handling.

---

## Learning Objectives
- Implement and apply fundamental data structures
- Work with real-world CSV datasets
- Practice object-oriented design in Java
- Write and execute automated tests

---

## Notes
This project uses publicly available crime data for educational purposes.

---

## Author
Sameer Mansuri

---

## Acknowledgments
This project was developed as part of coursework at Rutgers University. Starter code, project specifications, and datasets were provided by Rutgers instructors. Additional implementation, testing, and organization were completed by the author.
