import apple.laf.AquaLookAndFeel;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.ui.laf.darcula.DarculaLaf;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


class SwitchTheme {

    private final ColorSchemeManager colorSchemeManager = new ColorSchemeManagerImpl();

    void initTheme() {
        LocalDateTime currentTime = LocalDateTime.now();
        int hour = currentTime.getHour();

        String currentScheme = (hour > 18 || hour < 8) ? "Darcula" : "Default";

        LookAndFeel laf = UIManager.getLookAndFeel();

        if (laf instanceof DarculaLaf && currentScheme.equals("Darcula"))
            return;
        System.out.println("YAO");
        if (laf instanceof AquaLookAndFeel && currentScheme.equals("Default"))
            return;

        try {
            if (hour > 17 || hour < 8)
                UIManager.setLookAndFeel(new DarculaLaf());
            else
                UIManager.setLookAndFeel(new AquaLookAndFeel());
            JBColor.setDark(this.useDarkTheme(currentScheme));
            IconLoader.setUseDarkIcons(this.useDarkTheme(currentScheme));

            new DataLayer().setValue("theme", currentScheme);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        List<String> Schemes = Arrays.asList("Default", "Darcula");

        String makeActiveScheme = Schemes.get(0);

        if (hour > 17 || hour < 8)
            makeActiveScheme = Schemes.get(1);

        final EditorColorsScheme scheme = colorSchemeManager.getScheme(makeActiveScheme);


        if (scheme != null) {
            colorSchemeManager.setGlobalScheme(scheme);
        }

        if (hour > 17 || hour < 8) {
            UISettings.getInstance().fireUISettingsChanged();
            ActionToolbarImpl.updateAllToolbarsImmediately();
        }
    }

    void setTheme() {
        String currentScheme = colorSchemeManager.getGlobalColorScheme().getName();

        try {
            if (currentScheme.equals("Default"))
                UIManager.setLookAndFeel(new DarculaLaf());
            else
                UIManager.setLookAndFeel(new AquaLookAndFeel());
            JBColor.setDark(this.useDarkTheme(currentScheme));
            IconLoader.setUseDarkIcons(this.useDarkTheme(currentScheme));

            new DataLayer().setValue("theme", currentScheme);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        List<String> Schemes = Arrays.asList("Default", "Darcula");

        String makeActiveScheme = Schemes.get(0);

        if (currentScheme.equals(Schemes.get(0)))
            makeActiveScheme = Schemes.get(1);

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
}
