package mark;

public class MetaSentence {
	
	String meta;
	String sentence;
	String posTags;
	int score=0;
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public MetaSentence(String meta,String sentence)
	{
		this.meta = meta;
		this.sentence = sentence;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public void updateScore(int score)
	{
		this.score+=score;
	}
	public String getPosTags() {
		return posTags;
	}
	public void setPosTags(String posTags) {
		this.posTags = posTags;
	}
}
