package tags;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "joinsto")
@XmlAccessorType(XmlAccessType.FIELD)
public class JoinsTo {
	
	@XmlValue
	private String value;
	
	@XmlAttribute(name="dir")
	private String dir;
	
	@XmlAttribute(name="gap")
	private String gap="";
	
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public boolean isForward() {
		return this.dir.equals(Tag.FORWARD);
	}
	public boolean isBackward() {
		return this.dir.equals(Tag.BACKWARD);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean joinsTo(Tag t)
	{
		return getValue().equals(t.getName());
	}
	public String getGap() {
		return gap;
	}
	public void setGap(String gap) {
		this.gap = gap;
	}

}
