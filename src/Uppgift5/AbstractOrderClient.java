package Uppgift5;

import java.util.Timer;

public abstract class AbstractOrderClient {

    Timer pollingTimer;


    /**
     * Start an asynchronous request to {@link AbstractKitchenServer#receiveOrder(Order)}
     * Also start {@link #startPollingServer(Order)}
     */
    abstract public void submitOrder(Order order);

    /**
     * Start a new task with a periodic timer {@link #pollingTimer}
     * to ask a server periodically about the order status {@link AbstractKitchenServer#checkStatus(String)}.
     *
     * Call {@link #pickUpOrder(Order order)} when status is {@link OrderStatus#Ready} and stop the {@link #pollingTimer}.
     */
    abstract protected void startPollingServer(Order order);

    /**
     * Start an asynchronous request to {@link AbstractKitchenServer#serveOrder(Order)}
     */
    abstract protected void pickUpOrder(Order order);
}
