package tags;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "groups")
@XmlAccessorType(XmlAccessType.FIELD)
public class Groups {
	
	@XmlValue
	private String value;
	
	@XmlAttribute(name="dir")
	private String dir;
	
	@XmlAttribute(name="opt")
	private String opt;
	
	@XmlAttribute(name="count")
	private int count;
	
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
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean groups(Tag t)
	{
		return getValue().equals(t.getName());
	}
}
