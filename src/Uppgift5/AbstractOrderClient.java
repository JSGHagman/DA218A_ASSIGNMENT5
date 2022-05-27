package Uppgift5;

import java.util.Timer;

public abstract class AbstractOrderClient {

    Timer pollingTimer;


    /**
     * Start an asynchronous request to {@link AbstractKitchenServer#receiveOrder(Order)}
     * Also start {@link #startPollingServer(String)}
     */
    abstract public void submitOrder();

    /**
     * Start a new task with a periodic timer {@link #pollingTimer}
     * to ask a server periodically about the order status {@link AbstractKitchenServer#checkStatus(String)}.
     *
     * Call {@link #pickUpOrder()} when status is {@link OrderStatus#Ready} and stop the {@link #pollingTimer}.
     */
    abstract protected void startPollingServer(String orderId);

    /**
     * Start an asynchronous request to {@link AbstractKitchenServer#serveOrder(String)}
     */
    abstract protected void pickUpOrder();
}
