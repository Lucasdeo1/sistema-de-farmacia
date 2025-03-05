package com.farmacia.model;

import javafx.beans.property.*;

public class Medicamento {
    private IntegerProperty id;
    private StringProperty nome;
    private IntegerProperty quantidade;
    private DoubleProperty preco;

    // Construtor padrão
    public Medicamento(String nome1, String lote, double preco1) {
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.quantidade = new SimpleIntegerProperty();
        this.preco = new SimpleDoubleProperty();
    }

    // Construtor completo
    public Medicamento(int id, String nome, int quantidade, double preco) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.quantidade = new SimpleIntegerProperty(quantidade);
        this.preco = new SimpleDoubleProperty(preco);
    }

    // Getters e Setters com JavaFX Property
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade.get();
    }

    public void setQuantidade(int quantidade) {
        this.quantidade.set(quantidade);
    }

    public IntegerProperty quantidadeProperty() {
        return quantidade;
    }

    public double getPreco() {
        return preco.get();
    }

    public void setPreco(double preco) {
        this.preco.set(preco);
    }

    public DoubleProperty precoProperty() {
        return preco;
    }

    @Override
    public String toString() {
        return nome.get();
    }

    public void setLote(String lote) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
