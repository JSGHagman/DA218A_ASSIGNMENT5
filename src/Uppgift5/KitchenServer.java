package Uppgift5;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KitchenServer extends AbstractKitchenServer {
    private static final int inMillis = 1000;
    private static final Random rand = new Random();

    public KitchenServer() {
        threadPool = Executors.newFixedThreadPool(10);
        orderMap = new HashMap<>();
    }

    @Override
    public CompletableFuture<OrderStatus> receiveOrder(Order order) throws InterruptedException {
        order.setStatus(OrderStatus.Received);
        orderMap.put(order.getOrderID(), order);
        Runnable cook = () -> {
            cook(order);
        };
        threadPool.submit(cook);
        return CompletableFuture.supplyAsync(() -> OrderStatus.Received);
    }

    @Override
    public CompletableFuture<OrderStatus> checkStatus(String orderID) throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            Order order = orderMap.get(orderID);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return order.getStatus();
        });
    }

    @Override
    public CompletableFuture<OrderStatus> serveOrder(String orderID) throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> OrderStatus.Served);
    }

    @Override
    protected void cook(Order order) {
        int seconds = rand.nextInt(5,10);
        long sleep = seconds * inMillis;
        order.setStatus(OrderStatus.BeingPrepared);
        try{
            Thread.sleep(sleep);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
      order.setStatus(OrderStatus.Ready);
    }
}
