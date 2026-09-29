package dao;

import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.TipoPedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void guardar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, pedido.getDireccion());
            sentencia.setString(2, pedido.getTipo().name());
            sentencia.setString(3, pedido.getEstado().name());
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    pedido.setId(claves.getInt(1));
                }
            }
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedido ORDER BY id";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                pedidos.add(crearDesdeResultSet(resultado));
            }
        }

        return pedidos;
    }

    public List<Pedido> listarPendientes() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedido WHERE estado = ? ORDER BY id";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, EstadoPedido.PENDIENTE.name());

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    pedidos.add(crearDesdeResultSet(resultado));
                }
            }
        }

        return pedidos;
    }

    public void actualizarEstado(int idPedido, EstadoPedido estado) throws SQLException {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, estado.name());
            sentencia.setInt(2, idPedido);
            sentencia.executeUpdate();
        }
    }

    private Pedido crearDesdeResultSet(ResultSet resultado) throws SQLException {
        return new Pedido(
                resultado.getInt("id"),
                resultado.getString("direccion"),
                TipoPedido.valueOf(resultado.getString("tipo")),
                EstadoPedido.valueOf(resultado.getString("estado"))
        );
    }
}
