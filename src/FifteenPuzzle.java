import javax.swing.*;
import java.awt.*;

public class FifteenPuzzle extends JFrame {
    private final Dimension defaultSize;
    private final Dimension fancySize;
    private final Font defaultFont;
    private boolean isDecorated = true;
    private JFrame rootFrame = this;

    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        defaultSize = new Dimension(screenHeight / 3, screenHeight / 3);
        if (screenHeight > 600) fancySize = new Dimension(600, 600);
        else fancySize = defaultSize;
        defaultFont = new Font("Arial", Font.BOLD, 16);
    }

    public FifteenPuzzle() {
        super("Fifteen Puzzle");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(defaultSize);
        setMaximumSize(fancySize);
        setSize(defaultSize);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(false);
        setLayout(new GridLayout(4, 4));
        JButton[][] buttonGrid = new JButton[4][4];
        setFont(defaultFont);

        setJMenuBar(menuBar());


        int iterator = 1;
        for (int row = 0; row < buttonGrid.length; row++) {
            for (int col = 0; col < buttonGrid[row].length && iterator < buttonGrid.length * buttonGrid[row].length; col++) {
                buttonGrid[row][col] = new JButton(String.valueOf(iterator));
                ++iterator;
                add(buttonGrid[row][col]);
            }
        }

        Utils.setFontForEach(defaultFont, rootFrame, getContentPane());
        setVisible(true);
    }

    private JMenuBar menuBar() {
        String[] fontNames = {"Arial", "Times New Roman", "JetBrains Mono", "Comic Sans MS", "Courier New"};

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu fontsMenu = new JMenu("Font");
        jMenuBar.add(menuFile);
        jMenuBar.add(fontsMenu);

        /* File menu */
        JMenuItem startMenuItem = new JMenuItem("New game");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        menuFile.add(startMenuItem);
        menuFile.addSeparator();
        menuFile.add(exitMenuItem);


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

        return jMenuBar;
    }
}
