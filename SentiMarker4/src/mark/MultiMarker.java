package mark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import input.MarkerOptions;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import tags.TagsConf;

public class MultiMarker {
	
	static String inputText;
	static ArrayList<MetaSentence> metasentense = new ArrayList<MetaSentence>();
	static String posmodel_path;
	
	private static void initOptions(String[] args) throws Exception
	{
		CommandLineParser parser = new GnuParser();
		String input_file = null;
		int passed = 0;
		try{
			Options options = new MarkerOptions();
			CommandLine line = parser.parse(options,args);
			if (line.hasOption("help"))
			{
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("SentiMarker4", options);
				System.exit(0);
			}else
			{
				if(line.hasOption("inputFile"))
				{
					input_file = line.getOptionValue("inputFile");
					passed++;
				}
				if(line.hasOption("stanfordPosModel"))
				{
					posmodel_path = line.getOptionValue("stanfordPosModel");
					passed++;
				}
			}
			if(passed<2)
			{
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("SentiMarker4", options);
				System.exit(0);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		File f=new File(input_file);
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

		String line="";
		StringBuilder builder=new StringBuilder();
		try {
			while((line=br.readLine())!=null)
				builder.append(line+"\n");
			inputText = builder.toString();
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
	
	public static void main(String[] args) throws Exception
	{
		initOptions(args);
		textSplitter();
		multiProcess();
		printResult();
	}
	private static void textSplitter()
	{
		Pattern pattern = Pattern.compile("(|\\n)(\\[.*?\\])(.*?)(?=\\[.+|$)",Pattern.DOTALL);//
        Matcher matcher = pattern.matcher(inputText);
        while(matcher.find())
        {
        	metasentense.add(new MetaSentence(matcher.group(2),matcher.group(3)));
        }
	}
	private static void printResult() throws UnsupportedEncodingException
	{
		PrintStream out = new PrintStream(System.out,true,"UTF-8");
		for(MetaSentence ms:metasentense)
		{
			out.print(ms.getMeta());
			out.println(ms.getScore());
		}
	}
	private static void multiProcess() throws InterruptedException
	{
		TagsConf.getConf();
		MarkerPoolExecutor executor = new MarkerPoolExecutor(posmodel_path);
		executor.initExecutor(metasentense);
		executor.execute();
	}
}
