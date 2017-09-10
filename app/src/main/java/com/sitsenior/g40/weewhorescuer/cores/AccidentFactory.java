package com.sitsenior.g40.weewhorescuer.cores;

import com.sitsenior.g40.weewhorescuer.models.Accident;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class AccidentFactory {
    private static AccidentFactory accidentFactory;
    private List<Accident> accidentList; // Accident List for Total Accidents of Today;
    private List<Accident> rescuePendingIncident; // Accident List for Code not 'C';
    private List<Accident> awaitAccidentList; // Accident List for Code 'A';
    private List<Accident> goingForRescueAccidentList; // Accident List for Code 'G';
    private List<Accident> rescuingAccidentList; // Accident List for Code 'R';
    private List<Accident> closedAccidentList; // Accident List for Code 'C';

    private static Accident selectAccident;

    public static AccidentFactory getInstance(List<Accident> accidentList){
        if(accidentFactory == null){
            accidentFactory = new AccidentFactory();
            accidentFactory.setAccidentList(accidentList);
        } else {
            if(accidentList != null){
                accidentFactory.setAccidentList(accidentList);
            }
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

    ListIterator<Accident> accidentsItr;
    public AccidentFactory filterNonCloseIncident(){
        if(rescuePendingIncident == null){
            rescuePendingIncident = new ArrayList<Accident>();
        } else {
            rescuePendingIncident.clear();
        }
        rescuePendingIncident.addAll(this.accidentList);
        accidentsItr = rescuePendingIncident.listIterator();
        while(accidentsItr.hasNext()){
            if(accidentsItr.next().getAccCode() == Accident.ACC_CODE_C){
                accidentsItr.remove();
            }
        }
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
        accidentsItr = toFilterAccidentList.listIterator();
        if(toFilterAccidentList == null) {
            toFilterAccidentList = new ArrayList<>();
        } else { toFilterAccidentList.clear(); }
        toFilterAccidentList.addAll(this.accidentList);
        while(accidentsItr.hasNext()){
            if(accidentsItr.next().getAccCode() == accCode){
                accidentsItr.remove();
            }
        }
    }
    /* Getter List & Setter List */

    public List<Accident> getAccidentList() {
        return accidentList;
    }

    public void setAccidentList(List<Accident> accidentList) {
        /*sortByCode(accidentList, new char[]{Accident.ACC_CODE_A, Accident.ACC_CODE_G, Accident.ACC_CODE_R});*/
        if(this.accidentList != null) {accidentList.clear(); this.accidentList.addAll(accidentList);} else{ this.accidentList = accidentList;}
    }

    public List<Accident> getRescuePendingIncident() {
        return rescuePendingIncident;
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

    public static Accident getSelectAccident() {
        return AccidentFactory.selectAccident;
    }

    public static void setSelectAccident(Accident selectAccident) {
        AccidentFactory.selectAccident = selectAccident;
    }

    public Accident findByAccidentId(long accidentId){
        Accident fromNotiAccident = null;
        for(Accident accident : accidentList){
            if(accident.getAccidentId() == accidentId){
                fromNotiAccident = accident;
                break;
            }
        }
        return fromNotiAccident;
    }
}
