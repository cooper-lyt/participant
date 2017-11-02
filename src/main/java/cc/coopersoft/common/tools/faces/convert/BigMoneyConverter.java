package cc.coopersoft.common.tools.faces.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by cooper on 9/13/15.
 */
public class BigMoneyConverter implements javax.faces.convert.Converter{


    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }

        // If the specified value is null or zero-length, return null
        if (value == null) {
            return (null);
        }
        value = value.trim();
        if (value.length() < 1) {
            return (null);
        }

        try {
            return bigMoneyToNumber(value);
        } catch (NumberFormatException nfe) {
            throw new ConverterException(nfe);
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }


    public String getAsString(FacesContext context, UIComponent component, Object value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }

        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        // If the incoming value is still a string, play nice
        // and return the value unmodified
        if (value instanceof String) {
            return (String) value;
        }

        try {
            return numberToBigMoney((BigDecimal)value);
        } catch (Exception e) {
            throw new ConverterException(e);
        }

    }

    public static void main(String[] args) {

        String money = "伍仟零贰元贰角壹分";

        System.out.println(bigMoneyToNumber(money));

        System.out.println(numberToBigMoney(new BigDecimal(1320)));
    }

    public static String numberToBigMoney(BigDecimal amount){
            NumberFormat nf = new DecimalFormat("#0.###");
            String amt = nf.format(amount);
            String dotPart = "";
            String intPart = "";
            int dotPos;
            if ((dotPos = amt.indexOf('.')) != -1) {
                intPart = amt.substring(0, dotPos);
                dotPart = amt.substring(dotPos + 1);
            } else {
                intPart = amt;
            }
            if (intPart.length() > 16)
                throw new InternalError("The amount is too big.");
            String intBig = intToBig(intPart);
            String dotBig = dotToBig(dotPart);
            if ((dotBig.length() == 0) && (intBig.length() != 0)) {
                return intBig + "元整";
            } else if ((dotBig.length() == 0) && (intBig.length() == 0)) {
                return intBig + "零元";
            } else if ((dotBig.length() != 0) && (intBig.length() != 0)) {
                return intBig + "元" + dotBig;
            } else {
                return dotBig;
            }

    }

    private static final int GRADE = 4;
    private static final String NUM = "零壹贰叁肆伍陆柒捌玖";
    private static final String UNIT = "仟佰拾个";
    private static final String GRADEUNIT = "仟万亿兆";
    private static final String DOTUNIT = "角分厘";

    private static String dotToBig(String dotPart) {
        String strRet = "";
        for (int i = 0; i < dotPart.length() && i < 3; i++) {
            int num;
            if ((num = Integer.parseInt(dotPart.substring(i, i + 1))) != 0)
                strRet += NUM.substring(num, num + 1)
                        + DOTUNIT.substring(i, i + 1);
        }
        return strRet;
    }

    private static String intToBig(String intPart) {
        int grade;
        String result = "";
        String strTmp = "";
        grade = intPart.length() / GRADE;
        if (intPart.length() % GRADE != 0)
            grade += 1;
        for (int i = grade; i >= 1; i--) {
            strTmp = getNowGradeVal(intPart, i);
            result += getSubUnit(strTmp);
            result = dropZero(result);
            if (i > 1)
                if (getSubUnit(strTmp).equals("零零零零")) {
                    result += "零" + GRADEUNIT.substring(i - 1, i);
                } else {
                    result += GRADEUNIT.substring(i - 1, i);
                }
        }
        return result;
    }

    private static String getSubUnit(String strVal) {
        String rst = "";
        for (int i = 0; i < strVal.length(); i++) {
            String s = strVal.substring(i, i + 1);
            int num = Integer.parseInt(s);
            if (num == 0) {
                if (i != strVal.length())
                    rst += "零";
            } else {
                rst += NUM.substring(num, num + 1);
                if (i != strVal.length() - 1)
                    rst += UNIT.substring(i + 4 - strVal.length(), i + 4
                            - strVal.length() + 1);
            }
        }
        return rst;
    }

    private static String getNowGradeVal(String strVal, int grade) {
        String rst;
        if (strVal.length() <= grade * GRADE)
            rst = strVal.substring(0, strVal.length() - (grade - 1) * GRADE);
        else
            rst = strVal.substring(strVal.length() - grade * GRADE, strVal
                    .length()
                    - (grade - 1) * GRADE);
        return rst;
    }

    private static String dropZero(String strVal) {
        String strRst;
        String strBefore;
        String strNow;
        strBefore = strVal.substring(0, 1);
        strRst = strBefore;
        for (int i = 1; i < strVal.length(); i++) {
            strNow = strVal.substring(i, i + 1);
            if (strNow.equals("零") && strBefore.equals("零"))
                ;
            else
                strRst += strNow;
            strBefore = strNow;
        }
        if (strRst.substring(strRst.length() - 1, strRst.length()).equals("零"))
            strRst = strRst.substring(0, strRst.length() - 1);
        return strRst;
    }

    public static BigDecimal bigMoneyToNumber(String money) {

        long integerPart = 0;
        String newMoney = money.substring(0, money.indexOf("元") + 1).replace(" ","");//去除元后面的部分
        //分割成如下形式：数字仟数字佰数字拾数字。 然后调用分割后的和，之后在求整个的和。
        if (-1 != newMoney.indexOf("兆")){
            String[] spits = newMoney.split("兆");//如果存在亿的部分就分割出含亿的部分
            integerPart += getSubMoney(spits[0], "兆");
            newMoney = spits[1];
        }
        if (-1 != newMoney.indexOf("亿")) {
            String[] spits = newMoney.split("亿");//如果存在亿的部分就分割出含亿的部分
            integerPart += getSubMoney(spits[0], "亿");
            newMoney = spits[1];
        }
        if (-1 != newMoney.indexOf("万")) {//如果存在万的部分就分割出含万的部分
            String[] spits = newMoney.split("万");
            integerPart += getSubMoney(spits[0], "万");
            newMoney = spits[1];
        }
        if (!newMoney.equals("元")) {//如果分割的最后不只有元了，就求不上万部分的和
            integerPart += getSubMoney(newMoney.substring(0, newMoney.indexOf("元")), "元");
        }

        BigDecimal result = new BigDecimal(integerPart);
        String floatMoney = money.substring(money.indexOf("元") + 1, money.length()).replace(" ","").replace("整","");
        if (!"".equals(floatMoney)){
            int floatPart = 0;
            if (-1 != floatMoney.indexOf("角")){
                floatPart = getMoney(floatMoney.substring(0, floatMoney.indexOf("角")).charAt(0)) * 100;
            }
            if (-1 != floatMoney.indexOf("分")){
                floatPart += getMoney(floatMoney.substring(floatMoney.indexOf("分") - 1, floatMoney.indexOf("分")).charAt(0)) * 10;
            }
            if (-1 != floatMoney.indexOf("厘")){
                floatPart += getMoney(floatMoney.substring(floatMoney.indexOf("厘") - 1, floatMoney.indexOf("厘")).charAt(0));
            }
            BigDecimal floatResult = new BigDecimal(floatPart);
            result = result.add(floatResult.divide(new BigDecimal("1000"),3, BigDecimal.ROUND_DOWN));
        }

        return result;
    }


    private static long getSubMoney(String money, String unit) {//分割后返回分割部分的数和
        long subSum = 0;//存储子部分的和
        char[] moneys = money.toCharArray();
        int rememberInt = 0;//用来记录数字部分
        for (int i = 0; i < moneys.length; i++) {
            int increament = increateNum(moneys[i]);
            if (-1 == increament) {
                rememberInt = getMoney(moneys[i]);
                if (i == (moneys.length - 1)) {
                    subSum += getMoney(moneys[i]);
                }
            } else if (increament > 0) {//如果是单位：拾，仟，佰.。乘以对应的进制
                subSum += increament * rememberInt;
            } else {
                continue;
            }
        }
        return subSum * getUnit(unit);
    }

    private static long getUnit(String unit) {//根据单位返回零的个数
        if (unit.equals("元")) {
            return 1;
        } else if (unit.equals("万")) {
            return 10000;
        } else if (unit.equals("亿")) {
            return 100000000;
        } else {
            return -1;
        }
    }


    private static int increateNum(char c) {//根据单位返回要乘的数
        switch (c) {
            case '拾': {
                return 10;
            }
            case '佰': {
                return 100;
            }
            case '仟': {
                return 1000;
            }
            case '零': {
                return 0;
            }
            default: {
                return -1;
            }
        }
    }

    private static int getMoney(char m) {//返回大写人民币所对应的数字

        switch (m) {
            case '壹': {
                return 1;
            }
            case '贰': {
                return 2;
            }
            case '叁': {
                return 3;
            }
            case '肆': {
                return 4;
            }
            case '伍': {
                return 5;
            }
            case '陆': {
                return 6;
            }
            case '柒': {
                return 7;
            }
            case '捌': {
                return 8;
            }
            case '玖': {
                return 9;
            }
            default: {
                return 0;
            }
        }
    }


}
