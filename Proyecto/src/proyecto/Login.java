/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Login {
    ColaA colaA = new ColaA();
    ColaB colaB = new ColaB();
    ColaP colaP = new ColaP();

    private String nombreUsuario = "admin";
    private String contraseña = "1234";

    private final File archivoClientes = new File("clientes.txt");

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

        // Menu principal
        String opcion = "";
        while (!opcion.equals("3")) {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "Seleccione una opción:\n\n"
                    + "1. Registrar un cliente\n"
                    + "2. Atender un cliente\n"
                    + "3. Salir",
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
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema.");
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
            String id = JOptionPane.showInputDialog("ID del cliente:");
            int edad = Integer.parseInt(JOptionPane.showInputDialog("Edad del cliente:"));
            String tramite = JOptionPane.showInputDialog("Trámite:");
            String tipoC = JOptionPane.showInputDialog("Tipo de cliente (P, A, B):");
            if (tipoC == null) return;
            tipoC = tipoC.toUpperCase();

            TipoCliente tipo;
            if (tipoC.equals("P")) tipo = TipoCliente.P;
            else if (tipoC.equals("A")) tipo = TipoCliente.A;
            else if (tipoC.equals("B")) tipo = TipoCliente.B;
            else {
                JOptionPane.showMessageDialog(null, "Tipo de cliente inválido.");
                return;
            }

            Dato cliente = new Dato(nombre, id, edad, tramite, tipo);

            // Guardar en archivo
            guardarClienteArchivo(cliente);

            // Encolar en la cola correspondiente
            if (tipo == TipoCliente.P) colaP.asignarP(nombre, id, edad, tramite, tipo);
            else if (tipo == TipoCliente.A) colaA.asignarA(nombre, id, edad, tramite, tipo);
            else colaB.asignarB(nombre, id, edad, tramite, tipo);

            JOptionPane.showMessageDialog(null, "Cliente registrado correctamente.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Edad inválida.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar cliente.");
        }
    }

    private void atenderCliente() {
        try {
            String cola = JOptionPane.showInputDialog(
                    "Seleccione la cola a atender:\nP - Preferencial\nA - Adulto mayor\nB - Regular"
            );
            if (cola == null) return;
            cola = cola.toUpperCase();

            Dato atendido = null;

            if (cola.equals("P")) atendido = atenderArchivoYCola(colaP);
            else if (cola.equals("A")) atendido = atenderArchivoYCola(colaA);
            else if (cola.equals("B")) atendido = atenderArchivoYCola(colaB);
            else {
                JOptionPane.showMessageDialog(null, "Cola inválida.");
                return;
            }

            if (atendido != null) {
                JOptionPane.showMessageDialog(null,
                        "Se está atendiendo a: " + atendido.getNombre());
            } else {
                JOptionPane.showMessageDialog(null, "No hay clientes en la cola seleccionada.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al atender cliente.");
        }
    }

    private void guardarClienteArchivo(Dato cliente) throws IOException {
        try (FileWriter writer = new FileWriter(archivoClientes, true)) {
            writer.write(cliente.getNombre() + "," + cliente.getId() + "," + cliente.getEdad()
                    + "," + cliente.getTramite() + "," + cliente.getTipo() + "\n");
        }
    }

    private Dato atenderArchivoYCola(Object cola) throws IOException {
        List<String> lineas = new ArrayList<>();
        Dato atendido = null;

        if (!archivoClientes.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(archivoClientes))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        }

        for (int i = 0; i < lineas.size(); i++) {
            String[] partes = lineas.get(i).split(",");
            TipoCliente tipo = TipoCliente.valueOf(partes[4]);
            if ((cola instanceof ColaP && tipo == TipoCliente.P) ||
                (cola instanceof ColaA && tipo == TipoCliente.A) ||
                (cola instanceof ColaB && tipo == TipoCliente.B)) {

                atendido = new Dato(partes[0], partes[1], Integer.parseInt(partes[2]), partes[3], tipo);
                lineas.remove(i);
                break;
            }
        }

        // Reescribir archivo sin el cliente atendido
        try (FileWriter writer = new FileWriter(archivoClientes)) {
            for (String l : lineas) {
                writer.write(l + "\n");
            }
        }

        // Atender en la cola correspondiente
        if (cola instanceof ColaP) colaP.TiqueteAtendidoP();
        else if (cola instanceof ColaA) colaA.TiqueteAtendidoA();
        else if (cola instanceof ColaB) colaB.TiqueteAtendidoB();

        return atendido;
    }
}
