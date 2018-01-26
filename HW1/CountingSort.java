import java.util.ArrayList;
import java.util.Scanner;

class CountingSort {
  public static void main(String[] args) {
    ArrayList<Request> myRequests = new ArrayList<Request>();
    ArrayList<Request> schedule = new ArrayList<Request>();
    Scanner myScanner = new Scanner(System.in);
    while (myScanner.hasNextLine()) {
      myRequests.add(new Request(myScanner.nextLine()));
    }

    Request[] sortedSchedule = countingSort(myRequests, 1440);
      schedule = scheduler(sortedSchedule);
    
    for (Request r : schedule) {
      System.out.println(r.toString());
    }
    
  }
  
  // to sort through the passed array using counting sort algorithm
  static Request[] countingSort(ArrayList<Request> sort, int max) {
    Request counts[] = new Request[max];
    for (Request r : sort) {
        try { // finding the best end minute to add
      if (counts[r.getEndMinute()].getStartMinute() < r.getStartMinute()) {
        counts[r.getEndMinute()] = r;
        }
      }
        catch (NullPointerException n) { // checking for possible null values
            counts[r.getEndMinute()] = r;
        }
    }
    Request results[] = new Request[sort.size()];
    int curIdx = 0;
    for (Request r : counts) { // adding all non-null Requests
        if (r != null) {
            results[curIdx] = r;
            curIdx++;
  
     }
    }
    return results;
  }
  
  // to schedule the requests  
  static ArrayList<Request> scheduler(Request[] requests) {
    ArrayList<Request> schedule = new ArrayList<Request>();
    schedule.add(requests[0]); 
    int scheduleSize = 1;
    for (int i = 1; i < requests.length; i++) {
      if (requests[i] == null) {continue; }  // checking for possible nulls that were added during countingSort
      else if (!(requests[i].getStartMinute() < (schedule.get(scheduleSize - 1)).getEndMinute())) { //overlaps?
        schedule.add(requests[i]);
        scheduleSize++;  
      }  
    }
    
    return schedule;
    
    
  }


  }

  //Request class would normally be a separate file, but HackerRank wants
  //a single file for submission.
  class Request implements Comparable {
    private int startMinute;
    private int endMinute;


    public Request(int startMinute, int endMinute) {
        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }  
      
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