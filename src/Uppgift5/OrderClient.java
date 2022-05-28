package Uppgift5;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OrderClient extends AbstractOrderClient {
    private Order order;
    private KitchenServer kitchenServer;
    private CompletableFuture<Void> update = new CompletableFuture<>();
    private Callback callback;

    public OrderClient(KitchenServer server) {
        //order = new Order();
        kitchenServer = server;
    }

    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void submitOrder(Order order) {
        order.setSent(true);
        update.supplyAsync(
                () -> {
                    return kitchenServer.receiveOrder(order);
                }).thenAccept(OrderStatus -> callback.onUpdateEvent(order.getOrderID(), Uppgift5.OrderStatus.Submitted));
        startPollingServer(order);
    }

    @Override
    public void startPollingServer(Order order) {
        pollingTimer = new Timer();
        update.supplyAsync(
                () -> {
                    return kitchenServer.checkStatus(order.getOrderID());
                }).thenAccept(OrderStatus -> callback.onUpdateEvent(order.getOrderID(), Uppgift5.OrderStatus.Received));
        TimerTask polling = new TimerTask() {
            @Override
            public void run() {
                try {
                    OrderStatus result = kitchenServer.checkStatus(order.getOrderID()).get();
                    String status = String.format("Status for ID: %s | %s", order.getOrderID(), result.text);
                    if (result.equals(OrderStatus.Ready)) {
                        update.supplyAsync(
                                () -> {
                                    return kitchenServer.checkStatus(order.getOrderID());
                                }).thenAccept(OrderStatus -> callback.onUpdateEvent(order.getOrderID(), order.getStatus()));
                        pickUpOrder(order);
                        this.cancel();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        pollingTimer.scheduleAtFixedRate(polling, new Date(), 1000);
    }

    @Override
    public void pickUpOrder(Order order) {
        try {
            Thread.sleep(1500);
            update.supplyAsync(
                    () -> {
                        return kitchenServer.serveOrder(order);
                    }).thenAccept(OrderStatus -> callback.onUpdateEvent(order.getOrderID(), order.getStatus()));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        order.setDone(true);
    }

    public void addItemToOrder(ArrayList<OrderItem> items) {
        for (OrderItem orderItem : items) {
            order.addOrderItem(orderItem);
        }
    }

    public void removeItemToOrder(OrderItem item) {
        order.removeOrderItem(item);
    }

    public Order getOrder() {
        return order;
    }

    public void onOrderClick(String orderNbr) {
        order = new Order(orderNbr);
    }

}
