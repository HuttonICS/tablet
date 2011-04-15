// Copyright 2009-2011 Plant Bioinformatics Group, SCRI. All rights reserved.
// Use is subject to the accompanying licence terms.

package tablet.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import scri.commons.gui.*;

import tablet.analysis.*;
import tablet.data.*;
import tablet.data.auxiliary.*;
import tablet.gui.viewer.*;

public class ReadsPanel extends JPanel implements ListSelectionListener, ActionListener
{
	private ReadsPanelNB controls;
	private ReadsTableModel tableModel;
	private TableRowSorter<AbstractTableModel> sorter;
	private AssemblyPanel aPanel;
	private Contig contig;
	
	private JMenuItem mClipboardName, mClipboardData;
	private JMenuItem mFindStart, mFindEnd, mJumpToPair;

	ReadsPanel(AssemblyPanel aPanel, JTabbedPane ctrlTabs)
	{
		this.aPanel = aPanel;
		controls = new ReadsPanelNB(this);

		// Additional (duplicate) table-clicked handler to catch the user
		// re-clicking on the same row. This doesn't generate a table event, but
		// we still want to respond to it and highlight the selection againm
		controls.table.addMouseListener(new TableMouseListener());

		setLayout(new BorderLayout());
		add(controls);

		toggleComponentEnabled(false);
	}

	void setTableModel(List<Read> reads)
	{
		// Note: the model is created to be non-editable
		tableModel = new ReadsTableModel(reads);

		sorter = new TableRowSorter<AbstractTableModel>(tableModel);
		controls.table.setModel(tableModel);
		controls.table.setRowSorter(sorter);
		controls.readsLabel.setText(RB.format("gui.ReadsPanel.readsLabel", reads.size()));
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;

		processTableSelection();
	}

	// Highlights reads and updates read info labels.
	// Returns null if no read found. This only happens on a de-selection click
	private void processTableSelection()
	{
		if (controls.table.getModel().getRowCount() == 0)
			return;

		Read read = getReadFromTable();
		if (read == null)
			return;

		// Highlight read on screen
		int lineIndex = contig.getReadManager().getLineForRead(read);
		new ReadHighlighter(aPanel, read, lineIndex);

		updateReadInfo(read);
	}

	String getTableToolTip(MouseEvent e)
	{
		int row = controls.table.rowAtPoint(e.getPoint());
		row = controls.table.convertRowIndexToModel(row);

		String name = (String) tableModel.getValueAt(row, 0);
		int pos = (Integer) tableModel.getValueAt(row, 1);
		int len = (Integer) tableModel.getValueAt(row, 2);

		return RB.format("gui.ReadsPanel.tooltip", name,
			TabletUtils.nf.format(pos), TabletUtils.nf.format(len));
	}

	 // Update the labels displaying information on the currently highlighted read
	 // in the Reads table.
	private void updateReadInfo(Read read)
	{
		ReadNameData rnd = Assembly.getReadNameData(read);
		ReadMetaData rmd = Assembly.getReadMetaData(read, false);

		String cigar = Assembly.hasCigar() ? rnd.getCigar() : "";
		controls.cigarLabel.setText(cigar);

		if(read instanceof MatedRead)
		{
			int mPos = ((MatedRead)read).getMatePos();

			String properlyPaired = rnd.isProperPair() ? RB.getString("gui.ReadsPanel.properlyPaired.yes") : RB.getString("gui.ReadsPanel.properlyPaired.no");
			controls.properlyPairedLabel.setText(properlyPaired);

			String numberInPair = (rmd.getNumberInPair() == 1) ? RB.getString("gui.ReadsPanel.numberInPair.one") : RB.getString("gui.ReadsPanel.numberInPair.two");
			controls.numberInPairLabel.setText(numberInPair);

			String insertSize = (mPos != -1) ? TabletUtils.nf.format(rnd.getInsertSize()) : "";
			controls.insertSizeLabel.setText(insertSize);

			String matePos = (mPos != -1) ? TabletUtils.nf.format(mPos) : "";
			controls.matePosLabel.setText(matePos);

			String mateContig = rnd.getMateContig();
			controls.mateContigLabel.setText(mateContig);

			if (rmd.getIsPaired() && !rmd.getMateMapped())
			{
				controls.matePosLabel.setText("");
				controls.numberInPairLabel.setText("");
				controls.mateContigLabel.setText(RB.getString("gui.ReadsPanel.mContigLabel.mateUnmapped"));
			}
		}
	}

	void setAssembly(Assembly assembly)
	{
		if (assembly == null)
			toggleComponentEnabled(false);
	}

	void setContig(Contig contig)
	{
		if (contig != null)
		{
			this.contig = contig;
			controls.setLabelStates(Assembly.hasCigar(), Assembly.isPaired());
		}

		else
		{
			clear();

			tableModel = null;
			controls.table.setModel(new DefaultTableModel());
			controls.table.setRowSorter(null);
		}

		controls.readsLabel.setText(RB.format("gui.ReadsPanel.readsLabel", 0));
	}

	 // Clears the table of read info and sets labels to their default state.
	void clear()
	{
		if (tableModel != null)
			tableModel.clear();

		controls.setReadInfoToDefaults();
		controls.readsLabel.setText(RB.format("gui.ReadsPanel.readsLabel",
			RB.getString("gui.ReadsPanel.readsLabel.waiting")));

		controls.table.repaint();
	}

	// Retrieves a read from the currently selected row of the table.
	private Read getReadFromTable()
	{
		int row = controls.table.getSelectedRow();

		if (row == -1)
			return null;

		// Convert from view->model (deals with user-sorted table)
		row = controls.table.convertRowIndexToModel(row);
		return tableModel.getRead(row);
	}

	// Copies the name of the given read to the clipboard.
	private void copyReadNameToClipboard()
	{
		Read read = getReadFromTable();
		StringSelection selection = new StringSelection(Assembly.getReadName(read));
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
			selection, null);
	}

	// Copies the data relating to the given read to the clipboard.
	private void copyDataToClipboard()
	{
		Read read = getReadFromTable();

		ReadMetaData rmd = Assembly.getReadMetaData(read, false);
		ReadNameData rnd = Assembly.getReadNameData(read);

		String lb = System.getProperty("line.separator");
		String seq = rmd.toString();

		StringBuilder text = new StringBuilder(seq.length() + 500);
		text.append(rnd.getName()).append(lb);

		// Position
		int readS = read.getStartPosition();
		int readE = read.getEndPosition();

		String pos = RB.format("gui.ReadsPanel.from",
			(TabletUtils.nf.format(readS+1) + " U" + DisplayData.paddedToUnpadded(readS+1)),
			(TabletUtils.nf.format(readE+1) + " U" + DisplayData.paddedToUnpadded(readE+1)));

		text.append(pos).append(lb);

		// Length
		String length;
		if (Prefs.visHideUnpaddedValues)
			length = RB.format("gui.ReadsPanel.length",
				TabletUtils.nf.format(read.length()));
		else
			length = RB.format("gui.ReadsPanel.lengthUnpadded",
				TabletUtils.nf.format(read.length()),
				TabletUtils.nf.format(rnd.getUnpaddedLength()));

		text.append(length).append(lb);

		// Cigar
		String cigar = "";
		if (Assembly.hasCigar())
		{
			cigar = RB.format("gui.ReadsPanel.cigar", rnd.getCigar());
			text.append(cigar).append(lb);
		}

		if (rmd.isComplemented())
			text.append(RB.getString("gui.ReadsPanel.directionReverse")).append(lb).append(lb);
		else
			text.append(RB.getString("gui.ReadsPanel.directionForward")).append(lb).append(lb);

		// Produce a FASTA formatted string
		text.append(TabletUtils.formatFASTA(rnd.getName(), seq));

		StringSelection selection = new StringSelection(text.toString());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
			selection, null);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == controls.exportLinkLabel)
		{
			try
			{
				ReadsCanvas rCanvas = aPanel.getReadsCanvas();
				Tablet.winMain.getCommands().exportScreenData(contig.getReadManager(), rCanvas.getXS(), rCanvas.getXE(), rCanvas.getYS(), rCanvas.getYE());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		else if (e.getSource() == mClipboardName)
			copyReadNameToClipboard();
		
		else if (e.getSource() == mClipboardData)
			copyDataToClipboard();
		
		else if (e.getSource() == mFindStart)
		{
			Read read = getReadFromTable();
			highlightReadAtPosition(read, read.getStartPosition());
		}
		
		else if (e.getSource() == mFindEnd)
		{
			Read read = getReadFromTable();
			highlightReadAtPosition(read, read.getEndPosition());
		}
		
		else if (e.getSource() == mJumpToPair)
		{
			Read read = getReadFromTable();
			
			if (read instanceof MatedRead)
			{
				int matePos = ((MatedRead)read).getMatePos();
				aPanel.moveToPosition(0, matePos, true);

				try
				{
					PairSearcher pairSearcher = new PairSearcher(contig);
					// Must use searchForPair instead of search as DB query
					// cannot be relied upon once the reads have been sorted
					final Read found = pairSearcher.searchForPair(Assembly.getReadName(read), matePos);
					final int lineIndex = contig.getReadManager().getLineForRead(found);

					SwingUtilities.invokeLater(new Runnable() {
						public void run()
						{
							aPanel.moveToPosition(lineIndex, found.getStartPosition(), true);
							new ReadHighlighter(aPanel, found, lineIndex);
						}
					});
				}
				catch(Exception ex) {}
			}
		}
	}

	private void highlightReadAtPosition(Read read, int position)
	{
		int lineIndex = contig.getReadManager().getLineForRead(read);
		
		aPanel.moveToPosition(lineIndex, position, true);
		new ReadHighlighter(aPanel, read, lineIndex);
	}

	private void displayMenu(MouseEvent e)
	{
		mClipboardName = new JMenuItem("", Icons.getIcon("CLIPBOARDNAME"));
		RB.setText(mClipboardName, "gui.viewer.ReadsCanvasMenu.mClipboardName");
		mClipboardName.addActionListener(this);

		mClipboardData = new JMenuItem("", Icons.getIcon("CLIPBOARD"));
		RB.setText(mClipboardData, "gui.viewer.ReadsCanvasMenu.mClipboardData");
		mClipboardData.addActionListener(this);
		
		JMenu mJumpTo = new JMenu("");
		RB.setText(mJumpTo, "gui.viewer.ReadsCanvasMenu.mJumpTo");
		
		mFindStart = new JMenuItem("", Icons.getIcon("START16"));
		RB.setText(mFindStart, "gui.viewer.ReadsCanvasMenu.mJumpToReadStart");
		mFindStart.addActionListener(this);

		mFindEnd = new JMenuItem("", Icons.getIcon("END16"));
		RB.setText(mFindEnd, "gui.viewer.ReadsCanvasMenu.mJumpToReadEnd");
		mFindEnd.addActionListener(this);

		mJumpToPair = new JMenuItem("");
		RB.setText(mJumpToPair, "gui.viewer.ReadsCanvasMenu.mJumpToPair");
		mJumpToPair.addActionListener(this);

		mJumpTo.add(mFindStart);
		mJumpTo.add(mFindEnd);
		mJumpTo.add(mJumpToPair);
		
		JPopupMenu menu = new JPopupMenu();
		menu.add(mClipboardName);
		menu.add(mClipboardData);
		menu.addSeparator();
		menu.add(mJumpTo);

		// Set visibility on mJumpToPair
		int row = controls.table.rowAtPoint(e.getPoint());
		controls.table.setRowSelectionInterval(row, row);

		Read read = getReadFromTable();
		ReadMetaData rmd = Assembly.getReadMetaData(read, false);

		if (!rmd.getMateMapped())
			mJumpToPair.setEnabled(false);
		
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

	void toggleComponentEnabled(boolean enabled)
	{
		controls.toggleComponentEnabled(enabled);
	}

	private class TableMouseListener extends MouseInputAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if (!e.isPopupTrigger())
				processTableSelection();
		}

		public void mousePressed(MouseEvent e)
		{
			if (e.isPopupTrigger())
				displayMenu(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			if (e.isPopupTrigger())
				displayMenu(e);
		}
	}
}