package models.jobScheduling;

public class Job
{
    private long id;
    private long arrivalTime;
    private long burstTime;
    private long priority;

    public Job()
    {
        this(0, 0, 0, 0);
    }

    public Job(long id, long arrivalTime, long burstTime, long priority)
    {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public long getBurstTime()
    {
        return burstTime;
    }

    public void setBurstTime(long burstTime)
    {
        this.burstTime = burstTime;
    }

    public long getPriority()
    {
        return priority;
    }

    public void setPriority(long priority)
    {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Job && this.id == ((Job) obj).id;
    }

    /**
     * <p>Compares the jobs based on their arrival time.
     * If both jobs have same arrival time <code>-1</code> is returned.
     * </p>
     * <p>
     * This method may be used in Job scheduling algorithms,
     * which are <strong>not</strong> priority based.
     * </p>
     *
     * @param j1 Job 1
     * @param j2 Job 2
     * @return the <code>int</code> comparing jobs on their arrival time.
     */
    public static int compareArrivalTime(Job j1, Job j2)
    {
        if (j1 == null && j2 == null)
            return 0;
        if (j1 == null)
            return -1;
        if (j2 == null)
            return 1;
        if (j1.arrivalTime < j2.arrivalTime)
            return -1;
        if (j1.arrivalTime > j2.arrivalTime)
            return 1;
        if (j1.id < j2.id)
            return -1;
        if (j1.id > j2.id)
            return 1;
        return 0;
    }

    /**
     * <p>Compares the jobs based on their burst time.
     * If both jobs have same burst time, then jobs are compared over arrival time.
     * If both jobs have same arrival time and burst time <code>0</code> is returned.
     * </p>
     * <p>
     * This method may be used in Job scheduling algorithms,
     * which are <strong>not</strong> priority based.
     * </p>
     *
     * @param j1 Job 1
     * @param j2 Job 2
     * @return the <code>int</code> comparing jobs on their burst time.
     */
    public static int compareBurstTime(Job j1, Job j2)
    {
        if (j1 == null && j2 == null)
            return 0;
        if (j1 == null)
            return -1;
        if (j2 == null)
            return 1;
        if (j1.burstTime < j2.burstTime)
            return -1;
        if (j1.burstTime > j2.burstTime)
            return 1;
        if (j1.arrivalTime < j2.arrivalTime)
            return -1;
        if (j1.arrivalTime > j2.arrivalTime)
            return 1;
        if (j1.id < j2.id)
            return -1;
        if (j1.id > j2.id)
            return 1;
        return 0;
    }

    /**
     * <p>Compares the jobs based on their priority.
     * If both jobs have same priority, then jobs are compared over arrival time.
     * If both jobs have same priority and arrival time, then jobs are compared over their burst time.
     * If all priority, arrival time and burst time are equal, <code>-1</code> is returned.
     * </p>
     *
     * @param j1 Job 1
     * @param j2 Job 2
     * @return the <code>int</code> comparing jobs on their priority.
     */
    public static int comparePriority(Job j1, Job j2)
    {
        if (j1 == null && j2 == null)
            return 0;
        if (j1 == null)
            return -1;
        if (j2 == null)
            return 1;
        if (j1.priority < j2.priority)
            return -1;
        if (j1.priority > j2.priority)
            return 1;
        if (j1.arrivalTime < j2.arrivalTime)
            return -1;
        if (j1.arrivalTime > j2.arrivalTime)
            return 1;
        if (j1.burstTime < j2.burstTime)
            return -1;
        if (j1.burstTime > j2.burstTime)
            return 1;
        if (j1.id < j2.id)
            return -1;
        if (j1.id > j2.id)
            return 1;
        return 0;
    }
}
