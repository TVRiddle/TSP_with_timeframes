package main.java;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.SystemColor;

/**
 * Das ist die Fenster-Klasse, sie enthaelt die grafischen Anzeige- und Bedienelemente.
 *
 * @author Prof. Dr. Oliver S. Lazar
 * @version 1.1
 * @modified by Matthias Kronenberg s0571096
 */

public class TSPFrame extends JFrame implements ActionListener, Cloneable {

  private static final long serialVersionUID = 1L;
  private static final String PREFERRED_LOOK_AND_FEEL = null;
  private Rundreise rrAktuell, rrInitial;
  private JTextField pointDir, timeFrameDir, speed, deliveringTime, brakePoints, outputDir, sigma, sigmaReduktion, abbruch, zeit;
  private JComboBox<String> verfahren;
  private JButton start, stop;
  private SimulatedAnnealing sa;
  private JTabbedPane tabbedPane;
  private JTable table;
  private DefaultTableModel model;
  private long programStart;

  /**
   * Konstruktor
   */
  public TSPFrame() {
    super(
        "TSP mit Simulated Annealing [1.0] - Prof. Dr. Oliver S. Lazar edit by Matthias Kronenberg");

    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    getContentPane().add(tabbedPane, BorderLayout.CENTER);

    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.SOUTH);
    panel.setLayout(new GridLayout(1, 2));

    JPanel panelLeft = new JPanel();
    panelLeft.setLayout(new GridLayout(8, 1));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    JPanel panel3 = new JPanel();
    panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    JPanel panel4 = new JPanel();
    panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    JPanel panel5 = new JPanel();
    panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    JPanel panel6 = new JPanel();
    panel6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    JPanel panel7 = new JPanel();
    panel7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    JPanel panel8 = new JPanel();
    panel8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    JPanel panel9 = new JPanel();
    panel9.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

    panelLeft.add(panel2);
    panelLeft.add(panel3);
    panelLeft.add(panel4);
    panelLeft.add(panel5);
    panelLeft.add(panel6);
    panelLeft.add(panel7);
    panelLeft.add(panel8);
    panelLeft.add(panel9);
    panel.add(panelLeft);

    JLabel lblPointDir = new JLabel("Dir csv:                 ");
    panel2.add(lblPointDir);
    pointDir = new JTextField(26);
    pointDir.setText("src/main/resources/points.csv");
    panel2.add(pointDir);

    JLabel lblTimeFrame = new JLabel("TimeFrame csv: ");
    panel3.add(lblTimeFrame);
    timeFrameDir = new JTextField(26);
    timeFrameDir.setText("src/main/resources/timeframe1.csv");
    panel3.add(timeFrameDir);

    JLabel lblBrakePoints = new JLabel("Brakepoints in min csv-text: ");
    panel4.add(lblBrakePoints);
    brakePoints = new JTextField(20);
    brakePoints.setText("0.1,0.5,1,5");
    panel4.add(brakePoints);

    panel5.add(new JLabel("OutputDir: "));
    outputDir = new JTextField(8);
    outputDir.setText("D:/timeframe1");
    panel5.add(outputDir);
    panel5.add(new JLabel("Distanzverfahren"));
    String[] names = {"ManhattanMatrix", "Luftlinie"};
    verfahren = new JComboBox<>(names);
    verfahren.setSelectedIndex(0);
    panel5.add(verfahren);

    JLabel lblSpeed = new JLabel("Fahrgeschwindigkeit in km/h");
    panel6.add(lblSpeed);
    speed = new JTextField(3);
    speed.setText("10");
    panel6.add(speed);

    JLabel lblDelivertime = new JLabel("Auslieferungszeit in min");
    panel6.add(lblDelivertime);
    deliveringTime = new JTextField(3);
    deliveringTime.setText("5");
    panel6.add(deliveringTime);

    JLabel lblSigma = new JLabel("Toleranzwert Sigma:");
    panel7.add(lblSigma);
    sigma = new JTextField(5);
    sigma.setText("100000");
    panel7.add(sigma);

    JLabel lblReduktion = new JLabel("Sigma Reduktion:");
    panel7.add(lblReduktion);
    sigmaReduktion = new JTextField(5);
    sigmaReduktion.setText("0.00015");
    panel7.add(sigmaReduktion);

    panel8.add(new JLabel("Abbruch nach "));
    abbruch = new JTextField(3);
    abbruch.setText("-1");
    panel8.add(abbruch);
    panel8.add(new JLabel("stabilen Iterationen (zum Deaktivieren -1)"));

    zeit = new JTextField(3);
    zeit.setText("5");
    panel9.add(zeit);
    panel9.add(new JLabel("ms pro Iteration          "));
    start = new JButton("Rundreise optimieren");
    start.addActionListener(this);
    panel9.add(start);
    stop = new JButton("Stop");
    stop.addActionListener(this);
    panel9.add(stop);

    table = new JTable(7, 2);
    table.setBackground(SystemColor.window);
    table.setRowSelectionAllowed(false);
    table.setColumnSelectionAllowed(true);
    table.setCellSelectionEnabled(true);
    model = (DefaultTableModel) table.getModel();
    model.setValueAt("Sigma", 0, 0);
    model.setValueAt("Iterationen", 1, 0);
    model.setValueAt("stabile Iterationen", 2, 0);
    model.setValueAt("Initialdistanz", 3, 0);
    model.setValueAt("Finaldistanz", 4, 0);
    model.setValueAt("Timeframes", 5, 0);
    model.setValueAt("Rechenzeit in Sekunden", 6, 0);
    table.setEnabled(false);
    panel.add(table);

    // Fenstergroesse setzen.
    setSize(820, 750);

    // Definieren, was beim Schliessen des Fensters geschehen soll.
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // mittig ausrichten
    this.setLocationRelativeTo(null);

    // Fenster anzeigen.
    setVisible(true);

    boolean manhattan = verfahren.getSelectedItem().equals("ManhattanMatrix") ? true : false;
    BestCaster
        .getInstance(outputDir.getText(), speed.getText(), deliveringTime.getText(), manhattan);
    erzeugeRundreise();
    BestCaster.getInstance().unsetBc();
  }

  private static void installLnF() {
    try {
      String lnfClassname = PREFERRED_LOOK_AND_FEEL;
      if (lnfClassname == null) {
        lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
      }
      UIManager.setLookAndFeel(lnfClassname);
    } catch (Exception e) {
      System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
          + " on this platform:" + e.getMessage());
    }
  }

  /**
   * Startmethode
   */
  public static void main(String[] args) {
    installLnF();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new TSPFrame();
      }
    });
  }

  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == start) {
      programStart = System.currentTimeMillis();
      boolean manhattan = verfahren.getSelectedItem().equals("ManhattanMatrix") ? true : false;
      BestCaster
          .getInstance(outputDir.getText(), speed.getText(), deliveringTime.getText(), manhattan);
      // Ueberpruefe die Eingaben
      try {
        boolean check = true;
        double sigma = Double.parseDouble(this.sigma.getText());
        double sigmaReduktion = Double.parseDouble(this.sigmaReduktion.getText());
        double abbruch = Double.parseDouble(this.abbruch.getText());
        double zeit = Double.parseDouble(this.zeit.getText());

        if (sigma < 1) {
          JOptionPane.showMessageDialog(this, "Sigma: Ungueltiger Wert");
          check = false;
        }
        if (sigmaReduktion <= 0) {
          JOptionPane.showMessageDialog(this, "Reduktion: Ungueltiger Wert");
          check = false;
        }
        if (abbruch == 0) {
          JOptionPane.showMessageDialog(this, "Stabile Iterationen: Ungueltiger Wert");
          check = false;
        }
        if (zeit < 0) {
          JOptionPane.showMessageDialog(this, "Geschwindigkeit: Ungueltiger Wert");
          check = false;
        }

        if (check) {
          sa = new SimulatedAnnealing(this, rrAktuell, sigma, sigmaReduktion);
          sa.start();
        }

      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Ungueltiger Wert");
      }

    } else if (e.getSource() == stop) {
      if (sa != null && sa.isAlive()) {

        sa.stopThread();
      }
    }
  }

  /**
   * Erzeugt eine zuf�llige Rundreise (initiale Route).
   */
  private void erzeugeRundreise() {
    try {
      boolean check = true;
      String pointDir = this.pointDir.getText();
      String timeFrame = this.timeFrameDir.getText();

      if (check) {
        // Rundreise erzeugen
        rrAktuell = new Rundreise(pointDir, timeFrame);
        rrInitial = new Rundreise(rrAktuell.getFactor());
        rrInitial.setRundreise(rrAktuell.getRundreise());

        tabbedPane.removeAll();
        tabbedPane.addTab("Aktuelle Rundreise", null, rrAktuell, "Zeigt die aktuelle Rundreise");
        tabbedPane.addTab("Initiale Rundreise", null, rrInitial, "Zeigt die initiale Rundreise");
        repaint();

        model.setValueAt(rrInitial.getDistanz(), 3, 1);
        model.setValueAt("", 0, 1);
        model.setValueAt("", 1, 1);
        model.setValueAt("", 2, 1);
        model.setValueAt("", 4, 1);
//				model.setValueAt("", 5, 1);
      }

    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this,
          "Ein Wert in einer der csv-Datein konnte nicht richtig eingelesen werden");
    }
  }


  /**
   * Aktualisiert die Tabellenwerte in der GUI
   *
   * @param sigma              Aktueller Toleranzwert
   * @param iterationen        Anzahl Iterationen
   * @param stabileIterationen Anzahl stabiler Iterationen (ohne Staedtetausch)
   * @param distanz            Die aktuelle Distanz der Rundreise
   */
  public void setTableValues(double sigma, int iterationen, int stabileIterationen, int distanz,
      int timeFrame) {
    model.setValueAt(sigma, 0, 1);
    model.setValueAt(iterationen, 1, 1);
    model.setValueAt(stabileIterationen, 2, 1);
    model.setValueAt(distanz, 4, 1);
    model.setValueAt(timeFrame, 5, 1);
    model.setValueAt((System.currentTimeMillis() - programStart) / 1000, 6, 1);
  }

  //<editor-fold desc="Getter&Setter">

  public JButton getStart() {
    return start;
  }

  public JTextField getBreakPoints() {
    return brakePoints;
  }

  public int getAbbruch() {
    return Integer.parseInt(abbruch.getText());
  }

  public int getZeit() {
    return Integer.parseInt(zeit.getText());
  }

  public JTextField getOutputDir() {
    return outputDir;
  }
  //</editor-fold>
}
