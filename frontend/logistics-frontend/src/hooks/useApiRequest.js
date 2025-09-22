import { useState, useCallback } from 'react';
import { getApiErrorMessage } from '../utils/api/apiErrorHandler';

export const ApiStatus = {
    IDLE: 'idle',
    LOADING: 'loading',
    SUCCESS: 'success',
    ERROR: 'error'
};

export function useApiRequest() {
    const [status, setStatus] = useState(ApiStatus.IDLE);
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);

    const makeRequest = useCallback(async (apiCall) => {
        setStatus(ApiStatus.LOADING);
        try {
            const response = await apiCall();
            setData(response);
            setStatus(ApiStatus.SUCCESS);
            return response;
        } catch (err) {
            const errorMessage = getApiErrorMessage(err); 

            setError(errorMessage);
            setStatus(ApiStatus.ERROR);
            throw errorMessage; 
        }
    }, []);

    return {
        data,
        error,
        status,
        isLoading: status === ApiStatus.LOADING,
        isError: status === ApiStatus.ERROR,
        isSuccess: status === ApiStatus.SUCCESS,
        makeRequest
    };
}