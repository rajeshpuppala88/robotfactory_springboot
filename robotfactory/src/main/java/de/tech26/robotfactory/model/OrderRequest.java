package de.tech26.robotfactory.model;


import java.util.List;

public class OrderRequest {


    private List<String> components;

    public OrderRequest(){

    }
    public OrderRequest(List<String> components){
        this.components = components;
    }
    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }
}
