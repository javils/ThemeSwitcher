import apple.laf.AquaLookAndFeel;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.ui.laf.darcula.DarculaLaf;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


class SwitchTheme {

    private final ColorSchemeManager colorSchemeManager = new ColorSchemeManagerImpl();

    private static final String DARCULA_THEME = "Darcula";
    private static final String DEFAULT_THEME = "Default";

    void initTheme() {
        LocalDateTime currentTime = LocalDateTime.now();
        int hour = currentTime.getHour();
        int min = currentTime.getMinute();

        PluginSettings settings = ServiceManager.getService(PluginSettings.class);

        if (settings.timeToLightMs == null)
            return;

        if (settings.timeToDarkMs == null)
            return;

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String nowTime = df.format(calendar.getTime());
        calendar.setTimeInMillis(Long.valueOf(settings.timeToLightMs));

        String lightTime = df.format(calendar.getTime());

        calendar.setTimeInMillis(Long.valueOf(settings.timeToDarkMs));
        String darkTime = df.format(calendar.getTime());


        boolean darkTheme = false;
        try {
            darkTheme = !isTimeBetweenTwoTime(lightTime, darkTime, nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String currentScheme = darkTheme ? DARCULA_THEME : DEFAULT_THEME;

        try {
            if (darkTheme)
                UIManager.setLookAndFeel(new DarculaLaf());
            else
                UIManager.setLookAndFeel(new AquaLookAndFeel());

            JBColor.setDark(this.useDarkTheme(currentScheme));
            IconLoader.setUseDarkIcons(this.useDarkTheme(currentScheme));

            new DataLayer().setValue("theme", currentScheme);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        String makeActiveScheme = DEFAULT_THEME;

        if (darkTheme)
            makeActiveScheme = DARCULA_THEME;

        final EditorColorsScheme scheme = colorSchemeManager.getScheme(makeActiveScheme);


        if (scheme != null) {
            colorSchemeManager.setGlobalScheme(scheme);
        }

        if (darkTheme) {
            UISettings.getInstance().fireUISettingsChanged();
            ActionToolbarImpl.updateAllToolbarsImmediately();
        }
    }

    void setTheme() {
        String currentScheme = colorSchemeManager.getGlobalColorScheme().getName();

        try {
            if (currentScheme.equals(DEFAULT_THEME))
                UIManager.setLookAndFeel(new DarculaLaf());
            else
                UIManager.setLookAndFeel(new AquaLookAndFeel());
            JBColor.setDark(this.useDarkTheme(currentScheme));
            IconLoader.setUseDarkIcons(this.useDarkTheme(currentScheme));

            new DataLayer().setValue("theme", currentScheme);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }


        String makeActiveScheme = DEFAULT_THEME;

        if (currentScheme.equals(DEFAULT_THEME))
            makeActiveScheme = DARCULA_THEME;

        final EditorColorsScheme scheme = colorSchemeManager.getScheme(makeActiveScheme);


        if (scheme != null) {
            colorSchemeManager.setGlobalScheme(scheme);
        }

        UISettings.getInstance().fireUISettingsChanged();
        ActionToolbarImpl.updateAllToolbarsImmediately();

    }


    private boolean useDarkTheme(String theme) {
        return !theme.toLowerCase().equals("default");
    }


    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9])$";
        if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg)) {
            boolean valid = false;
            //Start Time
            Date inTime = new SimpleDateFormat("HH:mm").parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);

            //Current Time
            Date checkTime = new SimpleDateFormat("HH:mm").parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);

            //End Time
            Date finTime = new SimpleDateFormat("HH:mm").parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }

            java.util.Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0)
                    && actualTime.before(calendar2.getTime())) {
                valid = true;
            }
            return valid;
        } else {
            throw new IllegalArgumentException("Not a valid time, expecting HH:MM format");
        }

    }
}
