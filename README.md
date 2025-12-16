
# Personalized Trip Budgeting System

## Overview

The **Personalized Trip Budgeting System** is a Java Spring Boot application designed to generate **real-time travel recommendations** based on user-defined budget tiers. The system leverages **dynamic pricing rules**, **RESTful APIs**, and **MongoDB** to deliver tailored flight, hotel, and activity options while incorporating advanced **cancellation policy logic** to enhance financial planning and risk management.

---

## Key Features

* Budget tier-based planning (**Low**, **Medium**, **High**)
* Dynamic pricing engine
  * Seasonal adjustments
  * City popularity metrics
  * Customer loyalty discounts
  * Weekend surcharges
    
* Real-time recommendations for:
  * Flights
  * Hotels
  * Activities
    
* Budget-tiered cancellation policies
* RESTful API architecture
* MongoDB-backed persistence
* Dummy data initialization via DataLoaderController

---

## Technology Stack

* **Backend:** Java 8+, Spring Boot
* **API:** RESTful Web Services
* **Database:** MongoDB
* **Build Tool:** Maven
* **Data Format:** JSON

---

## System Architecture

```
Client (Web / Mobile)
        |
        v
REST Controllers
- TripController
- DataLoaderController
        |
        v
Service Layer
- Trip Orchestrator
- Trip type detector
- Cancellation Policy Service
        |
        v
MongoDB
```

---

## Dummy Data Loader

To simplify local development and testing, the application includes a **DataLoaderController** that inserts **dummy data** into MongoDB.

This allows developers to quickly populate the database with:

* Sample flights
* Hotels
* Activities
* City popularity metrics
* Seasonal pricing rules
* Loyalty discount configurations

### DataLoaderController Purpose

* Eliminates the need for manual database setup
* Ensures consistent test data across environments
* Enables immediate API testing after application startup

---

### Installation & Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/personalized-trip-budgeting.git
   cd personalized-trip-budgeting
   ```

2. **Configure MongoDB**

   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/trip_planner
   ```

3. **Build the application**

   ```bash
   mvn clean install
   ```

4. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

5. **Load Dummy Data**

   ```bash
   POST http://localhost:8080/api/data/loadAll
   ```

---

## Future Enhancements

* External flight and hotel API integrations
* User authentication and authorization
* Caching for frequent recommendation requests
* Admin UI for managing pricing rules

---

## License

This project is licensed under the **MIT License**.

---

## Author

Developed using **Java Spring Boot and MongoDB**, showcasing:

* Rule-based pricing engines
* RESTful backend design
* Dynamic budget-aware travel planning

---
