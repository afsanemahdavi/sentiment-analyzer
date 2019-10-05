package mark;

import java.util.ArrayList;
import java.util.List;

import tags.Groups;
import tags.Tag;

public class TagToken
{
	private String word;
	private Tag tag;
	private int ingroupCount=0;
	
	private List<Groups> groupsdone = new ArrayList<Groups>();
	
	public TagToken(Tag t,String word)
	{
		this.tag = t;
		this.word = word;
	}
	public Tag getTag()
	{
		return this.tag;
	}
	public String getToken()
	{
		return this.word;
	}
	public boolean isIngroup() {
		return ingroupCount>0?true:false;
	}
	public int getIngroupCount() {
		return ingroupCount;
	}
	public TagToken updateIngroup() {
		this.ingroupCount ++;
		return this;
	}
	public boolean canGetGroups()
	{
		if(tag.getIngroupcount()>ingroupCount)
			return true;
		return false;
	}
	public void preConcat(String s)
	{
		if(word==null)
			return;
		word = s.concat(word);
	}
	public void postConcat(String s)
	{
		if(word==null)
			return;
		word=word.concat(s);
	}
	public String getWord()
	{
		return this.word;
	}
	public boolean isGroupedDone(Groups g) {
		return groupsdone.contains(g);
	}
	public void setGroupedDone(Groups g) {
		List<Groups> gs = this.tag.getGroups();
		if(gs!=null&&gs.contains(g))
			this.groupsdone.add(g);
	}
}
