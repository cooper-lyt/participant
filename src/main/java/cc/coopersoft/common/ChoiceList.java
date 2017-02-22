package cc.coopersoft.common;

import java.util.List;

/**
 * Created by cooper on 7/6/16.
 */
public abstract class ChoiceList<E> implements java.io.Serializable {

    private List<BatchData<E>> dataList;

    protected abstract List<E> getOrgData();

    protected void initDatas(){
        if (dataList == null){
            dataList = BatchData.fillData(getOrgData());
        }
    }

    public void refresh(){
        dataList = null;
    }

    public List<BatchData<E>> getDataList() {
        initDatas();
        return dataList;
    }

    public boolean isCheckAll(){
        for(BatchData<E> data: getDataList()){
            if (!data.isSelected()){
               return false;
            }
        }
        return true;
    }

    public void setCheckAll(boolean checked){
        for(BatchData<E> data: getDataList()){
            data.setSelected(checked);
        }
    }

    public boolean isAnySelected(){
        for(BatchData<E> data: getDataList()){
            if (data.isSelected()){
                return true;
            }
        }
        return false;
    }

}
