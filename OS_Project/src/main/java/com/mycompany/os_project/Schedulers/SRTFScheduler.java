package com.mycompany.os_project.Schedulers;

import com.mycompany.os_project.Models.GanttRecord;
import com.mycompany.os_project.Models.Result;
import com.mycompany.os_project.Models.Process;

import java.util.*;

public class SRTFScheduler {

    public static Result run(ArrayList<Process> processes) {

        Result result = new Result();

        int time = 0;

        int completed = 0;

        while (completed < processes.size()) {

            Process shortest = null;

            for (Process p : processes) {

                if (p.arrivalTime <= time
                        && p.remainingTime > 0) {

                    if (shortest == null
                            || p.remainingTime
                            < shortest.remainingTime) {

                        shortest = p;
                    }
                }
            }

            if (shortest == null) {

                time++;

                continue;
            }

            if (!shortest.started) {

                shortest.responseTime =
                        time - shortest.arrivalTime;

                shortest.started = true;
            }

            int start = time;

            shortest.remainingTime--;

            time++;

            int end = time;

            result.ganttChart.add(
                    new GanttRecord(
                            "P" + shortest.id,
                            start,
                            end
                    )
            );

            if (shortest.remainingTime == 0) {

                completed++;

                shortest.completionTime = time;

                shortest.turnaroundTime =
                        shortest.completionTime
                        - shortest.arrivalTime;

                shortest.waitingTime =
                        shortest.turnaroundTime
                        - shortest.burstTime;
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