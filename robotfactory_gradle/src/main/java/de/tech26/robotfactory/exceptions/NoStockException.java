package de.tech26.robotfactory.exceptions;

public class NoStockException extends RuntimeException{
    public NoStockException(String message){
        super(message);
    }
}
