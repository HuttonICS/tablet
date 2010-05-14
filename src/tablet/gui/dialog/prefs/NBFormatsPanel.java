// Copyright 2007-2009 Plant Bioinformatics Group, SCRI. All rights reserved.
// Use is subject to the accompanying licence terms.

package tablet.gui.dialog.prefs;

import java.awt.*;
import javax.swing.*;

import tablet.gui.*;

import scri.commons.gui.*;

class NBFormatsPanel extends JPanel
{
	public NBFormatsPanel()
    {
        initComponents();

        setBackground(Color.white);
        panel.setBackground(Color.white);

		panel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.dialog.prefs.NBFormatsPanel.panelTitle")));

		RB.setText(checkAceQA, "gui.dialog.prefs.NBFormatsPanel.checkAceQA");
		RB.setText(checkBamValidation, "gui.dialog.prefs.NBFormatsPanel.checkBamValidation");

		checkAceQA.setSelected(Prefs.ioAceProcessQA);
		checkBamValidation.setSelected(Prefs.ioBamValidationLenient);
    }

	public void applySettings()
	{
		Prefs.ioAceProcessQA = checkAceQA.isSelected();
		Prefs.ioBamValidationLenient = checkBamValidation.isSelected();
	}


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        checkAceQA = new javax.swing.JCheckBox();
        checkBamValidation = new javax.swing.JCheckBox();

        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Assembly import options:"));

        checkAceQA.setText("Trim poor quality reads using QA tags (ACE only)");

        checkBamValidation.setText("Set BAM validation stringency to lenient");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkAceQA)
                    .addComponent(checkBamValidation))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkAceQA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(checkBamValidation))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkAceQA;
    private javax.swing.JCheckBox checkBamValidation;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}