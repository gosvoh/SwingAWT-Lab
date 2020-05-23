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

        Utils.addAllTo(buttons, changeSizeBTN, setUndecoratedBTN, recalcBTN, exitBTN);
        Utils.addAllTo(legend, new Rectangles(Color.GREEN), firstValLabel, firstValField,
                new Rectangles(Color.BLUE), secondValLabel, secondValField,
                new Rectangles(Color.RED), thirdValLabel, thirdValField);

        add(buttons, BorderLayout.WEST);
        add(legend, BorderLayout.EAST);
        add(diagram, BorderLayout.CENTER);
        Utils.setFontForEach(new Font("Helvetica Neue", Font.BOLD, 14), this);
        setVisible(true);
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




}
