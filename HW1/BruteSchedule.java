import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

// Solving the interval scheduling problem with brute force.
// Exponential time, and thus not a recommended approach.
public class BruteSchedule {
    public static void main(String[] args) {
        ArrayList<Request> myRequests = new ArrayList<Request>();
        Scanner myScanner = new Scanner(System.in);
        while (myScanner.hasNextLine()) {
            myRequests.add(new Request(myScanner.nextLine()));
        }
        
        // Try all schedules and remember best one with no conflicts
        ArrayList<ArrayList<Request>> allSchedules = powerSet(myRequests);

        ArrayList<Request> bestSchedule = new ArrayList<Request>();
        int bestScheduleCount = 0;
        for (ArrayList<Request> schedule : allSchedules) {
            if (schedule.size() <= bestScheduleCount) {
                continue;  // this can't beat the current champ
            }
            // Check for conflicts
            boolean conflict = false;
            for (int i = 1; i < schedule.size(); i++) {
                for (int j = 0; j < i; j++) {
                    if (schedule.get(i).overlaps(schedule.get(j))) {
                        conflict = true;
                        break;
                    }
                }
                if (conflict) {
                    break;
                }
            }
            if (!conflict) {
                bestSchedule = schedule;
                bestScheduleCount = schedule.size();
            }
        }

        // Still want to sort for presentation.
        Collections.sort(bestSchedule);

        for (Request r : bestSchedule) {
            System.out.println(r);
        }
    }

    // Recursive generation of all subsets:  power set of remaining elements
    // with or without the first element
    private static ArrayList<ArrayList<Request>> powerSet(ArrayList<Request> myRequests) {
        if (myRequests.isEmpty()) {
            ArrayList<ArrayList<Request>> setOfEmptySet = new ArrayList<ArrayList<Request>>();
            ArrayList<Request> emptySchedule = new ArrayList<Request>();
            setOfEmptySet.add(emptySchedule);
            return setOfEmptySet;
        }

        Request firstElement = myRequests.get(0);
        ArrayList<Request> rest = (ArrayList<Request>) myRequests.clone();
        rest.remove(0);
        ArrayList<ArrayList<Request>> psOfRest = powerSet(rest);
        ArrayList<ArrayList<Request>> psOfRestPlusOne = new ArrayList<ArrayList<Request>>();
        for (ArrayList<Request> schedule : psOfRest) {
            ArrayList<Request> clone = (ArrayList<Request>) schedule.clone();
            clone.add(firstElement);
            psOfRestPlusOne.add(clone);
        }
        ArrayList<ArrayList<Request>> allSchedules = new ArrayList<ArrayList<Request>>();
        allSchedules.addAll(psOfRest);
        allSchedules.addAll(psOfRestPlusOne);
        return allSchedules;
    }

    // Request class would normally be a separate file, but HackerRank wants
    // a single file for submission.
	private static class Request implements Comparable {
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

}
