package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.sale.data.MoneyManager;
import cc.coopersoft.house.sale.data.MoneyProtectedBank;
import cc.coopersoft.house.sale.data.OldHouseMoney;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class OldHouseMoneyProtectedInfo {

    @Inject
    private ContractHome contractHome;

    @Inject
    private ServerWord serverWord;

    public boolean isProtectedMoney(){
        return contractHome.getInstance().getMoneyManager() != null;
    }

    public void setProtectedMoney(boolean value){
        if (value){
            if (contractHome.getInstance().getMoneyManager() == null){
                contractHome.getInstance().setMoneyManager(new MoneyManager(contractHome.getInstance().getId()));
                contractHome.getInstance().getMoneyManager().setOldHouseMoney(new OldHouseMoney(contractHome.getInstance().getId()));
            }
        }else {
            if (contractHome.getInstance().getMoneyManager() != null){
                contractHome.getInstance().setMoneyManager(null);
            }
        }
    }

    public String getProtectedBankId(){
        if (isProtectedMoney()){
            return contractHome.getInstance().getMoneyManager().getBank();
        }
        return null;
    }

    public void setProtectedBankId(String value){
        if (value == null || "".equals(value.trim())){
            if (isProtectedMoney()){
                contractHome.getInstance().getMoneyManager().setBank(null);
                contractHome.getInstance().getMoneyManager().setAccount(null);
                contractHome.getInstance().getMoneyManager().setBankName(null);
            }
        }else{
            MoneyProtectedBank bank = serverWord.getOldHouseMoneyProtectedBankById(value);
            if (bank != null){
                contractHome.getInstance().getMoneyManager().setBank(bank.getId());
                contractHome.getInstance().getMoneyManager().setAccount(bank.getAccount());
                contractHome.getInstance().getMoneyManager().setBankName(bank.getBankName());
            }
        }

    }
}
