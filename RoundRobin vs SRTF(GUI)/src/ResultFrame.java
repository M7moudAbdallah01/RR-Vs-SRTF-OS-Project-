import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import Models.GanttRecord;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import Models.Process;
import Models.Result;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
public class ResultFrame extends javax.swing.JFrame {
    private Result rr;
    private Result srtf;

    public ResultFrame(Result rr, Result srtf) {
        initComponents();
        this.rr=rr;
        this.srtf=srtf;

        
       
        updateComparisonSummary();
        lblGanttRR.setText("RR Timeline: " + generateGanttString(rr.ganttChart));
        lblGanttSRTF.setText("SRTF Timeline: " + generateGanttString(srtf.ganttChart));
        lblReadyQueue.setText(generateReadyQueueString(rr.processes));
        timeSlotPanel_T0.setTransferHandler(new TransferHandler() {
    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

   @Override
public boolean importData(TransferSupport support) {
    try {
        String data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        JPanel p = (JPanel) support.getComponent();
        
        JLabel newProc = new JLabel(data);
        newProc.setBorder(new javax.swing.border.LineBorder(Color.BLACK));
        
        p.add(newProc);
        
        p.revalidate();
        p.repaint();
        JLabel lbl = new JLabel(data);
        lbl.setOpaque(true); 
        lbl.setBackground(new Color(0, 102, 204));
        lbl.setForeground(Color.WHITE);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT); 

        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        
        return true;
    } catch (Exception e) {
        return false;
    }
}

    });
        lblReadyQueue.setTransferHandler(new TransferHandler("text"));
        
        this.rr = rr;
        this.srtf = srtf;
        
        if (this.rr != null) {
            displayData();
            }
    }
private void displayData() {
    DefaultTableModel modelRR = (DefaultTableModel) tableRR.getModel();
    modelRR.setRowCount(0);
    for (Process p : rr.processes) {
        modelRR.addRow(new Object[]{"P"+p.id, p.arrivalTime, p.burstTime, p.completionTime, p.waitingTime, p.turnaroundTime, p.responseTime});
    }

    DefaultTableModel modelSRTF = (DefaultTableModel) tableSRTF.getModel();
    modelSRTF.setRowCount(0);
    for (Process p : srtf.processes) {
        modelSRTF.addRow(new Object[]{"P"+p.id, p.arrivalTime, p.burstTime, p.completionTime, p.waitingTime, p.turnaroundTime, p.responseTime});
    }
    
    lblAvgWaitRR.setText(String.format("%.2f", rr.avgWT));
    lblAvgWaitSRTF.setText(String.format("%.2f", srtf.avgWT));
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSRTF = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableRR = new javax.swing.JTable();
        lblAvgWaitRR = new javax.swing.JLabel();
        lblAvgWaitSRTF = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblRR_WT = new javax.swing.JLabel();
        lblSRTF_WT = new javax.swing.JLabel();
        lblWinnerWT = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblFinalWinner = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblRR_RT = new javax.swing.JLabel();
        lblSRTF_RT = new javax.swing.JLabel();
        lblWinnerRT = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblRR_TAT = new javax.swing.JLabel();
        lblSRTF_TAT = new javax.swing.JLabel();
        lblWinnerTAT = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblReadyQueue = new javax.swing.JLabel();
        lblGanttSRTF = new javax.swing.JLabel();
        lblGanttRR = new javax.swing.JLabel();
        timeSlotPanel_T0 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel2.setText("SRTF Results");

        tableSRTF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "PID", "AT", "BT", "CT", "WT", "TAT", "RT"
            }
        ));
        tableSRTF.setEnabled(false);
        jScrollPane1.setViewportView(tableSRTF);

        tableRR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "PID", "AT", "BT", "CT", "WT", "TAT", "RT"
            }
        ));
        tableRR.setAutoscrolls(false);
        tableRR.setEnabled(false);
        tableRR.setFocusable(false);
        tableRR.setUpdateSelectionOnSort(false);
        jScrollPane3.setViewportView(tableRR);

        lblAvgWaitRR.setText("lblAvgWaitRR");

        lblAvgWaitSRTF.setText("lblAvgWaitSRTF");

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Avg WT");

        jLabel7.setText("RR");

        jLabel8.setText("SRTF");

        lblRR_WT.setText("jLabel9");

        lblSRTF_WT.setText("jLabel10");

        lblWinnerWT.setText("jLabel9");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblRR_WT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(lblSRTF_WT))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblWinnerWT)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRR_WT)
                    .addComponent(lblSRTF_WT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblWinnerWT)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFinalWinner.setText("jLabel6");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblFinalWinner)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(lblFinalWinner)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("Avg RT");

        jLabel11.setText("RR");

        jLabel12.setText("SRTF");

        lblRR_RT.setText("jLabel9");

        lblSRTF_RT.setText("jLabel10");

        lblWinnerRT.setText("jLabel19");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblRR_RT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSRTF_RT))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblWinnerRT)
                            .addComponent(jLabel4))
                        .addGap(0, 56, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRR_RT)
                    .addComponent(lblSRTF_RT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblWinnerRT)
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Avg TAT");

        jLabel15.setText("RR");

        jLabel16.setText("SRTF");

        lblRR_TAT.setText("jLabel9");

        lblSRTF_TAT.setText("jLabel10");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel5)
                        .addGap(0, 56, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblRR_TAT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSRTF_TAT)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRR_TAT)
                    .addComponent(lblSRTF_TAT))
                .addGap(0, 24, Short.MAX_VALUE))
        );

        lblWinnerTAT.setText("jLabel10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(lblWinnerTAT)
                .addGap(76, 76, 76)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(175, 175, 175)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(402, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(lblWinnerTAT)
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(22, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel1.setText("Round Robin Results");

        lblReadyQueue.setText("Round Roben Ready Queue");
        lblReadyQueue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblReadyQueueMousePressed(evt);
            }
        });

        lblGanttSRTF.setText("jLabel9");

        lblGanttRR.setText("jLabel6");

        javax.swing.GroupLayout timeSlotPanel_T0Layout = new javax.swing.GroupLayout(timeSlotPanel_T0);
        timeSlotPanel_T0.setLayout(timeSlotPanel_T0Layout);
        timeSlotPanel_T0Layout.setHorizontalGroup(
            timeSlotPanel_T0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        timeSlotPanel_T0Layout.setVerticalGroup(
            timeSlotPanel_T0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(lblAvgWaitSRTF))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(timeSlotPanel_T0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGanttSRTF)
                            .addComponent(lblGanttRR)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(175, 175, 175)
                                .addComponent(jLabel2))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(320, 320, 320)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblAvgWaitRR))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblReadyQueue)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGanttRR)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGanttSRTF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblReadyQueue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(timeSlotPanel_T0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAvgWaitRR)
                    .addComponent(lblAvgWaitSRTF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jButton1)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
            new InputFrame().setVisible(true);
            this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void lblReadyQueueMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblReadyQueueMousePressed
        // TODO add your handling code here:
        JComponent c = (JComponent) evt.getSource();
    TransferHandler handler = c.getTransferHandler();
    handler.exportAsDrag(c, evt, TransferHandler.COPY);
    }//GEN-LAST:event_lblReadyQueueMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAvgWaitRR;
    private javax.swing.JLabel lblAvgWaitSRTF;
    private javax.swing.JLabel lblFinalWinner;
    private javax.swing.JLabel lblGanttRR;
    private javax.swing.JLabel lblGanttSRTF;
    private javax.swing.JLabel lblRR_RT;
    private javax.swing.JLabel lblRR_TAT;
    private javax.swing.JLabel lblRR_WT;
    private javax.swing.JLabel lblReadyQueue;
    private javax.swing.JLabel lblSRTF_RT;
    private javax.swing.JLabel lblSRTF_TAT;
    private javax.swing.JLabel lblSRTF_WT;
    private javax.swing.JLabel lblWinnerRT;
    private javax.swing.JLabel lblWinnerTAT;
    private javax.swing.JLabel lblWinnerWT;
    private javax.swing.JTable tableRR;
    private javax.swing.JTable tableSRTF;
    private javax.swing.JPanel timeSlotPanel_T0;
    // End of variables declaration//GEN-END:variables
private void updateComparisonSummary() {
    lblRR_WT.setText(String.format("%.2f", rr.avgWT));
    lblSRTF_WT.setText(String.format("%.2f", srtf.avgWT));
    lblWinnerWT.setText(rr.avgWT < srtf.avgWT ? "RR is Better" : "SRTF is Better");

    lblRR_TAT.setText(String.format("%.2f", rr.avgTAT));
    lblSRTF_TAT.setText(String.format("%.2f", srtf.avgTAT));
    lblWinnerTAT.setText(rr.avgTAT < srtf.avgTAT ? "RR is Better" : "SRTF is Better");

    lblRR_RT.setText(String.format("%.2f", rr.avgRT));
    lblSRTF_RT.setText(String.format("%.2f", srtf.avgRT));
    lblWinnerRT.setText(rr.avgRT < srtf.avgRT ? "RR is Better" : "SRTF is Better");

    if (rr.avgWT < srtf.avgWT) {
        lblFinalWinner.setText("Better Algorithm: Round Robin");
    } else {
        lblFinalWinner.setText("Better Algorithm: SRTF");
    }
}

class GanttPanel extends JPanel {
    private List<GanttRecord> records;

    public GanttPanel(List<GanttRecord> records) {
        this.records = records;
        this.setPreferredSize(new Dimension(500, 80));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (records == null || records.isEmpty()) return;

        int x = 20; 
        int y = 20; 
        int height = 30; 
        int scale = 15;  

        for (GanttRecord r : records) {
            int width = (r.endTime - r.startTime) * scale;
            
            g.drawRect(x, y, width, height);
            
            g.drawString("P" + r.processId, x + (width/4), y + 20);
            
            g.drawString(String.valueOf(r.startTime), x, y + height + 15);
            
            x += width;
            
            if (records.indexOf(r) == records.size() - 1) {
                g.drawString(String.valueOf(r.endTime), x, y + height + 15);
            }
        }
    }
}
private String generateGanttString(List<GanttRecord> gantt) {
    if (gantt == null || gantt.isEmpty()) return "No Data";
    
    StringBuilder sb = new StringBuilder("0");
    for (int i = 0; i < gantt.size(); i++) {
        GanttRecord current = gantt.get(i);
        int procId = current.processId;
        int end = current.endTime;

        while (i + 1 < gantt.size() && gantt.get(i + 1).processId == procId) {
            end = gantt.get(++i).endTime;
        }

        sb.append(" ➔ [P").append(procId).append("] ➔ ").append(end);
    }
    return sb.toString();
}
private String generateReadyQueueString(List<Process> processes) {
    if (processes == null || processes.isEmpty()) return "Queue: Empty";
    
    StringBuilder sb = new StringBuilder("Ready Queue: [ ");
    for (int i = 0; i < processes.size(); i++) {
        sb.append("P").append(processes.get(i).id);
        if (i < processes.size() - 1) {
            sb.append(" → ");
        }
    }
    sb.append(" ]");
    return sb.toString();
}

}



