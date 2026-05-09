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
    int n = processes.size();
    for (Process p : processes) p.remainingTime = p.burstTime; // إعادة ضبط

    int time = 0, completed = 0;
    while (completed < n) {
        Process shortest = null;
        int minRem = Integer.MAX_VALUE;

        for (Process p : processes) {
            if (p.arrivalTime <= time && p.remainingTime > 0 && p.remainingTime < minRem) {
                minRem = p.remainingTime;
                shortest = p;
            }
        }

        if (shortest == null) {
            // القفزة دي هي اللي بتمنع الـ Freeze
            int nextArr = Integer.MAX_VALUE;
            for (Process p : processes) {
                if (p.arrivalTime > time) nextArr = Math.min(nextArr, p.arrivalTime);
            }
            time = (nextArr == Integer.MAX_VALUE) ? time + 1 : nextArr;
            continue;
        }

        gantt.add(new GanttRecord(shortest.id, time, time + 1));
        time++;
        shortest.remainingTime--;

        if (shortest.remainingTime == 0) {
            completed++;
            shortest.completionTime = time;
            shortest.turnaroundTime = time - shortest.arrivalTime;
            shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;
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