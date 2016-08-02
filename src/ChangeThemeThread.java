/**
 * Created by Javier on 2/8/16.
 */
public class ChangeThemeThread implements Runnable {
    @Override
    public void run() {
        new SwitchTheme().setTheme();
    }
}
