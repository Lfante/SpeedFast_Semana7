package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class VentanaAsignarEntrega extends JFrame {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();

    private JComboBox<Pedido> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;

    public VentanaAsignarEntrega() {
        setTitle("SpeedFast - Asignar entrega");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(590, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializarComponentes();
        cargarDatos();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 15));
        panelPrincipal.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel(
                "Asignar repartidor / Iniciar entrega",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("SansSerif", Font.BOLD, 19));

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 12));

        formulario.add(new JLabel("Pedido pendiente:"));
        comboPedidos = new JComboBox<>();
        formulario.add(comboPedidos);

        formulario.add(new JLabel("Repartidor:"));
        comboRepartidores = new JComboBox<>();
        formulario.add(comboRepartidores);

        JButton botonIniciar = new JButton("Iniciar entrega");
        JButton botonRefrescar = new JButton("Refrescar");
        JButton botonCerrar = new JButton("Cerrar");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        acciones.add(botonIniciar);
        acciones.add(botonRefrescar);
        acciones.add(botonCerrar);

        botonIniciar.addActionListener(e -> registrarEntrega());
        botonRefrescar.addActionListener(e -> cargarDatos());
        botonCerrar.addActionListener(e -> dispose());

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(formulario, BorderLayout.CENTER);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void cargarDatos() {
        comboPedidos.removeAllItems();
        comboRepartidores.removeAllItems();

        try {
            for (Pedido pedido : pedidoDAO.listarPendientes()) {
                comboPedidos.addItem(pedido);
            }

            for (Repartidor repartidor : repartidorDAO.listarTodos()) {
                comboRepartidores.addItem(repartidor);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron cargar los datos.\n\n" + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void registrarEntrega() {
        Pedido pedido = (Pedido) comboPedidos.getSelectedItem();
        Repartidor repartidor = (Repartidor) comboRepartidores.getSelectedItem();

        if (pedido == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No existen pedidos pendientes para asignar.",
                    "Sin pedidos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (repartidor == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes registrar o seleccionar un repartidor.",
                    "Sin repartidor",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Entrega entrega = new Entrega(
                pedido.getId(),
                repartidor.getId(),
                LocalDate.now(),
                LocalTime.now().withNano(0)
        );

        try {
            entregaDAO.guardar(entrega);
            pedidoDAO.actualizarEstado(pedido.getId(), EstadoPedido.EN_REPARTO);

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega #" + entrega.getId()
                            + " registrada correctamente.\n"
                            + "Pedido #" + pedido.getId()
                            + " asignado a " + repartidor.getNombre() + ".",
                    "Entrega iniciada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarDatos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar la entrega.\n\n" + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
