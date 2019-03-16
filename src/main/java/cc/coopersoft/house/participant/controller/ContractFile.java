package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.annotations.Seller;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;

@Named
@RequestScoped
public class ContractFile {

    @Inject
    private ContractHome contractHome;

    @Seller
    @Transient
    public Class<? extends ViewConfig> completeContract(){
        contractHome.save();
        return cc.coopersoft.house.participant.pages.Seller.Contract.SubmitContract.class;
    }
}
