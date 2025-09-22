import axios from "axios";
import { getApiErrorMessage } from '../utils/api/apiErrorHandler';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
});

const ENDPOINTS = {
    PROVINCES: '/locations/provinces',
    DEPARTMENTS: '/locations/departments',
    MUNICIPALITIES: '/locations/municipalities',
};

let provincesCache = null;
let provincesPromise = null;
let cacheTimestamp = null;
const CACHE_DURATION = 24 * 60 * 60 * 1000; // 24 hours

async function makeRequest(config) {
    try {
        const response = await apiClient(config);
        return response.data;
    } catch (error) {
        const friendlyMessage = getApiErrorMessage(error);
        error.message = friendlyMessage;
        throw error;
    }
}

export async function getProvinces() {
    const now = Date.now();

    if (provincesCache && cacheTimestamp && (now - cacheTimestamp) < CACHE_DURATION) {
        return provincesCache;
    }

    if (provincesPromise) {
        return provincesPromise;
    }

    provincesPromise = makeRequest({ url: ENDPOINTS.PROVINCES }) 
        .then(data => {
            provincesCache = data;
            cacheTimestamp = Date.now();
            provincesPromise = null;
            return data;
        })
        .catch(error => {
            provincesPromise = null;
            throw error;
        });

    return provincesPromise;
}

export async function getDepartments(provinceID, options = {}) {
    return makeRequest({
        url: ENDPOINTS.DEPARTMENTS,
        params: { provinceID },
        ...options
    });
}

export async function getMunicipalities(provinceID, departmentID, options = {}) {
    return makeRequest({
        url: ENDPOINTS.MUNICIPALITIES,
        params: { provinceID, departmentID },
        ...options
    });
}