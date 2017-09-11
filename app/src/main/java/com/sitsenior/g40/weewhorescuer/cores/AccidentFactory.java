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

    public AccidentFactory update(){
        this.filterNonCloseIncident();
        this.filterForAwaitAccident();
        this.filterForGoingForRescueAccident();
        this.filterRescuingRescueAccident();
        this.filterClosedRescueAccident();
        return accidentFactory;
    }

    ListIterator<Accident> accidentsResuePendingItr;
    public AccidentFactory filterNonCloseIncident(){
        if(rescuePendingIncident == null){
            rescuePendingIncident = new ArrayList<Accident>();
        }
        rescuePendingIncident.clear();
        if(this.accidentList != null){
            rescuePendingIncident.addAll(this.accidentList);
        }
        accidentsResuePendingItr = rescuePendingIncident.listIterator();
        while(accidentsResuePendingItr.hasNext()){
            if(accidentsResuePendingItr.next().getAccCode() == Accident.ACC_CODE_C){
                accidentsResuePendingItr.remove();
            }
        }
        return accidentFactory;
    }

    ListIterator<Accident> accidentsAwaitingItr;
    public AccidentFactory filterForAwaitAccident(){
        if(awaitAccidentList == null){
            awaitAccidentList = new ArrayList<Accident>();
        } else {
            awaitAccidentList.clear();
        }
        if(this.accidentList != null){
            awaitAccidentList.addAll(this.accidentList);
        }
        accidentsAwaitingItr = awaitAccidentList.listIterator();
        while(accidentsAwaitingItr.hasNext()){
            if(accidentsAwaitingItr.next().getAccCode() != Accident.ACC_CODE_A){
                accidentsAwaitingItr.remove();
            }
        }
        return accidentFactory;
    }

    ListIterator<Accident> accidentsGoingItr;
    public AccidentFactory filterForGoingForRescueAccident(){
        if(goingForRescueAccidentList == null){
            goingForRescueAccidentList = new ArrayList<Accident>();
        } else {
            goingForRescueAccidentList.clear();
        }
        if(this.accidentList != null){
            goingForRescueAccidentList.addAll(this.accidentList);
        }
        accidentsGoingItr = goingForRescueAccidentList.listIterator();
        while(accidentsGoingItr.hasNext()){
            if(accidentsGoingItr.next().getAccCode() != Accident.ACC_CODE_G){
                accidentsGoingItr.remove();
            }
        }
        return accidentFactory;
    }

    ListIterator<Accident> accidentsRescuingItr;
    public AccidentFactory filterRescuingRescueAccident(){
        if(rescuingAccidentList == null){
            rescuingAccidentList = new ArrayList<Accident>();
        } else {
            rescuingAccidentList.clear();
        }
        if(this.accidentList != null){
            rescuingAccidentList.addAll(this.accidentList);
        }
        accidentsRescuingItr = rescuingAccidentList.listIterator();
        while(accidentsRescuingItr.hasNext()){
            if(accidentsRescuingItr.next().getAccCode() != Accident.ACC_CODE_R){
                accidentsRescuingItr.remove();
            }
        }
        return accidentFactory;
    }

    ListIterator<Accident> accidentsClosedItr;
    public AccidentFactory filterClosedRescueAccident(){
        if(closedAccidentList == null){
            closedAccidentList = new ArrayList<Accident>();
        } else {
            closedAccidentList.clear();
        }
        if(this.accidentList != null){
            closedAccidentList.addAll(this.accidentList);
        }
        accidentsClosedItr = closedAccidentList.listIterator();
        while(accidentsClosedItr.hasNext()){
            if(accidentsClosedItr.next().getAccCode() != Accident.ACC_CODE_C){
                accidentsClosedItr.remove();
            }
        }
        return accidentFactory;
    }

    /* Getter List & Setter List */

    public List<Accident> getAccidentList() {
        return accidentList;
    }

    public void setAccidentList(List<Accident> accidentList) {
        /*sortByCode(accidentList, new char[]{Accident.ACC_CODE_A, Accident.ACC_CODE_G, Accident.ACC_CODE_R});*/
        if(this.accidentList != null) {this.accidentList.clear(); this.accidentList.addAll(accidentList);} else{ this.accidentList = accidentList;}
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

    @Override
    public String toString() {
        return "AccidentFactory{" +
                "\n\n, accidentList=" + accidentList +
                "\n\n,> rescuePendingIncident=" + rescuePendingIncident +
                "\n\n, awaitAccidentList=" + awaitAccidentList +
                "\n\n, goingForRescueAccidentList=" + goingForRescueAccidentList +
                "\n\n, rescuingAccidentList=" + rescuingAccidentList +
                "\n\n, closedAccidentList=" + closedAccidentList +
                '}';
    }
}
