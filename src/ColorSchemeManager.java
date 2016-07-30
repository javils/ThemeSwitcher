import com.intellij.openapi.editor.colors.EditorColorsScheme;
import org.jetbrains.annotations.Nullable;

interface ColorSchemeManager {
    EditorColorsScheme getGlobalColorScheme();

    EditorColorsScheme getScheme(String scheme);

    void setGlobalScheme(@Nullable EditorColorsScheme var1);
}