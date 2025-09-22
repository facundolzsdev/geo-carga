import { ShippingForm } from '../components/form/ShippingForm';
import { useShippingCalculator } from '../hooks/useShippingCalculator';

export function HomePage() {
    const { calculate, isLoading, error, clearError } = useShippingCalculator();

    return (
        <div className="container-lg mx-auto px-3">
            {error && (
                <div className="alert alert-danger alert-dismissible mt-3">
                    <div dangerouslySetInnerHTML={{ __html: error.replace(/\n/g, '<br>') }} />
                    <button type="button" className="btn-close" onClick={clearError}></button>
                </div>
            )}
            <ShippingForm onSubmit={calculate} isLoading={isLoading} />
        </div>
    );
}