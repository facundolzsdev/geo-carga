export const ERROR_MESSAGES = {
    NETWORK: 'Servicio no disponible. Por favor, intente más tarde.',
    TIMEOUT: 'El servidor no responde. Verifique su conexión.',
    DEFAULT: 'Error inesperado. Intente nuevamente.',
};

export const getApiErrorType = (error) => {
    if (error.message?.includes('Network Error') || error.code === 'ERR_NETWORK') {
        return 'NETWORK';
    }
    if (error.code === 'ECONNABORTED') {
        return 'TIMEOUT';
    }
    return 'DEFAULT';
};