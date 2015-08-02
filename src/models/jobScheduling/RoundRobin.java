package models.jobScheduling;

import java.util.*;

public class RoundRobin extends JobSchedule
{
	private ArrayList<RunningProcess> processList;
	private long timeQuantum;

	public RoundRobin(Set<? extends Job> jobs, long timeQuantum)
	{
		processList = new ArrayList<>();
		this.timeQuantum = timeQuantum;
		solve(jobs);
	}

	@Override
	void solve(Set<? extends Job> jobs)
	{
		Job[] sortedJobs = jobs.toArray(new Job[jobs.size()]);
		Arrays.sort(sortedJobs, Job::compareArrivalTime);

		processTime = totTAT = totWT = totRT = 0;
		int jobCount = jobs.size();
	}

	@Override
	public Iterator<RunningProcess> iterator()
	{
		return null;
	}
}
