// Copyright 2009-2011 Plant Bioinformatics Group, SCRI. All rights reserved.
// Use is subject to the accompanying licence terms.

package tablet.gui.viewer;

import java.awt.*;

import tablet.data.*;
import tablet.data.auxiliary.*;
import tablet.gui.*;

/**
 * Responsible for drawing outlines around either the current read-under-the-
 * mouse, or any other supplementary features that should be drawn outlined.
 */
class OutlinerOverlay implements IOverlayRenderer
{
	private ReadsCanvas rCanvas;

	private Read read;
	private int readS, readE;
	private int lineIndex;

	OutlinerOverlay(AssemblyPanel aPanel)
	{
		rCanvas = aPanel.readsCanvas;
	}

	void setRead(Read read, int colIndex, int lineIndex)
	{
		this.read = read;
		this.lineIndex = lineIndex;

		if (read != null)
		{
			// Start and ending positions (against consensus)
			readS = read.getStartPosition();
			readE = read.getEndPosition();
		}
	}

	public void render(Graphics2D g)
	{
		int offset = -rCanvas.offset * rCanvas.ntW;

		// Remember the current stroke so it can be reset afterwards
		Stroke oldStroke = g.getStroke();

		// Set the outline width based on the zoom level
		if (Prefs.visReadsCanvasZoom > 18)
			g.setStroke(new BasicStroke(3));
		else if (Prefs.visReadsCanvasZoom > 8)
			g.setStroke(new BasicStroke(2));

		g.setColor(new Color(10, 10, 100));

		// Deal with any additional features first
		for (VisualOutline f: rCanvas.contig.getOutlines())
		{
			if (f.type == VisualOutline.READ)
			{
				int xS = f.value1 * rCanvas.ntW + offset;
				int xE = f.value2 * rCanvas.ntW + rCanvas.ntW + offset;
				int y  = f.value3 * rCanvas.ntH;

				if (xS <= rCanvas.pX2 && xE > rCanvas.pX1)
					g.drawRect(xS, y, xE-xS-1, rCanvas.ntH-1);
			}

			else if (f.type == VisualOutline.COL)
			{
				int xS = f.value1 * rCanvas.ntW + offset;
				int xE = xS + rCanvas.ntW;

				if (xS <= rCanvas.pX2 && xE > rCanvas.pX1)
					g.drawRect(xS, 0, xE-xS-1, rCanvas.getHeight()-1);
			}

			else if (f.type == VisualOutline.ROW)
			{
				int yS = f.value1 * rCanvas.ntH;
				int yE = yS + rCanvas.ntH;

				if (yS <= rCanvas.pY2 && yE > rCanvas.pY1)
					g.drawRect(0, yS, rCanvas.canvasW-1, yE-yS-1);
			}
		}

		// Draw an outline around whatever read is under the mouse
		if (read != null)
		{
			int xS = readS * rCanvas.ntW + offset;
			int xE = readE * rCanvas.ntW + rCanvas.ntW + offset;
			int y  = lineIndex * rCanvas.ntH;

			if (Prefs.visOutlineAlpha)
			{
				g.setPaint(new Color(255, 255, 255, 75));
				g.fillRect(xS, y, xE-xS-1, rCanvas.ntH-1);
			}

			g.setColor(TabletUtils.red1);
			g.drawRect(xS, y, xE-xS-1, rCanvas.ntH-1);
		}

		g.setStroke(oldStroke);
	}
}