package org.example.BusinessLogic;

import org.example.Model.Client;
import org.example.Model.SelectionPolicy;
import org.example.Model.Server;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxClientsPerServer, SelectionPolicy selectionPolicy) {
        servers = new ArrayList<>();
        Thread t[] = new Thread[maxNoServers];
        for (int i = 0; i < maxNoServers; i++) {
            servers.add(new Server(maxClientsPerServer, i));
            t[i] = new Thread(servers.get(i));
            t[i].start();

        }
        changeStrategy(selectionPolicy);
    }

    public void changeStrategy(SelectionPolicy policy) {

        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchClient(Client x) {
        strategy.addTask(servers, x);
    }

    public List<Server> getServers() {
        return servers;
    }
    public int sumSize()
    {
        int sum=0;
        for(Server i:servers)
        {
            sum+=i.getSize();
        }

    return sum;}
}
