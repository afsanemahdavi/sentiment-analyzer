package input;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class MarkerOptions extends Options {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1368987189573090786L;

	public MarkerOptions()
	{
		@SuppressWarnings("static-access")
		Option inputfile = OptionBuilder.withArgName("input file")
				.hasArg()
				.withDescription("the location of input file")
				.create("inputFile");
		Option help = new Option("help","print this message");
		@SuppressWarnings("static-access")
		Option stanfordPosModel = OptionBuilder.withArgName("stanford model.tagger")
				.hasArg()
				.withDescription("stanfored trained maxent pos tagger model")
				.create("stanfordPosModel");
		this.addOption(inputfile);
		this.addOption(help);
		this.addOption(stanfordPosModel);
	}
}
