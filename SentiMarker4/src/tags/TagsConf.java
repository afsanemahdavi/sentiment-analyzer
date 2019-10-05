package tags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;


public class TagsConf {
	
	private static Tags tags = null;

private static String getSentiMarkersRoot() throws IOException {

		String root = System.getProperty("sentimarker.root");

		if (root == null) {

			root = System.getenv("sentimarker.root");
		}
		
		if (root == null || ".".equals(root)) {
			root = System.getProperty("user.dir");
		}

		if (File.separatorChar != '/') {
			root = root.replaceAll("\\\\", "/");
		}

		if (root.charAt(root.length() - 1) == '/') {
			root = root.substring(0,root.length()-1);
		}

		System.setProperty("sentimarker.root", root);
		return root;
	}
	
	public static Tags getConf()
	{
		if(tags!=null)
			return tags;
		if(System.getProperty("penreport.root")==null)
		{
			try{
				getSentiMarkersRoot();
			}catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		String home = System.getProperty("sentimarker.root");
		String confPath = System.getProperty("sentimarker.conf");
		
		if (confPath == null) {
			confPath = home + "/tags.xml";
		}
		
		JAXBContext jc = null;
		try{
			jc = JAXBContext.newInstance(Tags.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			tags = (Tags) jc.createUnmarshaller().unmarshal(new FileInputStream(confPath));
			tags.setDictionaries(getDictinaryContents(tags.getDictionary_loc()));
			return tags.initTags();
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	private static String getDictinaryContents(String dict_loc) throws Exception
	{
		File f=new File(dict_loc);
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));
		String line="";
		StringBuilder builder=new StringBuilder();
		try {
			while((line=br.readLine())!=null)
				builder.append(line+"\n");
			return builder.toString();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			br.close();
		}
	}
}