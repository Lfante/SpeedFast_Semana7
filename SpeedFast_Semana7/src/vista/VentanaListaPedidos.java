package vista;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class VentanaListaPedidos extends JFrame {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Dirección", "Tipo", "Estado"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public VentanaListaPedidos() {
        setTitle("SpeedFast - Pedidos almacenados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 430);
        setLocationRelativeTo(null);
        inicializarComponentes();
        cargarPedidos();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Pedidos almacenados en MySQL", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(24);
        tabla.setFillsViewportHeight(true);

        JButton botonRefrescar = new JButton("Refrescar");
        JButton botonCerrar = new JButton("Cerrar");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(botonRefrescar);
        acciones.add(botonCerrar);

        botonRefrescar.addActionListener(e -> cargarPedidos());
        botonCerrar.addActionListener(e -> dispose());

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panelPrincipal.add(acciones, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
    }

    private void cargarPedidos() {
        modeloTabla.setRowCount(0);

        try {
            for (Pedido pedido : pedidoDAO.listarTodos()) {
                modeloTabla.addRow(new Object[]{
                        pedido.getId(),
                        pedido.getDireccion(),
                        pedido.getTipo(),
                        pedido.getEstado()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron consultar los pedidos.\n\n" + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
