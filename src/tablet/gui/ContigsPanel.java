package tablet.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import tablet.data.*;
import tablet.gui.viewer.*;

import scri.commons.gui.*;

class ContigsPanel extends JPanel implements ListSelectionListener
{
	private AssemblyPanel aPanel;
	private FeaturesPanel featuresPanel;
	private JTabbedPane ctrlTabs;

	private ContigsTableModel model;
	private JTable table;

	ContigsPanel(AssemblyPanel aPanel, JTabbedPane ctrlTabs)
	{
		this.aPanel = aPanel;
		this.ctrlTabs = ctrlTabs;

		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);

		setLayout(new BorderLayout());
		add(new JScrollPane(table));
	}

	void setFeaturesPanel(FeaturesPanel featuresPanel)
		{ this.featuresPanel = featuresPanel; }

	String getTitle(Assembly assembly)
	{
		if (assembly != null)
			return RB.format("gui.ContigsPanel.title", assembly.contigCount());
		else
			return RB.format("gui.ContigsPanel.title", 0);
	}

	void setAssembly(Assembly assembly)
	{
		table.setModel(new DefaultTableModel());
		if (assembly == null)
		{
			featuresPanel.setContig(null);
			return;
		}

		model = new ContigsTableModel(assembly, table);

		table.setModel(model);
		table.setRowSorter(new TableRowSorter<ContigsTableModel>(model));

		ctrlTabs.setTitleAt(0, getTitle(assembly));
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting())
			return;

		int row = table.getSelectedRow();

		if (row == -1)
		{
			// TODO: Update winMain with blank RHS split pane instead
			aPanel.setContig(null);
			featuresPanel.setContig(null);
		}
		else
		{
			// Convert from view->model (deals with user-sorted table)
			row = table.convertRowIndexToModel(row);

			// Then pull the contig out of the model and set...
			Contig contig = (Contig) model.getValueAt(row, 0);
			aPanel.setContig(contig);
			featuresPanel.setContig(contig);
		}
	}
}