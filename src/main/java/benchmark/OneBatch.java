package benchmark;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

public class OneBatch implements Callable<Stat>{

	
	private String rootDir;
	private int iteration;

	private  OneBatch(BatchBuilder bb) {
		this.rootDir = bb.rootDir;
		this.iteration = bb.iteration;
	}
	

	
	
	public static class BatchBuilder {
		
		private String rootDir;
		private int iteration;

		public BatchBuilder() {}
		
		public BatchBuilder iteration(int iteration) {
			this.iteration = iteration;
			return this;
		}
		
		public BatchBuilder rootDir(String rootDir) {
			this.rootDir = rootDir;
			return this;
		}
		
		public OneBatch build() {
			return new OneBatch(this);
		}
	}
	
	public Stat call() throws Exception {

        new File(rootDir).mkdirs();

		
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < iteration; i++) {
			File f = new File(rootDir,i+".txt");
			
			f.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(f);
			fos.write((""+iteration).getBytes());
			fos.close();
			
			File to = new File(rootDir,i+"-r.txt");
			f.renameTo(to);
			to.delete();
		}
		
		long end = System.currentTimeMillis();
		
		return new Stat(iteration,end-start);
	}
	
}
