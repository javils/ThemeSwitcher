import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import org.jetbrains.annotations.Nullable;

class ColorSchemeManagerImpl implements ColorSchemeManager {

    @Override
    public EditorColorsScheme getGlobalColorScheme() {
        return EditorColorsManager.getInstance().getGlobalScheme();
    }

    @Override
    public EditorColorsScheme getScheme(String scheme) {
        return EditorColorsManager.getInstance().getScheme(scheme);
    }

    @Override
    public void setGlobalScheme(@Nullable EditorColorsScheme var1) {
        EditorColorsManager.getInstance().setGlobalScheme(var1);
    }
}