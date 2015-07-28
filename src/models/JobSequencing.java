package models;

import java.util.*;

public class JobSequencing
{
    private static class Job
    {
        int arrival, burst;

        Job(int arrival, int burst)
        {
            this.arrival = arrival;
            this.burst = burst;
        }

        static int arrivalCompare(Job j1, Job j2)
        {
            if (j1.arrival < j2.arrival)
                return -1;
            else if (j1.arrival > j2.arrival)
                return 1;
            return 0;
        }

        static int burstCompare(Job j1, Job j2)
        {
            if (j1.burst < j2.burst)
                return -1;
            else if (j1.burst > j2.burst)
                return 1;
            return 0;
        }
    }

    /* calculates average waiting time when jobs are served with
     * FIRST COME FIRST SERVE POLICY
     */
    public static void FCFS(int[] arrival, int[] burst)
    {
        assert arrival.length == burst.length;

        int n = arrival.length;
        Job[] seq = new Job[n];
        for (int i = 0; i < n; i++)
            seq[i] = new Job(arrival[i], burst[i]);

        Arrays.sort(seq, Job::arrivalCompare);

        /* DEBUG */
        // for (int i = 0; i < n; i++)
        //     System.out.println(seq[i].arrival + " " + seq[i].burst);

        int totalWaiting = 0, lastJobComp = 0;
        for (int i = 0; i < n; i++)
        {
            if (lastJobComp <= seq[i].arrival)
                lastJobComp = seq[i].arrival + seq[i].burst;
            else
            {
                totalWaiting += lastJobComp - seq[i].arrival;
                lastJobComp += seq[i].burst;
            }
            /* ASSUMPTION: Burst time is not added to the waiting time */
            // totalWaiting += seq[i].burst;
        }
        /* DEBUG */
        // System.out.println("Total waiting time: " + totalWaiting);

        double avgWaiting = totalWaiting / (n + 0.0);
        System.out.println("Average waiting time: " + avgWaiting);
    }

    /* calculates average waiting time when jobs are served with
     * SHORTEST JOBS FIRST (non-preemptive)
     */
    public static void SJF(int[] arrival, int[] burst)
    {
        assert arrival.length == burst.length;

        int n = arrival.length;

        Job[] seq = new Job[n];
        for (int i = 0; i < n; i++)
            seq[i] = new Job(arrival[i], burst[i]);

        Arrays.sort(seq, Job::arrivalCompare);

        PriorityQueue<Job> queue = new PriorityQueue<>(Job::burstCompare);

        int totalWaiting = 0, currTime = 0, j = 0;
        Job nextJob;

        while (j < n || !queue.isEmpty())
        {
            while (j < n && seq[j].arrival <= currTime)
            {
                queue.add(seq[j]);
                j++;
            }
            if (j < n && queue.isEmpty())
            {
                currTime = seq[j].arrival;
                continue;
            }

            nextJob = queue.poll();
            totalWaiting += currTime - nextJob.arrival;
            currTime += nextJob.burst;
        }

        double avgWaiting = totalWaiting / (n + 0.0);
        System.out.println("Average waiting time: " + avgWaiting);
    }

    public static void main(String[] args)
    {
        Scanner scr = new Scanner(System.in);

        System.out.print("Enter number of jobs: ");
        int n = scr.nextInt();
        int[] arrival = new int[n], burst = new int[n];

        System.out.println("Enter arrival and burst time of each job:");
        for (int i = 0; i < n; i++)
        {
            arrival[i] = scr.nextInt();
            burst[i] = scr.nextInt();
        }

        System.out.println("First Come First Serve:");
        FCFS(arrival, burst);

        System.out.println("Shortest Job First:");
        SJF(arrival, burst);

        scr.close();
    }
}
