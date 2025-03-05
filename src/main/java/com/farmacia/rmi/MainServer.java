package com.farmacia.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServer {
    public static void main(String[] args) {
        try {
            FarmaciaServiceImpl service = new FarmaciaServiceImpl();
            Registry registry;

            try {
                // Tenta localizar o registro existente
                registry = LocateRegistry.getRegistry(1099);
                registry.lookup("FarmaciaService");
                System.out.println("O serviço já está registrado.");
            } catch (Exception e) {
                // Se o serviço não estiver registrado, cria um novo registro
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Novo registro RMI criado.");
            }

            // Registra ou substitui o serviço
            registry.rebind("FarmaciaService", service);
            System.out.println("Servidor RMI iniciado e serviço registrado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
