import { useEffect, useState } from 'react';
import axios from 'axios';
import { Client } from '@stomp/stompjs';

export default function MeseroView() {
    const [mesas, setMesas] = useState<any[]>([]);
    const [menu, setMenu] = useState<any[]>([]);
    const [mesaSeleccionada, setMesaSeleccionada] = useState<number | null>(null);
    const [instrucciones, setInstrucciones] = useState('');
    const [platosSeleccionados, setPlatosSeleccionados] = useState<number[]>([]);

    const cargarDatos = () => {
        axios.get('http://localhost:8080/api/mesas').then(res => setMesas(res.data));
        axios.get('http://localhost:8080/api/platillos').then(res => setMenu(res.data));
    };

    useEffect(() => {
        cargarDatos();
        const stompClient = new Client({
            brokerURL: 'ws://localhost:8080/ws-restaurante/websocket',
            onConnect: () => {
                stompClient.subscribe('/topic/mesero', (msg) => alert('🔔 NOTIFICACIÓN:\n' + msg.body));
            }
        });
        stompClient.activate();
        return () => { stompClient.deactivate(); };
    }, []);

    const togglePlato = (id: number) => {
        setPlatosSeleccionados(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
    };

    const registrarPedido = () => {
        if (!mesaSeleccionada) return alert('Selecciona una mesa');
        if (platosSeleccionados.length === 0) return alert('Selecciona al menos un platillo');
        
        const nuevoPedido = {
            mesaId: mesaSeleccionada,
            instruccionesEspeciales: instrucciones,
            platillos: platosSeleccionados.map(id => ({ id })) // Enviamos el arreglo de objetos con ID
        };

        axios.post('http://localhost:8080/api/pedidos', nuevoPedido).then(() => {
            alert('Pedido registrado con éxito');
            setInstrucciones('');
            setPlatosSeleccionados([]);
            setMesaSeleccionada(null);
            cargarDatos();
        });
    };

    const cobrarMesa = (id: number) => {
        axios.put(`http://localhost:8080/api/mesas/${id}/liberar`).then(() => cargarDatos());
    };

    // Agrupar menú por categoría
    const categorias = ['Entrada', 'Plato Fuerte', 'Postre', 'Bebida'];

    return (
        <div>
            <h3>Mapeo de Mesas</h3>
            <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                {mesas.map(m => (
                    <div key={m.id} onClick={() => setMesaSeleccionada(m.id)}
                         style={{ padding: '20px', border: mesaSeleccionada === m.id ? '3px solid blue' : '1px solid gray', backgroundColor: m.estado === 'LIBRE' ? '#d4edda' : '#f8d7da', cursor: 'pointer', borderRadius: '8px' }}>
                        <h4>Mesa {m.numero}</h4>
                        <p>{m.estado}</p>
                        {m.estado === 'OCUPADA' && <button onClick={(e) => { e.stopPropagation(); cobrarMesa(m.id); }}>Cobrar Mesa</button>}
                    </div>
                ))}
            </div>

            {mesaSeleccionada && (
                <div style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px', background: '#f9f9f9' }}>
                    <h3>Registrar Pedido - Mesa {mesaSeleccionada}</h3>
                    
                    <div style={{ display: 'flex', gap: '20px', marginBottom: '20px', flexWrap: 'wrap' }}>
                        {categorias.map(cat => (
                            <div key={cat} style={{ flex: '1', minWidth: '200px' }}>
                                <h4 style={{ borderBottom: '2px solid black' }}>{cat}s</h4>
                                {menu.filter(p => p.categoria === cat).map(p => (
                                    <div key={p.id} style={{ marginBottom: '5px' }}>
                                        <label style={{ cursor: 'pointer' }}>
                                            <input type="checkbox" checked={platosSeleccionados.includes(p.id)} onChange={() => togglePlato(p.id)} />
                                            {' '}{p.nombre} - ${p.precio}
                                        </label>
                                    </div>
                                ))}
                            </div>
                        ))}
                    </div>

                    <textarea value={instrucciones} onChange={(e) => setInstrucciones(e.target.value)} placeholder='Instrucciones especiales libres (ej. Sin cebolla)' style={{ width: '100%', height: '60px', marginBottom: '10px' }} />
                    <button onClick={registrarPedido} style={{ padding: '10px 20px', background: 'blue', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>Enviar a Cocina</button>
                </div>
            )}
        </div>
    );
}