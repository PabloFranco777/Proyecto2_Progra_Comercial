import { useEffect, useState } from 'react';
import axios from 'axios';
import { Client } from '@stomp/stompjs';

export default function CocinaView() {
    const [pedidos, setPedidos] = useState<any[]>([]);

    const cargarPedidos = () => {
        axios.get('http://localhost:8080/api/pedidos/activos').then(res => setPedidos(res.data));
    };

    useEffect(() => {
        cargarPedidos();

        // Conexión WebSocket para recibir nuevos pedidos en tiempo real
        const stompClient = new Client({
            brokerURL: 'ws://localhost:8080/ws-restaurante/websocket',
            onConnect: () => {
                console.log('Conectado a WebSockets (Cocina)');
                stompClient.subscribe('/topic/cocina', () => {
                    // Al recibir un evento, recargamos la lista automáticamente
                    cargarPedidos();
                });
            }
        });
        stompClient.activate();
        return () => { stompClient.deactivate(); };
    }, []);

    const marcarListo = (id: number) => {
        axios.put(`http://localhost:8080/api/pedidos/${id}/estado?estado=LISTO`).then(() => {
            cargarPedidos();
        });
    };

    return (
        <div>
            <h3>Pantalla de Cocina (Tiempo Real)</h3>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '15px' }}>
                {pedidos.length === 0 ? <p>No hay pedidos activos.</p> : null}
                {pedidos.map(p => (
                    <div key={p.id} style={{ padding: '15px', border: '2px solid orange', borderRadius: '8px', backgroundColor: '#fff3cd' }}>
                        <h4>Pedido #{p.id} - Mesa {p.mesaId}</h4>
                        <p><strong>Estado:</strong> {p.estado}</p>
                        
                        <p><strong>Platillos del Pedido:</strong></p>
                        <ul style={{ margin: '5px 0', paddingLeft: '20px' }}>
                            {p.platillos?.map((pl: any) => (
                                <li key={pl.id}><strong>{pl.nombre}</strong> <small>({pl.categoria})</small></li>
                            ))}
                        </ul>

                        <p><strong>Instrucciones:</strong> {p.instruccionesEspeciales || 'Ninguna'}</p>
                        
                        {p.estado === 'PENDIENTE' && (
                            <button onClick={() => marcarListo(p.id)} style={{ padding: '10px', background: 'green', color: 'white', marginTop: '10px', cursor: 'pointer' }}>
                                ✔️ Marcar como LISTO
                            </button>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}