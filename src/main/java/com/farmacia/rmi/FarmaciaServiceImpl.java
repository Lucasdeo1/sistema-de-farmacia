package com.farmacia.rmi;

import com.farmacia.dao.ClienteDAO;
import com.farmacia.dao.Conexao;
import com.farmacia.dao.MedicamentoDAO;
import com.farmacia.dao.VendaDAO;
import com.farmacia.model.Cliente;
import com.farmacia.model.Medicamento;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FarmaciaServiceImpl extends UnicastRemoteObject implements FarmaciaService {

    private Connection conexao;
    private MedicamentoDAO medicamentoDAO;
    private ClienteDAO clienteDAO;
    private VendaDAO vendaDAO;
    private List<Medicamento> medicamentos = new ArrayList<>();

    public FarmaciaServiceImpl() throws RemoteException {
        super();
        try {
            this.conexao = Conexao.getConexao();
            this.medicamentoDAO = new MedicamentoDAO(conexao);
            this.clienteDAO = new ClienteDAO(conexao);
            this.vendaDAO = new VendaDAO(conexao);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao conectar ao banco de dados.", e);
        }
    }

    @Override
    public List<Cliente> listarClientes() throws RemoteException {
        try {
            return clienteDAO.listarClientes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao listar clientes.", e);
        }
    }

    @Override
    public List<Medicamento> listarMedicamentos() throws RemoteException {
        return medicamentos;
    }

    @Override
    public void cadastrarMedicamento(String nome, String lote, double preco, int quantidade) throws RemoteException {
        try {
            Medicamento medicamento = new Medicamento(nome, lote, preco);
            medicamento.setNome(nome);
            medicamento.setLote(lote);
            medicamento.setPreco(preco);
            medicamento.setQuantidade(quantidade);
            medicamentoDAO.cadastrarMedicamento(medicamento);
            System.out.println("Medicamento cadastrado no banco de dados.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao cadastrar medicamento.", e);
        }
    }

    @Override
    public int registrarVenda(int clienteId, double valorTotal) throws RemoteException {
        try {
            int vendaId = vendaDAO.registrarVenda(clienteId, valorTotal, "Concluída");
            System.out.println("Venda registrada no banco de dados com ID: " + vendaId);
            return vendaId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao registrar venda.", e);
        }
    }

    @Override
    public void registrarItensVenda(int vendaId, List<Medicamento> medicamentos) throws RemoteException {
        try {
            vendaDAO.registrarItensVenda(vendaId, medicamentos);
            System.out.println("Itens de venda registrados no banco de dados.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao registrar itens de venda.", e);
        }
    }




    @Override
    public void atualizarEstoque(int medicamentoId, int quantidade) throws RemoteException {
        try {
            Medicamento medicamento = medicamentoDAO.buscarMedicamentoPorId(medicamentoId);
            if (medicamento != null) {
                medicamento.setQuantidade(medicamento.getQuantidade() - quantidade);
                medicamentoDAO.atualizarMedicamento(medicamento);
                System.out.println("Estoque atualizado no banco de dados.");
            } else {
                System.out.println("Medicamento não encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar estoque.", e);
        }
    }

    @Override
    public void cadastrarCliente(String nome, String cpf, String endereco) throws RemoteException {
        try {
            clienteDAO.cadastrarCliente(nome, cpf, endereco);
            System.out.println("Cliente cadastrado no banco de dados.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao cadastrar cliente.", e);
        }
    }

    @Override
    public List<String> listarVendas() throws RemoteException {
        try {
            return vendaDAO.listarVendas();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao listar vendas.", e);
        }
    }

}
