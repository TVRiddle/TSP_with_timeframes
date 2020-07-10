package main.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Diese Klasse dient der Ergebnisdokumentation und Speicherung des besten Durchlaufs
 *
 * @author Matthias Kronenberg
 * @version 1.0
 */

public class BestCaster {

  private static BestCaster bestCaster = null;

  private TimeFrame[] bestRundreise;
  private LocalTime programStart;
  private Map<LocalTime, Boolean> solutionAfter;
  private String outputDir;
  private int speed, deliveringTime;
  private boolean isManhattan;

  private BestCaster(String outputDir, String speed, String deliveringTime, boolean isManhattan) {
    solutionAfter = new HashMap<>();
    this.outputDir = outputDir;
    this.speed = Integer.parseInt(speed);
    this.deliveringTime = Integer.parseInt(deliveringTime);
    this.isManhattan = isManhattan;
  }

  public synchronized static BestCaster getInstance(String outputDir, String speed,
      String deliveringTime, boolean isManhattan) {
    if (bestCaster == null) {
      bestCaster = new BestCaster(outputDir, speed, deliveringTime, isManhattan);
      bestCaster.setBestRundreise(new TimeFrame[0]);
    }
    return bestCaster;
  }

  public synchronized static BestCaster getInstance() {
    if (bestCaster == null) {
      throw new RuntimeException("No outputdir given");
    }
    return bestCaster;
  }

  public void update() {
    Set<LocalTime> breakTimes = solutionAfter.keySet();

    for (LocalTime breakTime : breakTimes) {
      if (!solutionAfter.get(breakTime) && breakTime.isBefore(LocalTime.now())) {
        export(bestRundreise, defineFileName(breakTime));
        solutionAfter.put(breakTime, true);
      }
    }
  }

  public void setBreakPoints(String text) {
    String[] values = text.split(",");

    for (int i = 0; i < values.length; i++) {
      Double value = 60 * Double.parseDouble(values[i]);
      Long secounds = Long.parseLong(value.toString().split("\\.")[0]);
      solutionAfter.put(programStart.plusSeconds(secounds), false);
    }
  }

  public void evaluate() {
    export(bestRundreise,"best");
    unsetBc();
  }

  private String defineFileName(LocalTime breakTime) {
    long afterTime = ChronoUnit.MILLIS.between(programStart, breakTime) / 60000;
    String fileName = afterTime + "minutes";
    if (afterTime <= 0) {
      afterTime = ChronoUnit.MILLIS.between(programStart, breakTime) / 1000;
      fileName = afterTime + "seconds";
    }
    return fileName;
  }

  private void export(TimeFrame[] timeFrames, String name) {
    setTimeAndDistance(timeFrames);

    String preparedString = prepareStringForExport(timeFrames);
    File file = new File(outputDir + "/" + name + ".csv");
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdir();
    }
    try (FileWriter fr = new FileWriter(file)) {
      fr.write(preparedString);
      fr.flush();
    } catch (IOException e) {
      System.out.println("Something went wrong while writing file");
      System.err.println(e.getMessage());
      e.printStackTrace();
    }

  }

  private void setTimeAndDistance(TimeFrame[] timeFrame) {
    LocalTime time = timeFrame[0].getFrom();
    Point[] pointArray = getPointsInOneArray(timeFrame);

    for (int i = 0; i < pointArray.length; i++) {
      Point start = pointArray[i];
      Point ziel;
      if (i + 1 < pointArray.length) {
        ziel = pointArray[i + 1];
      } else {
        ziel = pointArray[0];
      }
      Double distance = start.berechneAbstand(ziel, isManhattan);

      long travelingTime = Long.parseLong(Double.valueOf((
          distance / (speed / 3.6))).toString().split("\\.")[0]);
      time = time.plusSeconds(travelingTime).plusMinutes(deliveringTime);
      ziel.setDiliverTime(time);
      ziel.setInTimeFrame((time.isAfter(ziel.getTimeFrame().getFrom()) && time
          .isBefore(ziel.getTimeFrame().getTo())) ? true : false);
      ziel.setDistanceToNext(distance);
    }
  }

  private Point[] getPointsInOneArray(TimeFrame[] timeFrames) {
    List<Point> list = new ArrayList<>();
    for (TimeFrame tf : timeFrames) {
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

  private String prepareStringForExport(TimeFrame[] timeFrames) {
    StringBuffer sb = new StringBuffer(
        "NewNr.,OldNr.,X,Y,From,To,Delivered at,In time frame,Distance to next\n");
    int counter = 0;
    for (int i = 0; i < timeFrames.length; i++) {
      for (int j = 0; j < timeFrames[i].getPoints().length; j++) {
        sb.append(counter++ + "," + timeFrames[i].getPoints()[j] + "\n");
      }
    }
    return sb.toString();
  }

  //<editor-fold desc="Getter&Setter">

  public TimeFrame[] getBestRundreise() {
    return bestRundreise;
  }

  public void setBestRundreise(TimeFrame[] bestRundreise) {
    this.bestRundreise = bestRundreise;
  }

  public void setProgramStart(LocalTime programStart) {
    this.programStart = programStart;
  }

  public LocalTime getProgramStart() {
    return programStart;
  }

  public Map<LocalTime, Boolean> getSolutionAfter() {
    return solutionAfter;
  }

  public boolean isManhattan() {
    return isManhattan;
  }

  public void unsetBc() {
    bestCaster = null;
  }
//</editor-fold>

}
