package main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Diese Klasse enthaelt die Stadtliste, welche in ihrer regulaeren Reihenfolge eine Rundreise
 * darstellt. Die Rundreise wird durch die paint(Graphics g)-Methode als sichtbarer Graph auf der
 * GUI gezeichnet.
 *
 * @author Prof. Dr. Oliver S. Lazar
 * @version 1.1
 * @modified by Matthias Kronenberg s0571096
 */
public class Rundreise extends JPanel {

  private static final long serialVersionUID = 1L;
  private Image img;

  private int factor = 1;

  /**
   * Die Rundreise mit den Staedten
   */
  private TimeFrame[] rundreise;

  /**
   * Konstruktor für eine Initalizierung mit leerer Rundreise.
   */
  public Rundreise(int factor) {
    this.factor = factor;
    ImageIcon imgIcon = new ImageIcon("src/main/resources/images/middle-earth.png");
    img = imgIcon.getImage();
  }

  /**
   * Konstruktor, der eine Rundreise initiiert.
   *
   * @param pointDir     Pfad zur Punkte csv
   * @param timeFrameDir Pfad zur Zeitfenster csv
   */
  public Rundreise(String pointDir, String timeFrameDir) {

    rundreise = PointsInput.getPointsOrdered(pointDir, timeFrameDir);

    ImageIcon imgIcon = new ImageIcon("src/main/resources/images/middle-earth.png");
    img = imgIcon.getImage();
    factor = calculateFactor();
  }

  /**
   * Setzt einen Faktor damit die Anzeige der Punkte immer passt
   *
   * @return
   */
  private int calculateFactor() {
    int maxX = 0;
    int maxY = 0;
    int factor = 1;
    for (int i = 0; i < rundreise.length; i++) {
      Point[] points = rundreise[i].getPoints();

      for (int j = 0; j < points.length; j++) {
        if (points[j].getX() > maxX) {
          maxX = points[j].getX();
        }
      }
      for (int k = 0; k < points.length; k++) {
        if (points[k].getY() > maxY) {
          maxY = points[k].getY();
        }
      }
      while ((maxX / factor) > 820) {
        factor++;
      }
      while ((maxY / factor) > 350) {
        factor++;
      }
    }
    return factor;
  }

  /**
   * @return Die aktuelle Rundreise
   */
  public TimeFrame[] getRundreise() {
    return rundreise;
  }

  public void setRundreise(TimeFrame[] timeFrames) {
    this.rundreise = timeFrames.clone();
  }

  /**
   * Liefert ein TimeFrame-Objekt passend zum Index
   *
   * @param position
   * @return Stadt-Objekt von der Position position
   */
  public TimeFrame getTimeFrame(int position) {
    return rundreise[position];
  }

  /**
   * Positioniert ein Stadt-Objekt an die Stelle position
   *
   * @param position
   * @param point
   */
  public void setPoint(int timeFrameIndex, int position, Point point) {
    rundreise[timeFrameIndex].getPoints()[position] = point;
  }

  /**
   * liefert die komplette Distanz der Rundreise
   *
   * @return Distanz der Rundreise
   */
  public int getDistanz() {

    int rrDistanz = 0;
    Point[] points = getPointsInOneArray();
    // Unsere Rundreise durchlaufen
    for (int i = 0; i < points.length; i++) {
      // Startstadt holen
      Point start = points[i];
      // Reiseziel holen
      Point ziel;
      // Wenn wir bei der letzten Stadt in unserer Rundreise angekommen sind,
      // dann wird das Ziel auf die Startstadt gesetzt (ist ja eine Rundreise)
      if (i + 1 < points.length) {
        ziel = points[i + 1];
      } else {
        ziel = points[0];
      }
      // Die Abstaende zwischen jeweils zwei Staedten summieren
      rrDistanz += start.berechneAbstand(ziel,BestCaster.getInstance().isManhattan());
    }
    return rrDistanz;
  }

  private Point[] getPointsInOneArray() {
    List<Point> list = new ArrayList<>();
    for (TimeFrame tf : rundreise) {
      for (int i = 0; i < tf.getPoints().length; i++) {
        list.add(tf.getPoints()[i]);
      }
    }
    Point[] array = new Point[list.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = list.get(i);
    }
    return array;
  }

  @Override
  public String toString() {
    String geneString = "|";
    for (int i = 0; i < rundreise.length; i++) {
      geneString += getTimeFrame(i) + "|";
    }
    return geneString;
  }

  public void paint(Graphics g) {

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.drawImage(img, 0, 0, 800, 400, null);

    Point[] points = getPointsInOneArray();

    for (int i = 0; i < points.length; i++) {

      // Verbindungslinie zum Nachfolger zeichnen
      int zielIndex = 0;
      if (i < points.length - 1) {
        zielIndex = i + 1;
      }
      g2d.setColor(Color.WHITE);
      g2d.drawLine(points[i].getX() / factor, points[i].getY() / factor,
          points[zielIndex].getX() / factor,
          points[zielIndex].getY() / factor);

      // Knoten zeichnen
      if (i == 0) {
        g2d.setColor(Color.RED);
      } else {
        g2d.setColor(Color.YELLOW);
      }

      g2d.fillOval((points[i].getX() / factor) - 5,
          (points[i].getY() / factor) - 5, 10,
          10);

      g2d.setColor(Color.BLACK);
      g2d.drawOval((points[i].getX() / factor) - 5,
          (points[i].getY() / factor) - 5, 10,
          10);


    }

    // Damit der Startknoten schoen aussieht
    g2d.setColor(Color.RED);
    g2d.fillOval(rundreise[0].getPoints()[0].getX() - 5, rundreise[0].getPoints()[0].getY() - 5, 10,
        10);
    g2d.setColor(Color.BLACK);
    g2d.drawOval(rundreise[0].getPoints()[0].getX() - 5, rundreise[0].getPoints()[0].getY() - 5, 10,
        10);
  }

  public int getFactor() {
    return factor;
  }
}


