package benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class BenchMark {

	public static void main(String[] args) throws InterruptedException, ParseException {
		
		
		int filesPerBatch = 1000;
		int concurrentExecution = 1; 
		int batchs = 1;
    String rootDir = "";
		
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("t", true, "number of concurrent batchs to run");
		options.addOption("b", true, "number of batchs to run");
		options.addOption("f", true, "number of files to create/rename/delete by batch");
		options.addOption("d", true, "root dir to use");
		
		CommandLineParser parser = new GnuParser();
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
		
		if (cmd.hasOption("d")) {
      rootDir =cmd.getOptionValue("d");
    }
		
		ExecutorService executor = Executors.newFixedThreadPool(concurrentExecution);
		
		
		
		List<Future<Stat>> results = new ArrayList<Future<Stat>>(batchs);
		
		
		for (int i = 0; i< batchs; i++) {
      OneBatch batch = new OneBatch.BatchBuilder().iteration(filesPerBatch)
          .rootDir(rootDir + File.separator + i).name("" + i).build();
			results.add(executor.submit(batch));
		}
				

		executor.shutdown();
		
		while (! executor.awaitTermination(10, TimeUnit.SECONDS)) {
      System.out.println("waiting termination");
		};
		
    long averageDuration = 0l;

    for (Future<Stat> future : results) {
      try {
        averageDuration += future.get().getDuration();
        System.out.println(future.get().getDuration() + "ms for " + filesPerBatch + "files");
      }
      catch (ExecutionException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
		
    System.out.println("average duration " + averageDuration / batchs);

	}
}
