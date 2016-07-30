import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class Switcher extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        new SwitchTheme().setTheme();
    }
}
