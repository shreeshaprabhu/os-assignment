package models.jobScheduling;

import java.util.*;

public class SRJF extends JobSchedule
{
    private ArrayList<RunningProcess> processList;

    public SRJF(Set<? extends Job> jobs)
    {
        processList = new ArrayList<>();
        solve(jobs);
    }

    @Override
    void solve(Set<? extends Job> jobs)
    {
        Job[] sortedJobs = jobs.toArray(new Job[jobs.size()]);
        Arrays.sort(sortedJobs, Job::compareArrivalTime);

        processTime = totTAT = totWT = totRT = 0;
        int jobCount = jobs.size();

        PriorityQueue<PreemptedJob> queue = new PriorityQueue<>(PreemptedJob::compareRemaingBurstTime);

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
                long burstTime = Math.min(arrivalTime - processTime, nextJob.getPseudoBurstTime());
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
            processTime += nextJob.getPseudoBurstTime();

            processList.add(new RunningProcess("P" + nextJob.getId(), nextJob.getPseudoBurstTime()));
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
}
