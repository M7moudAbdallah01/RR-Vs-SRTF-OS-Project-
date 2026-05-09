package com.mycompany.os_project.Models;

import java.util.ArrayList;

public class Result {

    public ArrayList<Process> processes;

    public ArrayList<GanttRecord> ganttChart;

    public double avgWT;
    public double avgTAT;
    public double avgRT;

    public Result() {

        processes = new ArrayList<>();

        ganttChart = new ArrayList<>();
    }
}