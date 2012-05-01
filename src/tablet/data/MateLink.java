package tablet.data;

/**
 * Dummy class used to represent pair link lines in the PairedPack.
 * With the new screen pixel based rendering system there was no sensbile way
 * for the PairedPack to know when to draw pair link lines. The MateLink dummy
 * read concept was born and solves this problem neatly by having reads which
 * represent the pair link lines within the PairedPack.
 */
public class MateLink extends Read
{
	public MateLink(int id, int position)
	{
		super(id, position);
	}
}
