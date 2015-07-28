package models.jobScheduling;

public class RunningProcess
{
    private final String name;
    private final long burstTime;

    public RunningProcess(String name, long burstTime)
    {
        this.name = name;
        this.burstTime = burstTime;
    }

    public String getName()
    {
        return name;
    }

    public long getBurstTime()
    {
        return burstTime;
    }
}
