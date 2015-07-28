package models.jobScheduling;

import java.util.Set;

/**
 * @author Shreesha Prabhu K
 */
public abstract class JobSchedule implements Iterable<RunningProcess>
{
    long totTAT, totWT, totRT, processTime;
    double avgTAT, avgWT, avgRT, utilization;

    /**
     * Abstract method expected to prepare schedule
     * for the given set of jobs using some job scheduling algorithm
     *
     * @param jobs Set of jobs that are to be sequenced
     */
    abstract void solve(Set<? extends Job> jobs);

    /**
     * @return Total process time of the schedule
     */
    final public long processTime()
    {
        return processTime;
    }

    /**
     * @return Total Turn Around Time of the Job Sequence
     */
    final public long totalTurnAroundTime()
    {
        return totTAT;
    }

    /**
     * @return Average Turn Around Time of the Job Sequence
     */
    final public double averageTurnAroundTime()
    {
        return avgTAT;
    }

    /**
     * @return Total Waiting Time
     */
    final public long totalWaitingTime()
    {
        return totWT;
    }

    /**
     * @return Average Waiting Time
     */
    final public double averageWaitingTime()
    {
        return avgWT;
    }

    /**
     * @return Total Response Time
     */
    final public long totalResponseTime()
    {
        return totRT;
    }

    /**
     * @return Average Response Time
     */
    final public double averageResponseTime()
    {
        return avgRT;
    }

    /**
     * @return CPU Utilization Percentage
     */
    final public double utilization()
    {
        return utilization;
    }
}
