import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Header } from './components/layout/Header';
import { Footer } from './components/layout/Footer';
import { ScrollToTop } from './components/layout/ScrollToTop';
import { HomePage } from './pages/HomePage';
import { ResultsPage } from './pages/ResultsPage';
import { ROUTES } from './utils/constants/routes';

export function App() {
  return (
    <BrowserRouter>
      <ScrollToTop />
      <Header />
      <Routes>
        <Route path={ROUTES.HOME} element={<HomePage />} />
        <Route path={ROUTES.RESULTS} element={<ResultsPage />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}               