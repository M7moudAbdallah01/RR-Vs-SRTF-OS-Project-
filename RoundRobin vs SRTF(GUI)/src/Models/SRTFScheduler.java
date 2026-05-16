package Models;

import java.util.*;

public class SRTFScheduler {

    public static Result run(List<Process> originalProcesses) {

        // ===== Validation =====
        if (originalProcesses == null || originalProcesses.isEmpty()) {
            throw new IllegalArgumentException("No processes found.");
        }

        // ===== Deep Copy =====
        List<Process> processes = new ArrayList<>();

        for (Process p : originalProcesses) {

            Process copy = new Process(
                    p.id,
                    p.arrivalTime,
                    p.burstTime
            );

            // لو عندك priority في constructor زودها
            // new Process(p.id, p.arrivalTime, p.burstTime, p.priority);

            copy.remainingTime = p.burstTime;
            copy.started = false;

            processes.add(copy);
        }

        List<GanttRecord> gantt = new ArrayList<>();

        int n = processes.size();
        int time = 0;
        int completed = 0;

        while (completed < n) {

            Process shortest = null;
            int minRemaining = Integer.MAX_VALUE;

            // ===== Find shortest remaining =====
            for (Process p : processes) {

                if (p.arrivalTime <= time
                        && p.remainingTime > 0) {

                    // Shorter burst
                    if (p.remainingTime < minRemaining) {

                        shortest = p;
                        minRemaining =
                                p.remainingTime;
                    }

                    // Tie Breaking
                    else if (p.remainingTime
                            == minRemaining) {

                        if (shortest != null &&
                                p.arrivalTime
                                < shortest.arrivalTime) {

                            shortest = p;
                        }
                    }
                }
            }

            // ===== CPU Idle =====
            if (shortest == null) {

                gantt.add(new GanttRecord(
                                                                time,
                                time + 1, time + 1)
                );

                time++;
                continue;
            }

            // ===== Response Time =====
            if (!shortest.started) {

                shortest.responseTime =
                        time
                        - shortest.arrivalTime;

                shortest.started = true;
            }

            // ===== Execute 1 Unit =====
            gantt.add(new GanttRecord(shortest.id, time, time + 1)); 

                shortest.remainingTime--;
                time++;

            // ===== Completion =====
            if (shortest.remainingTime == 0) {

                completed++;

                shortest.completionTime =
                        time;

                shortest.turnaroundTime =
                        shortest.completionTime
                        - shortest.arrivalTime;

                shortest.waitingTime =
                        shortest.turnaroundTime
                        - shortest.burstTime;
            }
        }

        return calculateResult(
                processes,
                gantt
        );
    }

    private static Result calculateResult(
            List<Process> processes,
            List<GanttRecord> gantt
    ) {

        double totalWT = 0;
        double totalTAT = 0;
        double totalRT = 0;

        for (Process p : processes) {

            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
            totalRT += p.responseTime;
        }

        int n = processes.size();

        double avgWT = totalWT / n;
        double avgTAT = totalTAT / n;
        double avgRT = totalRT / n;

        return new Result(
                processes,
                gantt,
                avgWT,
                avgTAT,
                avgRT
        );
    }
}
