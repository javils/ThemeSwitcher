import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class ColorScheme implements ApplicationComponent {

    @Override
    public void initComponent() {
        new SwitchTheme().initTheme();
        Thread t = new Thread(() -> {

            Calendar aux = Calendar.getInstance(Locale.getDefault());
            PluginSettings settings = ServiceManager.getService(PluginSettings.class);

            aux.setTimeInMillis(Long.valueOf(settings.timeToLightMs));
            LocalTime timeToLight = LocalTime.of(aux.get(Calendar.HOUR_OF_DAY), aux.get(Calendar.MINUTE));

            aux.setTimeInMillis(Long.valueOf(settings.timeToDarkMs));
            LocalTime timeToDark = LocalTime.of(aux.get(Calendar.HOUR_OF_DAY), aux.get(Calendar.MINUTE));

            LocalTime now = LocalTime.now();
            while (true) {
                now = LocalTime.now();
                now = now.withMinute(0);
                now = now.withNano(0);
                aux.setTimeInMillis(Long.valueOf(settings.timeToLightMs));
                timeToLight = LocalTime.of(aux.get(Calendar.HOUR_OF_DAY), aux.get(Calendar.MINUTE));

                aux.setTimeInMillis(Long.valueOf(settings.timeToDarkMs));
                timeToDark = LocalTime.of(aux.get(Calendar.HOUR_OF_DAY), aux.get(Calendar.MINUTE));

                if (timeToLight.equals(LocalTime.now()) || timeToDark.equals(LocalTime.now())) {
                    ApplicationManager.getApplication().invokeLater(new ChangeThemeThread());
                    try {
                        Thread.sleep(2 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "ColorScheme";
    }

    class ThreadAux implements Runnable {

        private ApplicationManager manager;

        public ThreadAux(ApplicationManager appMan) {
            manager = appMan;
        }

        @Override
        public void run() {
            boolean changed = false;
            Calendar calendarNow = Calendar.getInstance(Locale.getDefault());
            Calendar calendarToLight = Calendar.getInstance(Locale.getDefault());
            Calendar calendarToDark = Calendar.getInstance(Locale.getDefault());
            PluginSettings settings = ServiceManager.getService(PluginSettings.class);


            calendarToLight.setTimeInMillis(Long.valueOf(settings.timeToLightMs));
            calendarToDark.setTimeInMillis(Long.valueOf(settings.timeToDarkMs));

            while (true) {
                if (calendarNow.equals(calendarToLight) || calendarNow.equals(calendarToDark)) {
                    ApplicationManager.getApplication().invokeLater(new ChangeThemeThread());
                }
                calendarNow = Calendar.getInstance(Locale.getDefault());
            }

        }
    }

}