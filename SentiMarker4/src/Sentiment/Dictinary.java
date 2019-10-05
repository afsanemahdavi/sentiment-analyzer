package Sentiment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tags.Tags;
import tags.TagsConf;

import mark.TagToken;
import mark.TagTokensGroup;

public class Dictinary {

	private Tags tags = TagsConf.getConf();
	public Dictinary()
	{	
	}
	public int calculate(List<TagTokensGroup> ttglist)
	{
		int score = 0;
		for(int i=0;i<ttglist.size();i++)
		{
			score += calcGroupsRecursive(ttglist.get(i));
		}
		//System.out.println();
		return score;
	}
	private int calcGroupsRecursive(TagTokensGroup ttg)
	{
		int score = 0;
		String opt = ttg.getGroup()!=null?ttg.getGroup().getOpt():"+";
		score = getScore(ttg.getRoot().getWord());
		score = score!=0?score:opt.equals("*")?1:0;
		List<TagTokensGroup> tmplist = ttg.getTtglist();
		int plus_list=0;
		//System.out.print(ttg.getRoot().getTag().getName()+"["+score+"]"+opt+"(");
		for(int i=0;i<tmplist.size();i++)
		{
			plus_list = plus_list+calcGroupsRecursive(tmplist.get(i));
		}
		if(tmplist.size()>0)
		{
			if(opt.equals("*"))
				score *= plus_list;
			else
				score += plus_list;
		}
		else if(ttg.getTagTokens().size()==0)
		{
			if(opt.equals("*"))
				score = 0;
		}
		if(ttg.getTagTokens().size()==0)
		{
			//System.out.print(")="+score+"+");
			return score;
		}
		int s = calcOrderdTokens(ttg.getTagTokens());
		if(opt.equals("*"))
			score *= s;
		else
			score += s;
		//System.out.print(")="+score+"+");
		return score;
	}
	private int getScore(String w)
	{
		Pattern pattern = Pattern.compile("^"+w+tags.getDictionaryspliter()+"(.*)",Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(tags.getDictionaries());
        if(matcher.find())
        {
        	if(matcher.group(1).length()>0)
        		return Integer.parseInt(matcher.group(1));
        }
        return 0;
	}
	private int calcOrderdTokens(List<TagToken> tagtokens)
	{
		int score = 0;
		for(int i=0;i<tagtokens.size();i++)
		{
			score +=getScore(tagtokens.get(i).getWord());
			//System.out.print(tagtokens.get(i).getTag().getName()+"["+score);
			//System.out.print("]+");
		}
		return score;
	}
}
