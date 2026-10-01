from fastapi import FastAPI



# 2. Now you can safely import from your src folder package!
from src.schemas import Customer 
from src.predict import predict_churn


app = FastAPI(
    title="Customer Churn Prediction API",
    description="ML API for predicting customer churn",
    version="1.0"
)

@app.get("/health")
def health():
    return{
       "status": "healthy",
        "model_loaded": True
    }


@app.get("/")
def home():

    return {
        "message": "Customer Churn Prediction API",
        "status": "running"
    }


@app.post("/predict")
def predict_endpoint(customer: Customer):
    customer_dict=customer.model_dump()
    result=predict_churn(customer_dict)
    return result
   