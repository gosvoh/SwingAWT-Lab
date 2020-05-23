import javax.swing.*;
import java.awt.*;


public class FifteenPuzzle extends JFrame {
    private final Dimension defaultFrameSize;
    private final Font defaultFont;
    private final JFrame rootFrame = this;

    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        defaultFrameSize = new Dimension(screenHeight / 3, screenHeight / 3);
        defaultFont = new Font("Arial", Font.BOLD, 16);
    }

    public FifteenPuzzle(int squareSide) {
        super("Fifteen Puzzle");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(defaultFrameSize);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(false);
        setLayout(new GridLayout(squareSide, squareSide));
        JButton[][] buttonGrid = new JButton[squareSide][squareSide];
        setFont(defaultFont);

        setJMenuBar(menuBar());


        int iterator = 0;
        for (int row = 0; row < buttonGrid.length; row++) {
            for (int col = 0; col < buttonGrid[row].length; col++) {
                buttonGrid[row][col] = new JButton(String.valueOf(iterator));
                buttonGrid[row][col].setBackground(Color.YELLOW);
                ++iterator;
                add(buttonGrid[row][col]);
                buttonGrid[row][col].addActionListener(e -> {
                    //TODO логика проверки соседних клеток и перемещение
                });
            }
        }
        buttonGrid[0][0].setName("empty");
        buttonGrid[0][0].setVisible(false);

        Utils.setFontForEach(defaultFont, rootFrame, getContentPane());
        setVisible(true);
    }

    private JMenuBar menuBar() {
        String[] fontNames = {"Arial", "Times New Roman", "JetBrains Mono", "Comic Sans MS", "Courier New"};

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu screenSizeMenu = new JMenu("Screen size");
        JMenu fontsMenu = new JMenu("Font");
        JMenu helpMenu = new JMenu("Help");
        Utils.addAllTo(jMenuBar, menuFile, screenSizeMenu, fontsMenu, helpMenu);

        /* File menu */
        JMenuItem startMenuItem = new JMenuItem("New game");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        Utils.addAllTo(menuFile, startMenuItem, new JPopupMenu.Separator(), exitMenuItem);

        /* Screen size menu*/
        JMenuItem defaultSize = new JMenuItem((int) defaultFrameSize.getWidth() + "x" + (int) defaultFrameSize.getHeight());
        JMenuItem sixHundred = new JMenuItem("600x600");
        JMenuItem eightHundred = new JMenuItem("800x800");
        Utils.addAllTo(screenSizeMenu, defaultSize, sixHundred, eightHundred);
        defaultSize.addActionListener(e -> setNewScreenSize(defaultFrameSize));
        sixHundred.addActionListener(e -> setNewScreenSize(600, 600));
        eightHundred.addActionListener(e -> setNewScreenSize(800, 800));

        /* Font menu */
        JMenu fontFamily = new JMenu("Font family");
        JMenu fontSize = new JMenu("Font size");
        JMenu fontStyle = new JMenu("Font style");
        Utils.addAllTo(fontsMenu, fontFamily, fontSize, fontStyle);

        /* Font family items*/
        for (String fontName : fontNames) {
            JMenuItem item = new JMenuItem(fontName);
            Font font = new Font(fontName, getFont().getStyle(), getFont().getSize());
            item.setFont(font);
            item.addActionListener(e -> Utils.setFontForEach(font, getRootPane()));
            fontFamily.add(item);
        }

        /* Font size items */
        ButtonGroup sizeBTNGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            int size = 12 + i * 4;
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(String.valueOf(size));
            if (getFont().getSize() == size) item.setSelected(true);
            item.addActionListener(e -> Utils.setFontForEach(new Font(getFont().getName(), getFont().getStyle(), size), rootFrame));
            sizeBTNGroup.add(item);
            fontSize.add(item);
        }

        /* Font style items */
        ButtonGroup styleBTNGroup = new ButtonGroup();
        JRadioButtonMenuItem plain = new JRadioButtonMenuItem("Plain");
        JRadioButtonMenuItem bold = new JRadioButtonMenuItem("Bold");
        JCheckBoxMenuItem italic = new JCheckBoxMenuItem("Italic");
        styleBTNGroup.add(plain);
        styleBTNGroup.add(bold);
        Utils.addAllTo(fontStyle, plain, bold, italic);

        plain.addActionListener(e -> {
            System.out.println("Current font style " + getFont().getStyle());
            Utils.updateFontStyle(getFont().getStyle() - Font.BOLD, rootFrame);
        });
        Utils.updateFontStyle(Font.PLAIN, plain);
        bold.addActionListener(e -> {
            System.out.println("Current font style " + getFont().getStyle());
            Utils.updateFontStyle(getFont().getStyle() + Font.BOLD, rootFrame);
        });
        Utils.updateFontStyle(Font.BOLD, bold);
        italic.addActionListener(e -> {
            System.out.println("Current font style " + getFont().getStyle());
            if (italic.isSelected()) Utils.updateFontStyle(getFont().getStyle() + Font.ITALIC, rootFrame);
            else Utils.updateFontStyle(getFont().getStyle() - Font.ITALIC, rootFrame);
        });
        Utils.updateFontStyle(Font.ITALIC, italic);
        switch (getFont().getStyle()) {
            case Font.PLAIN -> plain.setSelected(true);
            case Font.BOLD -> bold.setSelected(true);
            case Font.ITALIC -> italic.setSelected(true);
            case Font.BOLD + Font.ITALIC -> {
                bold.setSelected(true);
                italic.setSelected(true);
            }
        }

        JMenuItem about = new JMenuItem("About");
        JMenuItem howToPlay = new JMenuItem("How to play");
        Utils.addAllTo(helpMenu, howToPlay, new JPopupMenu.Separator(), about);
        /* Java 14 preview text block */
        String aboutMSG = """
                This program created by Aleksey Vokhmin.
                ITMO University.
                2020
                """;
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, aboutMSG, about.getText(), JOptionPane.QUESTION_MESSAGE));
        howToPlay.addActionListener(e -> JOptionPane.showMessageDialog(null, "I don't know ¯\\_(ツ)_/¯", howToPlay.getText(), JOptionPane.ERROR_MESSAGE));

        return jMenuBar;
    }

    private void setNewScreenSize(int width, int height) {
        setNewScreenSize(new Dimension(width, height));
    }

    private void setNewScreenSize(Dimension dimension) {
        this.dispose();
        this.setSize(dimension);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
