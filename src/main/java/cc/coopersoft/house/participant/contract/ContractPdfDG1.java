package cc.coopersoft.house.participant.contract;

import cc.coopersoft.common.tools.faces.convert.BigMoneyConverter;
import cc.coopersoft.house.participant.data.ContractContextMap;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cooper on 09/01/2018.
 */
public class ContractPdfDG1 {

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

            document.add(new Paragraph(40," "));
            p = new Paragraph("房屋买卖协议",h1);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            for (int i=0 ; i<= 18 ;i++)
                document.add(new Paragraph(24," "));

            p = new Paragraph("东港市房地产管理处监制",h2);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.newPage();



            p = new Paragraph("说   明",h2);

            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.add(new Paragraph(25," ",h2));


            document.add(new Paragraph(24,"1、本协议文本为示范文本，也可作为房屋买卖签约使用文本。签约之前，买卖双方应当仔细阅读本协议内容。",b1));
            document.add(new Paragraph(24,"2、本协议所称房屋是指已进行房屋权属登记，领取房屋权属证书的房屋。",b1));
            document.add(new Paragraph(24,"3、为体现协议双方的自愿原则，本协议文本中相关条款后都有空白行，供双方自行的约定或补充约定。双方当事人可以对文本条款的内容进行修改增补或删减。协议签订生效后，未被修改的文本印刷文字视为双方同意内容。",b1));
            document.add(new Paragraph(24,"4、本协议所称无欠费，是指房屋交付时，已按规定标准交纳房屋公共部位公用设施设备维修基金、维修费、取暖费、水费、电费、煤气费、有线电视费、物业费等费用已结清。",b1));
            document.add(new Paragraph(24,"5、本协议所称维修基金是指房屋共用部位、共用设施设备维修基金。",b1));
            document.add(new Paragraph(24,"6、本协议书必须用蓝黑、炭素墨水笔填写。",b1));
            document.add(new Paragraph(24,"7、本协议书条款由东港市房地产管理处负责解释。",b1));

            document.newPage();

            p = new Paragraph("房屋买卖协议",h2);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.add(new Paragraph(40," ",b1));

            PdfPTable table = new PdfPTable(2);
            float f[] = {1,1};
            table.setWidths(f);
            table.setWidthPercentage(100);


            PdfPCell cel = new PdfPCell(new Paragraph("卖方:   " + contractContextMap.get("seller_name").getStringValue(),b1));
            cel.setBorder(0);
            table.addCell(cel);

            p = new Paragraph(20);

            p.add(new Phrase(pm(contractContextMap.get("seller_card_type").getStringValue(),3),bi));

            p.add(new Phrase("证号码:  " + contractContextMap.get("seller_card_number").getStringValue(),b1));



            cel = new PdfPCell(p);
            cel.setBorder(0);
            table.addCell(cel);

            cel = new PdfPCell(new Paragraph("买方:   " + contractContextMap.get("buyer_name").getStringValue(),b1));
            cel.setBorder(0);
            cel.setPaddingTop(20);
            table.addCell(cel);

            p = new Paragraph(20);

            p.add(new Phrase(pm(contractContextMap.get("buyer_card_name").getStringValue(),3),bi));

            p.add(new Phrase("证号码:  " + contractContextMap.get("buyer_card_number").getStringValue(),b1));


            cel = new PdfPCell(p);
            cel.setBorder(0);
            cel.setPaddingTop(20);
            table.addCell(cel);

            document.add(table);



            document.add(new Paragraph(" ",b1));

            document.add(new Paragraph("        根据《中华人民共和国城市房地产管理法》《物权法》等法律、法规规定，买卖双方就房屋买卖事宜，按照\"平等、自愿、合法\"的原则，签订本协议。",b2));

            document.add(new Paragraph(25,"        一、房屋的基本状况",h3));

            p = new Paragraph(20);
            p.add(new Phrase("        坐落:", b2));
            p.add(new Phrase(pm(contractContextMap.get("address").getStringValue(),50),bi));
            p.add(new Phrase("；产籍标示:",b2));
            p.add(new Phrase(pm(contractContextMap.get("map_number").getStringValue(),4),bi));
            p.add(new Phrase("图",b2));
            p.add(new Phrase(pm(contractContextMap.get("block_number").getStringValue(),4),bi));
            p.add(new Phrase("宅",b2));
            p.add(new Phrase(pm(contractContextMap.get("build_number").getStringValue(),4),bi));
            p.add(new Phrase("幢",b2));
            p.add(new Phrase(pm(contractContextMap.get("house_number").getStringValue(),4),bi));
            p.add(new Phrase("号；用途",b2));
            p.add(new Phrase(pm(contractContextMap.get("use_type").getStringValue(),10),bi));
            p.add(new Phrase("；结构:",b2));
            p.add(new Phrase(pm(contractContextMap.get("structure").getStringValue(),10),bi));
            p.add(new Phrase("；房屋总层数:",b2));
            p.add(new Phrase(pm(contractContextMap.get("floor_count").getStringValue(),4),bi));
            p.add(new Phrase("层;所在层数:",b2));
            p.add(new Phrase(pm(contractContextMap.get("in_floor").getStringValue(),4),bi));
            p.add(new Phrase("层；",b2));
            p.add(new Phrase(pm(contractContextMap.get("power_card_type").getStringValue(),4),bi));
            p.add(new Phrase("证号:",b2));
            p.add(new Phrase(pm(contractContextMap.get("power_card_number").getStringValue(),4),bi));
            p.add(new Phrase(",建筑面积:",b2));


            p.add(new Phrase(pm( dfArea.format(contractContextMap.get("house_area").getNumberValue()),4),bi));
            p.add(new Phrase("平方米,四至",b2));
            p.add(new Phrase(pm(contractContextMap.get("c1_1_1").getStringValue(),50),bi));
            p.add(new Phrase("。",b2));
            document.add(p);

            document.add(new Paragraph(25,"        二、房屋附属设施状况",h3));
            p = new Paragraph(20,"        ",b2);
            p.add(new Phrase(20,pm(contractContextMap.get("house_attr").getStringValue(),95),bi));
            document.add(p);


            p = new Paragraph(25);
            p.add(new Phrase("        三、房屋买卖成交价款为人民币(大写)",h3));
            p.add(new Phrase(pm(BigMoneyConverter.numberToBigMoney(contractContextMap.get("money").getNumberValue()),55),h3i));
            p.add(new Phrase("(小写)",h3));
            p.add(new Phrase(pm( dfMoney.format(contractContextMap.get("money").getNumberValue()),10),bi));
            p.add(new Phrase("元。",h3));

            document.add(p);


            document.add(new Paragraph(25,"        四、卖方保证所出售的房屋权属清楚，无抵押、查封等权利限制，无欠费。如有不实，由卖方承担责任。",h3));

            p = new Paragraph(25);
            p.add(new Phrase("        五、双方约定付款时间、方式、选择以下第",h3));
            p.add(new Phrase(pm(dfInt.format(contractContextMap.get("pay_type").getNumberValue()),5),h3i));
            p.add(new Phrase("条",h3));
            document.add(p);


            p = new Paragraph(20);
            p.add(new Phrase("        1、买卖双方签定本协议时，买方先交付房款人民币(大写):",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_1_1").getStringValue(),15),bi));
            p.add(new Phrase(",佘款人民币(大写):",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_1_2").getStringValue(),15),bi));
            p.add(new Phrase("在办理房屋转移登记，领取《不动产权证书》后付清。",b2));
            document.add(p);
            p = new Paragraph(20);
            p.add(new Phrase("        2、买卖双方签订本协议时，买方交付定金人民币(大写):",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_2_1").getStringValue(),15),bi));
            p.add(new Phrase("，在办理房屋转移登记，领取《不动产权证书》后定金转为购房款，购房款余额人民币(大写)",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_2_2").getStringValue(),15),bi));
            p.add(new Phrase("，买方一次性结情。",b2));
            document.add(p);

            document.add(new Paragraph(20,"        3、办理房屋转移登记，领取《不动产权证书》后买方一次性付清全部房款。",b2));

            p = new Paragraph(20);
            p.add(new Phrase("        4、买卖双方签订本协议，买方交付房款人民币(大写)",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_4_1").getStringValue(),15),bi));
            p.add(new Phrase(",余额人民币(大写)",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_4_2").getStringValue(),15),bi));
            p.add(new Phrase("，余款人民币(大写)",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_4_3").getStringValue(),15),bi));
            p.add(new Phrase("，由买方向银行贷款转付。",b2));
            document.add(p);
            document.add(new Paragraph(20,"        5、买卖双方签订本协议时，买方已将房款全部付清。",b2));
            p = new Paragraph(20,"        ",b2);
            p.add(new Phrase(20,pm(contractContextMap.get("c_5_5_1").getStringValue(),95),bi));
            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        6、",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_5_6_1").getStringValue(),91),bi));
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

            document.add(new Paragraph(25,"        六、房屋交付",h3));

            p = new Paragraph(20);
            p.add(new Phrase("        买卖双方约定于",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_6_1_1").getStringValue(),5),bi));
            p.add(new Phrase("年",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_6_1_2").getStringValue(),5),bi));
            p.add(new Phrase("月",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_6_1_3").getStringValue(),5),bi));
            p.add(new Phrase("日前，卖方将出售房屋交付给买方。",b2));
            document.add(p);


            document.add(new Paragraph(25,"        七、违约责任",h3));
            p = new Paragraph(20);
            p.add(new Phrase("        1、本协议签订生效后，若卖方违约使本协议终止履行，选择下列第",b2));

            p.add(new Phrase(pm(dfInt.format(contractContextMap.get("c_7_1_0").getNumberValue()),5),bi));
            p.add(new Phrase("种方式赔偿买方。",b2));
            document.add(p);
            document.add(new Paragraph(20,"        (1)买方所交定金的两倍(定金不超过房屋买卖成交价款的20%)",b2));


            p = new Paragraph(20);
            p.add(new Phrase("        (2)按房屋买卖成交价款",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_7_1_1").getStringValue(),5),bi));
            p.add(new Phrase("%的违约金。",b2));
            document.add(p);
            p = new Paragraph(20);
            p.add(new Phrase("        (3)",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_7_1_2").getStringValue(),91),bi));
            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        2、本协议生效后，若买方违约使本协议终止履行，选择下列第",b2));

            p.add(new Phrase(pm(dfInt.format(contractContextMap.get("c_7_2_0").getNumberValue()),5),bi));
            p.add(new Phrase("种方式赔偿卖方。",b2));
            document.add(p);
            document.add(new Paragraph(20,"        (1)买方所交定金归卖方。",b2));
            p = new Paragraph(20);
            p.add(new Phrase("        (2)按房屋买卖成交价款",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_7_2_1").getStringValue(),5),bi));
            p.add(new Phrase("%的违约金。",b2));
            document.add(p);
            p = new Paragraph(20);
            p.add(new Phrase("        (3)",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_7_2_2").getStringValue(),91),bi));
            document.add(p);

            p = new Paragraph(25);
            p.add(new Phrase("        八、  房屋交易税费，选择第",h3));
            p.add(new Phrase(pm(dfInt.format(contractContextMap.get("c_8_1_0").getNumberValue()),5),h3i));
            p.add(new Phrase("种方式交纳",h3));
            document.add(p);

            document.add(new Paragraph(20,"        1、依照法规规定双方各自承担应缴纳的税费。",b2));
            p = new Paragraph(20);
            p.add(new Phrase("        2．双方协商:",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_8_2_1").getStringValue(),80),bi));
            document.add(p);

            document.add(new Paragraph(25,"        九、买卖双方对出售的房屋已按规定对出售房屋结余的维修金进行交割，房屋维修基金不敷使用时，以买方按规定交纳。",h3));

            document.add(new Paragraph(25,"        十、双方约定的其他事项。",h3));


            p = new Paragraph(20,"        ",b2);
            p.add(new Phrase(pm(contractContextMap.get("c_5_6_1").getStringValue(),95),bi));
            document.add(p);

            p = new Paragraph(25);
            p.add(new Phrase("        十一、  在履行本协议过程中发生纠纷时，由买卖双方协商解决；如协商不成，按下述第",h3));
            p.add(new Phrase(pm(dfInt.format(contractContextMap.get("c_11_1_1").getNumberValue()),5),h3i));
            p.add(new Phrase("种方式解决:",h3));
            document.add(p);
            document.add(new Paragraph(20,"        1、提交市仲裁委员会促裁。",b2));
            document.add(new Paragraph(20,"        2、依法向人民法院起诉。",b2));

            p = new Paragraph(25);
            p.add(new Phrase("        十二、  本协议一式",h3));
            p.add(new Phrase(pm(contractContextMap.get("c_12_1_1").getStringValue(),5),h3i));
            p.add(new Phrase("份，具有同等法律效力，自双方签章之日生效，按以下方式持有:",h3));
            document.add(p);

            p = new Paragraph(20);
            p.add(new Phrase("        卖方",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_12_2_1").getStringValue(),5),bi));
            p.add(new Phrase("份，买方",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_12_2_2").getStringValue(),5),bi));
            p.add(new Phrase("份，",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_12_2_3").getStringValue(),15),bi));
            p.add(new Phrase("份，",b2));
            p.add(new Phrase(pm(contractContextMap.get("c_12_2_4").getStringValue(),15),bi));
            p.add(new Phrase("份。",b2));
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
