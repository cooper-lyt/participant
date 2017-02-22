package cc.coopersoft.common.util;

/**
 * Created by cooper on 6/22/16.
 */
public class ConditionAdapter implements java.io.Serializable{

    public static ConditionAdapter instance(String condition){
        return new ConditionAdapter(condition);
    }

    private String condition;

    private boolean empty;
    private String endWith;
    private String startWith;
    private String contains;

    private ConditionAdapter(String condition) {
        empty = condition != null && !"".equals(condition.trim());
        if (empty){
            endWith = "%" + condition.trim();
            startWith = condition.trim() + "%";
            contains = "%" + condition.trim() + "%";
            this.condition = condition.trim();
        }else{
            endWith = "%";
            startWith = "%";
            contains = "%";
            this.condition = "";
        }
    }

    public String getCondition() {
        return condition;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getEndWith() {
        return endWith;
    }

    public String getStartWith() {
        return startWith;
    }

    public String getContains() {
        return contains;
    }

    public boolean isDirty(String other){
        String otherValue = other == null ? "" : other.trim();
        return DataHelper.isDirty(condition,otherValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConditionAdapter that = (ConditionAdapter) o;

        return condition.equals(that.condition);

    }

    @Override
    public int hashCode() {
        return condition.hashCode();
    }
}
