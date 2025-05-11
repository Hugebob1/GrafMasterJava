import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.awt.Color;

public class Gui {

    //
    public static GraphChunk []splited;
    public static GraphChunk []graphs; //podgrafy
    public static String filename; //tmp
    public static JList<String> listaPlikow; //lista plikow dostepnych do wczytania
    public static JTextArea Errors = new JTextArea(2,20); // wyswietla komunikaty programu
    public static JTextField field = new JTextField(10); // wyswietla jaki graf wczytany
    public static JTextField fieldP = new JTextField(10); // liczba czesci z tego bedzie
    public static JTextField fieldD = new JTextField(10); // % z tego bedzie

    public static JTextField fieldTxt = new JTextField(10); // liczba czesci z tego bedzie
    public static JTextField fieldBin = new JTextField(10); // % z tego bedzie

    public static JTextField GrNr = new JTextField(10);

    public static void window(){
        JFrame frame = new JFrame("Wybierz plik");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());

        JPanel przyciskiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton rysujBtn = new JButton("Rysuj");
        JButton podzielBtn = new JButton("Podziel");
        JButton forceBtn = new JButton("Force");
        przyciskiPanel.add(rysujBtn);
        przyciskiPanel.add(podzielBtn);
        przyciskiPanel.add(forceBtn);

        DefaultListModel<String> model = getFiles("data");

        JList<String> listaPlikow = getStringJList(model);

        JScrollPane scrollPane = new JScrollPane(listaPlikow);

        frame.add(przyciskiPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
    private static JList<String> getStringJList(DefaultListModel<String> model) {
        JList<String> listaPlikow = new JList<>(model);
        listaPlikow.setFixedCellHeight(25);
        listaPlikow.setFont(new Font("SansSerif", Font.PLAIN, 14));
        listaPlikow.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER); // Wyśrodkowanie tekstu
                return label;
            }
        });
        return listaPlikow;
    }
    public static DefaultListModel<String> getFiles(String folderName){
        DefaultListModel<String> model = new DefaultListModel<>();
        File folder = new File(folderName);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    model.addElement(file.getName());
                }
            }
        }
        return model;
    }

    public static void lol(){
        Errors.setLineWrap(true);
        Errors.setWrapStyleWord(true);
        Errors.setEditable(false);

        JFrame frame = new JFrame("Panel z górnym paskiem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(new BorderLayout());

        // GÓRNY PANEL: jeden pasek na całą szerokość
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(0, 150));
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        DefaultListModel<String> model = getFiles("data");

        listaPlikow = getStringJList(model);

        JScrollPane scrollPane = new JScrollPane(listaPlikow);
        scrollPane.setPreferredSize(new Dimension(300, 120));

        JPanel listaPanel = new JPanel();
        TitledBorder border =  BorderFactory.createTitledBorder("Dostepne Pliki z Grafami");
        border.setTitleJustification(TitledBorder.CENTER);
        listaPanel.setBorder(border);
        listaPanel.add(scrollPane);



        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBorder(BorderFactory.createTitledBorder("Wczytany Graf"));
        fieldPanel.add(field, BorderLayout.CENTER);



        JPanel f2 = new JPanel(new BorderLayout());
        f2.setBorder(BorderFactory.createTitledBorder("Liczba Czesci"));
        f2.add(fieldP, BorderLayout.CENTER);


        JPanel f1 = new JPanel(new BorderLayout());
        f1.setBorder(BorderFactory.createTitledBorder("Roznica %"));
        f1.add(fieldD, BorderLayout.CENTER);


        JPanel f2T = new JPanel(new BorderLayout());
        f2T.setBorder(BorderFactory.createTitledBorder("Nazwa pliku txt"));
        f2T.add(fieldTxt, BorderLayout.CENTER);


        JPanel f1B = new JPanel(new BorderLayout());
        f1B.setBorder(BorderFactory.createTitledBorder("Nazwa pliku bin"));
        f1B.add(fieldBin, BorderLayout.CENTER);




        JPanel f5 = new JPanel(new BorderLayout());
        f5.setBorder(BorderFactory.createTitledBorder("Numer Grafu"));
        f5.add(GrNr, BorderLayout.CENTER);

//        JTextField Errors = new JTextField(15);

        JPanel Fe = new JPanel(new BorderLayout());
        Fe.setBorder(BorderFactory.createTitledBorder("Komunikaty Programu"));
        Fe.add(Errors, BorderLayout.CENTER);

        JButton loadGraf = new JButton("Wczytaj Graf");
        JButton drawGraf = new JButton("Rysuj Graf");
        JButton splitGraf = new JButton("Podziel Graf");
        JButton saveGraf = new JButton("Zapisz Graf");
        JButton forceGraf = new JButton("Force");

        JPanel gridPanel = new JPanel();

        loadGraf.addActionListener(e -> wczytajGraf());

// Przycisk 2: Rysuj graf
        drawGraf.addActionListener(e -> {
            if (graphs != null) {
                generujSiatkeZGrafami(graphs, gridPanel, frame);
            } else {
                Errors.setText( "Najpierw wczytaj graf.");
            }
        });
        // Przycisk 3: Force
        forceGraf.addActionListener(e -> przymusGraf());

// Przycisk 3: Podziel graf
        splitGraf.addActionListener(e -> podzielGraf());

        saveGraf.addActionListener(e -> zapiszGraf());

        topPanel.add(Fe); // komunikaty
        topPanel.add(loadGraf);
        topPanel.add(drawGraf);
        topPanel.add(splitGraf);
        topPanel.add(forceGraf);
        topPanel.add(saveGraf);
        topPanel.add(f2); // liczba czesci
        topPanel.add(f1); // roznica
        topPanel.add(f5); //numer grafu
        topPanel.add(f1B); // nazwa pliku bin
        topPanel.add(f2T); //nazwa pliku txt
        topPanel.add(fieldPanel); // wczytany graf
        topPanel.add(listaPanel); // lista plikow


        frame.add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPaneGrid = new JScrollPane(gridPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneGrid.getVerticalScrollBar().setUnitIncrement(16); // płynniejsze scrollowanie
        frame.add(scrollPaneGrid, BorderLayout.CENTER);


//        frame.add(gridPanel, BorderLayout.CENTER);
        frame.setVisible(true);


    }
    private static void wczytajGraf() {
        System.out.println("Kliknięto: Wczytaj Graf");
        String file = listaPlikow.getSelectedValue();
        System.out.println(file);
        if(file!=null){
            if(file.endsWith(".txt")){
                graphs = GraphLoader.loadGraphFromTxt(file);
            }
            else if(file.endsWith(".bin")){
                graphs = GraphLoader.loadGraphFromBin(file);
            }
            if(graphs!=null){
                Errors.setText("Graf Wczytano pomyslnie");
                field.setText(file);
            }
            else{
                Errors.setText("Blad podczas wczytywania");
            }

        }
        else{
            Errors.setText("Prosze wybrac plik");
        }
    }


    private static void podzielGraf() {
        System.out.println("Kliknięto: Podziel Graf");

        if (graphs == null) {
            Errors.setText("Najpierw wczytaj graf.");
            return;
        }

        if (fieldD == null || fieldP == null) {
            Errors.setText("Brak danych wejściowych.");
            return;
        }

        try {
            if (graphs.length == 1) {
                int n = Integer.parseInt(fieldP.getText().trim());
                float d = Float.parseFloat(fieldD.getText().trim());
                GraphChunk g = graphs[0];

                GraphChunk[] podzielone = GraphPartitioner.splitGraphRetryIfNeeded(g, n, d);
                if (podzielone == null) {
                    Errors.setText("Nie udało się podzielić grafu.");
                } else if (!GraphPartitioner.diff(podzielone, d)) {
                    Errors.setText("Roznica przekroczona");
                } else {
                    graphs = podzielone;
                    splited = graphs;
                    Errors.setText("Graf podzielono na " + n + " części.");
                }
            } else {
                if (GrNr == null || GrNr.getText().trim().isEmpty()) {
                    Errors.setText("Podaj numer grafu do podziału.");
                    return;
                }

                int gNum = Integer.parseInt(GrNr.getText().trim()) - 1;
                int n = Integer.parseInt(fieldP.getText().trim());
                float d = Float.parseFloat(fieldD.getText().trim());

                if (gNum >= graphs.length || gNum < 0) {
                    Errors.setText("Nie ma takiego grafu");
                    return;
                }

                GraphChunk g = graphs[gNum];
                GraphChunk[] podzielone = GraphPartitioner.splitGraphRetryIfNeeded(g, n, d);
                if (podzielone == null) {
                    Errors.setText("Nie udało się podzielić grafu.");
                } else if (!GraphPartitioner.diff(podzielone, d)) {
                    Errors.setText("Roznica przekroczona");
                } else {
                    graphs = podzielone;
                    splited = graphs;
                    Errors.setText("Graf podzielono na " + n + " części.");
                }
            }

            GrNr.setText("");
            fieldD.setText("");
            fieldP.setText("");

        } catch (NumberFormatException e) {
            Errors.setText("Błąd: podaj poprawne liczby (części i % różnicy).");
        }
    }


    private static void przymusGraf() {
        System.out.println("Kliknięto: Force");

        if (graphs == null) {
            Errors.setText("Najpierw wczytaj graf.");
            return;
        }

        if (fieldP == null) {
            Errors.setText("Brak danych wejściowych.");
            return;
        }

        try {
            int n = Integer.parseInt(fieldP.getText().trim());

            int gNum;
            if (graphs.length == 1) {
                gNum = 0;
            } else {
                if (GrNr == null || GrNr.getText().trim().isEmpty()) {
                    Errors.setText("Podaj numer grafu do podziału.");
                    return;
                }

                gNum = Integer.parseInt(GrNr.getText().trim()) - 1;

                if (gNum < 0 || gNum >= graphs.length) {
                    Errors.setText("Nie ma takiego grafu.");
                    return;
                }
            }

            GraphChunk g = graphs[gNum];
            GraphChunk[] podzielone = GraphPartitioner.splitGraphRetryIfNeeded(g, n, 10000);
            if (podzielone == null) {
                Errors.setText("Nie udało się podzielić grafu.");
            } else {
                graphs = podzielone;
                splited = graphs;
                Errors.setText("Graf podzielono na " + n + " części.");
            }

            GrNr.setText("");
            fieldD.setText("");
            fieldP.setText("");

        } catch (NumberFormatException e) {
            Errors.setText("Błąd: podaj poprawną liczbę części.");
        }
    }


    public static void zapiszGraf() {

        if (splited == null) {
            Errors.setText("Najpierw podziel graf.");
            return;
        }

        String fileTxt = fieldTxt.getText().trim();
        String fileBin = fieldBin.getText().trim();

        boolean saved = false;
        // Walidacja pliku tesktowego
        if (!fileTxt.isEmpty()) {
            if (!fileTxt.toLowerCase().endsWith(".txt")) {
                Errors.setText("Nieprawidłowe rozszerzenie pliku TXT! Oczekiwano pliku .txt.");
                return;
            }
            try {
                GraphSaver.saveGraphsTxt(splited, fileTxt);
                saved = true;
            } catch (Exception e) {
                Errors.setText("Błąd zapisu TXT: " + e.getMessage());
                return;
            }
        }
        // Walidacja pliku binarnego
        if (!fileBin.isEmpty()) {
            if (!fileBin.toLowerCase().endsWith(".bin")) {
                Errors.setText("Nieprawidłowe rozszerzenie pliku BIN! Oczekiwano pliku .bin.");
                return;
            }
            try {
                GraphSaver.saveGraphsBin(splited, fileBin);
                saved = true;
            } catch (Exception e) {
                Errors.setText("Błąd zapisu BIN: " + e.getMessage());
                return;
            }
        }
        if (!fileTxt.isEmpty()) {
            try {

                GraphSaver.saveGraphsTxt(splited, fileTxt);
                saved = true;
            } catch (Exception e) {
                Errors.setText("Błąd zapisu TXT: " + e.getMessage());
                return;
            }
        }

        if (!fileBin.isEmpty()) {
            try {
                GraphSaver.saveGraphsBin(splited, fileBin);
                saved = true;
            } catch (Exception e) {
                Errors.setText("Błąd zapisu BIN: " + e.getMessage());
                return;
            }
        }

        if (saved) {
            Errors.setText("Pomyślnie zapisano.");
            fieldTxt.setText("");
            fieldBin.setText("");

            DefaultListModel<String> model = getFiles("data");
            listaPlikow.setModel(model);
        } else {
            Errors.setText("Podaj przynajmniej jedną nazwę pliku.");
        }
    }

    private static void generujSiatkeZGrafami(GraphChunk[] graphs, JPanel gridWrapper, JFrame frame) {
        if (graphs == null || graphs.length == 0) {
            JOptionPane.showMessageDialog(frame, "Nie wczytano żadnych grafów.");
            return;
        }

        gridWrapper.removeAll();

        int numGraphs = graphs.length;
        int cols = Math.min(numGraphs, 3);
        int rows = (int) Math.ceil((double) numGraphs / cols);
        gridWrapper.setLayout(new GridLayout(rows, cols, 10, 10));

        List<Color> colors = generateColorPalette(numGraphs);

        for (int i = 0; i < numGraphs; i++) {
            GraphPanel panel = new GraphPanel(graphs[i], colors.get(i));
            if (numGraphs > 2) {
                panel.setPreferredSize(new Dimension(625, 418));
            }

            panel.setBackground(Color.WHITE);

            JLabel label = new JLabel("Graf " + (i + 1), SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));

            JPanel container = new JPanel(new BorderLayout());
            container.add(label, BorderLayout.NORTH);
            container.add(panel, BorderLayout.CENTER);
            container.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            JScrollPane scrollPane = new JScrollPane(panel,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBorder(null); // opcjonalnie, żeby nie było podwójnych ramek
            container.add(scrollPane, BorderLayout.CENTER);


            gridWrapper.add(container);
        }

        int totalCells = rows * cols;
        for (int i = numGraphs; i < totalCells; i++) {
            JPanel filler = new JPanel();
            filler.setBackground(new Color(240, 240, 240));
            gridWrapper.add(filler);
        }

        frame.revalidate();
        frame.repaint();
    }



    public static class PlaceholderTextField extends JTextField implements FocusListener {
        private final String placeholder;
        private boolean showingPlaceholder;

        public PlaceholderTextField(String placeholder, int columns) {
            super(placeholder, columns);
            this.placeholder = placeholder;
            this.showingPlaceholder = true;
            setForeground(Color.GRAY);
            addFocusListener(this);
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (showingPlaceholder) {
                setText("");
                setForeground(Color.BLACK);
                showingPlaceholder = false;
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (getText().isEmpty()) {
                setText(placeholder);
                setForeground(Color.GRAY);
                showingPlaceholder = true;
            }
        }

        @Override
        public String getText() {
            return showingPlaceholder ? "" : super.getText();
        }
    }
    public static class GraphPanel extends JPanel {
        private final GraphChunk graph;
        private final Color vertexColor;
        private double zoom = 1.0;
        private double panX = 0;
        private double panY = 0;
        private Point lastMousePos = null;

        public GraphPanel(GraphChunk graph, Color vertexColor) {
            this.graph = graph;
            this.vertexColor = vertexColor;
            setBackground(Color.WHITE);

            // Zoom przy Ctrl + scroll
            addMouseWheelListener(e -> {
                if (!e.isControlDown()) return;

                double mouseX = e.getX();
                double mouseY = e.getY();

                double zoomFactor = (e.getWheelRotation() < 0) ? 1.1 : 1.0 / 1.1;
                double oldZoom = zoom;
                zoom *= zoomFactor;
                zoom = Math.max(0.2, Math.min(zoom, 5.0));

                // Uwzględnij środek grafu i skalowanie
                Rectangle2D bounds = getGraphBounds();
                double panelWidth = getWidth();
                double panelHeight = getHeight();

                double scaleX = (bounds.getWidth() == 0) ? 1 : (panelWidth - 40) / bounds.getWidth();
                double scaleY = (bounds.getHeight() == 0) ? 1 : (panelHeight - 40) / bounds.getHeight();
                double scale = Math.min(scaleX, scaleY);

                double centerOffsetX = (panelWidth - bounds.getWidth() * scale) / 2.0;
                double centerOffsetY = (panelHeight - bounds.getHeight() * scale) / 2.0;

                // Pozycja kursora w układzie grafu
                double graphX = (mouseX - panX - centerOffsetX) / (oldZoom * scale);
                double graphY = (mouseY - panY - centerOffsetY) / (oldZoom * scale);

                // Nowy panX/panY tak, by zachować punkt pod kursorem
                panX = mouseX - (graphX * zoom * scale + centerOffsetX);
                panY = mouseY - (graphY * zoom * scale + centerOffsetY);

                repaint();
            });

            // Dragowanie
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        lastMousePos = e.getPoint();
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    lastMousePos = null;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    if (lastMousePos != null && SwingUtilities.isLeftMouseButton(e)) {
                        Point current = e.getPoint();
                        panX += current.x - lastMousePos.x;
                        panY += current.y - lastMousePos.y;
                        lastMousePos = current;
                        repaint();
                    }
                }
            });
        }

        private Rectangle2D getGraphBounds() {
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

            for (Vertex v : graph) {
                if (v == null) continue;
                minX = Math.min(minX, v.x);
                maxX = Math.max(maxX, v.x);
                minY = Math.min(minY, v.y);
                maxY = Math.max(maxY, v.y);
            }
            return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int radius = 20;

            Rectangle2D bounds = getGraphBounds();
            double minX = bounds.getX();
            double minY = bounds.getY();
            double width = bounds.getWidth();
            double height = bounds.getHeight();

            double panelWidth = getWidth();
            double panelHeight = getHeight();

            double scaleX = (width == 0) ? 1 : (panelWidth - 2 * radius) / width;
            double scaleY = (height == 0) ? 1 : (panelHeight - 2 * radius) / height;
            double scale = Math.min(scaleX, scaleY);

            double centerOffsetX = (panelWidth - width * scale) / 2.0;
            double centerOffsetY = (panelHeight - height * scale) / 2.0;

            // Rysowanie krawędzi
            g2.setColor(Color.BLACK);
            for (Vertex v : graph) {
                if (v == null || v.edges == null) continue;
                int x1 = (int) ((v.x - minX) * scale * zoom + centerOffsetX + panX);
                int y1 = (int) ((v.y - minY) * scale * zoom + centerOffsetY + panY);

                for (int i = 0; i < v.degree; i++) {
                    Vertex t = graph.getById(v.edges[i]);
                    if (t == null) continue;
                    int x2 = (int) ((t.x - minX) * scale * zoom + centerOffsetX + panX);
                    int y2 = (int) ((t.y - minY) * scale * zoom + centerOffsetY + panY);
                    g2.drawLine(x1, y1, x2, y2);
                }
            }

            // Rysowanie wierzchołków
            for (Vertex v : graph) {
                if (v == null) continue;
                int x = (int) ((v.x - minX) * scale * zoom + centerOffsetX + panX);
                int y = (int) ((v.y - minY) * scale * zoom + centerOffsetY + panY);

                g2.setColor(vertexColor);
                g2.fillOval(x - radius / 2, y - radius / 2, radius, radius);
                g2.setColor(Color.BLACK);
                g2.drawOval(x - radius / 2, y - radius / 2, radius, radius);

                String label = String.valueOf(v.id);
                FontMetrics fm = g2.getFontMetrics();
                int w = fm.stringWidth(label);
                int h = fm.getAscent();
                g2.drawString(label, x - w / 2, y + h / 2 - 2);
            }
        }
    }
    public static List<Color> generateColorPalette(int n) {
        List<Color> colors = new ArrayList<>();
        if (n <= 0) return colors;

        float step = 1.0f / n;
        for (int i = 0; i < n; i++) {
            colors.add(Color.getHSBColor(i * step, 0.6f, 0.95f));
        }
        return colors;
    }



}
