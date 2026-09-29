package vista;

import dao.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFast - Gestión con MySQL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 430);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 30, 25, 30));

        JLabel titulo = new JLabel("SpeedFast - Gestión de entregas", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitulo = new JLabel(
                "Persistencia de datos con JDBC y MySQL",
                SwingConstants.CENTER
        );

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 5));
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JButton botonRegistrarPedido = new JButton("Registrar pedido");
        JButton botonRegistrarRepartidor = new JButton("Registrar repartidor");
        JButton botonListarPedidos = new JButton("Listar pedidos");
        JButton botonAsignar = new JButton("Asignar repartidor / Iniciar entrega");
        JButton botonConexion = new JButton("Probar conexión a MySQL");

        JPanel botones = new JPanel(new GridLayout(5, 1, 10, 10));
        botones.setBorder(new EmptyBorder(10, 45, 0, 45));
        botones.add(botonRegistrarPedido);
        botones.add(botonRegistrarRepartidor);
        botones.add(botonListarPedidos);
        botones.add(botonAsignar);
        botones.add(botonConexion);

        botonRegistrarPedido.addActionListener(e ->
                new VentanaRegistroPedido().setVisible(true)
        );

        botonRegistrarRepartidor.addActionListener(e ->
                new VentanaRegistroRepartidor().setVisible(true)
        );

        botonListarPedidos.addActionListener(e ->
                new VentanaListaPedidos().setVisible(true)
        );

        botonAsignar.addActionListener(e ->
                new VentanaAsignarEntrega().setVisible(true)
        );

        botonConexion.addActionListener(e -> probarConexion());

        panelPrincipal.add(encabezado, BorderLayout.NORTH);
        panelPrincipal.add(botones, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private void probarConexion() {
        try (Connection conexion = ConexionBD.conectar()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Conexión a MySQL realizada correctamente.",
                    "Conexión exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible conectar con MySQL.\n\n" + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
