package com.farmacia.gui;

import com.farmacia.model.Cliente;
import com.farmacia.model.Medicamento;
import com.farmacia.rmi.FarmaciaService;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class FarmaciaInterface extends Application {
    private FarmaciaService service;

    // Elementos de UI
    private TextField tfNomeCliente, tfCpfCliente, tfEnderecoCliente;
    private TextField tfNomeMedicamento, tfLoteMedicamento, tfPrecoMedicamento, tfQuantidadeMedicamento;
    private ComboBox<Cliente> cbClientesVenda;
    private ComboBox<Medicamento> cbMedicamentosVenda;
    private TextField tfQuantidadeVenda;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Conectar ao serviço RMI
            String url = "rmi://localhost/FarmaciaService";
            service = (FarmaciaService) Naming.lookup(url);

            // Configurar a interface gráfica
            primaryStage.setTitle("Sistema de Farmácia");

            // Criando o layout
            TabPane tabPane = new TabPane();

            // Tab de Cadastro Cliente
            Tab tabCadastroCliente = new Tab("Cadastro Cliente");
            tabCadastroCliente.setContent(criarCadastroCliente());
            tabPane.getTabs().add(tabCadastroCliente);

            // Tab de Cadastro Medicamento
            Tab tabCadastroMedicamento = new Tab("Cadastro Medicamento");
            tabCadastroMedicamento.setContent(criarCadastroMedicamento());
            tabPane.getTabs().add(tabCadastroMedicamento);

            // Tab de Registro de Venda
            Tab tabRegistrarVenda = new Tab("Registrar Venda");
            tabRegistrarVenda.setContent(criarRegistrarVenda());
            tabPane.getTabs().add(tabRegistrarVenda);

            // Adicionar o TabPane à cena
            Scene scene = new Scene(tabPane, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Criar o formulário de Cadastro de Cliente
    private VBox criarCadastroCliente() {
        VBox vbox = new VBox(10);

        tfNomeCliente = new TextField();
        tfCpfCliente = new TextField();
        tfEnderecoCliente = new TextField();

        Button btnCadastrarCliente = new Button("Cadastrar Cliente");
        btnCadastrarCliente.setOnAction(event -> cadastrarCliente());

        vbox.getChildren().addAll(new Label("Nome"), tfNomeCliente, new Label("CPF"), tfCpfCliente,
                                  new Label("Endereço"), tfEnderecoCliente, btnCadastrarCliente);

        return vbox;
    }

    // Criar o formulário de Cadastro de Medicamento
    private VBox criarCadastroMedicamento() {
        VBox vbox = new VBox(10);

        tfNomeMedicamento = new TextField();
        tfLoteMedicamento = new TextField();
        tfPrecoMedicamento = new TextField();
        tfQuantidadeMedicamento = new TextField();

        Button btnCadastrarMedicamento = new Button("Cadastrar Medicamento");
        btnCadastrarMedicamento.setOnAction(event -> cadastrarMedicamento());

        vbox.getChildren().addAll(new Label("Nome do Medicamento"), tfNomeMedicamento, new Label("Lote"),
                                  tfLoteMedicamento, new Label("Preço"), tfPrecoMedicamento,
                                  new Label("Quantidade"), tfQuantidadeMedicamento, btnCadastrarMedicamento);

        return vbox;
    }

    // Criar o formulário de Registro de Venda
    private VBox criarRegistrarVenda() {
        VBox vbox = new VBox(10);

        cbClientesVenda = new ComboBox<>();
        cbMedicamentosVenda = new ComboBox<>();
        tfQuantidadeVenda = new TextField();

        // Carregar clientes e medicamentos
        carregarClientes();
        carregarMedicamentos();

        Button btnRegistrarVenda = new Button("Registrar Venda");
        btnRegistrarVenda.setOnAction(event -> registrarVenda());

        vbox.getChildren().addAll(new Label("Cliente"), cbClientesVenda, new Label("Medicamento"),
                                  cbMedicamentosVenda, new Label("Quantidade"), tfQuantidadeVenda,
                                  btnRegistrarVenda);

        return vbox;
    }

    // Cadastrar Cliente
    private void cadastrarCliente() {
        String nome = tfNomeCliente.getText();
        String cpf = tfCpfCliente.getText();
        String endereco = tfEnderecoCliente.getText();

        try {
            service.cadastrarCliente(nome, cpf, endereco);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente cadastrado com sucesso!", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar cliente.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Cadastrar Medicamento
    private void cadastrarMedicamento() {
        String nome = tfNomeMedicamento.getText();
        String lote = tfLoteMedicamento.getText();
        double preco = Double.parseDouble(tfPrecoMedicamento.getText());
        int quantidade = Integer.parseInt(tfQuantidadeMedicamento.getText());

        try {
            service.cadastrarMedicamento(nome, lote, preco, quantidade);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Medicamento cadastrado com sucesso!", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar medicamento.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Registrar Venda
    private void registrarVenda() {
        Cliente clienteSelecionado = cbClientesVenda.getValue();
        Medicamento medicamentoSelecionado = cbMedicamentosVenda.getValue();
        int quantidadeVenda = Integer.parseInt(tfQuantidadeVenda.getText());

        // Calcular valor total
        double valorTotal = medicamentoSelecionado.getPreco() * quantidadeVenda;

        List<Medicamento> medicamentosVenda = new ArrayList<>();
        medicamentoSelecionado.setQuantidade(quantidadeVenda);
        medicamentosVenda.add(medicamentoSelecionado);

        try {
            int vendaId = service.registrarVenda(clienteSelecionado.getId(), valorTotal);
            service.registrarItensVenda(vendaId, medicamentosVenda);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Venda registrada com sucesso!", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao registrar venda.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Carregar Clientes no ComboBox
    private void carregarClientes() {
        try {
            List<Cliente> clientes = service.listarClientes();
            cbClientesVenda.getItems().addAll(clientes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Carregar Medicamentos no ComboBox
    private void carregarMedicamentos() {
        try {
            List<Medicamento> medicamentos = service.listarMedicamentos();
            cbMedicamentosVenda.getItems().addAll(medicamentos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
