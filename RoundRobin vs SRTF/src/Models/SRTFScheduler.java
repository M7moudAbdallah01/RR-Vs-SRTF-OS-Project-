/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author 𝕐𝕠𝕦𝕤𝕤𝕚𝕗 𝔸𝕕𝕝𝕪
 */
import java.util.*;

public class SRTFScheduler {

    public static Result run(List<Process> processes) {
        List<GanttRecord> gantt = new ArrayList<>();

        int time = 0;
        int completed = 0;
        int n = processes.size();

        while (completed < n) {
            Process shortest = null;

            for (Process p : processes) {
                if (p.arrivalTime <= time && p.remainingTime > 0) {
                    if (shortest == null || p.remainingTime < shortest.remainingTime) {
                        shortest = p;
                    }
                }
            }

            if (shortest == null) {
                time++;
                continue;
            }

            if (!shortest.started) {
                shortest.responseTime = time - shortest.arrivalTime;
                shortest.started = true;
            }

            int start = time;
            shortest.remainingTime--;
            time++;

            gantt.add(new GanttRecord(shortest.id, start, time));

            if (shortest.remainingTime == 0) {
                shortest.completionTime = time;
                shortest.turnaroundTime = time - shortest.arrivalTime;
                shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
                completed++;
            }
        }

        return calculateResult(processes, gantt);
    }

    private static Result calculateResult(List<Process> processes, List<GanttRecord> gantt) {
        double totalWT = 0, totalTAT = 0, totalRT = 0;

        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
            totalRT += p.responseTime;
        }

        int n = processes.size();
        return new Result(processes, gantt,
                totalWT / n,
                totalTAT / n,
                totalRT / n);
    }
}