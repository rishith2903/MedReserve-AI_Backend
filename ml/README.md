# 🤖 MedReserve AI - Machine Learning Service

[![Python](https://img.shields.io/badge/Python-3.11-blue.svg)](https://python.org/)
[![FastAPI](https://img.shields.io/badge/FastAPI-0.104.0-green.svg)](https://fastapi.tiangolo.com/)
[![TensorFlow](https://img.shields.io/badge/TensorFlow-2.13.0-orange.svg)](https://tensorflow.org/)
[![scikit-learn](https://img.shields.io/badge/scikit--learn-1.3.0-orange.svg)](https://scikit-learn.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://docker.com/)

A comprehensive machine learning service for the MedReserve AI platform, providing intelligent health predictions, symptom analysis, and medical insights using advanced ML and deep learning models.

## 🌟 Features

### 🔬 Disease Prediction
- **Symptom-based Disease Prediction** using Random Forest and Neural Networks
- **Multi-model Ensemble** for improved accuracy and reliability
- **Confidence Scoring** for prediction reliability assessment
- **Real-time Inference** with optimized model serving
- **Model Versioning** and A/B testing capabilities

### 🧠 Machine Learning Models
- **Random Forest Classifier** for traditional ML-based predictions
- **Deep Neural Network** for complex pattern recognition
- **Ensemble Methods** combining multiple model predictions
- **Feature Engineering** with advanced preprocessing
- **Model Monitoring** and performance tracking

### 📊 Health Analytics
- **Risk Assessment** based on patient history and symptoms
- **Health Score Calculation** with personalized metrics
- **Trend Analysis** for health monitoring over time
- **Predictive Analytics** for early disease detection
- **Population Health Insights** and epidemiological analysis

### 🔍 Symptom Analysis
- **Natural Language Processing** for symptom description analysis
- **Symptom Mapping** to standardized medical terminology
- **Severity Assessment** based on symptom combinations
- **Differential Diagnosis** suggestions with confidence scores
- **Medical Knowledge Graph** integration

### 🚀 API Features
- **RESTful API** with FastAPI framework
- **Interactive Documentation** with Swagger UI
- **Async Processing** for high-performance inference
- **Batch Prediction** support for multiple patients
- **Real-time Health Monitoring** endpoints

## 🏗️ Tech Stack

- **Framework**: FastAPI 0.104.0 for high-performance API
- **ML Libraries**: scikit-learn 1.3.0, TensorFlow 2.13.0, XGBoost
- **Data Processing**: pandas 2.0.0, NumPy 1.24.0
- **NLP**: NLTK 3.8, spaCy 3.6.0 for text processing
- **Validation**: Pydantic 2.0 for data validation
- **Testing**: pytest 7.4.0, pytest-asyncio
- **Deployment**: Docker, Uvicorn ASGI server
- **Monitoring**: Prometheus metrics, structured logging
- **Database**: SQLite for model metadata, Redis for caching

## 📋 Prerequisites

- **Python 3.11+** (recommended for optimal performance)
- **pip 23+** or **conda** for package management
- **Docker** (optional, for containerized deployment)
- **Git** for version control
- **8GB+ RAM** (recommended for model training)
- **GPU Support** (optional, for deep learning acceleration)

## 🚀 Quick Start

### 1. Clone and Navigate
```bash
git clone <repository-url>
cd MedReserve/backend/ml
```

### 2. Environment Setup

#### Option A: Virtual Environment (Recommended)
```bash
# Create virtual environment
python -m venv venv

# Activate virtual environment
# Windows
venv\Scripts\activate
# macOS/Linux
source venv/bin/activate

# Upgrade pip
pip install --upgrade pip
```

#### Option B: Conda Environment
```bash
# Create conda environment
conda create -n medreserve-ml python=3.11
conda activate medreserve-ml
```

### 3. Install Dependencies
```bash
# Install production dependencies
pip install -r requirements.txt

# Install development dependencies (optional)
pip install -r requirements-dev.txt

# Install in development mode
pip install -e .
```

### 4. Environment Configuration
Create environment configuration:
```bash
# Copy example environment file
cp .env.example .env
```

Configure environment variables in `.env`:
```env
# API Configuration
API_HOST=0.0.0.0
API_PORT=8001
API_WORKERS=4
API_RELOAD=false

# Model Configuration
MODEL_PATH=./models
MODEL_VERSION=v1.0.0
ENSEMBLE_ENABLED=true
CONFIDENCE_THRESHOLD=0.7

# Database Configuration
DATABASE_URL=sqlite:///./ml_service.db
REDIS_URL=redis://localhost:6379/0

# Logging Configuration
LOG_LEVEL=INFO
LOG_FORMAT=json
LOG_FILE=./logs/ml_service.log

# Performance Configuration
MAX_BATCH_SIZE=100
PREDICTION_TIMEOUT=30
CACHE_TTL=3600

# Security Configuration
API_KEY_ENABLED=false
API_KEY=your_api_key_here
CORS_ORIGINS=["http://localhost:3000", "https://yourdomain.com"]

# Model Training Configuration
TRAINING_DATA_PATH=./data/training
VALIDATION_SPLIT=0.2
RANDOM_SEED=42
```

### 5. Download and Prepare Models
```bash
# Download pre-trained models (if available)
python scripts/download_models.py

# Or train models from scratch
python scripts/train_models.py

# Validate model setup
python scripts/validate_models.py
```

### 6. Start the Service
```bash
# Development mode with auto-reload
uvicorn main:app --host 0.0.0.0 --port 8001 --reload

# Production mode
uvicorn main:app --host 0.0.0.0 --port 8001 --workers 4

# Using the startup script
python main.py

# With custom configuration
python main.py --config config/production.yaml
```

### 7. Verify Installation
```bash
# Health check
curl http://localhost:8001/health

# API documentation
open http://localhost:8001/docs

# Test prediction endpoint
curl -X POST "http://localhost:8001/predict" \
  -H "Content-Type: application/json" \
  -d '{"symptoms": ["fever", "cough", "headache"]}'
```

## 🔗 API Endpoints

### Health and Status
```http
GET    /health              # Service health check
GET    /status              # Detailed service status
GET    /metrics             # Prometheus metrics
GET    /models              # Available models information
```

### Disease Prediction
```http
POST   /predict             # Single disease prediction
POST   /predict/batch       # Batch disease prediction
POST   /predict/detailed    # Detailed prediction with explanations
GET    /predict/history     # Prediction history
```

### Symptom Analysis
```http
POST   /symptoms/analyze    # Analyze symptom descriptions
POST   /symptoms/extract    # Extract symptoms from text
GET    /symptoms/list       # Get available symptoms
POST   /symptoms/similarity # Find similar symptoms
```

### Health Assessment
```http
POST   /assess/risk         # Health risk assessment
POST   /assess/score        # Calculate health score
POST   /assess/trends       # Analyze health trends
POST   /assess/recommendations # Get health recommendations
```

### Model Management
```http
GET    /models/info         # Model information and metadata
POST   /models/retrain      # Trigger model retraining
GET    /models/performance  # Model performance metrics
POST   /models/validate     # Validate model accuracy
```

## 📊 API Usage Examples

### Disease Prediction
```python
import requests

# Single prediction
response = requests.post(
    "http://localhost:8001/predict",
    json={
        "symptoms": ["fever", "cough", "headache", "fatigue"],
        "patient_age": 35,
        "patient_gender": "male",
        "medical_history": ["hypertension"]
    }
)

prediction = response.json()
print(f"Predicted disease: {prediction['disease']}")
print(f"Confidence: {prediction['confidence']:.2f}")
print(f"Recommendations: {prediction['recommendations']}")
```

### Batch Prediction
```python
# Batch prediction for multiple patients
patients = [
    {
        "patient_id": "P001",
        "symptoms": ["fever", "cough"],
        "age": 25,
        "gender": "female"
    },
    {
        "patient_id": "P002", 
        "symptoms": ["headache", "nausea"],
        "age": 45,
        "gender": "male"
    }
]

response = requests.post(
    "http://localhost:8001/predict/batch",
    json={"patients": patients}
)

results = response.json()
for result in results["predictions"]:
    print(f"Patient {result['patient_id']}: {result['disease']}")
```

### Symptom Analysis
```python
# Analyze symptom description
response = requests.post(
    "http://localhost:8001/symptoms/analyze",
    json={
        "description": "I have been feeling tired and have a persistent cough for the past week",
        "extract_symptoms": True,
        "severity_assessment": True
    }
)

analysis = response.json()
print(f"Extracted symptoms: {analysis['symptoms']}")
print(f"Severity: {analysis['severity']}")
```
