package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.UseType;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class EnumProducer {

    @Produces
    @SessionScoped
    @Named
    public UseType[] allUseType(){
        return UseType.values();
    }
}
