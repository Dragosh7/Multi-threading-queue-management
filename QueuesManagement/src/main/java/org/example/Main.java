package org.example;

import org.example.BusinessLogic.SimulationManager;

public class Main {
    public static void main(String[] args){
        SimulationManager simulate=new SimulationManager();
        Thread thread=new Thread(simulate);
        thread.start();
    }
}