import { ERROR_MESSAGES, getApiErrorType } from './errorConstants';

export function handleApiError(error) {
  let userMessage = ERROR_MESSAGES[getApiErrorType(error)] || ERROR_MESSAGES.DEFAULT;

  if (error.response?.data?.message) {
    userMessage = error.response.data.message;
  } else if (error.response?.data?.errors) {
    userMessage += `\n${Object.values(error.response.data.errors).join('\n')}`;
  }

  console.error('API Error:', error);
  return userMessage;
}

export function getApiErrorMessage(error) {
  if (error.response?.data?.message) {
    return error.response.data.message;
  }

  if (error.response?.data?.errors) {
    return Object.values(error.response.data.errors).join('\n');
  }

  const baseMessage = ERROR_MESSAGES[getApiErrorType(error)] || ERROR_MESSAGES.DEFAULT;
  return baseMessage;
}