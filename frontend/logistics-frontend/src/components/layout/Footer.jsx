export function Footer() {
  return (
    <footer className="container-lg mx-auto text-center mt-4 py-3 border-top">
      <div className="row">
        <div className="col-12">
          <p className="mb-1 small text-secondary">
            GEO-CARGA • Cotización de envíos y rutas en Argentina
          </p>
          <p className="mb-1 small">
            Desarrollado por{" "}
            <a
              href="https://github.com/facundolzsdev"
              target="_blank"
              rel="noopener noreferrer"
              className="text-decoration-none fw-semibold">
              Facundo Lozano
            </a>
          </p>
          <p className="mb-0 text-muted small">© 2025</p>
        </div>
      </div>
    </footer>
  );
}
