package com.mycompany.os_project.Schedulers;

import com.mycompany.os_project.Models.GanttRecord;
import com.mycompany.os_project.Models.Result;
import com.mycompany.os_project.Models.Process;

import java.util.*;

public class RoundRobinScheduler {

    public static Result run(ArrayList<Process> processes, int quantum) {

        Result result = new Result();

        Queue<Process> queue = new LinkedList<>();

        int time = 0;

        int completed = 0;

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int index = 0;

        while (completed < processes.size()) {

            while (index < processes.size()
                    && processes.get(index).arrivalTime <= time) {

                queue.add(processes.get(index));

                index++;
            }

            if (queue.isEmpty()) {

                time++;

                continue;
            }

            Process current = queue.poll();

            if (!current.started) {

                current.responseTime =
                        time - current.arrivalTime;

                current.started = true;
            }

            int start = time;

            int execute =
                    Math.min(quantum, current.remainingTime);

            time += execute;

            current.remainingTime -= execute;

            int end = time;

            result.ganttChart.add(
                    new GanttRecord(
                            "P" + current.id,
                            start,
                            end
                    )
            );

            while (index < processes.size()
                    && processes.get(index).arrivalTime <= time) {

                queue.add(processes.get(index));

                index++;
            }

            if (current.remainingTime > 0) {

                queue.add(current);

            } else {

                completed++;

                current.completionTime = time;

                current.turnaroundTime =
                        current.completionTime
                        - current.arrivalTime;

                current.waitingTime =
                        current.turnaroundTime
                        - current.burstTime;
            }
        }

        double totalWT = 0;
        double totalTAT = 0;
        double totalRT = 0;

        for (Process p : processes) {

            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
            totalRT += p.responseTime;

            result.processes.add(p);
        }

        result.avgWT = totalWT / processes.size();
        result.avgTAT = totalTAT / processes.size();
        result.avgRT = totalRT / processes.size();

        return result;
    }
}