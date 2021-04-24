package ru.diplom.FirePandaDelivery.exception;

public class AddressNotInDeliveryAreaException extends Exception{

    public AddressNotInDeliveryAreaException() {
        super();
    }

    public AddressNotInDeliveryAreaException(String message) {
        super(message);
    }
}
