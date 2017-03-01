package cc.coopersoft.house.participant.controller;

import cc.coopersoft.house.participant.data.model.SystemParam;
import cc.coopersoft.house.participant.data.repository.RunParamRepository;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cooper on 01/03/2017.
 */
@Named
@Startup
@ApplicationScoped
public class RunParam {

    private Map<String, SystemParam> systemParams = new HashMap<String, SystemParam>();

    @Inject
    private RunParamRepository runParamRepository;

    @PostConstruct
    public void loadParams(){
        systemParams.clear();
        for (SystemParam param : runParamRepository.findAll()) {
            systemParams.put(param.getId(), param);
        }
    }

    public String getStringParam(String name){
        SystemParam result = systemParams.get(name);
        if (result == null){
            return null;
        }
        return result.getValue();
    }


}
