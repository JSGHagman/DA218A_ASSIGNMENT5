/**
 * Class is used as fake client, representing a customer in a restaurant
 * @author Jakob Hagman
 */

package Uppgift5;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OrderClient extends AbstractOrderClient {
    private Order order; // The order
    private KitchenServer kitchenServer; // The kitchenserver
    private CompletableFuture<Void> update = new CompletableFuture<>(); // Used to make asynchrounous calls
    private Callback callback; // Used to update the gui

    /**
     * Constructor for this class.
     * Will only register the kitchen server and create this object
     * @param server
     */
    public OrderClient(KitchenServer server) {
        //order = new Order();
        kitchenServer = server;
    }


    /**
     * This method registers a callback interface.
     * The interface contains a method which is defined in the gui class
     * @param callback
     */
    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * This method will submit an order to the kitchen server to be prepared
     * @param order
     */
    @Override
    public void submitOrder(Order order) {
        order.setSent(true);
        update.supplyAsync(
                () -> {
                    return kitchenServer.receiveOrder(order);
                }).thenAccept(OrderStatus -> callback.onUpdateEvent(order.getOrderID(), Uppgift5.OrderStatus.Submitted));
        startPollingServer(order);
    }


    /**
     * This method is called after an order has been submitted
     * It will first call check status in the kitchenserver and then update the gui
     * It will then start a timertask which will check status with an interval of one second
     * @param order
     */
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


    /**
     * This method is used to pickup an order.
     * It will call a method in the kitchenserver asynchronously
     * When the method is finished the callback is called which updates the gui.
     * @param order
     */
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


    /**
     * Will add items to an order
     * @param items - an arrayList of items from the gui
     */
    public void addItemToOrder(ArrayList<OrderItem> items) {
        for (OrderItem orderItem : items) {
            order.addOrderItem(orderItem);
        }
    }


    /**
     * Will delete item from an order
     * @param item
     */
    public void removeItemToOrder(OrderItem item) {
        order.removeOrderItem(item);
    }


    /**
     * returns current order
     * @return order
     */
    public Order getOrder() {
        return order;
    }


    /**
     * This method is called when a user presses the orderbtn in the gui
     * It will create an order
     * @param orderNbr - a random 6 digit number that has been genrerated, this will be the order id
     */
    public void onOrderClick(String orderNbr) {
        order = new Order(orderNbr);
    }

}
