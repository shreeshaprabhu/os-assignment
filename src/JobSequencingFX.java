import gui.jobScheduling.MainApplication;

public class JobSequencingFX
{
    public static void main(String...args)
    {
        MainApplication js = new MainApplication(args);
        new Thread(js).start();
    }
}
