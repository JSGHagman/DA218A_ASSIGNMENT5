package Uppgift5;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OrderClient extends AbstractOrderClient{

    private Order order;
    private AbstractKitchenServer kitchenServer;


    public OrderClient(KitchenServer server) {
        order = new Order();
    }

    @Override
    public void submitOrder() {
        order.setSent(true);
        try {
            kitchenServer.receiveOrder(order);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startPollingServer(this.order.getOrderID());
        System.out.println("Submitted " + this.order.getOrderID());
        order = new Order();

    }

    @Override
    protected void startPollingServer(String orderId) {
        pollingTimer = new Timer();
        TimerTask polling = new TimerTask() {
            @Override
            public void run() {
                try {
                    OrderStatus result = kitchenServer.checkStatus(orderId).get();
                    if (result.equals(OrderStatus.Ready))
                      {
                            pickUpOrder();
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
    protected void pickUpOrder() {
        System.out.println("Pickup Order " +  this.order.getOrderID());
        try {
            kitchenServer.serveOrder(this.order.getOrderID());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        order.setDone(true);
    }



    public void addItemToOrder(OrderItem item) {
        order.addOrderItem(item);
    }

    public void removeItemToOrder(OrderItem item) {
        order.removeOrderItem(item);
    }

    public Order getOrder() {
        return order;
    }
}
