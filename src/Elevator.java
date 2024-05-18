import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Elevator implements Runnable {
    final static int floorsNumber = 10;
    private int currFloor = 0;
    private boolean isBusy = false;
    private final String number;

    BlockingQueue<Person> inside = new LinkedBlockingQueue<>();
    BlockingQueue<Person> outside = new LinkedBlockingQueue<>();

    public Elevator(String number) {
        this.number = number;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public int getCurrFloor() {
        return currFloor;
    }

    public String getNumber() {
        return number;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (inside.peek() != null) {
                isBusy = true;
                move(inside.peek().getArrival());
            } else while (outside.peek() == null) {
                isBusy = false;
                wait(1000);
            }
            if (outside.peek() != null && inside.peek() == null) {
                isBusy = true;
                move(outside.peek().getDeparture());
            }

            for (Person p : outside) {
                if (p.getDeparture() == currFloor) {
                    outside.remove(p);
                    inside.add(p);
                    System.out.printf("%s passenger entered on %s floor\n", number, currFloor);
                }
            }

            for (Person p : inside) {
                if (p.getArrival() == currFloor) {
                    inside.remove(p);
                    System.out.printf("%s passenger got off on %s floor\n", number, currFloor);
                }
            }
            wait(2000);
        }
    }

    public void move(int floor) {
        if (floor > currFloor) {
            for (int i = currFloor; i < floor; i++) {
                currFloor += 1;
                System.out.printf("%s moved from %d to %d\n", number, currFloor - 1, currFloor);
                wait(500);
            }
        } else if (floor < currFloor) {
            for (int i = currFloor; i > floor; i--) {
                currFloor -= 1;
                System.out.printf("%s moved from %d to %d\n", number, currFloor + 1, currFloor);
                wait(500);
            }
        }
    }

    public static void wait(int n) {
        try {
            TimeUnit.MILLISECONDS.sleep(n);
        } catch (InterruptedException ignored) {
        }
    }
}
