package Models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 𝕐𝕠𝕦𝕤𝕤𝕚𝕗 𝔸𝕕𝕝𝕪
 */
import java.util.List;

public class Result {
    public List<Process> processes;
    public List<GanttRecord> ganttChart;
    public double avgWT;
    public double avgTAT;
    public double avgRT;

    public Result(List<Process> processes, List<GanttRecord> ganttChart,
                  double avgWT, double avgTAT, double avgRT) {
        this.processes = processes;
        this.ganttChart = ganttChart;
        this.avgWT = avgWT;
        this.avgTAT = avgTAT;
        this.avgRT = avgRT;
    }
}
