package tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="tags")
@XmlType(name = "tags")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tags {

	public Tags initTags()
	{
		tmap = new HashMap<String,Tag>();
		for(Tag t:this.tag)
		{
			tmap.put(t.getName(),t);
		}
		return this;
	}
	@XmlAttribute(name="tags_splitter")
	private String splitter;
	
	@XmlAttribute(name="prefdir")
	private String prefdir="-";
	
	@XmlAttribute(name="dictionary_loc")
	private String dictionary_loc="";
	
	@XmlAttribute(name="dictionary_spliter")
	private String dictionaryspliter="-";

	@XmlElement(name="tag")
	private List<Tag> tag;
	
	@XmlTransient
	private Map<String,Tag> tmap = null;
	
	@XmlTransient
	private String dictionaries = "";

	public List<Tag> getTag() {
		return tag;
	}
	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}
	
	public Tag getTag(String tag)
	{
		if(this.tmap==null)
			return null;
		return this.tmap.get(tag);
	}
	public String getPrefdir() {
		return prefdir;
	}
	public void setPrefdir(String prefdir) {
		this.prefdir = prefdir;
	}
	public String getDictionaries()
	{
		return this.dictionaries;
	}
	public void setDictionaries(String dictionaries) {
		this.dictionaries = dictionaries;
	}
	public String getSplitter() {
		return splitter;
	}
	public void setSplitter(String splitter) {
		this.splitter = splitter;
	}
	public String getDictionary_loc() {
		return dictionary_loc;
	}
	public void setDictionary_loc(String dictionary_loc) {
		this.dictionary_loc = dictionary_loc;
	}
	public String getDictionaryspliter() {
		return dictionaryspliter;
	}
	public void setDictionaryspliter(String dictionaryspliter) {
		this.dictionaryspliter = dictionaryspliter;
	}
}
