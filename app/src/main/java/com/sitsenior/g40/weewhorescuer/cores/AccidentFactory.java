package com.sitsenior.g40.weewhorescuer.cores;

import com.sitsenior.g40.weewhorescuer.models.Accident;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class AccidentFactory {
    private static AccidentFactory accidentFactory;
    private List<Accident> accidentList; // Accident List for Total Accidents of Today;
    private List<Accident> awaitAccidentList; // Accident List for Code 'A';
    private List<Accident> goingForRescueAccidentList; // Accident List for Code 'G';
    private List<Accident> rescuingAccidentList; // Accident List for Code 'R';
    private List<Accident> closedAccidentList; // Accident List for Code 'C';

    private Accident selectAccident;

    public static AccidentFactory getInstance(List<Accident> accidentList){
        if(accidentFactory == null){
            accidentFactory = new AccidentFactory();
            accidentFactory.setAccidentList(accidentList);
        }
        return accidentFactory;
    }

    public AccidentFactory addAccident(Accident accident){
        this.accidentList.add(accident);
        this.filterForAwaitAccident();
        this.filterForGoingForRescueAccident();
        this.filterRescuingRescueAccident();
        this.filterClosedRescueAccident();
        return accidentFactory;
    }

    public AccidentFactory filterForAwaitAccident(){
        setFilteredAccidentList(this.awaitAccidentList, Accident.ACC_CODE_A);
        return accidentFactory;
    }

    public AccidentFactory filterForGoingForRescueAccident(){
        setFilteredAccidentList(this.goingForRescueAccidentList, Accident.ACC_CODE_G);
        return accidentFactory;
    }

    public AccidentFactory filterRescuingRescueAccident(){
        setFilteredAccidentList(this.rescuingAccidentList, Accident.ACC_CODE_R);
        return accidentFactory;
    }

    public AccidentFactory filterClosedRescueAccident(){
        setFilteredAccidentList(this.closedAccidentList, Accident.ACC_CODE_C);
        return accidentFactory;
    }

    public void setFilteredAccidentList(List<Accident> toFilterAccidentList, char accCode){
        toFilterAccidentList = new ArrayList<>();
        for(Accident accident : accidentList){
            if(accident.getAccCode() == accCode){
                toFilterAccidentList.add(accident);
            }
        }
    }

    private static void sortByCode(List<Accident> accidentList, char[] codes){
        Collections.sort(accidentList, Collections.reverseOrder(new Comparator<Accident>() {
            @Override
            public int compare(Accident accident, Accident t1) {
                return accident.getTime().compareTo(t1.getTime());
            }
        }));
        List<Accident> tempAccidentList = null;
        for(char code : codes){
            if(tempAccidentList == null){tempAccidentList = new ArrayList<>();}
            for(Accident accident : accidentList){
                if(accident.getAccCode() == code){
                    tempAccidentList.add(accident);
                }
            }
        }
        accidentList.clear();
        accidentList.addAll(tempAccidentList);
    }

    /* Getter List & Setter List */

    public List<Accident> getAccidentList() {
        return accidentList;
    }

    public void setAccidentList(List<Accident> accidentList) {
        sortByCode(accidentList, new char[]{Accident.ACC_CODE_A, Accident.ACC_CODE_G, Accident.ACC_CODE_R});
        this.accidentList = accidentList;
    }

    public List<Accident> getAwaitAccidentList() {
        return awaitAccidentList;
    }

    public List<Accident> getGoingForRescueAccidentList() {
        return goingForRescueAccidentList;
    }

    public List<Accident> getRescuingAccidentList() {
        return rescuingAccidentList;
    }

    public List<Accident> getClosedAccidentList() {
        return closedAccidentList;
    }

    public Accident getSelectAccident() {
        return selectAccident;
    }

    public void setSelectAccident(Accident selectAccident) {
        this.selectAccident = selectAccident;
    }
}
