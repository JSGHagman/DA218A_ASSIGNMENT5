package Uppgift5;
import java.util.concurrent.CompletableFuture;

public class KitchenServer extends AbstractKitchenServer{

    @Override
    public CompletableFuture<KitchenStatus> receiveOrder(Order order) throws InterruptedException {
        return null;
    }

    @Override
    public CompletableFuture<OrderStatus> checkStatus(String orderID) throws InterruptedException {
        return null;
    }

    @Override
    public CompletableFuture<KitchenStatus> serveOrder(String orderID) throws InterruptedException {
        return null;
    }

    @Override
    protected void cook(Order order) {

    }
}
