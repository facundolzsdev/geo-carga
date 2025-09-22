import logo from '../../assets/app-logo.png';

export function Header() {
  return (
    <header className="container-lg mx-auto text-center mb-1 mt-1">
      <img
        src={logo}
        alt="Logística"
        className="img-fluid"
        style={{ maxHeight: '185px' }} />
    </header>
  );
}