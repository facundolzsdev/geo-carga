const PACKAGE_LIMITS = {
    maxWeightKg: 2000,
    maxLengthCm: 350,
    maxWidthCm: 175,
    maxHeightCm: 150
};

export function validateShipmentData({ origin, destination, packageData }) {
    const errors = {};

    // Helper function to check if a field is empty
    const isEmpty = (value) => {
        if (value === null || value === undefined || value === "") return true;
        if (typeof value === 'object') return false;
        if (typeof value === 'string') return value.trim() === "";
        return false;
    };

    // Helper function to get display name for comparison
    const getDisplayName = (value) => {
        if (typeof value === 'object' && value?.name) return value.name;
        if (typeof value === 'string') return value;
        return "";
    };

    // Check required fields
    Object.entries(origin).forEach(([key, value]) => {
        if (isEmpty(value)) {
            errors[`origin.${key}`] = true;
        }
    });

    Object.entries(destination).forEach(([key, value]) => {
        if (isEmpty(value)) {
            errors[`destination.${key}`] = true;
        }
    });

    // Package validation
    Object.entries(packageData).forEach(([key, value]) => {
        const numValue = Number(value);

        if (value === "" || isNaN(numValue) || numValue <= 0) {
            errors[`packageData.${key}`] = true;
            return;
        }

        // Check limits
        const limits = {
            weightKg: PACKAGE_LIMITS.maxWeightKg,
            lengthCm: PACKAGE_LIMITS.maxLengthCm,
            widthCm: PACKAGE_LIMITS.maxWidthCm,
            heightCm: PACKAGE_LIMITS.maxHeightCm
        };

        if (limits[key] && numValue > limits[key]) {
            errors[`packageData.${key}`] = true;
        }
    });

    // Check same city
    const originCityName = getDisplayName(origin.municipality).toLowerCase();
    const destinationCityName = getDisplayName(destination.municipality).toLowerCase();

    if (originCityName && destinationCityName && originCityName === destinationCityName) {
        errors['destination.municipality'] = true;
    }

    return errors;
}