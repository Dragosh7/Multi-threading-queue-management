package org.example.BusinessLogic;

import org.example.Model.Client;
import org.example.Model.Server;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Client client) {
        int find = -1;
        int minQueueSize = Integer.MAX_VALUE;

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);

            if (!server.isFull() && server.getSize() < minQueueSize) {
                find = i;
                minQueueSize = server.getSize();
            }
        }

        if (find != -1) {
            servers.get(find).addClient(client);
        }
    }
}

