package org.example.Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    public int maxClientsPerServer;
    public int id;

    public Server(int maxClientsPerServer,int id){
        this.id=id;
        waitingPeriod=new AtomicInteger();
        this.waitingPeriod.set(0);
        clients= new ArrayBlockingQueue<>(maxClientsPerServer) ;
        this.maxClientsPerServer=maxClientsPerServer;

    }
    public BlockingQueue<Client> getClients() {
        return clients;
    }

    public int getId() {
        return id;
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public int getSize()
    {
     return clients.size();
    }
    public boolean isFull()
    {
        if(clients.size()< maxClientsPerServer){
            return false;}
        else return true;
    }

    private synchronized void incWaitingPeriod(Integer amount)
    {
        waitingPeriod.addAndGet(amount);
    }
    private synchronized void decWaitingPeriod(Integer amount) {waitingPeriod.addAndGet(-amount);}
    public void addClient(Client newClient){

        /*try {
            clients.put(newClient);
        } catch (InterruptedException e) */

        clients.add(newClient);
        incWaitingPeriod(newClient.getServiceTime());

    }
    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (clients.size() > 0) {
                clients.element().setServiceTime(clients.element().getServiceTime() - 1);
                decWaitingPeriod(1);
                if (clients.element().getServiceTime() == 0) {
                    try {
                        clients.take();
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
        }
    }

}
