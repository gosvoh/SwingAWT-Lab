import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

public class Diagram extends JPanel {
    /**
     * Значения, передаваемые в диаграмму.
     * В теории можно сколь угодно добавить,
     * но в таком случае нужно менять легенду.
     */
    private int[] values;

    /**
     * Конструктор
     *
     * @param values значения для постройки диаграммы
     */
    Diagram(int... values) {
        this.values = values;
    }

    @Override
    public void paint(Graphics g) {
        Dimension currentSize = getSize();
        double diameter = currentSize.getHeight();
        double centerX = (currentSize.getWidth() / 2f);
        double centerY = (currentSize.getHeight() / 2f);

        Graphics2D graphics2D = (Graphics2D) g;
        Ellipse2D ellipse2D = getEllipseFromCenter(centerX, centerY, diameter, diameter);

        double[] angles = new double[values.length];
        for (int i = 0; i < values.length; i++)
            angles[i] = getAngle(values[i], values);

        double offset = 0;

            /*for (double angle: angles) {
                graphics2D.setPaint(Colors.getNextColor());
                graphics2D.fill(new Arc2D.Double(ellipse2D.getBounds2D(), offset, angle, Arc2D.PIE));
                offset += angle;
            }*/

        graphics2D.setPaint(Color.GREEN);
        graphics2D.fill(new Arc2D.Double(ellipse2D.getBounds2D(), offset, angles[0], Arc2D.PIE));
        graphics2D.setPaint(Color.BLUE);
        graphics2D.fill(new Arc2D.Double(ellipse2D.getBounds2D(), offset += angles[0], angles[1], Arc2D.PIE));
        graphics2D.setPaint(Color.RED);
        //noinspection UnusedAssignment
        graphics2D.fill(new Arc2D.Double(ellipse2D.getBounds2D(), offset += angles[1], angles[2], Arc2D.PIE));


    }

    /**
     * Обновить значения диаграммы
     *
     * @param values значения для обновления
     */
    public void setNewValues(int... values) {
        this.values = values;
        repaint();
    }

    /**
     * Закрытый метод для получения угла из процентного
     * соотношения определённого числа ко всем переданным значениям
     *
     * @param value       число для получения угла
     * @param otherValues все числа
     * @return угол для отображения в диаграмме
     */
    private double getAngle(int value, int... otherValues) {
        double sum = 0;
        for (int otherValue : otherValues)
            sum += otherValue;
        return value / sum * 360;
    }

    /**
     * Посторить эллипс от центра заданных координат
     *
     * @param x      середина эллипса по оси Х
     * @param y      середина эллипса по оси Y
     * @param width  высота эллипса
     * @param height ширина эллипса
     * @return полученный эллипс
     */
    private Ellipse2D getEllipseFromCenter(double x, double y, double width, double height) {
        double newX = x - width / 2.0;
        double newY = y - height / 2.0;

        return new Ellipse2D.Double(newX, newY, width, height);
    }

    /**
     * Приватный класс для ротации цветов, чтобы не вводить каждый раз вручную
     */
    private static class Colors {
        private static final Color[] colors = {Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE, Color.PINK, Color.MAGENTA};
        private static int point = colors.length;

        private Colors() {
        }

        public static Color getNextColor() {
            return colors[nextPoint()];
        }

        private static int nextPoint() {
            point = (point + 1) % colors.length;
            return point;
        }
    }

    /**
     * Получить значения диаграммы
     *
     * @return значения диаграммы
     */
    public int[] getValues() {
        return values;
    }
}