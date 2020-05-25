import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Utils {

    /**
     * Добавить все переданные компоненты в указанный контейнер
     *
     * @param container  контейнер
     * @param components компоненты для добавления
     */
    public static void addAllTo(Container container, Component... components) {
        for (Component component : components)
            container.add(component);
    }

    /**
     * Установить фонт для каждого переданного элемента,
     * если этот элемент является контейнером,
     * то устанавливаем фонт для всех вложенных
     * элементов
     *
     * @param font       фонт
     * @param components компонент
     */
    public static void setFontForEach(Font font, Component... components) {
        for (Component component : components) {
            component.setFont(font);
            if (component instanceof Container)
                for (Component child : ((Container) component).getComponents())
                    setFontForEach(font, child);
        }
    }

    /**
     * Обновить шрифт для всех указанных, а так же дочерних компонентов
     *
     * @param name имя шрифта
     * @param components компоненты
     */
    public static void updateFontFamily(String name, Component... components) {
        Font currentFont = components[0].getFont();
        setFontForEach(new Font(name, currentFont.getStyle(), currentFont.getSize()), components);
    }

    /**
     * Обновить стиль шрифта для всех указанных, а так же дочерних компонентов
     *
     * @param style имя шрифта
     * @param components компоненты
     */
    public static void updateFontStyle(int style, Component... components) {
        Font currentFont = components[0].getFont();
        setFontForEach(new Font(currentFont.getName(), style, currentFont.getSize()), components);
    }

    /**
     * Обновить размер шрифта для всех указанных, а так же дочерних компонентов
     *
     * @param size имя шрифта
     * @param components компоненты
     */
    public static void updateFontSize(int size, Component... components) {
        Font currentFont = components[0].getFont();
        setFontForEach(new Font(currentFont.getName(), currentFont.getStyle(), size), components);
    }

    /**
     * Создать шрифт по полученной ссылке
     *
     * @param urlStr ссылка на фонт
     * @return фонт
     */
    public static Font createFont(String urlStr) {
        Font font = null;
        try {
            URL url = new URL(urlStr);
            InputStream is = url.openStream();
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            font = font.deriveFont(Font.BOLD, 14);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return font;
    }
}
