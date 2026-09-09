import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import MeseroView from './views/MeseroView';
import CocinaView from './views/CocinaView';

function App() {
  return (
    <BrowserRouter>
      <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
        <nav style={{ marginBottom: '20px', padding: '10px', background: '#333', color: 'white', borderRadius: '8px' }}>
          <h2>Sistema TPS - Restaurante</h2>
          <div style={{ display: 'flex', gap: '15px' }}>
            <Link to="/mesero" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>👨‍🍳 Vista Mesero</Link>
            <Link to="/cocina" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>🍳 Vista Cocina</Link>
          </div>
        </nav>

        <Routes>
          <Route path="/mesero" element={<MeseroView />} />
          <Route path="/cocina" element={<CocinaView />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;