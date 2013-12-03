package benchmark;

public class Stat {

	protected int count;
	protected long duration;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Stat(int count, long duration) {
		this.count = count;
		this.duration = duration;
	}
	
	
}
