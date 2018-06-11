package com.ifuture.iread.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maofn on 2017/5/25.
 */
public class DrawPdfUtil {

    private String basePath;

    private int maxPerRow;

    private String[] images;

    private String[] contents;

    public DrawPdfUtil(String basePath, String[] images, String[] contents, int maxPerRow) {
        this.basePath = basePath;
        this.maxPerRow = maxPerRow;
        this.images = images;
        this.contents = contents;
    }

    public void draw() throws IOException, DocumentException {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(basePath + File.separator  + Constants.PDF_NAME));

        document.open();

        PdfPTable table = new PdfPTable(maxPerRow);
        //设置表格相对每页面的宽度为100%
        table.setWidthPercentage(100);
        //设置表格宽度为100%
        table.setTotalWidth(100);
        //要显示的图片个数
        int imagesNum = images.length;
        //因为pdftable在不满一row时不会显示
        int forNum = imagesNum + maxPerRow - 1;
        for (int i = 0; i < forNum; i++) {
            //如果还在图片范围内
            if (i < imagesNum) {
                PdfPCell cell = new PdfPCell();
                //设置固定高度
                cell.setFixedHeight(130);
                //设置边框宽度为0
                cell.setBorderWidth(0);
                //设置图片
                ImageEvent imageEvent = new ImageEvent(Image.getInstance(basePath + File.separator + images[i]));
                cell.setCellEvent(imageEvent);
                //设置文字
                cell.setCellEvent(new PositionEvent(new Phrase(14, contents[i]), 0.5f, 0, Element.ALIGN_CENTER));
                table.addCell(cell);
            } else {
                PdfPCell cell = new PdfPCell(new Phrase(""));
                //设置边框宽度为0
                cell.setBorderWidth(0);
                table.addCell(cell);
            }
        }

        document.add(table);

        document.close();

    }

    private class ImageEvent implements PdfPCellEvent {
        protected Image img;
        public ImageEvent(Image img) {
            this.img = img;
        }
        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            //设置图片缩放比例
            img.scalePercent(90, 90);
            //计算左右间距
            float margin_left = (cell.getWidth() - img.getScaledWidth()) / 2;
            //设置绝对位置
            img.setAbsolutePosition(position.getLeft() + margin_left, position.getTop(10) - img.getHeight());
            PdfContentByte canvas = canvases[PdfPTable.BACKGROUNDCANVAS];
            try {
                canvas.addImage(img);
            } catch (DocumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class PositionEvent implements PdfPCellEvent {
        protected Phrase content;
        protected float wPct;
        protected float hPct;
        protected int alignment;

        public PositionEvent(Phrase content, float wPct, float hPct, int alignment) {
            this.content = content;
            this.wPct = wPct;
            this.hPct = hPct;
            this.alignment = alignment;
        }

        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
            //计算出相对位置（应该是相对页面）
            float x = position.getLeft() + wPct * position.getWidth();
            float y = position.getBottom() + hPct * (position.getHeight() - content.getLeading());
            ColumnText.showTextAligned(canvas, alignment, content, x, y, 0);
        }
    }
}
