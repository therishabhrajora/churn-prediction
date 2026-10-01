from fastapi import FastAPI, Request, status
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware
from fastapi.exceptions import RequestValidationError

from src.schemas import Customer 
from src.predict import predict_churn

app = FastAPI(
    title="Customer Churn Prediction API",
    description="ML API for predicting customer churn",
    version="1.0"
)

# 1. Broaden origins during debugging if localhost variations cause issues
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://127.0.0.1:5173"],
    allow_credentials=True,
    allow_methods=["*"],  # Allows GET, POST, OPTIONS automatically
    allow_headers=["*"],
)


@app.get("/")
def home():
    return {"status": "running"}

@app.get("/health")
def health():
    return {"status": "healthy", "model_loaded": True}

@app.post("/predict")
def predict_endpoint(customer: Customer):
    customer_dict = customer.model_dump()
    result = predict_churn(customer_dict)
    return result
