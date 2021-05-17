package MainForm;

public class GameLoop extends Thread
{
    private MainForm form;

    public GameLoop(MainForm form)
    {
        this.form = form;
    }

    // Основной игровой цикл
    public void run()
    {
        int animationTick = 0;
        while (form.getIsExitRequested() == false)
        {
            try {
                Thread.sleep(form.getGameLoopSleepTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (form.isPause())
                continue;
            animationTick++;
            if (animationTick >= form.ANIMATION_TICKS)
            {
                animationTick = 0;
                form.Tick();
            }


            form.AnimationTick(animationTick);
        }

        form.Exit();
    }


}
