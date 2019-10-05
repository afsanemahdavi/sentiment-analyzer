package tags;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "tag")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tag {
	
	@XmlTransient
	public static final String FORWARD = "+";  
	@XmlTransient
	public static final String BACKWARD = "-";
	
	@XmlElement(name="name")
	private String name;
	@XmlElement(name="joinsto")
	private List<JoinsTo> joinsto;
	@XmlElement(name="groups")
	private List<Groups> groups;
	@XmlAttribute(name="imp")
	private boolean imp;
	@XmlAttribute(name="ingroupcount")
	private int ingroupcount=1;
	
	@XmlTransient
	List<String> joins = null;
	
	public boolean isImp() {
		return imp;
	}
	public void setImp(boolean imp) {
		this.imp = imp;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean hasJoins()
	{
		if(this.joinsto!=null&&this.joinsto.size()>0)
				return true;
		return false;
	}
	
	public boolean makesGroups()
	{
		if(this.groups!=null&&this.groups.size()>0)
				return true;
		return false;
	}
	
	public List<String> getJoins()
	{
		if(hasJoins())
		{
			if(this.joins!=null)
				return this.joins;
			this.joins = new ArrayList<String>();
			for(JoinsTo jt:getJoinsto())
				this.joins.add(jt.getValue());
			return this.joins;
		}
		return null;
	}
	public boolean joinsTo(Tag t)
	{
		return getJoins().contains(t.getName());
	}
	public JoinsTo mutualJoins(Tag t)
	{
		if(getJoinsto()==null)
			return null;
		for(JoinsTo jt:getJoinsto())
		{
			for(JoinsTo jt2:t.getJoinsto())
			{
				if(jt.isForward()&&!jt2.isForward())
					continue;
				if(jt.getValue().equals(jt2.getValue()))
					return jt;
			}
		}
		return null;
	}
	public List<JoinsTo> getJoinsto() {
		return joinsto;
	}
	public void setJoinsto(List<JoinsTo> joinsto) {
		this.joinsto = joinsto;
	}
	public List<Groups> getGroups() {
		return groups;
	}
	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}
	public int getIngroupcount() {
		return ingroupcount;
	}
	public void setIngroupcount(int ingroupcount) {
		this.ingroupcount = ingroupcount;
	}
	public boolean hasBackWardGroup()
	{
		if(groups==null)
			return false;
		for(Groups g:groups)
		{
			if(g.isBackward()&&g.getCount()==-1)
				return true;
		}
		return false;
	}
	public boolean hasForWardGroup()
	{
		if(groups==null)
			return false;
		for(Groups g:groups)
		{
			if(g.isForward()&&g.getCount()==-1)
				return true;
		}
		return false;
	}
}
