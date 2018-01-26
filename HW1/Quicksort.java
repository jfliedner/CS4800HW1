import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

class Quicksort {
  public static void main(String[] args) {
    ArrayList<Request> myRequests = new ArrayList<Request>();
    ArrayList<Request> schedule = new ArrayList<Request>();
    Scanner myScanner = new Scanner(System.in);
    while (myScanner.hasNextLine()) {
      myRequests.add(new Request(myScanner.nextLine()));
    }

    // quicksort
    Collections.sort(myRequests);
    schedule = buildSchedule(myRequests);

    for (Request r : schedule) {
      System.out.println(r);
    }
  }
  

  // builds non-overlapping schedule from list of requests sorted with
  // quicksort
  static ArrayList<Request> buildSchedule(ArrayList<Request> requests) {
    ArrayList<Request> builtSchedule = new ArrayList<Request>();
    builtSchedule.add(requests.get(0));
    int scheduleSize = 1;
    for (int i = 1; i < requests.size(); i++) {
      if (!requests.get(i).overlaps(builtSchedule.get(scheduleSize - 1))) {
        builtSchedule.add(requests.get(i));
        scheduleSize++;
      }
    }

    return builtSchedule;
  }
}



//Request class would normally be a separate file, but HackerRank wants
//a single file for submission.
class Request implements Comparable {
  private int startMinute;
  private int endMinute;

  public Request(String inputLine) {
    String[] inputParts = inputLine.split(",");
    startMinute = toMinutes(inputParts[0]);
    endMinute = toMinutes(inputParts[1]);
  }

  private static int toMinutes(String time) {
    String[] timeParts = time.split(":");
    int hour = new Integer(timeParts[0]);
    int minute = new Integer(timeParts[1]);
    return hour*60 + minute;
  }

  public int getStartMinute() {
    return startMinute;
  }

  public int getEndMinute() {
    return endMinute;
  }


  public String toString() {
    return timeToString(startMinute) + "," + timeToString(endMinute);
  }

  private static String timeToString(int minutes) {
    if ((minutes % 60) < 10) {
      return (minutes/60) + ":0" + (minutes%60);
    }
    return (minutes/60) + ":" + (minutes%60);
  }

  public boolean overlaps(Request r) {
    // Four kinds of overlap...
    // r starts during this request:
    if (r.getStartMinute() >= getStartMinute() && 
        r.getStartMinute() < getEndMinute()) {
      return true;
    }
    // r ends during this request:
    if (r.getEndMinute() >= getStartMinute() &&
        r.getEndMinute() < getEndMinute()) {
      return true;
    }
    // r contains this request:
    if (r.getStartMinute() <= getStartMinute() &&
        r.getEndMinute() >= getEndMinute()) {
      return true;
    }
    // this request contains r:
    if (r.getStartMinute() >= getStartMinute() &&
        r.getEndMinute() <= getEndMinute()) {  
      return true;
    }
    return false;
  }

  // Allows use of Collections.sort() on this object
  // (implements Comparable interface)
  public int compareTo(Object o) {
    if (!(o instanceof Request)) {
      throw new ClassCastException();
    }
    Request r = (Request)o;
    if (r.getEndMinute() > getEndMinute()) {
      return -1;
    } else if (r.getEndMinute() < getEndMinute()) {
      return 1;
    } else if (r.getStartMinute() < getStartMinute()) {
      // Prefer later start times, so sort these first
      return -1;
    } else if (r.getStartMinute() > getStartMinute()) {
      return 1;
    } else {
      return 0;
    }
  }
}
