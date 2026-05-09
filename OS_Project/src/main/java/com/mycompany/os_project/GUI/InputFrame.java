package com.mycompany.os_project.GUI;

import com.mycompany.os_project.Models.Process;
import com.mycompany.os_project.Models.Result;
import com.mycompany.os_project.Schedulers.RoundRobinScheduler;
import com.mycompany.os_project.Schedulers.SRTFScheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class InputFrame extends JFrame {

    private JTextField numProcessesField;
    private JTextField quantumField;

    private JTable processTable;

    private JButton generateButton;
    private JButton runButton;
    private JButton resetButton;

    public InputFrame() {

        setTitle("CPU Scheduling Comparison");

        setSize(1100, 750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    //-------------------------------------------------------

    private void initComponents() {

        Color background = new Color(30, 30, 30);
        Color panelColor = new Color(45, 45, 45);
        Color buttonColor = new Color(0, 120, 215);

        Font mainFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 28);

        //------------------------------------------
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(background);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //------------------------------------------
        JLabel title = new JLabel("CPU Scheduling Comparison", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);

        //------------------------------------------
        JPanel inputPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        inputPanel.setBackground(panelColor);
        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Input Section",
                        0,
                        0,
                        new Font("Segoe UI", Font.BOLD, 18),
                        Color.WHITE
                )
        );

        //------------------------------------------
        numProcessesField = new JTextField();
        numProcessesField.setFont(mainFont);

        quantumField = new JTextField();
        quantumField.setFont(mainFont);

        //------------------------------------------
        generateButton = createStyledButton("Generate Table", buttonColor);
        runButton = createStyledButton("Run Simulator", buttonColor);
        resetButton = createStyledButton("Reset", Color.RED);

        //------------------------------------------
        inputPanel.add(createStyledLabel("Number of Processes:"));
        inputPanel.add(numProcessesField);
        inputPanel.add(generateButton);

        inputPanel.add(createStyledLabel("Quantum:"));
        inputPanel.add(quantumField);
        inputPanel.add(runButton);

        //------------------------------------------
        String[] columns = {
                "Process ID",
                "Arrival Time",
                "Burst Time"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        processTable = new JTable(model);

        processTable.setFont(mainFont);
        processTable.setRowHeight(28);

        processTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        JScrollPane tableScroll = new JScrollPane(processTable);

        //------------------------------------------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(background);

        bottomPanel.add(resetButton);

        //------------------------------------------
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        //------------------------------------------
        add(mainPanel);

        //------------------------------------------
        generateButton.addActionListener(e -> generateTable());

        runButton.addActionListener(e -> runSimulation());

        resetButton.addActionListener(e -> resetFields());
    }

    //-------------------------------------------------------

    private JLabel createStyledLabel(String text) {

        JLabel label = new JLabel(text);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        return label;
    }

    //-------------------------------------------------------

    private JButton createStyledButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        return button;
    }

    //-------------------------------------------------------

    private void generateTable() {

        try {

            int count = Integer.parseInt(
                    numProcessesField.getText().trim()
            );

            if (count <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Number of processes must be greater than 0!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            DefaultTableModel model =
                    (DefaultTableModel) processTable.getModel();

            model.setRowCount(0);

            for (int i = 1; i <= count; i++) {

                model.addRow(
                        new Object[]{
                                "P" + i,
                                "",
                                ""
                        }
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid positive integer!",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //-------------------------------------------------------

    private void runSimulation() {

        try {

            //--------------------------------------
            String quantumText =
                    quantumField.getText().trim();

            if (quantumText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantum field cannot be empty!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            int quantum =
                    Integer.parseInt(quantumText);

            if (quantum <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantum must be greater than 0!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            //--------------------------------------
            DefaultTableModel model =
                    (DefaultTableModel) processTable.getModel();

            if (model.getRowCount() == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please generate process table first!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            //--------------------------------------
            ArrayList<Process> rrProcesses = new ArrayList<>();
            ArrayList<Process> srtfProcesses = new ArrayList<>();

            for (int i = 0; i < model.getRowCount(); i++) {

                Object arrivalObj = model.getValueAt(i, 1);
                Object burstObj = model.getValueAt(i, 2);

                if (arrivalObj == null || burstObj == null ||
                        arrivalObj.toString().trim().isEmpty() ||
                        burstObj.toString().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "All Arrival and Burst fields are required!",
                            "Missing Data",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                int arrival =
                        Integer.parseInt(arrivalObj.toString());

                int burst =
                        Integer.parseInt(burstObj.toString());

                //----------------------------------

                if (arrival < 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Arrival Time cannot be negative!",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                if (burst <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Burst Time must be greater than 0!",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                //----------------------------------

                rrProcesses.add(
                        new Process(
                                i + 1,
                                arrival,
                                burst
                        )
                );

                srtfProcesses.add(
                        new Process(
                                i + 1,
                                arrival,
                                burst
                        )
                );
            }

            //--------------------------------------
            Result rrResult =
                    RoundRobinScheduler.run(
                            rrProcesses,
                            quantum
                    );

            Result srtfResult =
                    SRTFScheduler.run(
                            srtfProcesses
                    );

            //--------------------------------------
            ResultFrame resultFrame =
                    new ResultFrame(
                            rrResult,
                            srtfResult
                    );

            resultFrame.setVisible(true);

            dispose();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Arrival/Burst/Quantum must be valid integers!",
                    "Type Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected Error:\n" + e.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    //-------------------------------------------------------

    private void resetFields() {

        numProcessesField.setText("");

        quantumField.setText("");

        DefaultTableModel model =
                (DefaultTableModel) processTable.getModel();

        model.setRowCount(0);
    }
}