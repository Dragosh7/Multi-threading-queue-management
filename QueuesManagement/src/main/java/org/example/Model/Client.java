package org.example.Model;

public class Client {
    private int id;
    private int arrivalTime;
    private int serviceTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    public Client(int arrivalTime,int serviceTime)
    {
        this.serviceTime=serviceTime;
        this.arrivalTime=arrivalTime;
    }
}
