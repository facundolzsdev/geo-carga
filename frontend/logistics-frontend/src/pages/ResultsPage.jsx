import { useLocation, useNavigate } from 'react-router-dom';
import { ShippingResult } from '../components/response/ShippingResult';
import { ROUTES } from '../utils/constants/routes';

export function ResultsPage() {
    const location = useLocation();
    const navigate = useNavigate();

    if (!location.state?.result) {
        navigate(ROUTES.HOME);
        return null;
    }
    
    const { result } = location.state;
    return (
        <div className="container-lg mt-3">
            <ShippingResult result={result} />
            <button
                className="btn btn-dark mb-4"
                onClick={() => navigate(ROUTES.HOME)}>
                <i className="bi bi-arrow-left me-2"></i>Cotizar otro Envío
            </button>
        </div>
    );
}