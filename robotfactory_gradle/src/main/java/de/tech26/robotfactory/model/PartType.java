package de.tech26.robotfactory.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PartType {
    Face,
    Material,
    Arms,
    Mobility;
    public static List<PartType> getPartTypes(){
        return new ArrayList<>(Arrays.asList(PartType.values()));
    }
}
