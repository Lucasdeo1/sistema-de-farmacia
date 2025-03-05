package com.farmacia.dao;

import com.farmacia.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private Connection conexao;
    
    // Construtor padrão que obtém a conexão automaticamente
    public ClienteDAO() {
        try {
            this.conexao = Conexao.getConexao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Construtor alternativo para passar uma conexão específica
    public ClienteDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, endereco FROM Cliente";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("endereco")
                );
                clientes.add(cliente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientes;
    }

    // Método para cadastrar um novo cliente
    public void cadastrarCliente(String nome, String cpf, String endereco) {
        String sql = "INSERT INTO Cliente (nome, cpf, endereco) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, endereco);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar um cliente existente
    public void atualizarCliente(int id, String nome, String cpf, String endereco) {
        String sql = "UPDATE Cliente SET nome = ?, cpf = ?, endereco = ? WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, endereco);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para remover um cliente pelo ID
    public void removerCliente(int id) {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
