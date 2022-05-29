package Uppgift5;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class GenericRestaurantForm implements ActionListener, Callback, WindowListener {

    private JFrame frame;            // The Main window

    JLabel labelMenu;               // Label for menu section
    JLabel labelOrder;              // Label for Order section
    JLabel labelPrice;              // Label for price info
    JLabel labelStatus;             // Label for Status section

    JPanel menuItem1;               // panel for the first menu item
    JLabel menuItem1Name;           // label with the menu item name
    JLabel menuItem1Descr;          // label with menu item description
    JLabel menuItem1Cost;           // label with the cost for the menu item
    JButton menuItem1Button;        // button to add menu item to the cart

    JPanel menuItem2;               // panel for the first menu item
    JLabel menuItem2Name;           // label with the menu item name
    JLabel menuItem2Descr;          // label with menu item description
    JLabel menuItem2Cost;           // label with the cost for the menu item
    JButton menuItem2Button;        // button to add menu item to the cart

    JPanel menuItem3;               // panel for the first menu item
    JLabel menuItem3Name;           // label with the menu item name
    JLabel menuItem3Descr;          // label with menu item description
    JLabel menuItem3Cost;           // label with the cost for the menu item
    JButton menuItem3Button;        // button to add menu item to the cart

    DefaultListModel<String> orderCartModel;     // Stores a list of string that is displayed at orderCartArea
    JList<String> orderCartArea;                 // Displays current order
    JButton orderRemoveButton;                   // Button to remove an item from the order
    JButton orderSubmitButton;                   // Button to submit the order to KitchenServer

    DefaultListModel<String> orderStatusModel;   // Stores a list of string that is displayed at orderStatusArea
    JList<String> orderStatusArea;               // To display status of the submitted order

    private ArrayList<OrderItem> orderItems;
    private OrderItem orderItem;
    private KitchenServer kitchenServer = new KitchenServer();
    private OrderClient orderClient = new OrderClient(kitchenServer);
    private String ordernbr;
    private float orderPrice = 0;
    public GenericRestaurantForm() {
        registerCallback();
        orderItems = new ArrayList<>();
    }

    public void addOrderItems(String name, String description, float price){
        OrderItem item = new OrderItem(name, description, price);
        orderItems.add(item);
    }

    /**
     * Starts the application
     */
    public void Start() {
        frame = new JFrame();
        frame.setBounds(0, 0, 900, 482);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setTitle("Brottangrottans Restaurang");
        InitializeGUI();                    // Fill in components
        frame.setVisible(true);
        frame.setResizable(true);            // Prevent user from change size
        frame.setLocationRelativeTo(null);    // Start middle screen
        frame.addWindowListener(this);
    }

    private void InitializeGUI() {
        labelMenu = new JLabel("Menu");
        labelMenu.setBounds(10, 10, 128, 13);
        frame.add(labelMenu);
        //**********************
        //*** Menu item 1 *****
        //*********************
        menuItem1 = new JPanel();
        menuItem1.setBounds(10, 35, 300, 100);
        menuItem1.setBorder(BorderFactory.createLineBorder(Color.black));
        menuItem1.setLayout(null);
        frame.add(menuItem1);

        menuItem1Name = new JLabel("Sandwich");
        menuItem1Name.setBounds(5, 5, 100, 10);
        menuItem1.add(menuItem1Name);

        menuItem1Descr = new JLabel("Bread, meat, cheese, salad, vegetables, sauce");
        menuItem1Descr.setFont(new Font("Verdana", Font.PLAIN, 11));
        menuItem1Descr.setBounds(5, 20, 300, 10);
        menuItem1.add(menuItem1Descr);

        menuItem1Cost = new JLabel("23kr");
        menuItem1Cost.setFont(new Font("Dialog", Font.BOLD, 11));
        menuItem1Cost.setBounds(5, 60, 100, 10);
        menuItem1.add(menuItem1Cost);

        menuItem1Button = new JButton();
        menuItem1Button.setBounds(180, 50, 100, 30);
        menuItem1Button.setText("add");
        menuItem1Button.addActionListener(this);
        menuItem1.add(menuItem1Button);

        //**********************
        //*** Menu item 2 *****
        //*********************
        menuItem2 = new JPanel();
        menuItem2.setBounds(10, 150, 300, 100);
        menuItem2.setBorder(BorderFactory.createLineBorder(Color.black));
        menuItem2.setLayout(null);
        frame.add(menuItem2);

        menuItem2Name = new JLabel("Borscht");
        menuItem2Name.setBounds(5, 5, 100, 10);
        menuItem2.add(menuItem2Name);

        menuItem2Descr = new JLabel("Beetroot, cabbage, potato, beef");
        menuItem2Descr.setFont(new Font("Verdana", Font.PLAIN, 11));
        menuItem2Descr.setBounds(5, 20, 300, 10);
        menuItem2.add(menuItem2Descr);

        menuItem2Cost = new JLabel("84kr");
        menuItem2Cost.setFont(new Font("Dialog", Font.BOLD, 11));
        menuItem2Cost.setBounds(5, 60, 100, 10);
        menuItem2.add(menuItem2Cost);

        menuItem2Button = new JButton();
        menuItem2Button.setBounds(180, 50, 100, 30);
        menuItem2Button.setText("add");
        menuItem2Button.addActionListener(this);
        menuItem2.add(menuItem2Button);

        //**********************
        //*** Menu item 3 *****
        //*********************
        menuItem3 = new JPanel();
        menuItem3.setBounds(10, 265, 300, 100);
        menuItem3.setBorder(BorderFactory.createLineBorder(Color.black));
        menuItem3.setLayout(null);
        frame.add(menuItem3);

        menuItem3Name = new JLabel("Coffee");
        menuItem3Name.setBounds(5, 5, 100, 10);
        menuItem3.add(menuItem3Name);

        menuItem3Descr = new JLabel("Hot, black, good");
        menuItem3Descr.setFont(new Font("Verdana", Font.PLAIN, 11));
        menuItem3Descr.setBounds(5, 20, 300, 10);
        menuItem3.add(menuItem3Descr);

        menuItem3Cost = new JLabel("18kr");
        menuItem3Cost.setFont(new Font("Dialog", Font.BOLD, 11));
        menuItem3Cost.setBounds(5, 60, 100, 10);
        menuItem3.add(menuItem3Cost);

        menuItem3Button = new JButton();
        menuItem3Button.setBounds(180, 50, 100, 30);
        menuItem3Button.setText("add");
        menuItem3Button.addActionListener(this);
        menuItem3.add(menuItem3Button);

        //*********************
        //*** Order cart  *****
        //*********************
        labelOrder = new JLabel("Order#:");
        labelOrder.setBounds(340, 10, 125, 13);
        setOrderLabel();
        frame.add(labelOrder);

        labelPrice = new JLabel("Price: ");
        labelPrice.setBounds(labelOrder.getX() + labelOrder.getWidth(), 10, 125, 13);
        frame.add(labelPrice);

        orderCartModel = new DefaultListModel<String>();
        orderCartArea = new JList<String>(orderCartModel);
        orderCartArea.setBounds(340, 35, 250, 250);
        orderCartArea.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.add(orderCartArea);

        orderRemoveButton = new JButton();
        orderRemoveButton.setBounds(340, 300, 100, 30);
        orderRemoveButton.setText("remove");
        orderRemoveButton.addActionListener(this);
        frame.add(orderRemoveButton);

        orderSubmitButton = new JButton();
        orderSubmitButton.setBounds(490, 300, 100, 30);
        orderSubmitButton.setText("order!");
        orderSubmitButton.addActionListener(this);
        frame.add(orderSubmitButton);

        //*********************
        //***** Status  *******
        //*********************
        labelStatus = new JLabel("Status");
        labelStatus.setBounds(620, 10, 128, 13);
        frame.add(labelStatus);

        orderStatusModel = new DefaultListModel<String>();
        orderStatusArea = new JList<String>(orderStatusModel);
        orderStatusArea.setBounds(620, 35, 250, 335);
        orderStatusArea.setBorder(BorderFactory.createLineBorder(Color.black));
        addListener();
        frame.add(orderStatusArea);
    }

    public void setOrderLabel(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        ordernbr = String.format("%06d", number);
        String label = String.format("Order: #%s", ordernbr);
        labelOrder.setText(label);
    }

    public void setPriceLabel(float price){
        orderPrice += price;
        String label = String.format("Price: %s kr", orderPrice);
        labelPrice.setText(label);
    }

    public void subtractFromPrice(float price){
        orderPrice -= price;
        String label = String.format("Price: %s kr", orderPrice);
        labelPrice.setText(label);
    }

    public DefaultListModel<String> getOrderStatusModel() {
        return orderStatusModel;
    }

    public void registerCallback(){
        orderClient.registerCallback(this);
        kitchenServer.registerCallback(this);
    }

    public void addOrderStatusModel(String str) {
        this.orderStatusModel.addElement(str);
    }

    public void removeOrderStatusModel(){}

    public DefaultListModel<String> getOrderCartModel() {

        return orderCartModel;
    }

    public void setOrderCartModel(DefaultListModel<String> orderCartModel) {
        this.orderCartModel = orderCartModel;
    }

    public void addOrderCartModel(String str) {
        orderCartModel.addElement(str);
    }
    public int getListIndex()
    {
        return orderCartArea.getSelectedIndex();
    }

    public void removeOrderCartModel(int index) {
        orderCartModel.removeElementAt(index);
    }

    public void emptyOrderCartModel() {
        orderCartModel.clear();
    }

    public void addListener() {
        orderCartArea.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent evt) {
            }});
    }

    public String getOrderID(){
        return ordernbr;
    }

    /**
     * Actionlisteners for the buttons. 
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == menuItem1Button) {
            float price = Float.parseFloat(menuItem1Cost.getText().substring(0,menuItem1Cost.getText().length()-2));
            addOrderItems(menuItem1Name.getText(), menuItem1Descr.getText(), price);
            addOrderCartModel(menuItem1Name.getText() + " - " + menuItem1Cost.getText());
            setPriceLabel(price);
        }

        if (e.getSource() == menuItem2Button) {
            float price = Float.parseFloat(menuItem2Cost.getText().substring(0,menuItem2Cost.getText().length()-2));
            addOrderItems(menuItem2Name.getText(), menuItem2Descr.getText(), price);
            addOrderCartModel(menuItem2Name.getText() + " - " + menuItem2Cost.getText());
            setPriceLabel(price);
        }

        if (e.getSource() == menuItem3Button) {
            float price = Float.parseFloat(menuItem3Cost.getText().substring(0,menuItem3Cost.getText().length()-2));
            addOrderItems(menuItem3Name.getText(), menuItem3Descr.getText(), price);
            addOrderCartModel(menuItem3Name.getText() + " - " + menuItem3Cost.getText());
            setPriceLabel(price);
        }

        if (e.getSource() == orderRemoveButton) {
            orderItem = null;
            try{
                orderItem = orderItems.get(orderCartArea.getSelectedIndex());
            }catch (IndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(null,"Please select an item first");
            }

            if(orderItem != null){
                float price = orderItem.getCost();
                subtractFromPrice(price);
                orderItems.remove(orderItem);
                removeOrderCartModel(getListIndex());
            }
        }

        if (e.getSource() == orderSubmitButton) {
            if(!orderItems.isEmpty()) {
                orderClient.onOrderClick(ordernbr);
                orderClient.addItemToOrder(orderItems);
                emptyOrderCartModel();
                orderClient.submitOrder(orderClient.getOrder());
                orderPrice = 0;
                setOrderLabel();
                setPriceLabel(orderPrice);
                orderItems.clear();
            }else{
                JOptionPane.showMessageDialog(null,"Please add item before ordering");
            }
        }
    }

    /**
     * Method is from the Callback interface
     * It will update the status of the orders
     * @param orderID
     * @param update
     * @return
     */
    @Override
    public Consumer<? super CompletableFuture<OrderStatus>> onUpdateEvent(String orderID, OrderStatus update) {
        String updateText = String.format("#%s %s", orderID, update.text);
        addOrderStatusModel(updateText);
        return null;
    }

    /**
     * This method is used when the window is closed, it will close all the threads in the kitchen server.
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        kitchenServer.closeThreadPool();
        System.exit(0);
    }

    /**
     * The following methods are unused
     * But had to be implemented because of the windowlistener.
     * @param e
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}