import { useEffect, useState } from 'react';
import axios from 'axios';
import { Client } from '@stomp/stompjs';

export default function MeseroView() {
    const [mesas, setMesas] = useState<any[]>([]);
    const [menu, setMenu] = useState<any[]>([]);
    const [mesaSeleccionada, setMesaSeleccionada] = useState<number | null>(null);
    const [instrucciones, setInstrucciones] = useState('');
    const [cantidades, setCantidades] = useState<{ [key: number]: number }>({});

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

    const cambiarCantidad = (id: number, cantidad: number) => {
        if (cantidad <= 0) {
            const copia = { ...cantidades };
            delete copia[id];
            setCantidades(copia);
        } else {
            setCantidades({ ...cantidades, [id]: cantidad });
        }
    };

    const registrarPedido = () => {
        if (!mesaSeleccionada) return alert('Selecciona una mesa');
        
        const platosArray = Object.entries(cantidades).map(([id, cantidad]) => ({
            id: Number(id),
            cantidad: cantidad
        }));

        if (platosArray.length === 0) return alert('Selecciona al menos un platillo y su cantidad');
        
        const nuevoPedido = {
            mesaId: mesaSeleccionada,
            instruccionesEspeciales: instrucciones,
            platillos: platosArray
        };

        axios.post('http://localhost:8080/api/pedidos', nuevoPedido).then(() => {
            alert('Pedido registrado con éxito');
            setInstrucciones('');
            setCantidades({});
            setMesaSeleccionada(null);
            cargarDatos();
        });
    };

    const cobrarMesa = (id: number) => {
        axios.put(`http://localhost:8080/api/mesas/${id}/liberar`).then(() => cargarDatos());
    };

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
                                <div>
                                    {menu.filter(p => p.categoria === cat).map(p => (
                                        <div key={p.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                                            <span style={{ fontSize: '13px' }}>{p.nombre} (${p.precio})</span>
                                            <input 
                                                type="number" 
                                                min="0" 
                                                value={cantidades[p.id] !== undefined ? cantidades[p.id] : ''} 
                                                onChange={(e) => {
                                                    const val = e.target.value === '' ? 0 : parseInt(e.target.value);
                                                    cambiarCantidad(p.id, isNaN(val) ? 0 : val);
                                                }} 
                                                style={{ 
                                                    width: '45px', 
                                                    padding: '2px', 
                                                    textAlign: 'center', 
                                                    backgroundColor: 'white', 
                                                    color: 'black', 
                                                    border: '1px solid #ccc', 
                                                    borderRadius: '4px' 
                                                }}
                                            />
                                        </div>
                                    ))}
                                </div>
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