package de.tech26.robotfactory.controller;

import de.tech26.robotfactory.exceptions.BadRequestException;
import de.tech26.robotfactory.exceptions.InvalidRequestException;
import de.tech26.robotfactory.exceptions.NoStockException;
import de.tech26.robotfactory.factory.RobotFactory;
import de.tech26.robotfactory.model.OrderRequest;
import de.tech26.robotfactory.model.OrderResponse;
import de.tech26.robotfactory.model.PartType;
import de.tech26.robotfactory.model.RobotPart;
import de.tech26.robotfactory.service.PurchaseRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/")
public class RobotRestAPIController {

    @Autowired
    private PurchaseRobotService purchaseRobotService;

    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse purchaseRobotParts(@RequestBody OrderRequest orderRequest) {
        List<RobotPart> robotPartList = RobotFactory.getRoboPartsByCodes(orderRequest.getComponents());
        validateOrderRequest(robotPartList);
        return purchaseRobotService.placeOrder(robotPartList);
    }

    private void validateOrderRequest(List<RobotPart> robotPartList) {
        validateAllRequiredPartExists(robotPartList);
        validateStockAvailability(robotPartList);
    }

    private void validateStockAvailability(List<RobotPart> robotPartList) {
        for(RobotPart robotPart:robotPartList){
            if(robotPart.getAvailable() == 0){
                throw new NoStockException("parts not available "+robotPart.getCode());
            }
        }
    }

    private void validateAllRequiredPartExists(List<RobotPart> robotPartList) {
        if(CollectionUtils.isEmpty(robotPartList) || robotPartList.size() != 4 ){
            throw new InvalidRequestException("Invalid purchase request: All parts not included");
        }else {
            List<PartType> partTypes = PartType.getPartTypes();
            for(RobotPart robotPart:robotPartList){
                partTypes.remove(robotPart.getPartType());
            }
            if(!CollectionUtils.isEmpty(partTypes)){
                throw new InvalidRequestException("invalid purchase request: parts "+ partTypes +" are not included");
            }
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public String badRequest(BadRequestException ex) {
        return ex.getMessage();

    }
    @ResponseStatus(HttpStatus.IM_USED)
    @ExceptionHandler(NoStockException.class)
    public String noStock(NoStockException ex) {
        return ex.getMessage();

    }
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidRequestException.class)
    public String invalidRequest(InvalidRequestException ex) {
        return ex.getMessage();

    }
}
