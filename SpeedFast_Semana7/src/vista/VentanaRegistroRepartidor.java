package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class VentanaRegistroRepartidor extends JFrame {

    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private JTextField campoNombre;

    public VentanaRegistroRepartidor() {
        setTitle("SpeedFast - Registrar repartidor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(430, 220);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("Registrar repartidor", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel formulario = new JPanel(new GridLayout(1, 2, 10, 10));
        formulario.add(new JLabel("Nombre:"));
        campoNombre = new JTextField();
        formulario.add(campoNombre);

        JButton botonGuardar = new JButton("Guardar en MySQL");
        JButton botonCerrar = new JButton("Cerrar");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        acciones.add(botonGuardar);
        acciones.add(botonCerrar);

        botonGuardar.addActionListener(e -> guardarRepartidor());
        botonCerrar.addActionListener(e -> dispose());

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(formulario, BorderLayout.CENTER);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void guardarRepartidor() {
        String nombre = campoNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar el nombre del repartidor.",
                    "Dato requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Repartidor repartidor = new Repartidor(nombre);

        try {
            repartidorDAO.guardar(repartidor);

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor #" + repartidor.getId() + " guardado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            campoNombre.setText("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el repartidor.\n\n" + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
