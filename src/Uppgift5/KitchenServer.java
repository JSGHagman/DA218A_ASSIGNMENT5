package Uppgift5;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KitchenServer extends AbstractKitchenServer {
    private static final int inMillis = 1000;
    private static final Random rand = new Random();
    private CompletableFuture<OrderStatus> completableFuture = new CompletableFuture<>();
    private Callback callback;

    public KitchenServer() {
        threadPool = Executors.newFixedThreadPool(10);
        orderMap = new HashMap<>();
    }

    public void registerCallback(Callback callback){
        this.callback = callback;
    }

    @Override
    public CompletableFuture<OrderStatus> receiveOrder(Order order){
        order.setStatus(OrderStatus.Received);
        orderMap.put(order.getOrderID(), order);
        CompletableFuture.supplyAsync(
                () -> {
                    return checkStatus(order.getOrderID());
                }).thenAccept(OrderStatus -> callback.onUpdateEvent(order.getOrderID(), Uppgift5.OrderStatus.Paid));
        Runnable cook = () -> {
            cook(order);
        };
        threadPool.submit(cook);
        return completableFuture.supplyAsync(() -> order.getStatus());
    }

    @Override
    public CompletableFuture<OrderStatus> checkStatus(String orderID) {
        try{
            Thread.sleep(1000);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return completableFuture.supplyAsync(() -> {
            Order order = orderMap.get(orderID);
            return order.getStatus();
        });
    }

    @Override
    public CompletableFuture<OrderStatus> serveOrder(Order order) {
        order.setStatus(OrderStatus.Served);
        return completableFuture.supplyAsync(() -> order.getStatus());
    }

    @Override
    protected void cook(Order order) {
        int seconds = rand.nextInt(5,10);
        long sleep = seconds * inMillis;
        order.setStatus(OrderStatus.BeingPrepared);
        try{
            Thread.sleep(1000);
            completableFuture.supplyAsync(
                    () -> {
                        return checkStatus(order.getOrderID());
                    }).thenAccept(OrderStatus -> callback.onUpdateEvent(order.getOrderID(), order.getStatus()));
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }

        try{
            Thread.sleep(sleep);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
      order.setStatus(OrderStatus.Ready);
    }

    public void closeThreadPool() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("All threads have been shutdown");
    }
}
