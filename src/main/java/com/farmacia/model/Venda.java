package com.farmacia.model;

public class Venda {
    private int id;
    private int clienteId;
    private double valorTotal;
    private String dataVenda;
    private String status;

    // Construtor padrão
    public Venda() {
    }

    // Construtor completo
    public Venda(int id, int clienteId, double valorTotal, String dataVenda, String status) {
        this.id = id;
        this.clienteId = clienteId;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
        this.status = status;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Venda [id=" + id + ", clienteId=" + clienteId + ", valorTotal=" + valorTotal + 
               ", dataVenda=" + dataVenda + ", status=" + status + "]";
    }
}
