package com.farmacia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.farmacia.model.Medicamento;

public class VendaDAO {
    private Connection conexao;

    public VendaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Método para registrar uma venda e retornar o ID gerado
    public int registrarVenda(int clienteId, double valorTotal, String status) {
        String sql = "INSERT INTO Venda (cliente_id, valor_total, status) VALUES (?, ?, ?)";
        int vendaId = 0;
        try (PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, clienteId);
            stmt.setDouble(2, valorTotal);
            stmt.setString(3, status);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                vendaId = rs.getInt(1); // Recupera o ID da venda
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendaId;
    }

    // Método para registrar itens da venda (medicamentos)
    public void registrarItensVenda(int vendaId, List<Medicamento> medicamentos) {
        String sql = "INSERT INTO Venda_Medicamento (venda_id, medicamento_id, quantidade) VALUES (?, ?, ?)";
        try {
            // Iniciar transação
            conexao.setAutoCommit(false);

            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                for (Medicamento medicamento : medicamentos) {
                    // Verificar dados do medicamento antes de inserir
                    if (medicamento.getId() <= 0 || medicamento.getQuantidade() <= 0) {
                        System.out.println("Dados inválidos para medicamento ID: " + medicamento.getId());
                        continue; // Pula para o próximo medicamento
                    }

                    // Imprimir dados para verificação
                    System.out.println("Registrando item de venda - Medicamento ID: " + medicamento.getId() +
                            ", Quantidade: " + medicamento.getQuantidade());

                    stmt.setInt(1, vendaId);
                    stmt.setInt(2, medicamento.getId());
                    stmt.setInt(3, medicamento.getQuantidade());
                    int rowsAffected = stmt.executeUpdate();

                    // Verificar se a inserção foi bem-sucedida
                    if (rowsAffected > 0) {
                        System.out.println("Item de venda registrado com sucesso!");
                    } else {
                        System.out.println("Falha ao registrar item de venda.");
                    }

                    // Atualizar o estoque após a venda
                    atualizarEstoque(medicamento.getId(), medicamento.getQuantidade());
                }
            }

            // Commit da transação
            conexao.commit();
            System.out.println("Itens da venda e estoque atualizados com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback em caso de erro
                conexao.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                // Restaurar o autocommit
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para atualizar o estoque de medicamentos
    private void atualizarEstoque(int medicamentoId, int quantidadeVendida) {
        String sql = "UPDATE Medicamento SET quantidade = quantidade - ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, quantidadeVendida);
            stmt.setInt(2, medicamentoId);
            int rowsAffected = stmt.executeUpdate();

            // Verificar se a atualização foi bem-sucedida
            if (rowsAffected > 0) {
                System.out.println("Estoque atualizado para o medicamento ID: " + medicamentoId);
            } else {
                System.out.println("Falha ao atualizar estoque para medicamento ID: " + medicamentoId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Listar vendas existentes
    public List<String> listarVendas() {
        List<String> vendas = new ArrayList<>();
        String sql = "SELECT id, valor_total, status FROM Venda";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vendas.add("ID: " + rs.getInt("id") + ", Valor: " + rs.getDouble("valor_total") + ", Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendas;
    }
}
