package Uppgift5;

public enum OrderStatus {
    NotSent("Not sent to the server"),
    Received("Received"),
    BeingPrepared("Preparing"),
    Ready("Ready"),
    Served("Served"),
    Submitted("Submitted"),
    Paid("Paid"),
    NotFound("not found");

    public final String text;

    private OrderStatus(String name){
        this.text = name;
    }
}
