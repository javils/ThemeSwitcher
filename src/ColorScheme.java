import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class ColorScheme implements ApplicationComponent {

    @Override
    public void initComponent() {
        new SwitchTheme().initTheme();
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "ColorScheme";
    }


}