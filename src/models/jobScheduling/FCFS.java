package models.jobScheduling;

import java.util.*;
import java.io.*;

public class FCFS extends JobSchedule
{
    private ArrayList<RunningProcess> processList;

    public FCFS(Set<? extends Job> jobs)
    {
        processList = new ArrayList<>();
        solve(jobs);
    }

    @Override
    protected void solve(Set<? extends Job> jobs)
    {
        Job[] sortedJobs = jobs.toArray(new Job[jobs.size()]);
        Arrays.sort(sortedJobs, Job::compareArrivalTime);

        processTime = totWT = 0;
        long usefulTime = 0;
        int jobCount = jobs.size();
        for (Job job : sortedJobs)
        {
            if (job == null)
            {
                jobCount--;
                continue;
            }

            long arrivalTime = job.getArrivalTime();
            long burstTime = job.getBurstTime();
            if (processTime < arrivalTime)
            {
                processList.add(new RunningProcess("Idle", arrivalTime - processTime));
                processTime = arrivalTime;
            }

            processList.add(new RunningProcess("P" + job.getId(), burstTime));
            usefulTime += burstTime;
            totWT += processTime - arrivalTime;
            processTime += burstTime;
        }

        totRT = totWT;
        totTAT = totWT + usefulTime;

        avgRT = avgWT = (double) totWT / (double) jobCount;
        avgTAT = (double) totTAT / (double) jobCount;

        utilization = usefulTime * 100.0 / processTime;
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

        JobSchedule fcfs = new FCFS(jobs);
        System.out.println("Average Turn Around Time: " + fcfs.averageTurnAroundTime());
        System.out.println("Average Waiting Time: " + fcfs.averageWaitingTime());

        scr.close();
    }
}
