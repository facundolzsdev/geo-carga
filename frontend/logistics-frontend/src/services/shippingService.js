import axios from "axios";
import { getApiErrorMessage } from '../utils/api/apiErrorHandler';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
});

const ENDPOINT = '/app';

export async function calculateShipping(shippingRequest) {
    try {
        const response = await apiClient.post(ENDPOINT, shippingRequest);
        return response.data;
    } catch (error) {
        const friendlyMessage = getApiErrorMessage(error);
        error.message = friendlyMessage;
        throw error;
    }
}