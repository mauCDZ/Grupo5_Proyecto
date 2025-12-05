/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.time.LocalDateTime;
import javax.swing.JOptionPane;

public class ColaP {
    private Nodo prim, ult;
    private int cantClientes = 0;
    LocalDateTime hora = LocalDateTime.now();
    
    public boolean esVacia() {
        return prim==null;
    }
    
    //Encolar
    public void asignarP(String nombre, String id, int edad, String tramite, TipoCliente tipo) {
        Nodo nuevo = new Nodo(new Dato(nombre, id, edad, tramite, tipo));
        
        if (esVacia()) {
            prim = ult = nuevo;
            JOptionPane.showMessageDialog(null, nuevo + " es su turno. Pase a la caja 1 para ser atendido");
        } else {
                if (nuevo.getSig() == null) {
                    ult.setSig(nuevo);
                    ult = nuevo;
                    JOptionPane.showMessageDialog(null, nuevo + " usted será atendido en la caja 1. \nHay " + cantClientes + " clientes adelante suyo.");
                }
            } 
        cantClientes += 1;
    }

    //Atender
    public void TiqueteAtendidoP() {
    if (esVacia()) {
        JOptionPane.showMessageDialog(null, "Error, clientes no encontrados");
    } else {

        Nodo atendido = prim;             // cliente que se atiende
        atendido.getDato().setHoraAtencion(LocalDateTime.now());

        JOptionPane.showMessageDialog(null,
                "Se está atendiendo a: " + atendido.getDato().getNombre());

        prim = prim.getSig();             // avanzar en la cola

        if (prim == null) {               // si quedó vacía
            ult = null;
        }

        cantClientes--;
    }
}
    
    @Override
    public String toString() {
        String r="COLA:\n";
        Nodo aux=prim;
        while(aux!=null){
            r+=aux+"\n"; 
            aux=aux.getSig();
        }
        return r;
    }
}
