package cc.coopersoft.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cooper on 6/21/16.
 */
public class BatchData<E> implements java.io.Serializable{

    private E data;

    private boolean selected;

    public BatchData(E data, boolean selected) {
        this.data = data;
        this.selected = selected;
    }

    public BatchData(E data) {
        this.data = data;
        this.selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public static <T> List<BatchData<T>> fillData(List<T> datas){
        List<BatchData<T>> result = new ArrayList<BatchData<T>>(datas.size());
        for(T data: datas){
            result.add(new BatchData<T>(data));
        }
        return result;
    }
}
