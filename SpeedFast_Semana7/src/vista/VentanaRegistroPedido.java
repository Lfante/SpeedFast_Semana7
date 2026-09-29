package vista;

import dao.PedidoDAO;
import modelo.Pedido;
import modelo.TipoPedido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class VentanaRegistroPedido extends JFrame {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private JTextField campoDireccion;
    private JComboBox<TipoPedido> comboTipo;

    public VentanaRegistroPedido() {
        setTitle("SpeedFast - Registrar pedido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 280);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("Registrar pedido", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 12));

        formulario.add(new JLabel("ID:"));
        JTextField campoId = new JTextField("Automático");
        campoId.setEditable(false);
        formulario.add(campoId);

        formulario.add(new JLabel("Dirección:"));
        campoDireccion = new JTextField();
        formulario.add(campoDireccion);

        formulario.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(TipoPedido.values());
        formulario.add(comboTipo);

        JButton botonGuardar = new JButton("Guardar en MySQL");
        JButton botonCerrar = new JButton("Cerrar");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        acciones.add(botonGuardar);
        acciones.add(botonCerrar);

        botonGuardar.addActionListener(e -> guardarPedido());
        botonCerrar.addActionListener(e -> dispose());

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(formulario, BorderLayout.CENTER);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void guardarPedido() {
        String direccion = campoDireccion.getText().trim();
        TipoPedido tipo = (TipoPedido) comboTipo.getSelectedItem();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar una dirección.",
                    "Dato requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Pedido pedido = new Pedido(direccion, tipo);

        try {
            pedidoDAO.guardar(pedido);

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido #" + pedido.getId() + " guardado correctamente en MySQL.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            campoDireccion.setText("");
            comboTipo.setSelectedIndex(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el pedido.\n\n" + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
