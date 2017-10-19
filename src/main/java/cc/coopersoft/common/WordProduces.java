package cc.coopersoft.common;

import cc.coopersoft.comm.exception.HttpApiServerException;
import cc.coopersoft.house.participant.controller.RunParam;
import cc.coopersoft.house.sale.HouseSellService;
import cc.coopersoft.house.sale.data.Word;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cooper on 18/10/2017.
 */
@SessionScoped
@Named
public class WordProduces implements java.io.Serializable{

    @Inject
    private RunParam runParam;

    private Map<String,List<Word>> cachMap = new HashMap<String, List<Word>>();


    public List<Word> getWordList(String category){
        List<Word> result = cachMap.get(category);
        if (result == null){
            try{
                result = HouseSellService.getWord(runParam.getStringParam("nginx_address"),category);
                cachMap.put(category,result);
            } catch (HttpApiServerException e) {
                throw new IllegalArgumentException("server fail!" , e);
            }
        }
        return result;
    }

}
