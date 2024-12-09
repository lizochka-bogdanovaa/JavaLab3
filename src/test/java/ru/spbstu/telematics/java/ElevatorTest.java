package ru.spbstu.telematics.java;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;


public class ElevatorTest {

    @Test
    void testElevatorConcurrency() throws InterruptedException {
        Elevator elevator = new Elevator();
        int numPassengers = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numPassengers);
        CountDownLatch latch = new CountDownLatch(numPassengers);

        for (int i = 0; i < numPassengers; i++) {
            int finalI = i;
            executor.submit(() -> {
                try {
                    elevator.enter(finalI);
                    Thread.sleep((long) (Math.random() * 1000));
                    elevator.exit(finalI);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "");
        assertEquals(0, elevator.getCurrentLoad(), "Загруженность лифта не 0, после того как все пассажиры вышли");

    }

    private int getCurrentLoad(Elevator elevator){
        elevator.lock.lock();
        try{
            return elevator.getCurrentLoad();
        }finally {
            elevator.lock.unlock();
        }
    }


    @Test
    void testElevatorMaxCapacity() throws InterruptedException {
        Elevator elevator = new Elevator();
        ExecutorService executor = Executors.newFixedThreadPool(25);
        CountDownLatch latch = new CountDownLatch(25);
        for (int i = 0; i < 25; i++) {
            int finalI = i; // Не забудьте об этом!
            executor.submit(() -> {
                try {
                    elevator.enter(finalI);
                    latch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        // Проверяем, что в лифте не более MAX_CAPACITY пассажиров
        assertTrue(getCurrentLoad(elevator) <= Elevator.MAX_CAPACITY,
                "Больше, чем 10 пассажиров зашли в лифт: " + getCurrentLoad(elevator));
    }

}