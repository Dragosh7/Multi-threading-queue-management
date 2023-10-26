package org.example.BusinessLogic;

import org.example.Model.Client;
import org.example.Model.Server;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Client client) {
        int find = -1;
        int minWaitingTime = Integer.MAX_VALUE;

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);

            if (!server.isFull() && server.getWaitingPeriod() < minWaitingTime) {
                find = i;
                minWaitingTime = server.getWaitingPeriod();
            }
        }

        if (find != -1) {
            servers.get(find).addClient(client);
        }
    }
}