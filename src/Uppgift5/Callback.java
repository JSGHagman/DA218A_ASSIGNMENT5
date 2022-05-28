package Uppgift5;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Callback {
    Consumer<? super CompletableFuture<OrderStatus>> onUpdateEvent(String orderID, OrderStatus update);
}
