package com.farmacia.client;

import com.farmacia.model.Cliente;
import com.farmacia.rmi.FarmaciaService;
import java.rmi.Naming;
import java.util.List;

public class MainClient {
    public static void main(String[] args) {
        try {
            // Conectando ao serviço RMI
            String url = "rmi://localhost/FarmaciaService";
            FarmaciaService service = (FarmaciaService) Naming.lookup(url);

            // Cadastrar um novo cliente
            service.cadastrarCliente("jose da Silva", "00765432100", "Rua A, 123");
            System.out.println("Cliente cadastrado: João da Silva");

            // Registrar a venda para o cliente cadastrado (Cliente ID = 1)
            service.registrarVenda(1, 100.0);
            System.out.println("Venda registrada para Cliente ID: 1, Valor Total: 100.0");

            // Listar clientes
            System.out.println("Listando clientes...");
            List<Cliente> clientes = service.listarClientes();
            for (Cliente cliente : clientes) {
                System.out.println("Nome: " + cliente.getNome() + ", CPF: " + cliente.getCpf());
            }
            System.out.println("Listando vendas...");
            List<String> vendas = service.listarVendas();
            for (String venda : vendas) {
                System.out.println(venda);
            }


        } catch (Exception e) {
            System.err.println("Erro ao conectar ao serviço RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
