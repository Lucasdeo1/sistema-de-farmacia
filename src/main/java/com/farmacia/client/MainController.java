package com.farmacia.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import com.farmacia.dao.ClienteDAO;
import com.farmacia.dao.Conexao;  // Importando a classe de conexão
import com.farmacia.model.Cliente;
import com.farmacia.model.Medicamento;
import java.sql.Connection;

public class MainController {

    @FXML
    private Button listarClientesButton, cadastrarClienteButton, atualizarClienteButton, removerClienteButton;

    @FXML
    private TextField nomeField, cpfField, enderecoField, clienteIdField;

    @FXML
    private Label statusLabel;
    
    @FXML
    private TextField nomeMedicamentoField;
    @FXML
    private TextField loteMedicamentoField;
    @FXML
    private TextField precoMedicamentoField;
    @FXML
    private TableView<Medicamento> tableViewMedicamentos;
    @FXML
    private TableColumn<Medicamento, String> colNomeMedicamento;
    @FXML
    private TableColumn<Medicamento, String> colLoteMedicamento;
    @FXML
    private TableColumn<Medicamento, Double> colPrecoMedicamento;

    // Este é o método que será chamado ao clicar no botão "Cadastrar Medicamento"
    @FXML
    public void handleCadastrarMedicamento() {
        String nome = nomeMedicamentoField.getText();
        String lote = loteMedicamentoField.getText();
        double preco = Double.parseDouble(precoMedicamentoField.getText());

        // Criar um novo objeto Medicamento
        Medicamento medicamento = new Medicamento(nome, lote, preco);

        // Adicionar medicamento à lista da TableView
        tableViewMedicamentos.getItems().add(medicamento);

        // Limpar os campos após o cadastro
        nomeMedicamentoField.clear();
        loteMedicamentoField.clear();
        precoMedicamentoField.clear();
    }
    
    
    
    private Connection conexao;
    
    @FXML
    public void initialize() {
        try {
            conexao = Conexao.getConexao();
        } catch (Exception e) {
            statusLabel.setText("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML
    void listarClientes(ActionEvent event) {
        try {
            ClienteDAO clienteDAO = new ClienteDAO(conexao);  // Usando a conexão correta
            List<Cliente> clientes = clienteDAO.listarClientes();  // Agora List<Cliente>, não List<String>

            if (clientes.isEmpty()) {
                statusLabel.setText("Nenhum cliente encontrado.");
            } else {
                StringBuilder clientesTexto = new StringBuilder("Clientes:\n");
                for (Cliente cliente : clientes) {
                    clientesTexto.append("ID: ").append(cliente.getId())
                                 .append(", Nome: ").append(cliente.getNome())
                                 .append(", CPF: ").append(cliente.getCpf())
                                 .append(", Endereço: ").append(cliente.getEndereco())
                                 .append("\n");
                }
                statusLabel.setText(clientesTexto.toString());
            }
        } catch (Exception e) {
            statusLabel.setText("Erro ao listar clientes.");
            e.printStackTrace();
        }
    }


    @FXML
    void cadastrarCliente(ActionEvent event) {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String endereco = enderecoField.getText();

        if (nome.isEmpty() || cpf.isEmpty() || endereco.isEmpty()) {
            statusLabel.setText("Todos os campos são obrigatórios.");
            return;
        }

        try {
            ClienteDAO clienteDAO = new ClienteDAO(conexao);
            clienteDAO.cadastrarCliente(nome, cpf, endereco);
            statusLabel.setText("Cliente cadastrado com sucesso!");
            nomeField.clear();
            cpfField.clear();
            enderecoField.clear();
        } catch (Exception e) {
            statusLabel.setText("Erro ao cadastrar cliente.");
            e.printStackTrace();
        }
    }

    @FXML
    void atualizarCliente(ActionEvent event) {
        String id = clienteIdField.getText();
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String endereco = enderecoField.getText();

        if (id.isEmpty() || nome.isEmpty() || cpf.isEmpty() || endereco.isEmpty()) {
            statusLabel.setText("Todos os campos são obrigatórios para atualizar.");
            return;
        }

        try {
            ClienteDAO clienteDAO = new ClienteDAO(conexao);
            clienteDAO.atualizarCliente(Integer.parseInt(id), nome, cpf, endereco);
            statusLabel.setText("Cliente atualizado com sucesso!");
            clienteIdField.clear();
            nomeField.clear();
            cpfField.clear();
            enderecoField.clear();
        } catch (Exception e) {
            statusLabel.setText("Erro ao atualizar cliente.");
            e.printStackTrace();
        }
    }

    @FXML
    void removerCliente(ActionEvent event) {
        String id = clienteIdField.getText();

        if (id.isEmpty()) {
            statusLabel.setText("ID do cliente é obrigatório para remover.");
            return;
        }

        try {
            ClienteDAO clienteDAO = new ClienteDAO(conexao);
            clienteDAO.removerCliente(Integer.parseInt(id));
            statusLabel.setText("Cliente removido com sucesso!");
            clienteIdField.clear();
        } catch (Exception e) {
            statusLabel.setText("Erro ao remover cliente.");
            e.printStackTrace();
        }
    }
}
