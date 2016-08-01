import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Date;


@State(name = "SaveTimesSettings",
        storages = {@com.intellij.openapi.components.Storage(
                file = "/savetimes_settings.xml")})
class PluginSettings implements PersistentStateComponent<PluginSettings> {

    public String timeToLightMs;
    public String timeToDarkMs;

    @Nullable
    @Override
    public PluginSettings getState() {
        return this;
    }

    @Override
    public void loadState(PluginSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }


    void setConfig(Date timeToLight, Date timeToDark) {
        this.timeToLightMs = Long.toString(timeToLight.getTime());
        this.timeToDarkMs = Long.toString(timeToDark.getTime());
    }


}
