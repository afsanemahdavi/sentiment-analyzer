package mark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import postagger.StanfordPosTagger;

public class MarkerPoolExecutor {
	
	class MarkWorker implements Runnable
	{
		List<MetaSentence> mslist;
		public MarkWorker(List<MetaSentence> mslist)
		{
			this.mslist=mslist;
		}
		@Override
		public void run() {
			
			for(MetaSentence ms:mslist)
			{
				StanfordPosTagger postagger = new StanfordPosTagger(model_path);
				String[] sentences = ms.getSentence().split("\\.|\\n");
				for(String s:sentences)
				{
					Marker m = new Marker();
					try {
						ms.setPosTags(postagger.runTagger(s));
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.err.println(ms.getPosTags());
					m.mark(ms.getPosTags());
					m.reduceTokens();
					m.makeTwoWayGroups();
					m.printGroups();
					ms.updateScore(m.calculateSentiment());
				}
			}
			//System.out.println("Done");
		}
	}
	String model_path;
	public MarkerPoolExecutor(String model_path)
	{
		this.model_path = model_path;
	}
	private ExecutorService executor=null;
	private ArrayList<Runnable> executes = new ArrayList<Runnable>();
	public void initExecutor(List<MetaSentence> mslist)
	{
		List<List<MetaSentence>> mstmplists = new ArrayList<List<MetaSentence>>();
		if(mslist.size()==0)
			return;
		shutdown();
		final int THREADS =  Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(THREADS);
		int j=0;
		boolean first=true;
		while(j<mslist.size())
		{
			for(int i=0;i<THREADS&&j<mslist.size();i++,j++)
			{
				if(first)
				{
					ArrayList<MetaSentence> tmp = new ArrayList<MetaSentence>();
					tmp.add(mslist.get(j));
					mstmplists.add(tmp);	
				}
				else
				{
					mstmplists.get(i).add(mslist.get(j));
				}
			}
			first=false;
		}
		for(List<MetaSentence> item:mstmplists)
		{
			executes.add(new MarkWorker(item));
		}
		mstmplists.clear();
		mstmplists = null;
	}
	public void execute()
	{
		if(executes.size()==0||executor==null)
			return;
		for(Runnable item:executes)
		{
			executor.execute(item);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE,TimeUnit.HOURS);
		}catch (InterruptedException e){
				e.printStackTrace();
		}
	}
	public void shutdown()
	{
		if(executor!=null)
		{
			executor.shutdown();
			executor=null;
		}
	}
}
