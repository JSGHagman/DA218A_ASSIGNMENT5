package Uppgift5;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class OrderClient extends AbstractOrderClient implements Runnable {

    private Order order;
    private AbstractKitchenServer kitchenServer;


    public OrderClient() {
        order = new Order();
        kitchenServer = new KitchenServer();
        pollingTimer = new Timer();
    }

    @Override
    public void submitOrder() {
        order.setSent(true);
        startPollingServer(this.order.getOrderID());
        System.out.println("Submitted");

    }

    @Override
    protected void startPollingServer(String orderId) {

        TimerTask polling = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Polling");
                    Boolean status = kitchenServer.checkStatus(orderId).isDone();
                    if (status) {
                        pickUpOrder();
                        pollingTimer.cancel();
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        pollingTimer.scheduleAtFixedRate(polling, new Date(), 1000);

    }

    @Override
    protected void pickUpOrder() {
        System.out.println("Pickup Order");
        order.setDone(true);
    }

    @Override
    public void run() {
        submitOrder();
    }

    public void addItemToOrder(OrderItem item) {
        order.addOrderItem(item);
    }

    public void removeItemToOrder(OrderItem item) {
        order.removeOrderItem(item);
    }

}
