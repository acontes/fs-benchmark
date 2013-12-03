package benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class BenchMark {

	public static void main(String[] args) throws InterruptedException, ParseException {
		
		
		int filesPerBatch = 1000;
		int concurrentExecution = 1; 
		int batchs = 1;
		
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("t", false, "number of concurrent batchs to run");
		options.addOption("b", false, "number of batchs to run");
		options.addOption("f", false, "number of files to create/rename/delete by batch");
		
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse( options, args);
		
		if (args.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( BenchMark.class.getName(), options );
			System.exit(1);
		}
		
		if (cmd.hasOption("t")) {
			concurrentExecution = Integer.parseInt(cmd.getOptionValue("t"));
		}
		
		if (cmd.hasOption("b")) {
			batchs = Integer.parseInt(cmd.getOptionValue("b"));
		}
		
		if (cmd.hasOption("f")) {
			filesPerBatch = Integer.parseInt(cmd.getOptionValue("f"));
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(concurrentExecution);
		
		
		
		List<Future<Stat>> results = new ArrayList<Future<Stat>>(batchs);
		
		
		for (int i = 0; i< batchs; i++) {
			OneBatch batch = new OneBatch.BatchBuilder().iteration(filesPerBatch).rootDir("/tmp/toto").build();			
			results.add(executor.submit(batch));
		}
				
		while (! executor.awaitTermination(10, TimeUnit.SECONDS)) {};
		
		
		
	}
}