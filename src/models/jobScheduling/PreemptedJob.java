package models.jobScheduling;

public class PreemptedJob extends Job
{
	private long pseudoArrivalTime;
	private long pseudoBurstTime;

	public PreemptedJob()
	{
		super();
		pseudoArrivalTime = pseudoBurstTime = 0;
	}

	public PreemptedJob(Job job)
	{
		super(job.getId(), job.getArrivalTime(), job.getBurstTime(), job.getPriority());
		pseudoArrivalTime = job.getArrivalTime();
		pseudoBurstTime = job.getBurstTime();
	}

	public long getPseudoArrivalTime()
	{
		return pseudoArrivalTime;
	}

	public void setPseudoArrivalTime(long pseudoArrivalTime)
	{
		this.pseudoArrivalTime = pseudoArrivalTime;
	}

	public long getPseudoBurstTime()
	{
		return pseudoBurstTime;
	}

	public void setPseudoBurstTime(long pseudoBurstTime)
	{
		this.pseudoBurstTime = pseudoBurstTime;
	}

	/**
	 * Compares PreemptedJobs based on their remaining time (i.e. pseudoBurstTime).
	 * If they have same remaining time, then latest arrived job is preferred
	 * as it would reduce average response time.
	 *
	 * @param j1 Job 1
	 * @param j2 Job 2
	 * @return the <code>int</code> comparing preempted-job
	 */
	public static int compareRemaingBurstTime(PreemptedJob j1, PreemptedJob j2)
	{
		if (j1.pseudoBurstTime < j2.pseudoBurstTime)
			return -1;
		if (j1.pseudoBurstTime > j2.pseudoBurstTime)
			return 1;
		if (j1.getArrivalTime() > j2.getArrivalTime())
			return -1;
		if (j1.getArrivalTime() < j2.getArrivalTime())
			return 1;
		if (j1.getId() == j2.getId())
			return 0;
		if (j1.getId() < j2.getId())
			return -1;
		if (j1.getId() > j2.getId())
			return 1;
		return 0;
	}
}
