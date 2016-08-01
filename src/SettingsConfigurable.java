import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingsConfigurable implements Configurable {
    private SettingsPanel panel;
    private PluginSettings settings = ServiceManager.getService(PluginSettings.class);

    private class SettingsPanel extends JPanel {
        private boolean isModified = false;

        private JPanel controlPanel;
        JSpinner timeToDark;
        JSpinner timeToLight;

        SettingsPanel() {
            initView();
        }

        private void apply() {
            settings.setConfig((Date) timeToLight.getValue(), (Date) timeToDark.getValue());
            isModified = false;
        }

        private void prepareGUI() {
            setLayout(new GridLayout(3, 1));

            controlPanel = new JPanel();
            controlPanel.setLayout(new VerticalFlowLayout(FlowLayout.LEFT, 10));

            add(controlPanel);
            setVisible(true);
        }

        private void initView() {
            prepareGUI();

            JLabel timeToDarkLabel = new JLabel("Time to Dark: ", JLabel.LEFT);
            JLabel timeToLightLabel = new JLabel("Time to Light: ", JLabel.LEFT);

            timeToDark = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor timeToDarkEditor = new JSpinner.DateEditor(timeToDark, "HH:mm");
            timeToDark.setEditor(timeToDarkEditor);
            timeToDark.setValue(Calendar.getInstance(Locale.getDefault()).getTime()); // will only show the current time
            timeToDark.setSize(20, timeToDark.getHeight());

            timeToLight = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor timeToLightEditor = new JSpinner.DateEditor(timeToLight, "HH:mm");
            timeToLight.setEditor(timeToLightEditor);
            timeToLight.setValue(Calendar.getInstance(Locale.getDefault()).getTime()); // will only show the current time

            controlPanel.add(timeToLightLabel);
            controlPanel.add(timeToLight);
            controlPanel.add(timeToDarkLabel);
            controlPanel.add(timeToDark);

            timeToDark.addChangeListener(e -> isModified = true);

            timeToLight.addChangeListener(e -> isModified = true);
        }
    }

    @Override
    public String getDisplayName() {
        return "Theme Switcher";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        // No help
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (panel == null) {
            panel = new SettingsPanel();

            if (settings.timeToLightMs != null) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTimeInMillis(Long.valueOf(settings.timeToLightMs));
                panel.timeToLight.setValue(calendar.getTime());
            }

            if (settings.timeToDarkMs != null) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTimeInMillis(Long.valueOf(settings.timeToDarkMs));
                panel.timeToDark.setValue(calendar.getTime());
            }
        }
        return panel;
    }

    @Override
    public boolean isModified() {
        return panel != null && panel.isModified;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (panel != null) {
            panel.apply();
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
        panel = null;
    }
}