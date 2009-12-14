// Copyright 2009 Plant Bioinformatics Group, SCRI. All rights reserved.
// Use is subject to the accompanying licence terms.

package tablet.analysis;

import java.io.*;
import javax.swing.*;

import tablet.data.*;
import tablet.gui.*;
import tablet.gui.dialog.*;

import scri.commons.gui.*;

public class CoveragePrinter extends SimpleJob
{
	private File file;
	private Assembly assembly;
	private Contig contig;

	public CoveragePrinter(File file, Assembly assembly)
	{
		this.assembly = assembly;
		this.file = file;
	}

	public void runJob(int jobIndex)
		throws Exception
	{
		for (Contig contig: assembly)
			maximum++;

		BufferedWriter out = new BufferedWriter(new FileWriter(file));

		for (Contig contig: assembly)
		{
			if (!okToRun)
				break;

			this.contig = contig;

			out.write(contig.getName());
			out.newLine();

			CoverageCalculator cc = new CoverageCalculator(contig);
			cc.runJob(0);

			int[] coverage = cc.getCoverage();
			int s = contig.getConsensusOffset();
			int e = s + contig.getConsensus().length() - 1;

			int c = 0;
			for (int i = s; i <= e && okToRun; i++, c++)
			{
				out.write(" " + coverage[i]);
				if (c == 49)
				{
					out.newLine();
					c = -1;
				}
			}

			if (c > 0)
				out.newLine();

			progress++;
		}

		out.close();
	}

	public String getMessage()
		{ return "Processing " + contig.getName(); }
}