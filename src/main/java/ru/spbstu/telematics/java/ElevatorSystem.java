package ru.spbstu.telematics.java;

public class ElevatorSystem {
    public static void main(String[] args) {
        Elevator elevator = new Elevator();
        for (int i = 0; i < 12; i++) {
            new Thread(new Passenger(elevator,i)).start();
        }
    }
}
