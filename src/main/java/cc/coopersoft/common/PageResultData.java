package cc.coopersoft.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cooper on 7/24/16.
 */
public class PageResultData<E> {

    private static final int PAGE_MAX_COUNT = 15;

    public static class DataPage{

        private String title;
        private int page;
        private int firstResult;

        public DataPage(String title, int page, int firstResult) {
            this.title = title;
            this.page = page;
            this.firstResult = firstResult;
        }

        public String getTitle() {
            return title;
        }

        public int getPage() {
            return page;
        }

        public int getFirstResult() {
            return firstResult;
        }
    }

    private List<E> resultData;

    private int page;

    private List<DataPage> pages;

    private int firstResult;

    private long pageCount;

    private long dataCount;

    private int pageSize;

    public PageResultData(List<E> resultData, int firstResult, long dataCount,int pageSize) {
        this.resultData = resultData;
        this.firstResult = firstResult;
        this.dataCount = dataCount;
        this.pageSize = pageSize;
        pages = new ArrayList<DataPage>();
        pageCount = dataCount/pageSize;
        if (dataCount%pageSize > 0){
            pageCount++;
        }
        if (firstResult == 0){
            page = 1;
        }else
            page = (firstResult / pageSize) + 1;

        for (int i = 1; i <= pageCount ; i++){
            pages.add(new DataPage(String.valueOf(i),i, (i - 1) * pageSize));
        }

    }

    public boolean isHaveNext(){
        return (page < pageCount);
    }

    public boolean isHavePrevious(){
        return (firstResult > 0);
    }

    public int getNextFirstResult(){
        return firstResult + pageSize;
    }

    public int getPreviousFirstResult(){
        return firstResult - pageSize;
    }

    public List<E> getResultData() {
        return resultData;
    }

    public int getPage() {
        return page;
    }

    public List<DataPage> getPages() {
        return pages;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public long getDataCount() {
        return dataCount;
    }

    public boolean isEmptyResult(){
        return dataCount == 0;
    }
}
