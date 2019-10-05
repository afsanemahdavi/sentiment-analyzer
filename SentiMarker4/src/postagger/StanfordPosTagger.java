package postagger;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordPosTagger {
	
	String model;
	
	public StanfordPosTagger(String model)
	{
		this.model = model;
	}
	public String runTagger(String text)
		    throws Exception
		  {
		    MaxentTagger tagger = new MaxentTagger(this.model);
		    return tagger.tagString(text);
		  }
}
