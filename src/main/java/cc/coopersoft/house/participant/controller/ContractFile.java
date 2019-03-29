package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.annotations.Seller;
import cc.coopersoft.house.sale.data.CommitFile;
import org.apache.deltaspike.core.api.config.view.ViewConfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Transient;
import java.util.logging.Logger;

@Named
@RequestScoped
public class ContractFile {

    @Inject
    private ContractHome contractHome;

    @Inject
    private Logger logger;

    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Seller
    @Transient
    public Class<? extends ViewConfig> completeContract(){
        contractHome.save();
        return cc.coopersoft.house.participant.pages.Seller.Contract.SubmitContract.class;
    }

    @Seller
    @Transient
    public void removeFile(){
        for (CommitFile file:contractHome.getInstance().getCommitFiles()){
            if (file.getId().equals(fileId)){
                contractHome.getInstance().getCommitFiles().remove(file);
                contractHome.saveOrUpdate();
                logger.config("delete file :" + fileId);
                return;
            }
        }
        logger.config("delete file  not found:" + fileId);
    }
}
