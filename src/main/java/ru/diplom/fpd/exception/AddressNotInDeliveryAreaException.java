package ru.diplom.fpd.exception;

public class AddressNotInDeliveryAreaException extends Exception{

    public AddressNotInDeliveryAreaException() {
        super("the address is not in the delivery area");
    }

    public AddressNotInDeliveryAreaException(String message) {
        super(message);
    }
}
