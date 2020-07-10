package main.java;

import java.time.LocalTime;

/**
 * Diese Klasse simmuliert einen Punkt auf der Karte und kann für weitere Auswertungen ein
 * zugehöriges Zeitfenster und die Distanz zum nächtsen Punkt aufnehmen
 *
 * @author Matthias Kronenberg
 * @version 1.0
 */
public class Point {

  private Integer nr;
  private int x;
  private int y;
  private TimeFrame timeFrame;
  private LocalTime diliverTime;
  private double distanceToNext;
  private boolean inTimeFrame;

  public Point(int nr, int x, int y, TimeFrame timeFrame) {
    this.nr = nr;
    this.y = y;
    this.x = x;
    this.timeFrame = timeFrame;
  }

  public double berechneAbstand(Point p, boolean manhattan) {
    if (manhattan) {
      return Math.abs(getX() - p.getX()) + Math.abs(getY() - p.getY());
    } else {
      double xAbstand = getX() - p.getX();
      double yAbstand = getY() - p.getY();
      double abstand = Math.sqrt((xAbstand * xAbstand) + (yAbstand * yAbstand));

      return abstand;
    }
  }

  @Override
  public String toString() {
    return this.nr + "," + this.x + "," + this.y + "," + this.timeFrame
        + "," + diliverTime + "," + inTimeFrame + "," + distanceToNext;
  }

  //<editor-fold desc="Getter & Setter">

  public int getNr() {
    return nr;
  }

  public void setNr(int nr) {
    this.nr = nr;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setNr(Integer nr) {
    this.nr = nr;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  public void setTimeFrame(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
  }

  public LocalTime getDiliverTime() {
    return diliverTime;
  }

  public void setDiliverTime(LocalTime diliverTime) {
    this.diliverTime = diliverTime;
  }

  public void setDistanceToNext(double distanceToNext) {
    this.distanceToNext = distanceToNext;
  }

  public boolean isInTimeFrame() {
    return inTimeFrame;
  }

  public void setInTimeFrame(boolean inTimeFrame) {
    this.inTimeFrame = inTimeFrame;
  }

  public double getDistanceToNext() {
    return distanceToNext;
  }

  //</editor-fold>
}
