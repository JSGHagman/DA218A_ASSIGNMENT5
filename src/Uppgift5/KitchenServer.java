/**
 * This method is a fake server, it represents the kitchen/restaurant.
 * An actual client/server has not been implemented
 * Therefore there are timers and sometime sleepmethods for threads to simulate a delay
 * @author Jakob Hagman
 */
package Uppgift5;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KitchenServer extends AbstractKitchenServer {
    private static final int inMillis = 1000; // Used to convert a random integer to a number in milliseconds, which is used for threads to sleep
    private static final Random rand = new Random(); // Will later randomize a number for the thread to sleep
    private CompletableFuture<OrderStatus> completableFuture = new CompletableFuture<>(); //Used to return a value asynchronously
    private Callback callback; //Interface used to update gui

    /**
     * This is the constructor for this class
     * It starts a threadpool with a fixed number of threads, in this case 10.
     * It also creates a hashmap which will hold all the orders placed during the session
     */
    public KitchenServer() {
        threadPool = Executors.newFixedThreadPool(10);
        orderMap = new HashMap<>();
    }

    /**
     * This method registers an interface called callback, it's method is defined in the gui-class
     * @param callback
     */
    public void registerCallback(Callback callback){
        this.callback = callback;
    }

    /**
     * This method is used to recieve an order
     * It will update the status of the order to recieved
     * It will then update the gui as well.
     * @param order
     * @return OrderStatus as a CompletableFuture
     */
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

    /**
     * This method is used to check the status of the order
     * @param orderID - the order which status needs to be checked
     * @return OrderStatus as a CompletableFuture
     */
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

    /**
     * This method is called when an order is ready.
     * It will set the status of the order to Served
     * @param order
     * @return - OrderStatus as a CompletableFuture
     */
    @Override
    public CompletableFuture<OrderStatus> serveOrder(Order order) {
        order.setStatus(OrderStatus.Served);
        return completableFuture.supplyAsync(() -> order.getStatus());
    }


    /**
     * This method is called when an order is received
     * It will generate a random number between five and ten
     * The thread will be asleep for that random time.
     * @param order
     */
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


    /**
     * This method is used to close the threadpool
     * It is called from the gui, when the exit button is pressed.
     */
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
