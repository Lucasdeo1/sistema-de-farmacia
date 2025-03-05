package com.farmacia.client;

import com.farmacia.dao.MedicamentoDAO;
import com.farmacia.model.Medicamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.util.List;

public class MedicamentoController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField precoField;

    @FXML
    private TextField quantidadeField;

    @FXML
    private TableView<Medicamento> medicamentoTable;

    @FXML
    private TableColumn<Medicamento, String> colNome;

    @FXML
    private TableColumn<Medicamento, Integer> colQuantidade;

    @FXML
    private TableColumn<Medicamento, Double> colPreco;

    private ObservableList<Medicamento> listaMedicamentos;
    private Connection conexao;

    public MedicamentoController(Connection conexao) {
        this.conexao = conexao;
    }

    @FXML
    public void initialize() {
        configurarColunas();
        carregarMedicamentos();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colQuantidade.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty().asObject());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());
    }

    private void carregarMedicamentos() {
        MedicamentoDAO medicamentoDAO = new MedicamentoDAO(conexao);
        List<Medicamento> medicamentos = medicamentoDAO.listarMedicamentos();
        listaMedicamentos = FXCollections.observableArrayList(medicamentos);
        medicamentoTable.setItems(listaMedicamentos);
    }

    @FXML
    private void cadastrarMedicamento() {
        String nome = nomeField.getText();
        double preco;
        int quantidade;

        try {
            preco = Double.parseDouble(precoField.getText());
            quantidade = Integer.parseInt(quantidadeField.getText());

            Medicamento medicamento = new Medicamento(0, nome, quantidade, preco);
            MedicamentoDAO medicamentoDAO = new MedicamentoDAO(conexao);
            medicamentoDAO.cadastrarMedicamento(medicamento);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Medicamento cadastrado com sucesso!", ButtonType.OK);
            alert.showAndWait();

            limparFormulario();
            carregarMedicamentos();  // Atualizar a lista após o cadastro

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Preço e quantidade devem ser numéricos.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void limparFormulario() {
        nomeField.clear();
        precoField.clear();
        quantidadeField.clear();
    }
}
