package de.tech26.robotfactory.factory;

import de.tech26.robotfactory.exceptions.BadRequestException;
import de.tech26.robotfactory.exceptions.InvalidRequestException;
import de.tech26.robotfactory.model.RobotPart;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Configuration
@ConfigurationProperties
public class RobotFactory {
    private static Map<String, RobotPart> roboFactoryMap = new HashMap<>();

    public void setRobotStocks(List<RobotPart> robotStocks) {
        if(!CollectionUtils.isEmpty(robotStocks)){
            for (RobotPart robotPart:robotStocks){
                roboFactoryMap.put(robotPart.getCode(),robotPart);
            }
        }
    }

    public static List<RobotPart> getRoboPartsByCodes(List<String> codes){
        List<RobotPart> roboPartList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(codes)){
            for(String code: codes){
                RobotPart roboPart = getRoboPartByCode(code);
                if(roboPart!=null){
                    roboPartList.add(roboPart);
                }else{
                    throw new BadRequestException("part: "+code + " does not exists");
                }
            }
        }
        return roboPartList;
    }

    public static RobotPart getRoboPartByCode(String code){
        return roboFactoryMap.get(code);
    }
    private static void loadRobotFactory() {

    }
}
