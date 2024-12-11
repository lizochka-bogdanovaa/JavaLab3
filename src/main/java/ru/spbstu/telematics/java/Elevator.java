package ru.spbstu.telematics.java;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Elevator implements Runnable{
    static final int MAX_CAPACITY = 10;
    private int currentLoad = 0;
    final Lock lock = new ReentrantLock();
    private final Semaphore semaphore = new Semaphore(MAX_CAPACITY);
    private boolean isRunning = true;

    public void enter(int passengerId) throws InterruptedException {
        semaphore.acquire();
        lock.lock();
        try {
            currentLoad++;
            System.out.println("Пассажир "+ passengerId + " зашел в лифт. "+ "Текущая загрузка: " + currentLoad);
        } finally {
            lock.unlock();
        }
    }

    public void exit(int passengerId) {
        lock.lock();
        try {
            if (currentLoad > 0) {
                currentLoad--;
                System.out.println("Пассажир "+passengerId+" вышел из лифта. "+"Текущая загрузка: " + currentLoad);
                semaphore.release();
            }
        } finally {
            lock.unlock();
        }
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void stopElevator() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(100); // имитация работы лифта
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Лифт остановился.");
    }
}
