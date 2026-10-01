import { useState, useId } from 'react';
import { predictChurn } from '../services/api';

// ==========================================
// 1. CONSTANTS & SCHEMAS
// ==========================================
const INITIAL_FORM = {
  gender: 'Male',
  SeniorCitizen: 0,
  Partner: 'Yes',
  Dependents: 'No',
  tenure: 12,
  PhoneService: 'Yes',
  MultipleLines: 'No',
  InternetService: 'Fiber optic',
  OnlineSecurity: 'No',
  OnlineBackup: 'No',
  DeviceProtection: 'No',
  TechSupport: 'No',
  StreamingTV: 'No',
  StreamingMovies: 'No',
  Contract: 'Month-to-month',
  PaperlessBilling: 'Yes',
  PaymentMethod: 'Electronic check',
  MonthlyCharges: 70,
  TotalCharges: 840
};

const OPTIONS = {
  yesNo: [{ label: 'Yes', value: 'Yes' }, { label: 'No', value: 'No' }],
  gender: [{ label: 'Male', value: 'Male' }, { label: 'Female', value: 'Female' }],
  senior: [{ label: 'No', value: 0 }, { label: 'Yes', value: 1 }],
  internet: [{ label: 'DSL', value: 'DSL' }, { label: 'Fiber optic', value: 'Fiber optic' }, { label: 'No', value: 'No' }],
  contract: ['Month-to-month', 'One year', 'Two year'].map(v => ({ label: v, value: v })),
  payment: ['Electronic check', 'Mailed check', 'Bank transfer (automatic)', 'Credit card (automatic)'].map(v => ({ label: v, value: v })),
  
  get service() {
    return [...this.yesNo, { label: 'No internet service', value: 'No internet service' }];
  },
  get multipleLines() {
    return [...this.yesNo, { label: 'No phone service', value: 'No phone service' }];
  }
};

const INTERNET_SERVICES_FIELDS = [
  { name: 'OnlineSecurity', label: 'Online Security' },
  { name: 'OnlineBackup', label: 'Online Backup' },
  { name: 'DeviceProtection', label: 'Device Protection' },
  { name: 'TechSupport', label: 'Tech Support' },
  { name: 'StreamingTV', label: 'Streaming TV' },
  { name: 'StreamingMovies', label: 'Streaming Movies' }
];

// ==========================================
// 2. SUB-COMPONENTS
// ==========================================
function SelectField({ label, name, value, onChange, options }) {
  const id = useId();
  return (
    <div className="form-group">
      <label htmlFor={id}>{label}</label>
      <select id={id} name={name} value={value} onChange={onChange}>
        {options.map(o => (
          <option key={String(o.value)} value={o.value}>{o.label}</option>
        ))}
      </select>
    </div>
  );
}

function InputField({ label, name, value, onChange, min = 0, step }) {
  const id = useId();
  return (
    <div className="form-group">
      <label htmlFor={id}>{label}</label>
      <input 
        id={id}
        type="number" 
        name={name} 
        value={value} 
        min={min} 
        step={step} 
        onChange={onChange} 
      />
    </div>
  );
}

// ==========================================
// 3. MAIN COMPONENT
// ==========================================
export default function PredictionForm({ onResult }) {
  const [form, setForm] = useState(INITIAL_FORM);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    
    // Systematically parse numbers vs strings based on target field type or name
    let parsedValue = value;
    if (type === 'number' || ['SeniorCitizen', 'tenure'].includes(name)) {
      parsedValue = Number(value);
    }

    setForm(prev => ({ ...prev, [name]: parsedValue }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
        console.log(form);
      const data = await predictChurn(form);
      onResult(data);
    } catch (err) {
      setError(err.message || 'An error occurred during prediction.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="prediction-form" onSubmit={handleSubmit}>
      
      {/* Section: Customer Demographics */}
      <section className="form-section">
        <h2>Customer Information</h2>
        <div className="form-grid">
          <SelectField label="Gender" name="gender" value={form.gender} onChange={handleChange} options={OPTIONS.gender} />
          <SelectField label="Senior Citizen" name="SeniorCitizen" value={form.SeniorCitizen} onChange={handleChange} options={OPTIONS.senior} />
          <SelectField label="Partner" name="Partner" value={form.Partner} onChange={handleChange} options={OPTIONS.yesNo} />
          <SelectField label="Dependents" name="Dependents" value={form.Dependents} onChange={handleChange} options={OPTIONS.yesNo} />
          <InputField label="Tenure (months)" name="tenure" value={form.tenure} onChange={handleChange} />
        </div>
      </section>

      {/* Section: Core & Add-on Services */}
      <section className="form-section">
        <h2>Services</h2>
        <div className="form-grid">
          <SelectField label="Phone Service" name="PhoneService" value={form.PhoneService} onChange={handleChange} options={OPTIONS.yesNo} />
          <SelectField label="Multiple Lines" name="MultipleLines" value={form.MultipleLines} onChange={handleChange} options={OPTIONS.multipleLines} />
          <SelectField label="Internet Service" name="InternetService" value={form.InternetService} onChange={handleChange} options={OPTIONS.internet} />
          
          {INTERNET_SERVICES_FIELDS.map(({ name, label }) => (
            <SelectField 
              key={name} 
              label={label} 
              name={name} 
              value={form[name]} 
              onChange={handleChange} 
              options={OPTIONS.service} 
            />
          ))}
        </div>
      </section>

      {/* Section: Financials & Terms */}
      <section className="form-section">
        <h2>Contract & Billing</h2>
        <div className="form-grid">
          <SelectField label="Contract" name="Contract" value={form.Contract} onChange={handleChange} options={OPTIONS.contract} />
          <SelectField label="Paperless Billing" name="PaperlessBilling" value={form.PaperlessBilling} onChange={handleChange} options={OPTIONS.yesNo} />
          <SelectField label="Payment Method" name="PaymentMethod" value={form.PaymentMethod} onChange={handleChange} options={OPTIONS.payment} />
          <InputField label="Monthly Charges" name="MonthlyCharges" value={form.MonthlyCharges} onChange={handleChange} step="0.01" />
          <InputField label="Total Charges" name="TotalCharges" value={form.TotalCharges} onChange={handleChange} step="0.01" />
        </div>
      </section>

      {/* Error Feedback */}
      {error && <div className="error-message" role="alert">{error}</div>}

      {/* Form Action */}
      <button type="submit" className="predict-button" disabled={loading}>
        {loading ? 'Analyzing Customer...' : 'Predict Churn'}
      </button>
      
    </form>
  );
}
