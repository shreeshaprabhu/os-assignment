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
}
