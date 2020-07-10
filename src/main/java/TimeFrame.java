package main.java;

import java.time.LocalTime;

/**
 * Diese Klasse definiert ein Zeitfenster in dem einzelne Punkte später angeordnet werden.
 *
 * @author Matthias Kronenberg
 * @version 1.0
 */
public class TimeFrame implements Comparable<TimeFrame> {

  private LocalTime from;
  private LocalTime to;
  private Point[] points;

  public TimeFrame(LocalTime from, LocalTime to) {
    this.from = from;
    this.to = to;
    this.points = new Point[0];
  }

  public boolean addPoint(Point p) {
    Point[] pArray = new Point[points.length + 1];
    for (int i = 0; i < pArray.length - 1; i++) {
      pArray[i] = points[i];
    }
    pArray[points.length] = p;
    points = pArray;
    return true;
  }

  @Override
  public int compareTo(TimeFrame o) {
    if (o.from.isBefore(from)) {
      return 1;
    }
    if (o.from.isAfter(from)) {
      return -1;
    }
    if (o.to.isBefore(to)) {
      return 1;
    }
    if (o.to.isAfter(to)) {
      return -1;
    }
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    TimeFrame o = (TimeFrame) obj;
    if (o.from.equals(from) && o.to.equals(to)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return from + "," + to;
  }

  //<editor-fold desc="Getter&Setter">

  public Point[] getPoints() {
    return points;
  }

  public void setPoints(Point[] points) {
    this.points = points;
  }

  public LocalTime getFrom() {
    return from;
  }

  public void setFrom(LocalTime from) {
    this.from = from;
  }

  public LocalTime getTo() {
    return to;
  }

  public void setTo(LocalTime to) {
    this.to = to;
  }
//</editor-fold>
}