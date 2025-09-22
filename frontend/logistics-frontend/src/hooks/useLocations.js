import { useState, useEffect } from 'react';
import { useApiRequest } from './useApiRequest';
import { getProvinces, getDepartments, getMunicipalities } from '../services/locationService';

export function useLocations(initialProvince = null, initialDepartment = null) {
    const [provinces, setProvinces] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [municipalities, setMunicipalities] = useState([]);

    const [selectedProvince, setSelectedProvince] = useState(initialProvince);
    const [selectedDepartment, setSelectedDepartment] = useState(initialDepartment);
    const [isInitialLoad, setIsInitialLoad] = useState(true);

    // Provinces:
    const { data: provincesData, error: provincesError, makeRequest: fetchProvinces } = useApiRequest();
    // Departments:
    const { data: departmentsData, loading: loadingDepartments, error: departmentsError, makeRequest: fetchDepartments } = useApiRequest();
    // Municipalities:
    const { data: municipalitiesData, loading: loadingMunicipalities, error: municipalitiesError, makeRequest: fetchMunicipalities } = useApiRequest();

    // Initial loading of provinces
    useEffect(() => {
        const controller = new AbortController();
        const loadProvinces = async () => {
            try {
                await fetchProvinces(() => getProvinces({ signal: controller.signal }));
                setIsInitialLoad(false);
            } catch (err) {
                if (err.name !== 'AbortError') {
                    console.error("Error loading provinces:", err);
                    setIsInitialLoad(false);
                }
            }
        };
        loadProvinces();
        return () => controller.abort();
    }, [fetchProvinces]);

    // Load departments on province change
    useEffect(() => {
        if (!selectedProvince) {
            setDepartments([]);
            setMunicipalities([]);
            return;
        }
        const controller = new AbortController();
        const provinceId = typeof selectedProvince === 'object' ? selectedProvince.id : selectedProvince;
        fetchDepartments(() => getDepartments(provinceId, { signal: controller.signal }))
            .catch(err => err.name !== 'AbortError' && console.error("Error loading departments:", err));
        return () => controller.abort();
    }, [selectedProvince, fetchDepartments]);

    // Load municipalities on department change
    useEffect(() => {
        if (!selectedDepartment) {
            setMunicipalities([]);
            return;
        }

        const controller = new AbortController();
        const provinceId = typeof selectedProvince === 'object' ? selectedProvince.id : selectedProvince;
        const departmentId = typeof selectedDepartment === 'object' ? selectedDepartment.id : selectedDepartment;

        fetchMunicipalities(() => getMunicipalities(provinceId, departmentId, { signal: controller.signal }))
            .catch(err => err.name !== 'AbortError' && console.error("Error loading municipalities:", err));

        return () => controller.abort();
    }, [selectedProvince, selectedDepartment, fetchMunicipalities]);

    // Update states
    useEffect(() => { if (provincesData) setProvinces(provincesData); }, [provincesData]);
    useEffect(() => { if (departmentsData) setDepartments(departmentsData); }, [departmentsData]);
    useEffect(() => { if (municipalitiesData) setMunicipalities(municipalitiesData); }, [municipalitiesData]);

    return {
        provinces,
        departments,
        municipalities,
        loadingProvinces: isInitialLoad,
        loadingDepartments,
        loadingMunicipalities,
        provincesError,
        departmentsError,
        municipalitiesError,
        selectedProvince,
        selectedDepartment,
        setSelectedProvince,
        setSelectedDepartment
    };
}