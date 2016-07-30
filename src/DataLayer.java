import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.util.PropertiesComponent;

class DataLayer {

    private String prefix;

    DataLayer() {

        try {
            this.prefix = PluginManager.getPluginByClassName(this.getClass().getName()).toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    void setValue(String key, String value) {
        PropertiesComponent.getInstance().setValue(this.prefix + "." + key, value);
    }

}