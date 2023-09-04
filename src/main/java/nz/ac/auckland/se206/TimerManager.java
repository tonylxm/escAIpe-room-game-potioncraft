package nz.ac.auckland.se206;

public class TimerManager {
  public static String stringTime = "2:00";
  public static CountdownTimer countdownTimer = new CountdownTimer(stringTime);

  public static CountdownTimer getCountdownTimer() {
    System.out.println("getting timer");
    return countdownTimer;
  }
}
