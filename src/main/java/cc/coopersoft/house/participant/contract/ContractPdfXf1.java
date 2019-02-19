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

            Font h3 = new Font(bfChinese, 13, Font.BOLD,
                    BaseColor.BLACK);

            Font h3i = new Font(bfChinese, 13, Font.UNDERLINE,
                    BaseColor.BLACK);

            Font b1 = new Font(bfChinese, 13, Font.NORMAL,
                    BaseColor.BLACK);

            Font b2 = new Font(bfChinese, 12, Font.NORMAL,
                    BaseColor.BLACK);

            Font bi = new Font(bfChinese,12,Font.UNDERLINE,BaseColor.BLACK);

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
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);


            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);

            document.add(new Paragraph(25,"        第五条 房产交付",h3));
            p = new Paragraph(20,"        房屋座落：",b2);

            p.add(new Phrase("条",h3));
            document.add(p);





            document.add(new Paragraph(25," ",h2));
            table = new PdfPTable(4);

            table.setWidths(f);
            table.setWidthPercentage(100);

            cel = new PdfPCell(new Paragraph("卖              方(签章):",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell(new Paragraph("买              方(签章):",b2));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell(new Paragraph("委托代理人(签章):",b2));
            cel.setBorder(0);
            cel.setPaddingTop(10);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("委托代理人(签章):",b2));
            cel.setBorder(0);
            cel.setPaddingTop(10);
            table.addCell(cel);


            p = new Paragraph(20);
            p.add(new Phrase("                       ",bi));
            p.add(new Phrase("证号码:",b2));
            cel = new PdfPCell(p);
            cel.setBorder(0);
            cel.setPaddingTop(10);
            table.addCell(cel);

            p = new Paragraph(20);
            p.add(new Phrase("                       ",bi));
            p.add(new Phrase("证号码:",b2));
            cel = new PdfPCell(p);
            cel.setBorder(0);
            cel.setPaddingTop(10);
            table.addCell(cel);


            cel = new PdfPCell(new Paragraph("联系电话:",b2));
            cel.setBorder(0);
            cel.setPaddingTop(10);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("联系电话:",b2));
            cel.setBorder(0);
            cel.setPaddingTop(10);
            table.addCell(cel);
            table.setKeepTogether(true);
            document.add(table);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            p = new Paragraph(sdf.format(new Date()),b2);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);

            document.close();



        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

}
