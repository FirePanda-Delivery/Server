package ru.diplom.fpd.exception;

public class AddressNotInDeliveryAreaException extends Exception{

    public AddressNotInDeliveryAreaException() {
        super();
    }

    public AddressNotInDeliveryAreaException(String message) {
        super(message);
    }
}
