import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DiagramFrame extends JFrame {
    private final Dimension defaultSize;
    private final Dimension fancySize;
    private boolean isDecorated = true;

    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        defaultSize = new Dimension(screenWidth / 3, screenHeight / 3);
        if (screenHeight > 600 && screenWidth > 800) fancySize = new Dimension(800, 600);
        else fancySize = defaultSize;
    }

    public DiagramFrame(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(defaultSize);
        setMaximumSize(fancySize);
        setSize(defaultSize);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(false);

        JFrame mainFrame = this;

        Diagram diagram = new Diagram(3, 4, 5);

        JLabel firstValLabel = new JLabel(String.valueOf(diagram.getValues()[0]));
        JLabel secondValLabel = new JLabel(String.valueOf(diagram.getValues()[1]));
        JLabel thirdValLabel = new JLabel(String.valueOf(diagram.getValues()[2]));
        firstValLabel.setHorizontalAlignment(JLabel.CENTER);
        secondValLabel.setHorizontalAlignment(JLabel.CENTER);
        thirdValLabel.setHorizontalAlignment(JLabel.CENTER);

        JTextField firstValField = new JTextField(String.valueOf(diagram.getValues()[0]));
        JTextField secondValField = new JTextField(String.valueOf(diagram.getValues()[1]));
        JTextField thirdValField = new JTextField(String.valueOf(diagram.getValues()[2]));

        JPanel buttons = new JPanel(new GridLayout(4, 1));
        JPanel legend = new JPanel(new GridLayout(3, 3));

        buttons.setPreferredSize(new Dimension(getWidth() / 7, getHeight()));
        legend.setPreferredSize(new Dimension(getWidth() / 7, getHeight()));

        JButton changeSizeBTN = new JButton((int) fancySize.getWidth() + "x" + (int) fancySize.getHeight());
        changeSizeBTN.addActionListener(e -> {
            mainFrame.dispose();
            if (mainFrame.getSize().equals(defaultSize)) {
                changeSizeBTN.setText((int) defaultSize.getWidth() + "x" + (int) defaultSize.getHeight());
                mainFrame.setSize(fancySize);
            } else {
                changeSizeBTN.setText((int) fancySize.getWidth() + "x" + (int) fancySize.getHeight());
                mainFrame.setSize(defaultSize);
            }
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
        changeSizeBTN.setSize(10, 10);
        JButton setUndecoratedBTN = new JButton("<html>Border-<br />less</html>");
        setUndecoratedBTN.addActionListener(e -> {
            if (isDecorated) {
                isDecorated = false;
                setUndecoratedBTN.setText("Normal");
                mainFrame.dispose();
                mainFrame.setUndecorated(true);
            } else {
                isDecorated = true;
                setUndecoratedBTN.setText("<html>Border-<br />less</html>");
                mainFrame.dispose();
                mainFrame.setUndecorated(false);
            }
            mainFrame.setVisible(true);
        });
        setUndecoratedBTN.setSize(10, 10);
        JButton recalcBTN = new JButton("Recalc");
        recalcBTN.addActionListener(e -> {
            mainFrame.dispose();
            diagram.setNewValues(Integer.parseInt(firstValField.getText()),
                    Integer.parseInt(secondValField.getText()),
                    Integer.parseInt(thirdValField.getText()));
            firstValLabel.setText(String.valueOf(diagram.getValues()[0]));
            secondValLabel.setText(String.valueOf(diagram.getValues()[1]));
            thirdValLabel.setText(String.valueOf(diagram.getValues()[2]));
            mainFrame.setVisible(true);

        });
        recalcBTN.setSize(10, 10);
        JButton exitBTN = new JButton("Exit");
        exitBTN.addActionListener(e -> System.exit(0));
        exitBTN.setSize(10, 10);

        addAllTo(buttons, changeSizeBTN, setUndecoratedBTN, recalcBTN, exitBTN);
        addAllTo(legend, new Rectangles(Color.GREEN), firstValLabel, firstValField,
                new Rectangles(Color.BLUE), secondValLabel, secondValField,
                new Rectangles(Color.RED), thirdValLabel, thirdValField);

        add(buttons, BorderLayout.WEST);
        add(legend, BorderLayout.EAST);
        add(diagram, BorderLayout.CENTER);
        setFontForEach(new Font("Helvetica Neue", Font.BOLD, 14), this);
        setVisible(true);
    }

    /**
     * Приватный класс для создания диграммы
     */
    private static class Diagram extends JPanel {
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

    private static class Rectangles extends JPanel {
        private final Color color;

        public Rectangles(Color color) {
            this.color = color;
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D graphics2D = (Graphics2D) g;
            Dimension dimension = getSize();

            Rectangle2D rect = new Rectangle2D.Double(0, 0, dimension.getWidth(), dimension.getHeight());
            graphics2D.setColor(color);
            graphics2D.fill(rect);
        }
    }

    /**
     * Создать фонт по полученной ссылке
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

    /**
     * Установить фонт для каждого переданного элемента,
     * если этот элемент является контейнером,
     * то устанавливаем фонт для всех вложенных
     * элементов
     *
     * @param font       фонт
     * @param components компонент
     */
    private void setFontForEach(Font font, Component... components) {
        for (Component component : components) {
            component.setFont(font);
            if (component instanceof Container)
                for (Component child : ((Container) component).getComponents())
                    setFontForEach(font, child);
        }
    }

    /**
     * Добавить все переданные компоненты в указанный контейнер
     *
     * @param container  контейнер
     * @param components компоненты для добавления
     */
    private void addAllTo(Container container, Component... components) {
        for (Component component : components)
            container.add(component);
    }
}
