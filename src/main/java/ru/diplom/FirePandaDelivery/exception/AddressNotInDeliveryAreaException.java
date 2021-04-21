package ru.diplom.FirePandaDelivery.exception;

public class AddressNotInDeliveryAreaException extends RuntimeException{

    public AddressNotInDeliveryAreaException() {
        super();
    }

    public AddressNotInDeliveryAreaException(String message) {
        super(message);
    }
}
