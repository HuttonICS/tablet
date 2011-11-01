package tablet.analysis;

import java.util.*;

import tablet.analysis.Finder.*;
import tablet.analysis.tasks.*;
import tablet.gui.viewer.*;

import tablet.gui.dialog.RestrictionEnzymeDialog.*;

public class EnzymeFinderTask extends BackgroundTask
{
	private ArrayList<ArrayList<Finder>> finders;

	// Create an EnzymeFinder for each enzymes in the provided ArrayList and
	// add it to a list of finders to be run as a background task
	public EnzymeFinderTask(ArrayList<RestrictionEnzyme> enzymes, AssemblyPanel aPanel)
	{
		finders = new ArrayList<ArrayList<Finder>>();

		for (RestrictionEnzyme enzyme : enzymes)
		{
			ArrayList<Finder> enzymeFinder = new ArrayList<Finder>();

			for (String sequence : enzyme.getSequences())
				enzymeFinder.add(new Finder(aPanel, sequence, false, Finder.ENZYME));

			finders.add(enzymeFinder);
		}
	}

	@Override
	protected void doCleanup()
	{
		finders = null;
	}

	@Override
	public void run()
	{
		try
		{
			for (ArrayList<Finder> enzymeFinder : finders)
				for (Finder f : enzymeFinder)
					f.runJob(0);

			notifyAndFinish();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<ArrayList<ArrayList<SearchResult>>> getResults()
	{
		ArrayList<ArrayList<ArrayList<SearchResult>>> results = new ArrayList<ArrayList<ArrayList<SearchResult>>>();

		for (ArrayList<Finder> enzymeFinder : finders)
		{
			// The results for a single RestrictionEnzyme (commonly more than one
			// sequence so more than one set of reuslts)
			ArrayList<ArrayList<SearchResult>> enzymeResults = new ArrayList<ArrayList<SearchResult>>();

			for (Finder f : enzymeFinder)
				enzymeResults.add(f.getResults());

			results.add(enzymeResults);
		}

		return results;
	}
}