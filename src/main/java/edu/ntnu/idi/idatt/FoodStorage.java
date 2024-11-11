package edu.ntnu.idi.idatt;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Name, getName, as key, and Item object as the value
 */
public class FoodStorage {
    private HashMap<String, Item> items;

    /**
     * Constructor
     */
    public FoodStorage(){
        this.items = new HashMap<>();}

    /**
     * getter
     * @return items hastmap
     */
    public HashMap<String, Item> getItems(){return items;}

    /**
     * A method that adds a item to the storage.
     * If the item already exist (check by name) the quantity increases with the quantity of the added item.
     * If not it is getting added to the map
     * @param item
     */
    public void addItem(Item item) {
        // Check if the item already exists in the map by name, getName
        /*
        items.merge(
                item.getName(), // Key for the map entry, getName
                item,           // Value to add if the key (item) do not already exist in the map
                (existingItem, newItem) -> { // BiFunction to handle the case where the key already exists
                    // If the item with the same name (key) already exists, increase its quantity
                    existingItem.increaseQuantity(newItem.getQuantity());
                    // Return the existing item (with updated quantity) to keep it in the map
                    return existingItem;
                }
        );
         */
        items.entrySet().stream()
                .filter(itemEntry -> itemEntry.getKey().equals(item.getName()))
                .findFirst()
                .ifPresentOrElse(
                        // If found, increase the quantity with increaseQUantity method of the existing item
                        entry -> entry.getValue().increaseQuantity(item.getQuantity()),
                        // If not found, add the item to the map with put method
                        () -> items.put(item.getName(), item)
                );
    }
    /**
     * @param nameItem that represent the name attribute of the item. Can be called with getName
     * It also represents the key for the map
     */
    public void findItemByName(String nameItem) {
        if (items.containsKey(nameItem)){
            System.out.println("Item found: " + items.get(nameItem));
        } else {
            System.out.println("Item do not exist");
        }
    }

    /**
     * Updated!!
     * If the item exists in the map and if the item's quantity is greater than the amount to remove
     * Reduce the item's quantity
     * If the quantity is less than or equal to the amount to remove, remove the item from the map
     * @param name name of the item
     * @param quantity quantity of the item
     */
    public void removeItem(String name, double quantity) {
        Item item = items.get(name);
        if (item == null) {
            System.out.println(name + " does not exist in the food storage");
            return;
        }

        double newQuantity = item.getQuantity() - quantity;
        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            System.out.println("Quantity after " + quantity + " items removed: " + newQuantity);
        } else {
            items.remove(name);
            System.out.println(item.getName() + " is removed from food storage");
        }
    }

    /**
     * prints out all expired items plus how much it costs
     * @return
     */
    public void getExpiredItems(){
        //ArrayList<Item> expiredItems = new ArrayList<Item>();
        //double totalValue = 0;
        List<Item> expiredItems = items.values().stream()
                .filter(item -> item.getExpirationDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
        if (expiredItems.isEmpty()){
            System.out.println("No items is expired! Nice!");
            return;
        }

        double totalValue = expiredItems.stream()
                //.filter(item -> item.getExpirationDate().isBefore(LocalDate.now()))
                .mapToDouble(Item::getPerUnitPrice)
                .sum();
        System.out.println("Expired items");
        expiredItems.forEach(System.out::println);
        System.out.printf("Total cost of expired items: %.3f kr%n", totalValue); //3 desimaler

        /*
        for (Item item : items.values()) {
            if (item.getExpirationDate().isBefore(LocalDate.now())){
                expiredItems.add(item);
                System.out.println("Expired items: " + item.getQuantity() + " " + item.getUnit() + " " + item.getName() + ": " + item.getExpirationDate());
                System.out.println("Expired items: " + item);
                totalValue += item.getPerUnitPrice();
            }
        }
        System.out.println("Total cost for the expired items: " + totalValue + " kr");
        return expiredItems;
         */
    }

    /**
     * Using streams to find every item with a expirationdate before input date
     * @param date items date
     */
    public void getExpiredItemsBeforeDate(LocalDate date){
        System.out.println("Every item with expiration date before: " + date);
        List<Item> expiredItems = items.values().stream()
                .filter(item -> item.getExpirationDate().isBefore(date))
                .collect(Collectors.toList());
        expiredItems.forEach(System.out::println);
    }

    /**
     * Prints out total value. Checks if storage is empty
     */
    public void totalValue(){
        if(items.isEmpty()){
            System.out.println("Food storage is empty");
            return;
        }
        double totalValue = items.values().stream()
                .mapToDouble(Item::getPerUnitPrice)
                .sum();
        System.out.println("The total value of the food storage is: " + totalValue + " kr");
    }

    /**
     * Method that sorts out the items map alphabetically by name using streams sorted method
     */
    public void sortAlphabetically(){
        System.out.println("Food storage sorted out alphabetically by name:");
        items.values().stream()
                .sorted(Comparator.comparing(Item::getName))
                .forEach(System.out::println);
        /*
        items.keySet().stream()
                .sorted(Comparator.comparing(String::toString))
                .forEach(System.out::println);
         */
    }

    /**
     * returns every items in items map, food storage
     * @return
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append("Items in storage:\n");
        for (Map.Entry<String, Item> entry : items.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}

