package com.bomberomedia.pocketengineer;

import java.util.ArrayList;

class HoseTypes {
    Double diameter;
    Double coefficient;
    Boolean active;

    HoseTypes(Double diam, Double coeff, Boolean act){
        this.diameter = diam;
        this.coefficient = coeff;
        this.active = act;
    }

    public static ArrayList<HoseTypes> getUsHoseTypes(){
        ArrayList<HoseTypes> hoseTypes = new ArrayList<>();

        hoseTypes.add(new HoseTypes(0.75, 1100.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.0, 150.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.25, 80.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.5, 24.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(1.75, 15.5, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(2.0, 8.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(2.5, 2.0, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(3.0, 0.677, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(3.5, 0.34, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(4.0, 0.2, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(4.5, 0.1, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(5.0, 0.08, Boolean.TRUE));
        hoseTypes.add(new HoseTypes(6.0, 0.05, Boolean.TRUE));

        return  hoseTypes;
    }
}