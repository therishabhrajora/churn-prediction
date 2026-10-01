# Customer Churn Prediction - React Frontend

React + Vite frontend for the FastAPI Customer Churn Prediction API.

## Run

```bash
npm install
npm run dev
```

Set `VITE_API_URL` in `.env` if the FastAPI backend is not running at `http://localhost:8000`.

## Backend

Start FastAPI, for example:

```bash
uvicorn main:app --reload
```

If React (5173) and FastAPI (8000) run on different origins, enable CORS in FastAPI for `http://localhost:5173`.
