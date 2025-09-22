import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { calculateShipping } from '../services/shippingService';
import { getApiErrorMessage } from '../utils/api/apiErrorHandler';
import { ROUTES } from '../utils/constants/routes';

export const useShippingCalculator = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const clearError = () => {
        setError(null);
    };

    const calculate = async (formData) => {
        try {
            setIsLoading(true);
            setError(null);
            const result = await calculateShipping(formData);

            if (result.originAccuracy !== "FALLBACK" && result.destinationAccuracy !== "FALLBACK") {
                navigate(ROUTES.RESULTS, { state: { result, formData } });
            }
            else {
                if (result.originAccuracy === "FALLBACK") {
                    setError(result.originAccuracyMessage);
                } else {
                    setError(result.destinationAccuracyMessage);
                }
            }
        } catch (error) {
            const errorMessage = getApiErrorMessage(error);
            setError(errorMessage);
        } finally {
            setIsLoading(false);
        }
    };

    return { calculate, isLoading, error, clearError };
};