import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;


@SuppressWarnings("FieldCanBeLocal")
public class FifteenPuzzle {
    private JFrame _ROOT_FRAME;
    private final Dimension _SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private final Dimension _DEFAULT_FRAME_SIZE = new Dimension((int) _SCREEN_SIZE.getHeight() / 4, (int) _SCREEN_SIZE.getHeight() / 4);
    private Dimension _currentScreenSize = _DEFAULT_FRAME_SIZE;
    private final Font _DEFAULT_FONT = new Font("Arial", Font.BOLD, 12);
    private Font _currentFont = _DEFAULT_FONT;
    private final int _SQUARE_SIDE;
    private final int _FIELD_SIZE;
    private int _emptyCell;
    private final JButton[][] buttonGrid;
    private final String[] _WIN;

    public FifteenPuzzle() {
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(3, 3, 10, 1);
        JSpinner fieldSize = new JSpinner(spinnerNumberModel);
        JOptionPane.showMessageDialog(null, fieldSize, "Set square size", JOptionPane.QUESTION_MESSAGE);

        System.out.println(fieldSize.getValue());

        this._SQUARE_SIDE = Integer.parseInt(fieldSize.getValue().toString());
        //this._SQUARE_SIDE = 3;
        /* ----------------------- */
        _FIELD_SIZE = _SQUARE_SIDE * _SQUARE_SIDE;
        _WIN = new String[_FIELD_SIZE - 1];
        for (int i = 1; i < _FIELD_SIZE; i++)
            _WIN[i - 1] = String.valueOf(i);
        buttonGrid = new JButton[_SQUARE_SIDE][_SQUARE_SIDE];

        initializeBoard();
    }

    private void initializeBoard() {
        if (_ROOT_FRAME != null) {
            _currentScreenSize = _ROOT_FRAME.getSize();
            _currentFont = _ROOT_FRAME.getFont();
            _ROOT_FRAME.dispose();
        }
        JFrame root = new JFrame("Fifteen Puzzle");
        root.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e);
            }
        });
        _ROOT_FRAME = root;
        _ROOT_FRAME.setSize(_currentScreenSize);
        _ROOT_FRAME.setFont(_currentFont);
        _ROOT_FRAME.setLayout(new GridLayout(_SQUARE_SIDE, _SQUARE_SIDE));
        _ROOT_FRAME.setJMenuBar(menuBar());
        _ROOT_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _ROOT_FRAME.setResizable(false);
        _ROOT_FRAME.setLocationRelativeTo(null);
        _ROOT_FRAME.setUndecorated(false);

        ArrayList<Integer> listOfIndexes = null;
        for (boolean isSolvable = false; !isSolvable; ) {
            listOfIndexes = new ArrayList<>(_FIELD_SIZE);
            for (int i = 0; i < _FIELD_SIZE; i++)
                listOfIndexes.add(i);
            Collections.shuffle(listOfIndexes);
            isSolvable = isSolvable(listOfIndexes);
        }

        for (int index = 0; index < _FIELD_SIZE; index++) {
            int row = index / _SQUARE_SIDE;
            int col = index % _SQUARE_SIDE;
            buttonGrid[row][col] = new JButton(String.valueOf(listOfIndexes.get(index)));
            buttonGrid[row][col].setBackground(Color.ORANGE);
            buttonGrid[row][col].setForeground(Color.BLUE);
            buttonGrid[row][col].addActionListener(e -> {
                if (isWin())
                    return;
                swapWithEmpty(row, col);
            });
            _ROOT_FRAME.add(buttonGrid[row][col]);
            if (listOfIndexes.get(index) == 0) {
                _emptyCell = index;
                buttonGrid[row][col].setVisible(false);
            }
        }

        Utils.setFontForEach(_currentFont, _ROOT_FRAME);
        _ROOT_FRAME.setVisible(true);
    }

    private boolean isSolvable(ArrayList<Integer> listOfIndexes) {
        int countInversions = 0;

        for (int i = 0; i < listOfIndexes.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (listOfIndexes.get(j) > listOfIndexes.get(i))
                    countInversions++;
            }
        }

        return countInversions % 2 == 0;
    }

    private void swapWithEmpty(int row, int col) {
        int emptyCellRow = _emptyCell / _SQUARE_SIDE;
        int emptyCellCol = _emptyCell % _SQUARE_SIDE;
        int colDif = Math.abs(col - emptyCellCol);
        int rowDif = Math.abs(row - emptyCellRow);
        boolean sameRow = (row == emptyCellRow);
        boolean sameCol = (col == emptyCellCol);
        boolean canSwap = (sameRow || sameCol) && (colDif == 1 || rowDif == 1);
        if (canSwap) {
            buttonGrid[emptyCellRow][emptyCellCol].setText(buttonGrid[row][col].getText());
            buttonGrid[row][col].setText(String.valueOf(0));
            buttonGrid[emptyCellRow][emptyCellCol].setVisible(true);
            buttonGrid[row][col].setVisible(false);
            _emptyCell = getIndex(row, col);
        }
        if (isWin())
            JOptionPane.showMessageDialog(_ROOT_FRAME, "You win this game!", "You WIN!", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isWin() {
        for (int i = _WIN.length - 1; i >= 0; i--) {
            String num = buttonGrid[i / _SQUARE_SIDE][i % _SQUARE_SIDE].getText();
            if (!num.equals(_WIN[i]))
                return false;
        }
        return true;
    }

    /**
     * Получить индекс элемента, соответствующий строке (row) и столбцу (col)
     *
     * @param i, row
     * @param j, column
     * @return индекс элемента, соответствующий строке и столбцу
     */
    private int getIndex(int i, int j) {
        return ((i * _SQUARE_SIDE) + j);
    }

    /**
     * Инициализировать Menu bar со всеми кнопками
     *
     * @return JMenuBar
     */
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
        JMenuItem getSolution = new JMenuItem("Solution");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        startMenuItem.addActionListener(e -> initializeBoard());
        getSolution.addActionListener(e -> {
            for (int index = 0; index < _FIELD_SIZE - 2; index++) {
                int row = index / _SQUARE_SIDE;
                int col = index % _SQUARE_SIDE;
                buttonGrid[row][col].setText(String.valueOf(index + 1));
                buttonGrid[row][col].setVisible(true);
            }
            buttonGrid[_SQUARE_SIDE - 1][_SQUARE_SIDE - 2].setText(String.valueOf(0));
            buttonGrid[_SQUARE_SIDE - 1][_SQUARE_SIDE - 2].setVisible(false);
            _emptyCell = getIndex(_SQUARE_SIDE - 1, _SQUARE_SIDE - 2);
            buttonGrid[_SQUARE_SIDE - 1][_SQUARE_SIDE - 1].setText(String.valueOf(_FIELD_SIZE - 1));
            buttonGrid[_SQUARE_SIDE - 1][_SQUARE_SIDE - 1].setVisible(true);
        });
        exitMenuItem.addActionListener(e -> System.exit(0));
        Utils.addAllTo(menuFile, startMenuItem, getSolution, new JPopupMenu.Separator(), exitMenuItem);

        /* Screen size menu*/
        JMenuItem defaultSize = new JMenuItem("Default size " + (int) _DEFAULT_FRAME_SIZE.getHeight() + "x" + (int) _DEFAULT_FRAME_SIZE.getHeight());
        JMenuItem fancySize = new JMenuItem("Fancy size " + (int) _SCREEN_SIZE.getHeight() / 3 + "x" + (int) _SCREEN_SIZE.getHeight() / 3);
        JMenuItem megaFancySize = new JMenuItem("Mega fancy size " + (int) _SCREEN_SIZE.getHeight() / 2 + "x" + (int) _SCREEN_SIZE.getHeight() / 2);
        Utils.addAllTo(screenSizeMenu, defaultSize, fancySize, megaFancySize);
        defaultSize.addActionListener(e -> setNewScreenSize(_DEFAULT_FRAME_SIZE));
        fancySize.addActionListener(e -> setNewScreenSize((int) _SCREEN_SIZE.getHeight() / 3, (int) _SCREEN_SIZE.getHeight() / 3));
        megaFancySize.addActionListener(e -> setNewScreenSize((int) _SCREEN_SIZE.getHeight() / 2, (int) _SCREEN_SIZE.getHeight() / 2));

        /* Font menu */
        JMenu fontFamily = new JMenu("Font family");
        JMenu fontSize = new JMenu("Font size");
        JMenu fontStyle = new JMenu("Font style");
        Utils.addAllTo(fontsMenu, fontFamily, fontSize, fontStyle);

        /* Font family items*/
        for (String fontName : fontNames) {
            JMenuItem item = new JMenuItem(fontName);
            Font font = new Font(fontName, _ROOT_FRAME.getFont().getStyle(), _ROOT_FRAME.getFont().getSize());
            item.setFont(font);
            item.addActionListener(e -> Utils.setFontForEach(font, _ROOT_FRAME.getRootPane()));
            fontFamily.add(item);
        }

        /* Font size items */
        ButtonGroup sizeBTNGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            int size = 12 + i * 4;
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(String.valueOf(size));
            if (_ROOT_FRAME.getFont().getSize() == size) item.setSelected(true);
            item.addActionListener(e -> Utils.setFontForEach(new Font(_ROOT_FRAME.getFont().getName(), _ROOT_FRAME.getFont().getStyle(), size), _ROOT_FRAME));
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

        plain.addActionListener(e -> Utils.updateFontStyle(_ROOT_FRAME.getFont().getStyle() - Font.BOLD, _ROOT_FRAME));
        Utils.updateFontStyle(Font.PLAIN, plain);
        bold.addActionListener(e -> Utils.updateFontStyle(_ROOT_FRAME.getFont().getStyle() + Font.BOLD, _ROOT_FRAME));
        Utils.updateFontStyle(Font.BOLD, bold);
        italic.addActionListener(e -> {
            if (italic.isSelected()) Utils.updateFontStyle(_ROOT_FRAME.getFont().getStyle() + Font.ITALIC, _ROOT_FRAME);
            else Utils.updateFontStyle(_ROOT_FRAME.getFont().getStyle() - Font.ITALIC, _ROOT_FRAME);
        });
        Utils.updateFontStyle(Font.ITALIC, italic);
        switch (_ROOT_FRAME.getFont().getStyle()) {
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
        howToPlay.addActionListener(e -> JOptionPane.showMessageDialog(null, "I dunno ¯\\_(ツ)_/¯", howToPlay.getText(), JOptionPane.ERROR_MESSAGE));

        return jMenuBar;
    }

    /**
     * Установить новый размер главного окна (rootFrame)
     *
     * @param width  ширина
     * @param height высота
     */
    private void setNewScreenSize(int width, int height) {
        setNewScreenSize(new Dimension(width, height));
    }

    private void setNewScreenSize(Dimension dimension) {
        _ROOT_FRAME.dispose();
        _ROOT_FRAME.setSize(dimension);
        _ROOT_FRAME.setLocationRelativeTo(null);
        _ROOT_FRAME.setVisible(true);
    }
}
