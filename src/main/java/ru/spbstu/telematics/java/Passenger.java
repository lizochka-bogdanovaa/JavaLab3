package ru.spbstu.telematics.java;

class Passenger implements Runnable {
    private final int passengerId;
    private final Elevator elevator;

    public Passenger(Elevator elevator, int passengerId) {
        this.elevator = elevator;
        this.passengerId = passengerId;
    }

    @Override
    public void run() {
        try {
            elevator.enter(passengerId);
            Thread.sleep((long) (Math.random() * 1000)); // Имитация времени, проведенного в лифте
            elevator.exit(passengerId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}