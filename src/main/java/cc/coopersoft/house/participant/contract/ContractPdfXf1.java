package cc.coopersoft.house.participant.contract;

import cc.coopersoft.common.tools.faces.convert.BigMoneyConverter;
import cc.coopersoft.house.participant.data.ContractContextMap;
import com.dgsoft.house.SalePayType;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContractPdfXf1 {


    private static String pm(String s, int len){
        StringBuffer result = new StringBuffer(s == null ? "" : s.trim());
        if (!"".equals(result.toString())){
            result.insert(0,"  ");
            result.append("  ");
            return result.toString();
        }else {
            for (int i = 0; i < (len / 2); i++) {
                result.append("  ");
            }
            return  result.toString() + "-" + result.toString();
        }

    }

    private static String pm(int i){
        String s = (i < 1) ? "" : String.valueOf(i);
        return pm(s,4);
    }

    public static void agentPdf(ContractContextMap contractContextMap, OutputStream outputStream){
        Document document = new Document(PageSize.A4,40,40,60,60);

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);

            pdfWriter.setEncryption(null, null, PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);


            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
                    "UniGB-UCS2-H", false);


            Font h1 = new Font(bfChinese, 32, Font.BOLD,
                    BaseColor.BLACK);


            Font h2 = new Font(bfChinese, 18, Font.BOLD,
                    BaseColor.BLACK);

            Font h3 = new Font(bfChinese, 12, Font.BOLD,
                    BaseColor.BLACK);

            Font h3i = new Font(bfChinese, 13, Font.UNDERLINE,
                    BaseColor.BLACK);

            Font b1 = new Font(bfChinese, 13, Font.NORMAL,
                    BaseColor.BLACK);

            Font b2 = new Font(bfChinese, 12, Font.NORMAL,
                    BaseColor.BLACK);

            Font bi = new Font(bfChinese, 12, Font.UNDERLINE, BaseColor.BLACK);

            DecimalFormat dfArea = new DecimalFormat("#0.00");
            DecimalFormat dfMoney = new DecimalFormat("¥#0.00");
            DecimalFormat dfInt = new DecimalFormat("#0");

            document.open();

            Paragraph p = new Paragraph("委托协议书",h2);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.add(new Paragraph(" ",b1));

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setKeepTogether(true);


            PdfPCell cel;

            PdfPTable titleTable = new PdfPTable(4);
            float f[] = {3,5,12,12};
            titleTable.setWidths(f);
            titleTable.setWidthPercentage(100);
            titleTable.setKeepTogether(true);

            cel = new PdfPCell(new Paragraph("姓名",b1 ));
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setColspan(2);
            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph("身份证号",b1 ));
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph("联系电话",b1 ));
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph("委托人",b2 ));
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setPaddingLeft(0);
            cel.setPaddingRight(0);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("seller_name").getStringValue(),b2 ));

            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("seller_id_number").getStringValue(),b2 ));

            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("seller_tel").getStringValue(),b2 ));

            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph("委托人",b2 ));
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setPaddingLeft(0);
            cel.setPaddingRight(0);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("group_owner").getStringValue(),b2 ));

            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("group_cer_number").getStringValue(),b2 ));

            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("group_tel").getStringValue(),b2 ));

            cel.setBorderWidth(1);
            cel.setPadding(10);
            cel.setBorderColor(BaseColor.BLACK);
            titleTable.addCell(cel);

            cel = new PdfPCell(titleTable);
            cel.setBorderWidth(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);



            cel = new PdfPCell(p);

            p = new Paragraph("第一条   委托人自愿委托受托方" ,b2);

            p.add(new Phrase(pm(contractContextMap.get("group_name").getStringValue(),70),bi));
            p.add(new Phrase("，办理房屋买卖契约合同见证，所需一切税费均由委托方负担。",b2));
            cel.addElement(p);

            p = new Paragraph("第二条   委托标的房地产坐落：西丰县" ,b2);

            p.add(new Phrase(pm(contractContextMap.get("house_address").getStringValue(),70),bi));
            p.add(new Phrase("。建筑面积",b2));

            p.add(new Phrase(pm(dfArea.format(contractContextMap.get("house_area").getNumberValue()) ,8),bi));
            p.add(new Phrase("平方米，系",b2));
            p.add(new Phrase(pm(contractContextMap.get("house_design_type").getStringValue(),70),bi));

            p.add(new Phrase("用房，房权证号西字第",b2));
            p.add(new Phrase(pm(contractContextMap.get("house_card_number").getStringValue(),70),bi));
            p.add(new Phrase("号。",b2));

            cel.addElement(p);
            p = new Paragraph("第三条   委托期限" ,b2);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            p.add(new Phrase(pm(sdf.format(contractContextMap.get("time_limit_begin").getDateValue()),70),bi));
            p.add(new Phrase("至",b2));
            p.add(new Phrase(pm("",6),bi));
            p.add(new Phrase("年",b2));
            p.add(new Phrase(pm("",4),bi));
            p.add(new Phrase("月",b2));
            p.add(new Phrase(pm("",4),bi));
            p.add(new Phrase("日。",b2));
            cel.addElement(p);

            cel.addElement(new Paragraph("第四条   买卖双方承诺共同履行《房屋买卖合同（契约）》所订立的内容，现据实申请见证上述房屋，如有不实，愿负法律责任。" ,b2));

            cel.addElement(new Paragraph("第五条   补充条款" ,b2));
            cel.addElement(new Paragraph(" " ,h1));
            cel.addElement(new Paragraph(" " ,h1));
            cel.addElement(new Paragraph(" " ,h1));




            cel.addElement(new Paragraph("第六条   本协议经双方签字盖章后生效。" ,b2));
            cel.setBorderWidth(1);
            cel.setBorderColor(BaseColor.BLACK);
            cel.setPadding(10);
            table.addCell(cel);


            PdfPTable footTable = new PdfPTable(2);
            float ff[] = {5,7};
            footTable.setWidths(ff);
            footTable.setWidthPercentage(100);
            footTable.setKeepTogether(true);

            cel = new PdfPCell(new Paragraph("委托人：" ,b1));
            cel.setBorderWidth(0);
            cel.setPadding(20);
            footTable.addCell(cel);


            cel = new PdfPCell(new Paragraph("受委托人："  + contractContextMap.get("group_name").getStringValue() ,b1));
            cel.setBorderWidth(0);
            cel.setPadding(20);
            footTable.addCell(cel);

            cel = new PdfPCell();
            cel.setBorderWidth(0);
            cel.setPadding(20);
            footTable.addCell(cel);

            cel = new PdfPCell(new Paragraph("住所：" + contractContextMap.get("group_address").getStringValue()  ,b1));
            cel.setBorderWidth(0);
            cel.setPadding(20);
            footTable.addCell(cel);

            cel = new PdfPCell();
            cel.setBorderWidth(0);
            cel.setPadding(20);
            footTable.addCell(cel);

            cel = new PdfPCell(new Paragraph("法定代表人：" + contractContextMap.get("group_owner").getStringValue()  ,b1));
            cel.setBorderWidth(0);
            cel.setPadding(20);
            footTable.addCell(cel);

            cel = new PdfPCell();
            cel.setBorderWidth(0);
            cel.setPadding(20);
            footTable.addCell(cel);

            cel = new PdfPCell(new Paragraph(sdf.format(new Date()) ,b1));
            cel.setBorderWidth(0);
            cel.setPadding(20);
            cel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footTable.addCell(cel);


            cel = new PdfPCell(footTable);

            table.addCell(cel);
            document.add(table);
            document.close();

        }catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pdf(ContractContextMap contractContextMap, OutputStream outputStream){

        Document document = new Document(PageSize.A4,40,40,60,60);

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document,outputStream);

            pdfWriter.setEncryption(null,null,PdfWriter.ALLOW_PRINTING,PdfWriter.STANDARD_ENCRYPTION_128);


            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
                    "UniGB-UCS2-H", false);



            Font h1 = new Font(bfChinese, 32, Font.BOLD,
                    BaseColor.BLACK);


            Font h2 = new Font(bfChinese, 18, Font.BOLD,
                    BaseColor.BLACK);

            Font h3 = new Font(bfChinese, 12, Font.BOLD,
                    BaseColor.BLACK);

            Font h3i = new Font(bfChinese, 13, Font.UNDERLINE,
                    BaseColor.BLACK);

            Font b1 = new Font(bfChinese, 13, Font.NORMAL,
                    BaseColor.BLACK);

            Font b2 = new Font(bfChinese, 11, Font.NORMAL,
                    BaseColor.BLACK);

            Font bi = new Font(bfChinese,11,Font.UNDERLINE,BaseColor.BLACK);

            DecimalFormat dfArea = new DecimalFormat("#0.00");
            DecimalFormat dfMoney = new DecimalFormat("¥#0.00");
            DecimalFormat dfInt = new DecimalFormat("#0");

            document.open();
            Paragraph p = new Paragraph("合同编号:" + contractContextMap.get("contract_number").getStringValue() ,b1);
            document.add(p);

            document.add(new Paragraph(40," "));
            p = new Paragraph("房屋交易合同书",h1);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            for (int i=0 ; i<= 18 ;i++)
                document.add(new Paragraph(24," "));

            p = new Paragraph("西丰县房产管理局监制",h2);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.newPage();



            p = new Paragraph("房屋交易合同书",h2);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.add(new Paragraph(40," ",b1));

            PdfPTable table = new PdfPTable(4);
            float f[] = {4,8,6,18};
            table.setWidths(f);
            table.setWidthPercentage(100);


            PdfPCell cel = new PdfPCell(new Paragraph("出卖人:",b1 ));
            cel.setBorder(0);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph(pm(contractContextMap.get("seller_name").getStringValue(),6),b1));
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColorBottom(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("seller_card_type").getStringValue() + "号:",b1));
            cel.setBorder(0);

            table.addCell(cel);

            cel = new PdfPCell(new Paragraph(pm(contractContextMap.get("seller_card_number").getStringValue(),10),b1));
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColorBottom(BaseColor.BLACK);
            table.addCell(cel);



            for(ContractContextMap pool: contractContextMap.get("seller_pool").getArrayValue()){
                cel = new PdfPCell(new Paragraph("共有人:" ,b1));
                cel.setBorder(0);
                table.addCell(cel);

                cel = new PdfPCell(new Paragraph(pm(pool.get("name").getStringValue(),6),b1));
                cel.setBorder(0);
                cel.setBorderWidthBottom(1);
                cel.setBorderColorBottom(BaseColor.BLACK);
                table.addCell(cel);

                cel = new PdfPCell(new Paragraph(pool.get("card_type").getStringValue() + "号:",b1));
                cel.setBorder(0);
                table.addCell(cel);

                cel = new PdfPCell(new Paragraph(pm(pool.get("card_number").getStringValue(),10),b1));
                cel.setBorder(0);
                cel.setBorderWidthBottom(1);
                cel.setBorderColorBottom(BaseColor.BLACK);
                table.addCell(cel);
            }

            cel = new PdfPCell(new Paragraph("买受人:",b1 ));
            cel.setBorder(0);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph(pm(contractContextMap.get("buyer_name").getStringValue(),6),b1));
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColorBottom(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph(contractContextMap.get("buyer_card_name").getStringValue() + "号:",b1));
            cel.setBorder(0);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph(pm(contractContextMap.get("buyer_card_number").getStringValue(),10),b1));
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColorBottom(BaseColor.BLACK);
            table.addCell(cel);

            for(ContractContextMap pool: contractContextMap.get("buyer_pool").getArrayValue()){
                cel = new PdfPCell(new Paragraph("共有人:" ,b1));
                cel.setBorder(0);
                table.addCell(cel);

                cel = new PdfPCell(new Paragraph(pm(pool.get("name").getStringValue(),6),b1));
                cel.setBorder(0);
                cel.setBorderWidthBottom(1);
                cel.setBorderColorBottom(BaseColor.BLACK);
                table.addCell(cel);

                cel = new PdfPCell(new Paragraph(pool.get("card_type").getStringValue() + "号:",b1));
                cel.setBorder(0);
                table.addCell(cel);

                cel = new PdfPCell(new Paragraph(pm(pool.get("card_number").getStringValue(),10),b1));
                cel.setBorder(0);
                cel.setBorderWidthBottom(1);
                cel.setBorderColorBottom(BaseColor.BLACK);
                table.addCell(cel);
            }



            cel = new PdfPCell(p);
            cel.setBorder(0);
            cel.setPaddingTop(20);
            table.addCell(cel);

            document.add(table);



            document.add(new Paragraph(" ",b1));

            document.add(new Paragraph("        根据《中华人民共和国合同法》、《中华人民共和国城市房地产管理法》及其他有关法律、法规之规定，买受人和出卖人在平等、自愿、协商一致的基础上就买卖房屋达成如下协议：",b2));

            document.add(new Paragraph(25,"        第一条  项目建设依据",h3));

            p = new Paragraph(20);
            p.add(new Phrase("        1、出卖人以 【", b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_1_1").getStringValue(),4),bi));
            p.add(new Phrase("】方式取得座落于",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_1_2").getStringValue(),40),bi));
            p.add(new Phrase("地块的国有土地使用权。",b2));
            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        2、该地块【",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_1").getStringValue(),8),bi));
            p.add(new Phrase("】为：",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_2").getStringValue(),10),bi));
            p.add(new Phrase("，土地使用面积为：",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_3").getStringValue(),4),bi));
            p.add(new Phrase("，买受人购买的房屋所在土地用途为：",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_4").getStringValue(),4),bi));
            p.add(new Phrase("， 土地使用年限自",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_5").getStringValue(),6),bi));
            p.add(new Phrase("年",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_6").getStringValue(),4),bi));
            p.add(new Phrase("月",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_7").getStringValue(),4),bi));
            p.add(new Phrase("日至",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_8").getStringValue(),6),bi));
            p.add(new Phrase("年",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_9").getStringValue(),4),bi));
            p.add(new Phrase("月",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_1_2_10").getStringValue(),4),bi));
            p.add(new Phrase("日止。",b2));
            document.add(p);

            document.add(new Paragraph(25,"        第二条  房屋情况",h3));
            p = new Paragraph(20,"        房屋座落：",b2);
            p.add(new Phrase(pm(contractContextMap.get("address").getStringValue(),70),bi));
            p.add(new Phrase("，设计用途",b2));
            p.add(new Phrase(pm(contractContextMap.get("use_type").getStringValue(),6),bi));
            p.add(new Phrase("；建筑结构",b2));
            p.add(new Phrase(pm(contractContextMap.get("structure").getStringValue(),4),bi));
            p.add(new Phrase("；建筑层数为",b2));
            p.add(new Phrase(pm(contractContextMap.get("floor_count").getStringValue(),4),bi));
            p.add(new Phrase("层。建筑面积",b2));
            p.add(new Phrase(pm(dfArea.format(contractContextMap.get("house_area").getNumberValue()),4),bi));
            p.add(new Phrase("平方米，其中：套内建筑面积",b2));
            p.add(new Phrase(pm(dfArea.format(contractContextMap.get("use_area").getNumberValue()),4),bi));
            p.add(new Phrase("平方米，公共部位分摊建筑面积",b2));
            p.add(new Phrase(pm(dfArea.format(contractContextMap.get("comm_area").getNumberValue()),4),bi));
            p.add(new Phrase("平方米。原房权证号：",b2));
            p.add(new Phrase(pm(contractContextMap.get("power_card_number").getStringValue(),4),bi));
            p.add(new Phrase("。",b2));
            document.add(p);


            document.add(new Paragraph(25,"        第三条 计价方式与价款",h3));

            int select3 = contractContextMap.get("c_3_0_1").getNumberValue().intValue();
            p = new Paragraph(20,"        出卖人与买受人约定按下述第",b2);
            p.add(new Phrase(pm(select3),bi));
            p.add(new Phrase("种方式计算该房屋价款；",b2));
            document.add(p);

            p = new Paragraph(20,"        1、按建筑面积计算，该房屋单价为（人民币）每平方米",b2);
            if (select3 == 1) {
                p.add(new Phrase(pm(dfMoney.format(contractContextMap.get("price_house_area").getNumberValue()), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元，总金额（人民币）",b2));
            if (select3 == 1) {
                p.add(new Phrase(pm(dfMoney.format(contractContextMap.get("money").getNumberValue()), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元。",b2));
            document.add(p);

            p = new Paragraph(20,"        2、按套内建筑面积计算，该房屋单价为（人民币）每平方米",b2);
            if (select3 == 2) {
                p.add(new Phrase(pm(dfMoney.format(contractContextMap.get("price_use_area").getNumberValue()), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元，总金额（人民币）",b2));
            if (select3 == 2) {
                p.add(new Phrase(pm(dfMoney.format(contractContextMap.get("money").getNumberValue()), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元。",b2));
            document.add(p);

            p = new Paragraph(20,"        3、按套（单元）计算，该房屋总价款为（人民币）",b2);

            if (select3 == 3) {
                p.add(new Phrase(pm(dfMoney.format(contractContextMap.get("money").getNumberValue()), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元。",b2));
            document.add(p);


            document.add(new Paragraph(25,"        第四条 付款方式及期限",h3));

            int select4 = 0;
            if (SalePayType.ALL_PAY.name().equals(contractContextMap.get("pay_type").getStringValue())){
                select4 = 1;
            }else if (SalePayType.PART_PAY.name().equals(contractContextMap.get("pay_type").getStringValue())){
                select4 = 2;
            }else if (SalePayType.DEBIT_PAY.name().equals(contractContextMap.get("pay_type").getStringValue())){
                select4 = 3;
            }else if (SalePayType.OTHER_PAY.name().equals(contractContextMap.get("pay_type").getStringValue())){
                select4 = 4;
            }
            p = new Paragraph(20,"        买受人采取下列第",b2);
            p.add(new Phrase(pm(select4),bi));
            p.add(new Phrase("种方式付款；",b2));
            document.add(p);

            p = new Paragraph(20,"        1、一次性付款，自签约日期起",b2);
            if (select4 == 1) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_1_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("日内付全部价款",b2));
            if (select4 == 1) {
                p.add(new Phrase(pm(BigMoneyConverter.numberToBigMoney(contractContextMap.get("money").getNumberValue()), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元。（小写：",b2));
            if (select4 == 1) {
                p.add(new Phrase(pm(dfMoney.format(contractContextMap.get("money").getNumberValue()), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元）。",b2));
            document.add(p);

            document.add(new Paragraph("        2、分期付款：",b2));
            p = new Paragraph(20,"        （1）第一次付款自签约日起",b2);
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("日内付全部价款的",b2));
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_2").getStringValue(), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("%，人民币",b2));
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_3").getStringValue(), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元。（小写：",b2));
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_4").getStringValue(), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }

            p.add(new Phrase("元）。",b2));
            document.add(p);

            p = new Paragraph(20,"        （2）第二次付款自签约日起",b2);
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_5").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("日内付全部价款的",b2));
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_6").getStringValue(), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("%，人民币",b2));
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_7").getStringValue(), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("元。（小写：",b2));
            if (select4 == 2) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_2_8").getStringValue(), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }

            p.add(new Phrase("元）。",b2));
            document.add(p);


            p = new Paragraph(20,"        3、货款方式付款。买受人可以首期支付购房总价款的",b2);
            if (select4 == 3) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_3_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("%，其余价款要以向",b2));
            if (select4 == 3) {
                p.add(new Phrase(pm(contractContextMap.get("c_4_3_2").getStringValue(), 8), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }

            p.add(new Phrase("银行或住房公积金管理机构借款支付。",b2));
            document.add(p);

            if (select4 == 4){
                p = new Paragraph(20,"        4、",b2);
                p.add(new Phrase(pm(contractContextMap.get("c_4_4_1").getStringValue(), 8), bi));
                p.add(new Phrase("。",b2));
                document.add(p);
            }

            document.add(new Paragraph(25,"        第五条 房产交付",h3));

            document.add(new Paragraph(20,"        1、房产实物状况、权利状况，符合有关规定和双方约定的，方可交付；不符合有关规定和双方约定的，不得交付。",b2));
            p = new Paragraph(20,"        2、出卖人须于",b2);

            p.add(new Phrase(pm(contractContextMap.get("c_5_2_1").getStringValue(), 4), bi));
            p.add(new Phrase("前，将该房产及相关证明交付买受人。如遇不可抗力，双方约定处理方式：",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_2_2").getStringValue(), 40), bi));
            p.add(new Phrase("。",b2));
            document.add(p);

            document.add(new Paragraph(20,"        3、上述相关证明包括：身份证、户口本、婚姻证明及复印件。",b2));

            document.add(new Paragraph(25,"        第六条 产权转移登记及其它相关设施登记",h3));
            document.add(new Paragraph(20,"        1、协议订立后，买卖双方应在30日内，到房屋所有权登记机关办理房屋所有权转移登记手续。",b2));
            document.add(new Paragraph(20,"        2、按照有关规定，其他相关设施应办理登记的，应在规定期限内办理。",b2));
            document.add(new Paragraph(20,"        3、如因一方原因造成另一方未能及时办理房屋所有权转移登记、其他相关设施登记造成损失的，应依法承担违约责任。",b2));

            document.add(new Paragraph(25,"        第七条 出卖人逾期交付房屋的处理",h3));
            int select7 = contractContextMap.get("c_7_0_2").getNumberValue().intValue();
            p = new Paragraph(20,"        除遇不可抗力外，出卖人如未按本合同约定日期交付房屋，逾期在",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_7_0_1").getStringValue(), 4), bi));
            p.add(new Phrase("日内的，买受人有权向出卖人追究已付款利息，利息自合同约定出卖人应交付房屋之日次日起至实际交付房屋之日按银行同期货款利率计算。如超过上述约定期限的，买受人有权按照下述的第",b2));
            p.add(new Phrase(pm(select7), bi));
            p.add(new Phrase("种约定，追究出卖人违约责任；",b2));
            document.add(p);

            p = new Paragraph(20,"        1、合同继续履行。出卖人应支付买受人已付款利息，利息自合同约定出卖人应交付房屋之日次日起至实际交付房屋之日止，按银行同期贷款利率计算。此外，出卖人还应每日按房屋价款的",b2);
            if (select7 == 1){
                p.add(new Phrase(pm(contractContextMap.get("c_7_1_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("‰向买受人支付违约金。",b2));
            document.add(p);
            document.add(new Paragraph(20,"        2、解除合同。出卖人应退还买受人已付款、支付已付款利息，利息自合同约定出卖人应付房屋之日次日起至出卖人退还买受人已付款之日止，按银行同期贷款利率计算。",b2));

            p = new Paragraph(20,"        此外，出卖人还应该每日按房屋价款的",b2);
            if (select7 == 2){
                p.add(new Phrase(pm(contractContextMap.get("c_7_2_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("‰向买受人支付违约金。买受人的实际损失超过出卖人支付的违约金时，出卖人还应承担赔偿责任。",b2));
            document.add(p);

            document.add(new Paragraph(25,"        第八条 买受人逾期付款的处理",h3));
            int select8 = contractContextMap.get("c_8_0_2").getNumberValue().intValue();

            p = new Paragraph(20,"        买受人如未按本合同约定的日期付款，逾期在",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_8_0_1").getStringValue(), 4), bi));
            p.add(new Phrase("日内，出卖人有权追究买受人逾期付款及其利息，利息自合同约定买受人应付款之日次日起至实际付款之日止，按银行同期贷款利率计算。如超过上述约定期限的，出卖人有权按照下述的第",b2));
            p.add(new Phrase(pm(select8), bi));
            p.add(new Phrase("种约定，追究买受人违约责任。",b2));
            document.add(p);

            p = new Paragraph(20,"        1、合同继续履行。买受人应支付逾期付款及其利息，利息自合同约定买受人应付款之日次日起至实际付款之日止，按银行同期贷款利率计算。此外，买受人还应每日按房屋价款的",b2);
            if (select8 == 1){
                p.add(new Phrase(pm(contractContextMap.get("c_8_1_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("‰向出卖人支付违约金。",b2));
            document.add(p);

            p = new Paragraph(20,"        2、解除合同，买受人应每日按房屋价款的",b2);
            if (select8 == 2){
                p.add(new Phrase(pm(contractContextMap.get("c_8_2_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 4), bi));
            }
            p.add(new Phrase("‰向出卖人支付违约金。",b2));
            document.add(p);

            document.add(new Paragraph(25,"        第九条 面积确认及面积差异处理",h3));
            int select9 = contractContextMap.get("c_9_0_1").getNumberValue().intValue();
            document.add(new Paragraph(20,"        （一）根据当事人选择的计价方式，本条规定以（建筑面积/套内建筑面积）（本条款中均简称面积）为依据进行面积确认及面积差异处理。",b2));
            document.add(new Paragraph(20,"        （二）当事人选择按套计价的，不适用本条约定。",b2));
            document.add(new Paragraph(20,"        （三）合同约定面积与产权登记面积有差异的，以产权登记面积为准。",b2));
            p = new Paragraph(20,"        （四）房屋交付后，产权登记面积与合同约定面积发生差异，双方同意按",b2);
            p.add(new Phrase(pm(select9), bi));
            p.add(new Phrase("种方式进行处理：",b2));
            document.add(p);

            p = new Paragraph(20,"        1、双方自行约定：",b2);
            if (select9 == 1) {
                p.add(new Phrase(pm(contractContextMap.get("c_9_1_1").getStringValue(), 4), bi));
            }else{
                p.add(new Phrase(pm("-", 40), bi));
            }
            p.add(new Phrase("。", b2));
            document.add(p);
            document.add(new Paragraph(20,"        2、双方同意按以下原则处理：",b2));
            document.add(new Paragraph(20,"        （1）面积误差比绝对值在3%以内（含3%）的，据实结算房款；",b2));
            document.add(new Paragraph(20,"        （2）面积误差比绝对值超出3%时，买受人有权退房。",b2));

            p = new Paragraph(20,"        （五）买受人退房的，出卖人在买受人提出退房之日起30天内将买受人已付款退还给买受人，并按",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_9_5_1").getStringValue(), 9), bi));
            p.add(new Phrase("利率付给利息。", b2));
            document.add(p);

            document.add(new Paragraph(20,"        （六）买受人不退房的，产权登记面积大于合同约定面积时，面积误差比3%以内（含3%）部分的房价款由买受人补足；超出3%部分的房价款由出卖人承担，产权归买受人。产权登记面积小于合同约定面积款由出卖人双倍返还买受人。",b2));
            document.add(new Paragraph(20,"        产权登记面积-合同约定面积",b2));
            document.add(new Paragraph(20,"        面积误差比=   X100%",b2));
            document.add(new Paragraph(20,"        合同约定面积",b2));
            document.add(new Paragraph(20,"        （七）因设计变更造成面积差异，双方不解除合同的，应当签署补充协议。",b2));


            document.add(new Paragraph(25,"        第十条 出卖人关于房屋产权状况的承诺",h3));
            document.add(new Paragraph(20,"        出卖人保证销售的房屋没有产权纠纷和债权债务纠纷。因出卖人原因，造成该房屋不能办理产权登记或发生债权债务纠纷的，由出卖人承担全部责任。若出售的房屋设有他项权利的，出卖人应当在出售前征得他项权利人的书面同意，并以书面形式公示和明确告知买受人。",b2));

            document.add(new Paragraph(25,"        第十一条  风险责任的转移",h3));
            document.add(new Paragraph(20,"        该商品房的风险责任自交付之日起由出卖人转移给买受人。如买受人未按约定的日期办理该房屋的验收交接手续，出卖人应当发出书面催告书一次。买受人未按催告书规定的日期办理该房屋的验收交接手续的，则自催告书约定的验收交接日之第二日起该房屋的风险责任转移由买受人承担。",b2));


            document.add(new Paragraph(25,"        第十二条  声明及保证",h3));
            document.add(new Paragraph(20,"        出卖人：",b2));
            document.add(new Paragraph(20,"        1、出卖人有权签署并有能力履行本合同。",b2));
            document.add(new Paragraph(20,"        2、出卖人签署和履行本合同所需的一功手续（身份证、户口本、婚姻证明）均已办妥并合法有效。",b2));
            document.add(new Paragraph(20,"        3、在签署本合同时，任何法院、仲裁机构、行政机关或监督机构未作出任何足以对出卖人履行本合同产生重大不利影响的判决、裁定、裁决或具体行政行为。",b2));
            document.add(new Paragraph(20,"        4、出卖人为签署本合同所需的内部授权程序均已完成，本合同的签署人是出卖人法定代表人或授权代表人。本合同生效后即对合同双方具有法律约束力。",b2));
            document.add(new Paragraph(20,"        买受人：",b2));
            document.add(new Paragraph(20,"        1、买受人有权签署并有能力履行本合同。",b2));
            document.add(new Paragraph(20,"        2、买受人签署和履行本合同所需的一切手续（身份证、户口本、婚姻证明）均已办妥并合法有效。",b2));
            document.add(new Paragraph(20,"        3、在签署本合同时，任何法院、仲裁机构、行政机关或监督机构均未作出任何足以对出卖人履行本合同产生重大不利影响的判决、裁定、裁决或具体行政行为。",b2));
            document.add(new Paragraph(20,"        4、买受人为签署本合同所需的内部授权程序均已完成，本合同的签署人是买受人法定代表人或授权代表人。本合同生效后即对合同双方具有法律约束力。",b2));


            document.add(new Paragraph(25,"        第十三条 保密",h3));
            p = new Paragraph(20,"        买卖双方保证对在讨论、签订、执行本协议过程中所获悉的属于对方的且无法自公开渠道获得的文件及资料（包括商业秘密、公司计划、运营活动、财务信息、技术信息、经营信息及其他商业秘密）予以保密。未经该资料和文件的原提供方同意，另一方不得向任何第三方泄漏该商业秘密的全部或部分内容、但法律、法规另有规定或双方另有约定的除外。保密期限为",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_13_0_1").getStringValue(), 9), bi));
            p.add(new Phrase("年。", b2));
            document.add(p);

            document.add(new Paragraph(25,"        第十四条 通知",h3));
            p = new Paragraph(20,"        1、根据本合同需要一方向另一方发出的全部通知以及双方的文件往来及与本合同有关的通知和要求等，必须用书面形式，可采用",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_14_1_1").getStringValue(), 8), bi));
            p.add(new Phrase("（书信、传真、电报、当面送交等）方式传递。以上方式无法送达的，方可采取公告送达方式。",b2));
            document.add(p);

            p = new Paragraph(20,"        2、各方通讯地址如下：",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_14_2_1").getStringValue(), 40), bi));
            p.add(new Phrase("。",b2));
            document.add(p);

            p = new Paragraph(20,"        3、一方变更通知或通讯地址的，应自变更之日起",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_14_3_1").getStringValue(), 8), bi));
            p.add(new Phrase("日内，以书面形式通知对方；否则，由未通知方承担由此引起的相关责任。",b2));
            document.add(p);

            document.add(new Paragraph(25,"        第十五条 合同的变更",h3));
            p = new Paragraph(20,"        本合同履行期间，发生特殊情况时，买卖任何一方需要变更本合同的，要求变更一方应及时书面通知对方，征得对方同意后，双方在规定的时限内（书面通知发出",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_15_0_1").getStringValue(), 4), bi));
            p.add(new Phrase("天内）签订书面变更协议，该协议将成为合同不可分割的部分。未经双方签署书面文件，任何一方无权变更本合同，否则，由此造成对方的经济损失，由责任方承担。",b2));
            document.add(p);

            document.add(new Paragraph(25,"        第十六条 合同的转让",h3));
            document.add(new Paragraph(20,"        除合同中另有规定外或经双方协商同意外，本合同所规定双方的任何权利和义务任何一方在未经得另一方书面同意之前，不得转让给第三者。任何转让，未经另一方书面明确同意，均属无效。",b2));

            document.add(new Paragraph(25,"        第十七条 争议的处理",h3));
            int select17 = contractContextMap.get("c_17_2_1").getNumberValue().intValue();
            document.add(new Paragraph(20,"        1、本合同受中华人民共和国法律管辖并按其进行解释。",b2));
            p = new Paragraph(20,"        2、本合同在履行过程中发生的争议，由双方当事人协商解决，也可由有关部门调解；协商或调解不成的，按下列第",b2);
            p.add(new Phrase(pm(select17), bi));
            p.add(new Phrase("种方式解决；",b2));
            document.add(p);

            p = new Paragraph(20,"        （1）提交",b2);
            if (select17 == 1){
                p.add(new Phrase(pm(contractContextMap.get("c_17_2_2").getStringValue(), 9), bi));
            }else{
                p.add(new Phrase(pm("-", 9), bi));
            }
            p.add(new Phrase("仲裁委员会仲裁；",b2));
            document.add(p);
            document.add(new Paragraph(20,"        （2）依法向人民法院起诉。",b2));

            document.add(new Paragraph(25,"        第十八条 不可抗力",h3));
            document.add(new Paragraph(20,"        1、如果本合同任何一方因受不可抗力事件影响而未能履行其在本合同下的全部或部分义务，该义务的履行在不可抗力事件妨碍其履行期间应予中止。",b2));
            p = new Paragraph(20,"        2、声称受到不可抗力事件影响的一方尽可能在最短的时间内通过书面形式将不可抗力事件的发生通知另一方，并在该不可抗力事件发生后",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_18_2_1").getStringValue(), 4), bi));
            p.add(new Phrase("日内向另一方提供关于此种不可抗力事件及其持续时间的适当证据及合同不能履行或者需要延期履行的书面材料。声称不可抗力事件导致其对本合同的履行在客观上成为不可能或不实际的一方，有责任尽一功合理的努力消除或减轻此等不可抗力事件的影响。",b2));
            document.add(p);
            document.add(new Paragraph(20,"        3、不可抗力事件发生时，双方应立即通过友好协商决定如何执行本合同。不可抗力事件或其影响终止或消除后，双方须立即恢复履行各自在本合同项目下的各项义务。如不可抗力及其影响无法终止或消除而致使合同任何一方丧失继续履行合同的能力，则双方可协商解除合同或暂时延迟合同的履行，且遭遇不可抗力一方无须为此承担责任。当事人延迟履行后发生不可抗力的，不能免除责任。",b2));
            document.add(new Paragraph(20,"        4、本合同所称“不可抗力”是指受影响一方不能合理控制的，无法预料或即使可预料也不可避免且无法克服，并于本合同签订日之后出现的，使该方对本合同全部或部分的履行在客观上成为不可能或不实际的任何事件。此等事件包括但不限于自然灾害如水灾、火灾、台风、地震，以及社会事件如战争（不论曾否宣战）动乱、罢工，政府行为或法律规定等。",b2));

            document.add(new Paragraph(25,"        第十九条 合同的解释",h3));
            document.add(new Paragraph(20,"        本合同未尽事宜或条款内容不明确，合同双方当事人可以根据本合同的原则、合同的目的、交易习惯及关联条款的内容，按照通常理解对本合同作出合理解释。该解释具有约束力；除非解释与法律或本合同相抵触。",b2));

            document.add(new Paragraph(25,"        第二十条 补充与附件",h3));
            document.add(new Paragraph(20,"        本合同未尽事宜，依照有关法律、法规执行，法律、法规未作规定的，买卖双方可以达成书面补充合同。本合同的附件和补充合同均为本合同不可分割的组成部分，与本合同具有同等法律效力。",b2));

            document.add(new Paragraph(25,"        第二十一条 合同的效力",h3));
            document.add(new Paragraph(20,"        1、本合同自双方或双方法定代表人或其授权代表人签字并加盖单位公章或合同专用章之日起生效。",b2));

            p = new Paragraph(20,"        2、本协议一式",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_21_2_1").getStringValue(), 4), bi));
            p.add(new Phrase("份，出卖人、买受人各",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_21_2_2").getStringValue(), 4), bi));
            p.add(new Phrase("份，具有同等法律效力。",b2));
            document.add(p);
            document.add(new Paragraph(20,"        3、本合同的附件和补充合同均为本合同不可分割的组成部分，与本合同具有同等的法律效力。",b2));




            document.add(new Paragraph(25," ",h2));
            table = new PdfPTable(4);
            float fb[] = {2,4,2,4};
            table.setWidths(fb);
            table.setWidthPercentage(100);

            cel = new PdfPCell(new Paragraph("出卖方产权人：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("买受方产权人：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("电话：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);


            cel = new PdfPCell(new Paragraph("电话：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("共有人：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("共有人：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("电话：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);


            cel = new PdfPCell(new Paragraph("电话：",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell();
            cel.setBorder(0);
            cel.setBorderWidthBottom(1);
            cel.setBorderColor(BaseColor.BLACK);
            table.addCell(cel);

            cel = new PdfPCell();
            cel.setBorder(0);
            table.addCell(cel);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            cel = new PdfPCell(new Paragraph(sdf.format(new Date()),b2));
            cel.setVerticalAlignment(Element.ALIGN_RIGHT);
            cel.setBorder(0);
            table.addCell(cel);

            cel = new PdfPCell();
            cel.setBorder(0);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph(sdf.format(new Date()),b2));
            cel.setVerticalAlignment(Element.ALIGN_RIGHT);
            cel.setBorder(0);
            table.addCell(cel);

            table.setKeepTogether(true);
            document.add(table);


            document.close();



        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

}
