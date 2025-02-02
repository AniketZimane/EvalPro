# AI-Powered Answer Sheet Evaluation System

## 📌 Project Overview
This project is an **AI-powered answer sheet evaluation system** that scans handwritten student answer sheets, compares them with a model answer paper, and assigns a similarity score. It integrates **Spring Boot (Java) backend**, **FastAPI (Python) AI model**, and a **TailwindCSS-based frontend**.

---

## 🎯 Features
✅ **OCR-based text extraction** from handwritten answer sheets using **Tesseract OCR**
✅ **AI-driven answer evaluation** using **BERT-based similarity model**
✅ **Spring Boot & FastAPI integration** for seamless processing
✅ **Web-based UI with TailwindCSS** for easy answer uploads
✅ **Real-time evaluation & automated scoring**

---

## 🛠 Tech Stack
- **Backend:** Spring Boot (Java), WebClient, REST API
- **AI Model:** Python, FastAPI, BERT, OCR (Tesseract)
- **Database:** MySQL (optional for storing results)
- **Frontend:** HTML, TailwindCSS, JavaScript

---

## 🚀 Installation & Setup
### **1️⃣ Clone the Repository**
```bash
git clone https://github.com/yourusername/ai-answer-evaluation.git
cd ai-answer-evaluation
```

### **2️⃣ Set Up the AI Model (Python + FastAPI)**
#### **Install Dependencies:**
```bash
pip install fastapi uvicorn transformers torch pytesseract opencv-python scikit-learn numpy
```

#### **Run AI Server:**
```bash
uvicorn ai_model:app --reload --host 0.0.0.0 --port 8000
```

### **3️⃣ Set Up the Spring Boot Backend**
#### **Configure `pom.xml` (Dependencies):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

#### **Run Spring Boot Backend:**
```bash
mvn spring-boot:run
```

### **4️⃣ Run Frontend (HTML + TailwindCSS)**
- Open `index.html` in a browser.
- Upload answer sheets & get AI-generated results.

---

## 📌 API Endpoints
### **🔹 AI Model (FastAPI)**
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/evaluate` | Uploads student & model answer papers, returns a similarity score |

### **🔹 Spring Boot Backend**
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/answers/evaluate` | Calls the AI model and returns the evaluated score |

---

## 🎯 How It Works
1. **Upload student answer sheet & model answer paper**
2. **OCR extracts handwritten text** from images
3. **AI compares answers using BERT similarity model**
4. **Generates a score based on answer similarity**
5. **Displays results on the frontend**

---

## 🛠 Future Enhancements
- **Improve OCR accuracy with Google Vision API**
- **Deep learning-based handwriting recognition**
- **Automated grading system with teacher feedback**
- **Database integration for storing results**

---

## 🤝 Contributing
We welcome contributions! Feel free to **fork** the repo, create a new branch, and submit a **pull request**.

---

## 📜 License
This project is licensed under the **MIT License**.

---

## ✨ Author
👤 **Aniket Zimane**  
📧 **aniket@example.com**  
🔗 **[LinkedIn](https://linkedin.com/in/aniket-zimane)**  

