package com.farmacia.client;

import com.farmacia.model.Cliente;
import com.farmacia.model.Medicamento;
import com.farmacia.rmi.FarmaciaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import javafx.util.StringConverter;

public class VendaController {
    @FXML
    private ComboBox<Cliente> clienteComboBox;

    @FXML
    private ComboBox<Medicamento> medicamentoComboBox;

    @FXML
    private TextField quantidadeField;

    @FXML
    private TextField valorTotalField;

    private FarmaciaService service;

    @FXML
    public void initialize() {
        try {
            service = (FarmaciaService) Naming.lookup("//localhost/FarmaciaService");
            carregarClientes();
            carregarMedicamentos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarClientes() {
        try {
            List<Cliente> clientes = service.listarClientes();
            ObservableList<Cliente> clientesObservable = FXCollections.observableArrayList(clientes);
            clienteComboBox.setItems(clientesObservable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void carregarMedicamentos() {
    try {
        List<Medicamento> medicamentos = service.listarMedicamentos();
        ObservableList<Medicamento> medicamentosObservable = FXCollections.observableArrayList(medicamentos);
        medicamentoComboBox.setItems(medicamentosObservable);

        medicamentoComboBox.setConverter(new StringConverter<Medicamento>() {
            @Override
            public String toString(Medicamento medicamento) {
                return medicamento != null ? medicamento.getNome() : "";
            }

            @Override
            public Medicamento fromString(String string) {
                return null; // Não é necessário para um ComboBox de seleção
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML
    private void registrarVenda() {
        Cliente clienteSelecionado = clienteComboBox.getSelectionModel().getSelectedItem();
        Medicamento medicamentoSelecionado = medicamentoComboBox.getSelectionModel().getSelectedItem();
        String quantidadeTexto = quantidadeField.getText();

        if (clienteSelecionado == null || medicamentoSelecionado == null || quantidadeTexto.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um cliente, um medicamento e informe uma quantidade válida.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeTexto);
            if (medicamentoSelecionado.getQuantidade() < quantidade) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Quantidade insuficiente no estoque!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            double valorTotal = medicamentoSelecionado.getPreco() * quantidade;
            valorTotalField.setText(String.format("%.2f", valorTotal));

            // Registrar a venda
            int vendaId = service.registrarVenda(clienteSelecionado.getId(), valorTotal);

            // Criar a lista de medicamentos
            List<Medicamento> medicamentosVenda = new ArrayList<>();
            Medicamento medicamento = new Medicamento(nome, lote, preco);
            medicamento.setId(medicamentoSelecionado.getId());  // Define o ID do medicamento selecionado
            medicamento.setQuantidade(quantidade);  // Define a quantidade do medicamento
            medicamentosVenda.add(medicamento);  // Adiciona o medicamento à lista

            // Registrar os itens da venda
            service.registrarItensVenda(vendaId, medicamentosVenda);

            // Atualizar o estoque
            service.atualizarEstoque(medicamentoSelecionado.getId(), -quantidade);

            // Mensagem de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Venda registrada com sucesso!", ButtonType.OK);
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A quantidade deve ser um número válido.", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao registrar venda.", ButtonType.OK);
            alert.showAndWait();
        }

    }
}
