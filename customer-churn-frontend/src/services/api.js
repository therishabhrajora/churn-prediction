// const API_URL = 'http://127.0.0.1:8000';
const API_URL = 'https://churn-prediction-m1w6.onrender.com';


/**
 * Checks the health status of the backend API.
 * @returns {Promise<Object>} The JSON response from the health endpoint.
 */
export const checkHealth = async () => {
  const response = await fetch(`${API_URL}/health`);
  
  if (!response.ok) {
    throw new Error('Backend health check failed');
  }
  
  return response.json();
};

/**
 * Sends customer data to the backend to get a churn prediction.
 * @param {Object} customerData - The data of the customer to analyze.
 * @returns {Promise<Object>} The JSON response containing the prediction.
 */
export const predictChurn = async (customerData) => {
  const response = await fetch(`${API_URL}/predict`, {
    method: 'POST',
    headers: { 
      'Content-Type': 'application/json' 
    },
    body: JSON.stringify(customerData)
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    throw new Error(errorData?.detail || 'Unable to get churn prediction');
  }

  return response.json();
};
