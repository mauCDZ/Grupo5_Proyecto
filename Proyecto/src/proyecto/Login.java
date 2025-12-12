/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Login {

    ColaA colaA = new ColaA();
    ColaB colaB = new ColaB();
    ColaP colaP = new ColaP();

    private String nombreUsuario = "admin";
    private String contraseña = "1234";

    public void MenuLogin() {

        JOptionPane.showMessageDialog(
                null,
                "Bienvenido al Sistema de Atención.\nIngrese sus credenciales.",
                "Login",
                JOptionPane.INFORMATION_MESSAGE
        );

        boolean accesoConcedido = false;

        while (!accesoConcedido) {

            String username = JOptionPane.showInputDialog(null, "Usuario:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (username == null) return;

            String usercont = JOptionPane.showInputDialog(null, "Contraseña:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (usercont == null) return;

            if (username.equals(nombreUsuario) && usercont.equals(contraseña)) {
                JOptionPane.showMessageDialog(null, "Acceso concedido.");
                accesoConcedido = true;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Usuario o contraseña incorrectos. Inténtelo de nuevo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        String opcion = "";

        while (!opcion.equals("4")) {

            opcion = JOptionPane.showInputDialog(
                    null,
                    "Seleccione una opción:\n\n"
                            + "1. Registrar un cliente\n"
                            + "2. Atender un cliente\n"
                            + "3. Ver reportes\n"
                            + "4. Salir\n"
                            + "5. Ver tipo de cambio del dolar",
                    "Menu Principal",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == null) return;

            switch (opcion) {
    case "1":
        registrarCliente();
        break;

    case "2":
        atenderCliente();
        break;

    case "3":
        mostrarReportes();
        break;

    case "4":
        JOptionPane.showMessageDialog(null, "Saliendo del sistema.");
        break;

    case "5":
        mostrarTipoCambio();
        break;

    default:
        JOptionPane.showMessageDialog(null, "Opción inválida.");
        break;
}

        }
    }

    private void registrarCliente() {

        try {
            String nombre = JOptionPane.showInputDialog("Nombre del cliente:");
            if (nombre == null) return;

            String id = JOptionPane.showInputDialog("ID del cliente:");
            if (id == null) return;

            int edad = Integer.parseInt(JOptionPane.showInputDialog("Edad del cliente:"));

            String tramite = JOptionPane.showInputDialog("Trámite:");
            if (tramite == null) return;

            String tipoC = JOptionPane.showInputDialog("Tipo de cliente (P, A, B):");
            if (tipoC == null) return;

            tipoC = tipoC.toUpperCase();

            TipoCliente tipo;

            switch (tipoC) {
                case "P": 
                    tipo = TipoCliente.P;
                    colaP.asignarP(nombre, id, edad, tramite, tipo);
                    guardarClienteEnArchivo("clientesP.txt", nombre, id, edad, tramite, tipo);
                    break;

                case "A":
                    tipo = TipoCliente.A; 
                    colaA.asignarA(nombre, id, edad, tramite, tipo); 
                    guardarClienteEnArchivo("clientesA.txt", nombre, id, edad, tramite, tipo);
                break;

                case "B":
                    tipo = TipoCliente.B;
                    colaB.asignarB(nombre, id, edad, tramite, tipo);
                    guardarClienteEnArchivo("clientesB.txt", nombre, id, edad, tramite, tipo);
                    break; 
             

                default:
                    JOptionPane.showMessageDialog(null, "Tipo de cliente inválido.");
                    break;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar cliente. Verifique los datos.");
        }
    }

    private void guardarAtendido(String archivo, Dato cliente, long tiempo) {
    try (FileWriter writer = new FileWriter(archivo, true)) {
        writer.write(
                cliente.getNombre() + "," +
                cliente.getId() + "," +
                cliente.getEdad() + "," +
                cliente.getTramite() + "," +
                cliente.getTipo() + "," +
                tiempo + "\n"
        );
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
    private void atenderCliente() {

        String colaStr = JOptionPane.showInputDialog(
                "Seleccione la cola a atender:\nP - Preferencial\nA - Adulto mayor\nB - Regular"
        );

        if (colaStr == null) return;

        colaStr = colaStr.toUpperCase();

        switch (colaStr) {

case "P":
    cargarClientesDesdeArchivo(colaP, "clientesP.txt");

    if (!colaP.esVacia()) {
        long inicio = System.currentTimeMillis();
        
        Dato atendido = colaP.getPrimDato();
        colaP.TiqueteAtendidoP();
        eliminarClienteDelArchivo("clientesP.txt", atendido);

        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio) / 1000;

        guardarAtendido("atendidosP.txt", atendido, tiempo);
    } else {
        JOptionPane.showMessageDialog(null, "No hay clientes en la cola P.");
    }
    break;


case "A":
    cargarClientesDesdeArchivo(colaA, "clientesA.txt");

    if (!colaA.esVacia()) {
        long inicio = System.currentTimeMillis();
        
        Dato atendido = colaA.getPrimDato();
        colaA.TiqueteAtendidoA();
        eliminarClienteDelArchivo("clientesA.txt", atendido);

        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio) / 1000;

        guardarAtendido("atendidosA.txt", atendido, tiempo);
    } else {
        JOptionPane.showMessageDialog(null, "No hay clientes en la cola A.");
    }
    break;


            case "B":
    cargarClientesDesdeArchivo(colaB, "clientesB.txt");

    if (!colaB.esVacia()) {
        long inicio = System.currentTimeMillis();
        
        Dato atendido = colaB.getPrimDato();
        colaB.TiqueteAtendidoB();
        eliminarClienteDelArchivo("clientesB.txt", atendido);

        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio) / 1000;

        guardarAtendido("atendidosB.txt", atendido, tiempo);
    } else {
        JOptionPane.showMessageDialog(null, "No hay clientes en la cola B.");
    }
    break;


            default:
                JOptionPane.showMessageDialog(null, "Cola inválida.");
        }
    }

    //      MÉTODOS DE ARCHIVO

    private void guardarClienteEnArchivo(String archivoNombre, String nombre, String id, int edad, String tramite, TipoCliente tipo) {
        try (FileWriter writer = new FileWriter(archivoNombre, true)) {
            writer.write(nombre + "," + id + "," + edad + "," + tramite + "," + tipo + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarClientesDesdeArchivo(ColaP cola, String archivoNombre) {
        File archivo = new File(archivoNombre);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(",");

                cola.asignarP(
                        partes[0],
                        partes[1],
                        Integer.parseInt(partes[2]),
                        partes[3],
                        TipoCliente.valueOf(partes[4])
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarClientesDesdeArchivo(ColaA cola, String archivoNombre) {
        File archivo = new File(archivoNombre);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(",");

                cola.asignarA(
                        partes[0],
                        partes[1],
                        Integer.parseInt(partes[2]),
                        partes[3],
                        TipoCliente.valueOf(partes[4])
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarClientesDesdeArchivo(ColaB cola, String archivoNombre) {
        File archivo = new File(archivoNombre);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(",");

                cola.asignarB(
                        partes[0],
                        partes[1],
                        Integer.parseInt(partes[2]),
                        partes[3],
                        TipoCliente.valueOf(partes[4])
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarClienteDelArchivo(String archivoNombre, Dato cliente) {

        File archivo = new File(archivoNombre);
        if (!archivo.exists()) return;

        List<String> lineas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (!linea.startsWith(cliente.getNombre() + ",")) {
                    lineas.add(linea);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(archivo, false)) {

            for (String l : lineas) writer.write(l + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //      REPORTES

    private void mostrarReportes() {

        Reportes rep = new Reportes(colaA, colaB, colaP);

        String mensaje = "";
        mensaje += rep.cajaConMasAtendidos();
        mensaje += rep.totalAtendidos();
        mensaje += rep.mejorTiempoPromedio();
        mensaje += rep.promedioGeneral();

        JOptionPane.showMessageDialog(null, mensaje, "Reportes", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarTipoCambio() {
    // Valores de ejemplo (puedes modificarlos cuando quieras)
    double compra = 530.00;
    double venta = 540.00;

    JOptionPane.showMessageDialog(
            null,
            " *Tipo de Cambio del Dólar (BCCR)*\n\n" +
            "• Compra: ₡" + compra + "\n" +
            "• Venta: ₡" + venta + "\n",
            "Tipo de Cambio",
            JOptionPane.INFORMATION_MESSAGE
    );
}

}
