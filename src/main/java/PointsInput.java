package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Diese Handelt den Input und erstellt die daraus resultierende Punkte
 *
 * @author Matthias Kronenberg
 * @version 1.0
 */

public class PointsInput {

  /**
   * Hilfsmethode mit der man ein Array an Punkten erhält
   *
   * @param pointDir
   * @param timeFrameDir
   * @return
   */
  public static TimeFrame[] getPointsOrdered(String pointDir, String timeFrameDir) {
    List<TimeFrame> timeFramesList = new ArrayList<>();
    List<Point> list = getPoints(pointDir, timeFrameDir);
    for (Point p : list) {
      if (timeFramesList.size() == 0) {
        timeFramesList.add(p.getTimeFrame());
      }
      if (!checkTfAlreadyInList(p, timeFramesList)) {
        timeFramesList.add(p.getTimeFrame());
      }
    }
    for (Point p : list) {
      for (TimeFrame tf : timeFramesList) {
        if (tf.compareTo(p.getTimeFrame()) == 0) {
          tf.addPoint(p);
        }
      }
    }

    Collections.sort(timeFramesList, new Comparator<TimeFrame>() {
      @Override
      public int compare(TimeFrame o1, TimeFrame o2) {
        if (o1.getFrom().compareTo(o2.getFrom()) != 0) {
          return o1.getFrom().compareTo(o2.getFrom());
        }
        if (o1.getTo().compareTo(o2.getTo()) != 0) {
          return o1.getTo().compareTo(o2.getTo());
        }
        return 0;
      }
    });

    return getArray(timeFramesList);
  }

  private static boolean checkTfAlreadyInList(Point p, List<TimeFrame> timeFramesList) {
    for (TimeFrame timeFrame : timeFramesList) {
      if (p.getTimeFrame().equals(timeFrame)) {
        return true;
      }
    }
    return false;
  }

  private static List<Point> getPoints(String pointDir, String timeFrameDir) {
    List<String[]> sp = getStringArray(pointDir);
    List<String[]> tf = getStringArray(timeFrameDir);

    if (sp.size() > tf.size()) {
      throw new InputMismatchException("Es sind mehr Punkte als Zeitframes angegeben worden");
    }

    return getPointList(sp, tf);
  }

  private static List<Point> getPointList(List<String[]> sp, List<String[]> tf) {
    List<Point> list = new ArrayList<>();
    for (int i = 0; i < sp.size(); i++) {
      int nr = Integer.parseInt(sp.get(i)[0]);
      int x = (int) Double.parseDouble(sp.get(i)[1]);
      int y = (int) Double.parseDouble(sp.get(i)[2]);
      String[] tfLine = getTfForNr(tf, nr);
      LocalTime[] time = getTime(tfLine);
      LocalTime from = time[0];
      LocalTime to = time[1];
      list.add(new Point(nr, x, y, new TimeFrame(from, to)));
    }
    return list;
  }

  private static LocalTime[] getTime(String[] tfLine) {
    LocalTime[] times = new LocalTime[2];
    String[] test = tfLine[1].split("-");

    times[0] = LocalTime.parse(test[0].replace('.', ':'));
    times[1] = LocalTime.parse(test[1].replace('.', ':'));
    return times;
  }

  private static String[] getTfForNr(List<String[]> tf, int nr) {
    for (int i = 0; i < tf.size(); i++) {
      if (isNumber(tf.get(i)[0])) {
        if (Integer.parseInt(tf.get(i)[0]) == nr) {
          return tf.get(i);
        }
      }
    }
    throw new InputMismatchException(
        "Eine der gelisteten Punkte hat kein Zeitfenster zugewiesen bekommen");
  }

  private static boolean isNumber(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static List<String[]> getStringArray(String pointDir) {
    List<String[]> list = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(pointDir))) {
      String line = br.readLine();
      while (line != null) {
        list.add(line.split(","));
        line = br.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  private static TimeFrame[] getArray(List<TimeFrame> list) {
    TimeFrame[] array = new TimeFrame[list.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = list.get(i);
    }
    return array;
  }
}
