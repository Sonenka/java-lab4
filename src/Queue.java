import java.util.concurrent.TimeUnit;

public class Queue implements Runnable {
    private final Elevator[] elevators;

    public Queue(Elevator... elevators) {
        this.elevators = elevators;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Person person = new Person(getRandFloor(Elevator.floorsNumber), getRandFloor(Elevator.floorsNumber));
            Elevator elevator = chooseElevator(person);
            elevator.outside.add(person);
            System.out.printf("%s was called from %d to %d\n", elevator.getNumber(), person.getDeparture(), person.getArrival());
            wait(3000);
        }
    }

    private Elevator chooseElevator(Person person) {
        Elevator minDistanceElevator = null;
        int minDistance = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.getCurrFloor() - person.getDeparture());
            if (distance < minDistance) {
                minDistanceElevator = elevator;
                minDistance = distance;
            } else if (distance == minDistance) {
                if (elevator.isBusy()) {
                    minDistanceElevator = elevator;
                }
            }
        }
        return minDistanceElevator;
    }


    public static int getRandFloor(int n) {
        return (int) (Math.random() * n) + 1;
    }

    public static void wait(int n) {
        try {
            TimeUnit.MILLISECONDS.sleep(n);
        } catch (InterruptedException ignored) {
        }
    }
}
