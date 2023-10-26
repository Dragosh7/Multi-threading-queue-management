package org.example.BusinessLogic;

import org.example.Model.Client;
import org.example.Model.SelectionPolicy;
import org.example.Model.Server;
import org.example.GUI.SimulationFrame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private Scheduler scheduler;
    private SimulationFrame simulationFrame;
    private List<Client> generatedClients;

    public SimulationManager() {
        this.simulationFrame = new SimulationFrame();
        setParameters(simulationFrame.getParameters());
        this.scheduler = new Scheduler(numberOfServers, 500, selectionPolicy);
        generatedClients = Collections.synchronizedList(new ArrayList<>());
        generateNRandomTasks();
    }

    public void setParameters(ArrayList<Integer> params) {
        this.numberOfClients = params.get(0);
        this.numberOfServers = params.get(1);
        this.timeLimit = params.get(2);
        this.minProcessingTime = params.get(3);
        this.maxProcessingTime = params.get(4);
        this.minArrivalTime = params.get(5);
        this.maxArrivalTime = params.get(6);

    }

    private void generateNRandomTasks() {
        Random random = new Random();

        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = random.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            generatedClients.add(new Client(processingTime, arrivalTime));
        }
        generatedClients.sort(Comparator.comparing(Client::getArrivalTime));
        for (int i = 0; i < numberOfClients; i++) {
            generatedClients.get(i).setId(i);
        }
    }

    private String showStatus() {
        String info = "Waiting clients: ";
        for (Client i : generatedClients) {
            info += "(" + i.getId() + "," + i.getArrivalTime() + "," + i.getServiceTime() + ");";
        }
        info += "\n";
        for (Server i : scheduler.getServers()) {
            info += "Queue " + (i.getId() + 1) + ": ";
            if (i.getSize() == 0)
                info += "closed";
            else {
                BlockingQueue<Client> tmp = new LinkedBlockingQueue<>(i.getClients());
                Client[] clients = new Client[tmp.size()];
                int k = 0;
                for (Client x : tmp) {
                    clients[k] = x;
                    k++;
                }
                //am salvat clientii intr-un vector
                for (int j = 0; j < i.getSize(); j++) {
                    info += "(" + clients[j].getId() + "," + clients[j].getArrivalTime() + ',' + clients[j].getServiceTime() + ");";
                }
            }
            info += "\n";
        }
        return info;
    }

    @Override
    public void run() {
        double totalServiceTime = 0;
        double totalWaitingTime = 0;
        for (Client client : generatedClients) {
            totalServiceTime += client.getServiceTime();
            totalWaitingTime += client.getArrivalTime();
        }
        double avgServiceTime = totalServiceTime / generatedClients.size();
        double avgWaitingTime = totalWaitingTime / generatedClients.size();
        double peak=0;
        int peakHour=0;

        StringBuilder outputBuilder = new StringBuilder();
        int simulationTime = 0;
        while (simulationTime < timeLimit) {
           int aux=scheduler.sumSize();
           if(aux>peak){
               peak=aux;
               peakHour=simulationTime-1;
           }

            boolean run = true;
            while (run && !generatedClients.isEmpty()) {
                Client client = generatedClients.get(0);
                if (client.getArrivalTime() == simulationTime) {
                    scheduler.dispatchClient(client);
                    generatedClients.remove(0);
                } else if (client.getArrivalTime() > simulationTime) {
                    run = false;
                }
            }

            String log = "Time " + simulationTime + "\n" + showStatus() + "\n";
            outputBuilder.append(log);
            System.out.println(log);
            simulationFrame.showProgress(log);

            simulationTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulation interrupted");
                return;
            }
        }

        String log = "Simulation has finished successfully!\n"
                + "Average service time: " + String.format("%.2f", avgServiceTime) + " seconds\n"
                + "Average waiting time: " + String.format("%.2f", avgWaitingTime) + " seconds\n"
                + "Peak hour: " + String.format("%d", peakHour) + " seconds\n";
        outputBuilder.append(log);
        System.out.println(log);
        simulationFrame.showProgress(log);

        String output = outputBuilder.toString();
        try (FileWriter file = new FileWriter("log.txt")) {
            file.write(output);
        } catch (IOException e) {
            System.out.println("File write error");
        }
    }
}
