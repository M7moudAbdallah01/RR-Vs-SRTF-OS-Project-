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

public class RoundRobinScheduler {

    public static Result run(List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>();
        List<GanttRecord> gantt = new ArrayList<>();
        List<String> queueSnapshots = new ArrayList<>();
        int time = 0;
        int completed = 0;
        int n = processes.size();

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int i = 0;
        while (completed < n) {

            while (i < n && processes.get(i).arrivalTime <= time) {
                queue.add(processes.get(i));
                i++;
            }

            if (queue.isEmpty()) {
                time++;
                continue;
            }

            Process p = queue.poll();

            if (!p.started) {
                p.responseTime = time - p.arrivalTime;
                p.started = true;
            }

            int execTime = Math.min(quantum, p.remainingTime);

// سجل التنفيذ وحدة وحدة
            for (int t = 0; t < execTime; t++) {

                gantt.add(new GanttRecord(
                        p.id,
                        time,
                        time + 1
                ));

                time++;
                p.remainingTime--;

                // إضافة الـ processes اللي وصلت أثناء التنفيذ
                while (i < n
                        && processes.get(i).arrivalTime <= time) {

                    queue.add(processes.get(i));
                    i++;
                }
            }

            while (i < n && processes.get(i).arrivalTime <= time) {
                queue.add(processes.get(i));
                i++;
            }

            if (p.remainingTime > 0) {
                queue.add(p);
            } else {
                p.completionTime = time;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
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
