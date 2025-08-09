# Simple Dropbox

A lightweight Dropbox-like backend service built with **Spring Boot**, **PostgreSQL**, **React.js** and **AWS S3** (via LocalStack).  
It supports file upload, download, and management, all running locally with Docker.

---

## 🚀 Tech Stack
- **Java 17** + **Spring Boot**
- **PostgreSQL** (Database)
- **LocalStack** (AWS S3 Simulation)
- **Maven** (Build Tool)
- **Docker & Docker Compose**

---

## 🛠 Setup Instructions

Follow these steps to run the project locally.

---

### **Step 1 - Navigate to src folder of backend service**
```bash
cd simplified-dropbox/src
```

### **Step 2 - Start required services**
```bash
docker compose up -d
```

### **Step 3 - Build the backend**
```bash
mvn clean install
```

### **Step 4 - Run the backend**
```bash
mvn spring-boot:run
```

Backend should be available at: http://localhost:8081

### **Step 5 Start the React Frontend**
```bash
cd dropbox-client
npm install
npm run dev
```
Frontend should be available at: http://localhost:5173

