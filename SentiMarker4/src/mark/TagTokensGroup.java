package mark;

import java.util.ArrayList;
import java.util.List;

import tags.Groups;

public class TagTokensGroup {
	
	private List<TagTokensGroup> ttglist = new ArrayList<TagTokensGroup>();
	private TagToken root;
	private List<TagToken> ttlist = new ArrayList<TagToken>();
	private Groups group = null;
	public TagTokensGroup(TagToken root,Groups g) {
		this.root = root;
		this.group = g;
	}
	public void addTagTokens(TagToken tt)
	{
		this.ttlist.add(tt);
	}
	public List<TagToken> getTagTokens()
	{
		return ttlist;
	}
	public TagToken getRoot()
	{
		return root;
	}
	public void updateTtg(TagTokensGroup ttg)
	{
		ttglist.add(ttg);
	}
	public List<TagTokensGroup> getTtglist()
	{
		return this.ttglist;
	}
	public Groups getGroup()
	{
		return this.group;
	}
}
