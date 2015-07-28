import gui.jobScheduling.MainApplication;

public class JobSchedulingFX
{
    public static void main(String[] args)
    {
        MainApplication js = new MainApplication(args);
        new Thread(js).start();
    }
}
