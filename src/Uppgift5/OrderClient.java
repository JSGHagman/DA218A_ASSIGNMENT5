package Uppgift5;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class OrderClient extends AbstractOrderClient{

    private Order order;
    private AbstractKitchenServer kitchenServer;


    public OrderClient() {
        order = new Order();
        kitchenServer = new KitchenServer();

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
        System.out.println("Submitted");

    }

    @Override
    protected void startPollingServer(String orderId) {
        pollingTimer = new Timer();
        TimerTask polling = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Polling");
                //System.out.println(order.getStatus());
                    if (order.getStatus().equals(OrderStatus.Ready)) {
                        pickUpOrder();
                        this.cancel();
                    }
            }
        };
        pollingTimer.scheduleAtFixedRate(polling, new Date(), 1000);

    }

    @Override
    protected void pickUpOrder() {
        System.out.println("Pickup Order");
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

}
