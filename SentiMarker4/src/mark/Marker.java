package mark;

import java.util.ArrayList;
import java.util.List;

import Sentiment.Dictinary;

import tags.Groups;
import tags.JoinsTo;
import tags.Tag;
import tags.Tags;
import tags.TagsConf;

public class Marker {
	
	private List<TagToken> tagtokens = new ArrayList<TagToken>();
	private List<TagTokensGroup> ttglist = new ArrayList<TagTokensGroup>();
	private Tags tags = TagsConf.getConf();
	
	public void mark(String sentence)
	{
		String tokens[] = sentence.split(" ");
		for(String token:tokens)
		{
			processToken(token);
		}
	}
	private void addToken(TagToken tg)
	{
		this.tagtokens.add(tg);
	}
	private void processToken(String tokens)
	{
		String sub_token[] = tokens.split(tags.getSplitter());
		if(sub_token.length!=2)
			return;
		Tag t = tags.getTag(sub_token[1]);
		if(t==null)
			return;
		if(t.hasJoins())
		{
			for(JoinsTo jt: t.getJoinsto())
			{
				if(jt.isForward())
				{
					for(int i=tagtokens.size()-1;i>=0;i--)
					{
						JoinsTo jttmp = tagtokens.get(i).getTag().mutualJoins(t);
						if(jttmp!=null)
						{
							tagtokens.get(i).postConcat(jttmp.getGap()+sub_token[0]);
							return;
						}
					}
					addToken(new TagToken(t,sub_token[0]));
					return;
				}
				if(jt.isBackward())
				{
					for(String mytag:t.getJoins())
					{
						Tag current_tag = tags.getTag(mytag);
						for(int i=tagtokens.size()-1;i>=0;i--)
						{
							if(tagtokens.get(i).getTag().equals(current_tag));
								tagtokens.get(i).postConcat(sub_token[0]);
						}
					}
					return;
				}
			}
		}
		for(int i=tagtokens.size()-1;i>=0;i--)
		{
			if(tagtokens.get(i).getTag().getJoinsto()==null)
				continue;
			for(JoinsTo jt:tagtokens.get(i).getTag().getJoinsto())
			{
				if(jt.isForward()&&jt.joinsTo(t))
				{
					TagToken tg = new TagToken(t, sub_token[0]);
					tg.preConcat(tagtokens.get(i).getWord()+jt.getGap());
					addToken(tg);
					tagtokens.remove(i);
					return;
				}
			}
		}
		addToken(new TagToken(t,sub_token[0]));
	}
	public void reduceTokens()
	{
		List<TagToken> tmp_tagtokens = new ArrayList<TagToken>();
		for(TagToken tg :tagtokens)
		{
			if(!tg.getTag().isImp())
				tmp_tagtokens.add(tg);
		}
		tagtokens.removeAll(tmp_tagtokens);
	}
	public void makeOneWayGroups()
	{
		singlesGrouping();
		noForwardGrouping();
	}
	public void makeTwoWayGroups()
	{
		singlesGrouping();
		if(tags.getPrefdir().equals(Tag.BACKWARD))
		{
			forwardGrouping();
			backWardGrouping();
			return;
		}
		forwardGrouping();
		backWardGrouping();
	}
	
	public void printOrderdTokens()
	{
		System.out.println(getOrderdTokens(this.tagtokens));
	}
	private String getOrderdTokens(List<TagToken> tagtokens)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<tagtokens.size();i++)
			sb.append(tagtokens.get(i).getTag().getName())
			.append("(")
			.append(tagtokens.get(i).getWord())
			.append(")");
		return sb.toString();
	}
	public void printGroups()
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<ttglist.size();i++)
		{
			sb.append(getGroupsRecursive(ttglist.get(i)));
		}
		System.err.println(sb);
	}
	private String getGroupsRecursive(TagTokensGroup ttg)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(ttg.getRoot().getTag().getName())
		.append("[")
		.append(ttg.getRoot().getWord())
		.append("]")
		.append("(");
		List<TagTokensGroup> tmplist = ttg.getTtglist();
		for(int i=0;i<tmplist.size();i++)
		{
			sb.append(getGroupsRecursive(tmplist.get(i)));
		}
		sb.append(getOrderdTokens(ttg.getTagTokens()));
		sb.append(")");
		return sb.toString();
	}
	public void singlesGrouping()
	{
		for(int i=0;i<tagtokens.size();i++)
		{
			if(!tagtokens.get(i).getTag().makesGroups())
			{
				if(tagtokens.get(i).canGetGroups())
					ttglist.add(new TagTokensGroup(tagtokens.get(i),null));
				continue;
			}
			List<Groups> groups = tagtokens.get(i).getTag().getGroups();
			for(int j=0;j<groups.size();j++)
			{
				if(groups.get(j).getCount()==-1)
				{
					ttglist.add(new TagTokensGroup(tagtokens.get(i),groups.get(j)));
					continue;
				}
				int grouped = 0;
				TagTokensGroup ttg = new TagTokensGroup(tagtokens.get(i),groups.get(j));
				if(groups.get(j).isForward())
				{
					for(int c=i+1;c<tagtokens.size();c++)
					{
						if(groups.get(j).groups(tagtokens.get(c).getTag()))
						{
							if(tagtokens.get(c).canGetGroups())
							{
								ttg.addTagTokens(tagtokens.get(c).updateIngroup());
								grouped++;
							}
							if(groups.get(j).getCount()<=grouped)
							{
								tagtokens.get(i).setGroupedDone(groups.get(j));
								break;
							}
						}
					}
					ttglist.add(ttg);
				}
				else if(groups.get(j).isBackward())
				{
					for(int c=i-1;c>=0;c--)
					{
						if(groups.get(j).groups(tagtokens.get(c).getTag()))
						{
							if(tagtokens.get(c).canGetGroups())
							{
								ttg.addTagTokens(tagtokens.get(c).updateIngroup());
								grouped++;
							}
							if(groups.get(j).getCount()<=grouped)
							{
								tagtokens.get(i).setGroupedDone(groups.get(j));
								break;
							}
						}
					}
					ttglist.add(ttg);
				}
			}
		}
	}
	public void noForwardGrouping()
	{
		List<TagTokensGroup> tmpttglist = new ArrayList<TagTokensGroup>();
		boolean is_there_update = true;
		while(ttglist.size()>1&&is_there_update)
		{
			is_there_update = false;
			tmpttglist.clear();
			for(int i=0;i<ttglist.size();i++)
			{
				if(!ttglist.get(i).getRoot().getTag().makesGroups())
					continue;
				if(ttglist.get(i).getGroup().getCount()!=-1
						||ttglist.get(i).getRoot().isGroupedDone(ttglist.get(i).getGroup()))
					continue;
				if(ttglist.get(i).getGroup().isForward())
				{
					for(int c=i;c<ttglist.size();c++)
					{
						ttglist.get(i).updateTtg(ttglist.get(c));
						tmpttglist.add(ttglist.get(c));
						is_there_update = true;
					}
				}
				else if(ttglist.get(i).getGroup().isBackward())
				{
					for(int c=i-1;c>=0;c--)
					{
						ttglist.get(i).updateTtg(ttglist.get(c));
						tmpttglist.add(ttglist.get(c));
						is_there_update = true;
					}
				}
				ttglist.get(i).getRoot().setGroupedDone(ttglist.get(i).getGroup());
				ttglist.removeAll(tmpttglist);
				if(is_there_update)
					break;
			}
		}
	}
	private void backWardGrouping()
	{
		List<TagTokensGroup> tmpttglist = new ArrayList<TagTokensGroup>();
		boolean is_there_update = true;
		while(ttglist.size()>1&&is_there_update)
		{
			is_there_update = false;
			tmpttglist.clear();
			for(int i=0;i<ttglist.size();i++)
			{
				if(!ttglist.get(i).getRoot().getTag().makesGroups())
					continue;
				if(ttglist.get(i).getGroup().getCount()!=-1
						||ttglist.get(i).getGroup().isForward()
						||ttglist.get(i).getRoot().isGroupedDone(ttglist.get(i).getGroup()))
					continue;
				for(int c=i-1;c>=0;c--)
				{
					if(tags.getPrefdir().equals(Tag.FORWARD)&&ttglist.get(c).getRoot().getTag().hasForWardGroup())
						break;
					ttglist.get(i).updateTtg(ttglist.get(c));
					tmpttglist.add(ttglist.get(c));
					is_there_update = true;
				}
				ttglist.get(i).getRoot().setGroupedDone(ttglist.get(i).getGroup());
				ttglist.removeAll(tmpttglist);
				if(is_there_update)
					break;
			}
		}
	}
	private void forwardGrouping()
	{
		List<TagTokensGroup> tmpttglist = new ArrayList<TagTokensGroup>();
		boolean is_there_update = true;
		
		while(ttglist.size()>1&&is_there_update)
		{
			is_there_update = false;
			tmpttglist.clear();
			for(int i=ttglist.size()-1;i>=0;i--)
			{
				if(!ttglist.get(i).getRoot().getTag().makesGroups())
					continue;
				if(ttglist.get(i).getGroup().getCount()!=-1
						||ttglist.get(i).getGroup().isBackward()
						||ttglist.get(i).getRoot().isGroupedDone(ttglist.get(i).getGroup()))
					continue;
				for(int c=i+1;c<ttglist.size();c++)
				{
					if(tags.getPrefdir().equals(Tag.BACKWARD)&&ttglist.get(c).getRoot().getTag().hasBackWardGroup())
						break;
					ttglist.get(i).updateTtg(ttglist.get(c));
					tmpttglist.add(ttglist.get(c));
					is_there_update = true;
				}
				ttglist.get(i).getRoot().setGroupedDone(ttglist.get(i).getGroup());
				ttglist.removeAll(tmpttglist);
				if(is_there_update)
					break;
			}
		}
	}
	public int calculateSentiment()
	{
		Dictinary dict = new Dictinary();
		return dict.calculate(this.ttglist);
	}
}
