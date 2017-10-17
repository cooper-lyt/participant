package cc.coopersoft.house.participant.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by cooper on 9/12/15.
 */
public class ContractContextMap extends HashMap<String, ContractContextMap.ContarctContextItem> {

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        for (Map.Entry<String, ContarctContextItem> entry : entrySet()) {
            if (!entry.getValue().isNull()) {
                entry.getValue().putToJson(result, entry.getKey());
            }
        }
        return result;
    }

    public ContractContextMap() {
        super();
    }

    public ContractContextMap(JSONObject jsonObject) throws JSONException {
        super();
        Iterator it = jsonObject.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            put(key, new ContarctContextItem(jsonArray));
        }
    }

    @Override
    public ContarctContextItem get(Object key) {
        ContarctContextItem result = super.get(key);
        if (result == null) {
            result = new ContarctContextItem();
            super.put((String) key, result);
        }
        return result;
    }


    private final static String STRING_TYPE = "STRING";

    private final static String NUMBER_TYPE = "NUMBER";

    private final static String DATE_TYPE = "DATE";

    private final static String ARRAY_TYPE = "ARRAY";

    public static class ContarctContextItem {

        private Object data;

        public ContarctContextItem() {
        }

        public ContarctContextItem(String data) {
            setStringValue(data);
        }

        public ContarctContextItem(BigDecimal data) {
            setNumberValue(data);
        }

        public ContarctContextItem(Date data) {
            setDateValue(data);
        }

        public ContarctContextItem(List<ContractContextMap> data) {
            this.data = data;
        }

        public ContarctContextItem(JSONArray value) throws JSONException {
            if (STRING_TYPE.equals(value.getString(0))) {
                data = value.getString(1);
            }
            if (NUMBER_TYPE.equals(value.getString(0))) {
                data = new BigDecimal(value.getString(1));
            }
            if (DATE_TYPE.equals(value.getString(0))) {
                data = new Date(value.getLong(1));
            }
            if (ARRAY_TYPE.equals(value.getString(0))) {
                JSONArray subArray = value.getJSONArray(1);
                List<ContractContextMap> result = new ArrayList<ContractContextMap>();
                for (int i = 0; i < subArray.length(); i++) {
                    result.add(new ContractContextMap(subArray.getJSONObject(i)));
                }
                data = result;
            }
        }

        public boolean isNull() {
            return data == null;
        }

        @Deprecated
        public boolean isEmpty(){
            if (data == null){
                return true;
            }
            if (isString()) {
                return getStringValue().trim().equals("");
            }else if(isArray()){
                return getArrayValue().isEmpty();
            }else{
                return false;
            }

        }

        public boolean isEmptyData(){
            if (data == null){
                return true;
            }
            if (isString()) {
                return getStringValue().trim().equals("");
            }else if(isArray()){
                return getArrayValue().isEmpty();
            }else if (isDate()){
                return getDateValue().getTime() == 0;
            }else{
                return false;
            }
        }

        public boolean isArray() {
            if (data == null) {
                return false;
            }
            return data instanceof List;
        }

        public boolean isNumber() {
            if (data == null)
                return false;
            return data instanceof BigDecimal;
        }

        public boolean isString() {
            if (data == null)
                return false;
            return data instanceof String;
        }

        public boolean isDate() {
            if (data == null) {
                return false;
            }
            return data instanceof Date;
        }

        public List<ContractContextMap> getArrayValue() {
            List<ContractContextMap> result;
            if (isArray()) {
                result = (List<ContractContextMap>) data;
            } else {
                result = new ArrayList<ContractContextMap>();
                data = result;
            }
            return result;
        }

        public String getStringValue() {
            if (!isString())
                return null;
            return (String) data;
        }

        public void setStringValue(String value) {
            this.data = value;
        }

        public BigDecimal getNumberValue() {
            if (!isNumber()) {
                return null;
            }
            return (BigDecimal) data;
        }

        public void setNumberValue(BigDecimal value) {
            this.data = value;
        }

        public Date getDateValue() {
            if (!isDate()) {
                return null;
            }
            return (Date) data;
        }

        public void setDateValue(Date value) {
            this.data = value;
        }

        public void putToJson(JSONObject jsonObject, String key) throws JSONException {
            if (data != null) {
                JSONArray jsonArray = new JSONArray();
                if (isString()) {
                    jsonArray.put(STRING_TYPE);
                    jsonArray.put(getStringValue());
                } else if (isNumber()) {
                    jsonArray.put(NUMBER_TYPE);
                    jsonArray.put(getNumberValue().toString());
                } else if (isDate()) {
                    jsonArray.put(DATE_TYPE);
                    jsonArray.put(getDateValue().getTime());
                } else if (isArray()) {
                    if (!getArrayValue().isEmpty()) {
                        jsonArray.put(ARRAY_TYPE);
                        JSONArray subArray = new JSONArray();
                        for (ContractContextMap data : getArrayValue()) {
                            subArray.put(data.toJson());
                        }
                        jsonArray.put(subArray);
                    } else {
                        return;
                    }
                } else {
                    throw new IllegalArgumentException("unkonw type");
                }
                jsonObject.put(key, jsonArray);
            }
        }


    }
}
