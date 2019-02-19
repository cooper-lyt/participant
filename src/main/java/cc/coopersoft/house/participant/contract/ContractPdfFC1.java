package cc.coopersoft.house.participant.contract;

import cc.coopersoft.common.tools.faces.convert.BigMoneyConverter;
import cc.coopersoft.house.participant.data.ContractContextMap;
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

public class ContractPdfFC1 {

    //TODO 条码 二维码 水印


    private static String pm(String s, int len){
        StringBuffer result = new StringBuffer(s == null ? "" : s.trim());
        if (!"".equals(result.toString())){
            result.insert(0,"  ");
            result.append("  ");
        }else {
            for (int i = 0; i < len ; i++) {
                result.append("  ");
            }
        }
        return result.toString();
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


            p = new Paragraph("房地产买卖契约",h1);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);





            PdfPTable table = new PdfPTable(2);
            float f[] = {1,8};
            table.setWidths(f);
            table.setWidthPercentage(100);


            PdfPCell cell = new PdfPCell(new Paragraph("立契约人",b1));
            cell.setRowspan(2);
            table.addCell(cell);

            PdfPCell cel = new PdfPCell(new Paragraph("甲方（卖方）: " + contractContextMap.get("seller_name").getStringValue() + contractContextMap.get("seller_card_type").getStringValue() +  "证号码: " + contractContextMap.get("seller_card_number").getStringValue(),b1));
            cel.setBorder(0);
            table.addCell(cel);


            cel = new PdfPCell(new Paragraph("乙方（买方）:" + contractContextMap.get("buyer_name").getStringValue() + contractContextMap.get("buyer_card_name").getStringValue() + "证号码: " + contractContextMap.get("buyer_card_number").getStringValue(),b1));
            cel.setBorder(0);
            table.addCell(cel);

            document.add(table);



            document.add(new Paragraph(" ",b1));

            document.add(new Paragraph("        甲、乙双方经过平等协商，一致同意就下列房地产买卖事项订立本契约，共同遵守。",b2));


            p = new Paragraph(20);
            p.add(new Phrase("        一、甲方自愿将座落在凤城市", b2));
            p.add(new Phrase(pm(contractContextMap.get("address").getStringValue(),50),bi));
            p.add(new Phrase("的房地产（房屋建筑面积",b2));
            p.add(new Phrase(pm(contractContextMap.get("house_area").getStringValue(),4),bi));
            p.add(new Phrase("平方米；土地使用面积",b2));
            p.add(new Phrase(pm(contractContextMap.get("land_area").getStringValue(),4),bi));
            p.add(new Phrase("平方米）出售给乙方。",b2));
            p.add(new Phrase(pm(contractContextMap.get("power_card_type").getStringValue(),4),bi));
            p.add(new Phrase("证号",b2));
            p.add(new Phrase(pm(contractContextMap.get("power_card_number").getStringValue(),4),bi));
            p.add(new Phrase("，用途",b2));
            p.add(new Phrase(pm(contractContextMap.get("use_type").getStringValue(),10),bi));
            p.add(new Phrase("，结构:",b2));
            p.add(new Phrase(pm(contractContextMap.get("structure").getStringValue(),10),bi));
            p.add(new Phrase("，所在层数:",b2));
            p.add(new Phrase(pm(contractContextMap.get("in_floor").getStringValue(),4),bi));
            p.add(new Phrase("四至界限为",b2));
            p.add(new Phrase(pm(contractContextMap.get("c1_1_1").getStringValue(),50),bi));
            p.add(new Phrase("。乙方经过充分了解愿意购买该房地产。",b2));
            document.add(p);



            p = new Paragraph(20);
            p.add(new Phrase("        二、甲乙双方议定上述房地产成交价格为人民币（大写）",b2));
            p.add(new Phrase(pm(BigMoneyConverter.numberToBigMoney(contractContextMap.get("money").getNumberValue()),55),bi));
            p.add(new Phrase(";¥",b2));
            p.add(new Phrase(pm( dfMoney.format(contractContextMap.get("money").getNumberValue()),10),bi));
            p.add(new Phrase("元。乙方由",b2));

            p.add(new Phrase(pm(contractContextMap.get("c_2_1_1").getStringValue(),4),bi));
            p.add(new Phrase("年",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_2_1_2").getStringValue(),4),bi));
            p.add(new Phrase("月",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_2_1_3").getStringValue(),4),bi));
            p.add(new Phrase("日前",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_2_1_4").getStringValue(),4),bi));
            p.add(new Phrase("次付清给甲方房款。付款方式",b2));
            p.add(new Phrase(pm(contractContextMap.get("sale_pay_type").getStringValue(),4),bi));
            p.add(new Phrase("。",b2));
            document.add(p);

            if (!contractContextMap.get("bank_name").isEmptyData()) {
                p = new Paragraph(20);
                p.add(new Phrase("        买卖双方约定监管银行为:", b2));
                p.add(new Phrase(pm(contractContextMap.get("bank_name").getStringValue(), 20),bi));
                p.add(new Phrase("账号为:",b2));
                p.add(new Phrase(pm(contractContextMap.get("bank_account").getStringValue(),20),bi));
                p.add(new Phrase("监管资金为:人民币",b2));
                p.add(new Phrase(dfMoney.format(contractContextMap.get("protected_money").getNumberValue()),bi));
                p.add(new Phrase("元（大写）:",b2));
                p.add(new Phrase(BigMoneyConverter.numberToBigMoney(contractContextMap.get("protected_money").getNumberValue()),bi));
                document.add(p);
                document.add(new Paragraph(20,"        出卖人确认的监管资金收款账户为：",b2));
                p = new Paragraph(20);
                p.add(new Phrase("        收款人姓名:",b2));
                p.add(new Phrase(contractContextMap.get("card_name").getStringValue(),bi));
                p.add(new Phrase("收款人账号:",b2));
                p.add(new Phrase(contractContextMap.get("card_number").getStringValue(),bi));
                p.add(new Phrase("开户银行:",b2));
                p.add(new Phrase(contractContextMap.get("card_bank").getStringValue(),bi));

                document.add(p);
            }

            p = new Paragraph(20);
            p.add(new Phrase("        三、双方同意于",h3));
            p.add(new Phrase(pm(contractContextMap.get("c_6_1_1").getStringValue(),4),bi));
            p.add(new Phrase("年",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_6_1_2").getStringValue(),4),bi));
            p.add(new Phrase("月",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_6_1_3").getStringValue(),4),bi));
            p.add(new Phrase("日由甲方将上述房地产正式交付给乙方。房屋移交给乙方时，其该建筑物范围内的土地使用权一并转移给乙方。",b2));
            document.add(p);



            p = new Paragraph(20);
            p.add(new Phrase("        四、甲方保证上述房地产权属清楚，无纠纷，房地产无限制交易的情形。甲乙双方所提交的证件资料真实、合法、有效、无隐瞒、伪报。如有不实，甲乙双方愿负全部法律及经济责任。",b2));
            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        五、上述房地产办理过户手续所需缴纳的税费，由",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_1_1").getStringValue(),4),bi));
            p.add(new Phrase("承担。甲方须协助乙方办理房地产买卖过户事宜。",b2));
            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        六、本契约经甲乙双方签章后生效。任何一方违约，由违约方向对方给付上述房地产价款百分之",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_6_1_1").getStringValue(),4),bi));
            p.add(new Phrase("的违约金。",b2));
            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        七、本契约一式四份，甲乙双方各执一份，房地产交易中心和公证处各一份。",b2));

            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        八、双方约定的其他事项：",b2));

            document.add(p);

            p = new Paragraph(20,"        ",b2);
            p.add(new Phrase(20,pm(contractContextMap.get("c8_1_1").getStringValue(),95),bi));
            document.add(p);



            document.add(new Paragraph(25," ",h2));
            table = new PdfPTable(2);

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
