package com.mycompany.os_project.Models;

public class Process {

    public int id;
    public int arrivalTime;
    public int burstTime;

    public int remainingTime;

    public int completionTime;
    public int turnaroundTime;
    public int waitingTime;
    public int responseTime;

    public boolean started = false;

    public Process(int id, int arrivalTime, int burstTime) {

        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;

        this.remainingTime = burstTime;
    }
}
