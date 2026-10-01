from pathlib import Path
import joblib
import pandas as pd

BASE_DIR = Path(__file__).resolve().parent.parent
MODEL_PATH = BASE_DIR / "models" / "best_model.pkl"
model = joblib.load(MODEL_PATH)

def predict_churn(customer_data):
    customer_df = pd.DataFrame([customer_data])

    
    probability = float(model.predict_proba(customer_df)[0][1])

    prediction = model.predict(customer_df)[0]

    if probability >= 0.70:
        risk = "HIGH"
    elif probability >= 0.40:
        risk = "MEDIUM"
    else:
        risk = "LOW"

    return {
        "prediction": "Likely to Churn" if prediction == "Yes" else "Likely to Stay",
        "churn_probability": round(probability * 100, 2),
        "risk_level": risk
    }
