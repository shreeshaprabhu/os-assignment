package models.jobScheduling;

import java.io.*;
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

		Deque<PreemptedJob> queue = new ArrayDeque<>();

		for (Job job: sortedJobs)
		{
			if (job == null)
			{
				jobCount--;
				continue;
			}

			long arrivalTime = job.getArrivalTime();
			while (!queue.isEmpty() && processTime < arrivalTime)
			{
				PreemptedJob nextJob = queue.poll();
				if (nextJob.getArrivalTime() == nextJob.getPseudoArrivalTime())
				{
					totRT += processTime - nextJob.getArrivalTime();
					totTAT += nextJob.getBurstTime();
				}

				totWT += processTime - nextJob.getPseudoArrivalTime();
				long burstTime = Math.min(timeQuantum, nextJob.getPseudoBurstTime());
				processTime += burstTime;

				processList.add(new RunningProcess("P" + nextJob.getId(), burstTime));

				if (burstTime < nextJob.getPseudoBurstTime())
				{
					nextJob.setPseudoArrivalTime(processTime);
					nextJob.setPseudoBurstTime(nextJob.getPseudoBurstTime() - burstTime);
					queue.add(nextJob);
				}

			}

			if (queue.isEmpty() && processTime < arrivalTime)
			{
				processList.add(new RunningProcess("Idle", arrivalTime - processTime));
				processTime = arrivalTime;
			}

			PreemptedJob last = queue.peekLast();
			if (last != null && last.getPseudoArrivalTime() > job.getArrivalTime())
			{
				queue.pollLast();
				queue.add(new PreemptedJob(job));
				queue.add(last);
			}
			else
				queue.add(new PreemptedJob(job));
		}

		while (!queue.isEmpty())
		{
			PreemptedJob nextJob = queue.poll();

			if (nextJob.getArrivalTime() == nextJob.getPseudoArrivalTime())
			{
				totRT += processTime - nextJob.getArrivalTime();
				totTAT += nextJob.getBurstTime();
			}

			totWT += processTime - nextJob.getPseudoArrivalTime();
			long burstTime = Math.min(timeQuantum, nextJob.getPseudoBurstTime());
			processTime += burstTime;

			processList.add(new RunningProcess("P" + nextJob.getId(), burstTime));

			if (burstTime < nextJob.getPseudoBurstTime())
			{
				nextJob.setPseudoArrivalTime(processTime);
				nextJob.setPseudoBurstTime(nextJob.getPseudoBurstTime() - burstTime);
				queue.add(nextJob);
			}
		}
		mergeProcess();

		utilization = totTAT * 100.0 / processTime;

		totTAT += totWT;

		avgTAT = (double) totTAT / (double) jobCount;
		avgWT = (double) totWT / (double) jobCount;
		avgRT = (double) totRT / (double) jobCount;
	}

	private void mergeProcess()
	{
		Deque<RunningProcess> deque = new ArrayDeque<>();

		Iterator<RunningProcess> it = processList.iterator();

		if (it.hasNext())
			deque.add(it.next());

		while (it.hasNext())
		{
			RunningProcess prevProcess = deque.pollLast();
			RunningProcess nextProcess = it.next();

			if (prevProcess.getName().compareTo(nextProcess.getName()) == 0)
				deque.addLast(new RunningProcess(nextProcess.getName(),
						prevProcess.getBurstTime() + nextProcess.getBurstTime()));
			else
			{
				deque.add(prevProcess);
				deque.add(nextProcess);
			}
		}

		processList.clear();
		processList.addAll(deque);
	}

	@Override
	public Iterator<RunningProcess> iterator()
	{
		return processList.iterator();
	}

	public static void main(String[] args) throws IOException
	{
		Scanner scr = new Scanner(System.in);

		System.out.print("Enter the number of jobs: ");
		int n = scr.nextInt();

		Set<Job> jobs = new HashSet<>();
		System.out.println("Enter the arrival time and burst time of each job");
		for (int i = 1; i <= n; i++)
			jobs.add(new Job(i, scr.nextLong(), scr.nextLong(), 1));

		System.out.print("Enter the time quantum: ");
		int t = scr.nextInt();

		JobSchedule rr = new RoundRobin(jobs, t);
		long time = 0;
		for (RunningProcess process: rr)
		{
			System.out.print(time + " ");
			System.out.println(process.getName() + " " + process.getBurstTime());
			time += process.getBurstTime();
		}
		System.out.println("Average Turn Around Time: " + rr.averageTurnAroundTime());
		System.out.println("Average Waiting Time: " + rr.averageWaitingTime());

		scr.close();
	}
}
