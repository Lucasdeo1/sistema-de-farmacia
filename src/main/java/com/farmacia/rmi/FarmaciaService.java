package com.farmacia.rmi;

import com.farmacia.model.Cliente;
import com.farmacia.model.Medicamento;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FarmaciaService extends Remote {
    List<Cliente> listarClientes() throws RemoteException;
    List<Medicamento> listarMedicamentos() throws RemoteException;
    void cadastrarMedicamento(String nome, String lote, double preco, int quantidade) throws RemoteException;
    int registrarVenda(int clienteId, double valorTotal) throws RemoteException;
    void registrarItensVenda(int vendaId, List<Medicamento> medicamentos) throws RemoteException;  // Novo método
    void atualizarEstoque(int medicamentoId, int quantidade) throws RemoteException;
    void cadastrarCliente(String nome, String cpf, String endereco) throws RemoteException;
    List<String> listarVendas() throws RemoteException;
}
