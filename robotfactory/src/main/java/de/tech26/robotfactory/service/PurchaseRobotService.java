package de.tech26.robotfactory.service;

import de.tech26.robotfactory.model.OrderRequest;
import de.tech26.robotfactory.model.OrderResponse;
import de.tech26.robotfactory.model.RobotPart;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PurchaseRobotService {

    public OrderResponse placeOrder(List<RobotPart> robotPartList) {
        double total = 0.0;
        for(RobotPart robotPart:robotPartList){
            robotPart.setAvailable(robotPart.getAvailable()-1);
            total +=robotPart.getPrice();
        }
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(UUID.randomUUID().toString());
        orderResponse.setTotal(total);
        return orderResponse;
    }
}
