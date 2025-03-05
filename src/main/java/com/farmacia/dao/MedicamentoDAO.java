package com.farmacia.dao;

import com.farmacia.model.Medicamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {
    private Connection conexao;

    public MedicamentoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Método para cadastrar um novo medicamento com objeto Medicamento
    public void cadastrarMedicamento(Medicamento medicamento) {
        String sql = "INSERT INTO Medicamento (nome, quantidade, preco) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, medicamento.getNome());
            stmt.setInt(2, medicamento.getQuantidade());
            stmt.setDouble(3, medicamento.getPreco());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método sobrecarregado para cadastrar medicamento diretamente com parâmetros
    public void cadastrarMedicamento(String nome, String lote, double preco) {
        String sql = "INSERT INTO Medicamento (nome, lote, preco) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, lote);
            stmt.setDouble(3, preco);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para listar todos os medicamentos como objetos Medicamento
    public List<Medicamento> listarMedicamentos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        String sql = "SELECT id, nome, quantidade, preco FROM Medicamento";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicamento medicamento = new Medicamento(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco")
                );
                medicamentos.add(medicamento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return medicamentos;
    }
    
    // Método para buscar um medicamento por seu ID
    public Medicamento buscarMedicamentoPorId(int medicamentoId) {
        Medicamento medicamento = null;
        String sql = "SELECT id, nome, quantidade, preco FROM Medicamento WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, medicamentoId);  // Define o parâmetro do ID do medicamento
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Cria um objeto Medicamento a partir dos dados retornados
                    medicamento = new Medicamento(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getInt("quantidade"),
                            rs.getDouble("preco")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return medicamento;  // Retorna o medicamento encontrado ou null caso não exista
    }
    
    // Método para atualizar os dados de um medicamento
    // Método para atualizar os dados de um medicamento
    public void atualizarMedicamento(Medicamento medicamento) {
        String sql = "UPDATE Medicamento SET nome = ?, quantidade = ?, preco = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, medicamento.getNome());
            stmt.setInt(2, medicamento.getQuantidade());
            stmt.setDouble(3, medicamento.getPreco());
            stmt.setInt(4, medicamento.getId());  // Define o ID do medicamento

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Medicamento atualizado com sucesso!");
            } else {
                System.out.println("Nenhum medicamento encontrado para atualizar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    
}
