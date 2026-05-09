package com.mycompany.os_project.GUI;

import com.mycompany.os_project.Models.Process;
import com.mycompany.os_project.Models.Result;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ResultFrame extends JFrame {

    public ResultFrame(Result rr, Result srtf) {

        setTitle("CPU Scheduling Results");

        setSize(1300, 800);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(30, 30, 30));

        //------------------------------------------

        JTabbedPane tabs = new JTabbedPane();

        tabs.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        tabs.addTab(
                "Round Robin",
                createAlgorithmPanel(
                        rr,
                        "Round Robin"
                )
        );

        tabs.addTab(
                "SRTF",
                createAlgorithmPanel(
                        srtf,
                        "Shortest Remaining Time First"
                )
        );

        //------------------------------------------

        add(tabs);

        setVisible(true);
    }

    //------------------------------------------------------------

    private JPanel createAlgorithmPanel(
            Result result,
            String titleText
    ) {

        JPanel mainPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        mainPanel.setBackground(
                new Color(40, 40, 40)
        );

        //------------------------------------------
        JLabel title = new JLabel(
                titleText,
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 24)
        );

        title.setForeground(Color.WHITE);

        //------------------------------------------
        JPanel titlePanel = new JPanel(
                new BorderLayout()
        );

        titlePanel.setBackground(
                new Color(40, 40, 40)
        );

        titlePanel.add(
                title,
                BorderLayout.NORTH
        );

        //------------------------------------------
        JPanel averagesPanel = new JPanel();

        averagesPanel.setBackground(
                new Color(50, 50, 50)
        );

        averagesPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Performance Metrics",
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                16
                        ),
                        Color.WHITE
                )
        );

        averagesPanel.add(
                createMetricLabel(
                        "Average WT: " +
                        String.format("%.2f", result.avgWT)
                )
        );

        averagesPanel.add(
                createMetricLabel(
                        "Average TAT: " +
                        String.format("%.2f", result.avgTAT)
                )
        );

        averagesPanel.add(
                createMetricLabel(
                        "Average RT: " +
                        String.format("%.2f", result.avgRT)
                )
        );

        titlePanel.add(
                averagesPanel,
                BorderLayout.SOUTH
        );

        //------------------------------------------
        String[] columns = {
                "Process ID",
                "Arrival Time",
                "Burst Time",
                "Waiting Time",
                "Turnaround Time",
                "Response Time"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                );

        JTable table = new JTable(model);

        table.setRowHeight(28);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        table.setGridColor(Color.GRAY);

        //------------------------------------------

        for (Process p : result.processes) {

            model.addRow(
                    new Object[]{

                        "P" + p.id,

                        p.arrivalTime,

                        p.burstTime,

                        p.waitingTime,

                        p.turnaroundTime,

                        p.responseTime
                    }
            );
        }

        JScrollPane tableScroll =
                new JScrollPane(table);

        //------------------------------------------
        JPanel ganttContainer = new JPanel(
                new BorderLayout()
        );

        ganttContainer.setBackground(
                new Color(40, 40, 40)
        );

        ganttContainer.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        "Gantt Chart",
                        0,
                        0,
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                16
                        ),
                        Color.WHITE
                )
        );

        ganttContainer.add(
                new GanttChartPanel(result),
                BorderLayout.CENTER
        );

        //------------------------------------------
        mainPanel.add(
                titlePanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                tableScroll,
                BorderLayout.CENTER
        );

        mainPanel.add(
                ganttContainer,
                BorderLayout.SOUTH
        );

        return mainPanel;
    }

    //------------------------------------------------------------

    private JLabel createMetricLabel(String text) {

        JLabel label = new JLabel(text);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        return label;
    }
}
