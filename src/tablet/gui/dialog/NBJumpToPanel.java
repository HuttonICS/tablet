package tablet.gui.dialog;

import java.awt.*;
import javax.swing.*;

import scri.commons.gui.*;

class NBJumpToPanel extends javax.swing.JPanel
{
	public NBJumpToPanel(JumpToDialog parent, int defaultValue)
	{
		initComponents();

		bJumpPadded.addActionListener(parent);
		bJumpUnpadded.addActionListener(parent);

		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		RB.setText(jumpLabel, "gui.dialog.NBJumpToPanel.jumpLabel");
		RB.setText(bJumpPadded, "gui.dialog.NBJumpToPanel.bJumpPadded");
		RB.setText(bJumpUnpadded, "gui.dialog.NBJumpToPanel.bJumpUnpadded");
		RB.setText(descriptionLabel1, "gui.dialog.NBJumpToPanel.descriptionLabel1");
		RB.setText(descriptionLabel2, "gui.dialog.NBJumpToPanel.descriptionLabel2");

		jumpField.setText("" + defaultValue);
		jumpField.getDocument().addDocumentListener(parent);

		bJumpUnpadded.setEnabled(false);
	}

	String getInputText()
	{
		return jumpField.getText();
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jumpLabel = new javax.swing.JLabel();
        jumpField = new javax.swing.JTextField();
        bJumpPadded = new javax.swing.JButton();
        bJumpUnpadded = new javax.swing.JButton();
        descriptionLabel1 = new javax.swing.JLabel();
        descriptionLabel2 = new javax.swing.JLabel();

        jumpLabel.setLabelFor(jumpField);
        jumpLabel.setText("Jump to base:");

        jumpField.setColumns(8);

        bJumpPadded.setText("Padded Jump");

        bJumpUnpadded.setText("Unpadded Jump");

        descriptionLabel1.setText("descriptionLabel1");

        descriptionLabel2.setText("descriptionLabel2");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jumpLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jumpField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(bJumpPadded)
                        .add(6, 6, 6)
                        .add(bJumpUnpadded))
                    .add(descriptionLabel1)
                    .add(descriptionLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jumpLabel)
                    .add(bJumpPadded)
                    .add(bJumpUnpadded)
                    .add(jumpField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(descriptionLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(descriptionLabel2)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton bJumpPadded;
    javax.swing.JButton bJumpUnpadded;
    private javax.swing.JLabel descriptionLabel1;
    private javax.swing.JLabel descriptionLabel2;
    private javax.swing.JTextField jumpField;
    private javax.swing.JLabel jumpLabel;
    // End of variables declaration//GEN-END:variables

}