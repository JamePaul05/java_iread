package com.ifuture.iread.util;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.lowagie.text.html.HtmlWriter;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by maofn on 2017/3/30.
 * 生成pdf
 */
public class GeneratePdfUtil {

    public static void html2Pdf(String basePath, String html, String[] base64s) throws Exception {
        //输出位置
        String baseUrl = basePath + Constants.IMG_BASE_URL;
        String pdfUrl = baseUrl + Constants.PDF_NAME;
        String htmlUrl = baseUrl + "qrcode.html";
        //pdf输出流
        OutputStream os = new BufferedOutputStream(new FileOutputStream(pdfUrl));
        //预处理html
        html = preRenderHtml(html);
        OutputStream os2 = new BufferedOutputStream(new FileOutputStream(htmlUrl));
        os2.write(html.getBytes());
        os2.flush();
        os2.close();

        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, os);

        document.open();

        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlUrl), Charset.forName("cp1252"));

        document.close();
        /*//渲染器
        ITextRenderer renderer = new ITextRenderer();
        //预处理html
        html = preRenderHtml(html, base64s);

        renderer.setDocumentFromString(html);
        //设置相对路径，解决图片的相对路径问题
        renderer.getSharedContext().setBaseURL("file:" + basePath + Constants.IMG_BASE_URL);
        renderer.layout();
        renderer.createPDF(os);
        os.close();*/


    }

    /**
     * html预处理
     * 有许多规范
     * @param html
     * @return
     */
    /*
     * 头部必须是这样
     * <?xml version="1.0" encoding="UTF-8"?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html xmlns="http://www.w3.org/1999/xhtml">
        所有的标签必须闭合
    * */
    private static String preRenderHtml(String html) {
        html = html.replace("></div><p class=\"\" id=\"bianhao", "/></div><p class=\"\" id=\"bianhao");
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        sb.append("<style type=\"text/css\"></style>");
        sb.append("</head><body>");
        sb.append(html);
        sb.append("</body></html>");
        System.out.println(sb);
        return sb.toString();
        /*html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>\n" +
                "    <style type=\"text/css\">\n" +
                "\t\tbody, html {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t\theight: 100%;\n" +
                "\t\t}\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<img src=\"1.png\" style=\"width: 100%; height: 100%\"></img>\n" +
                "</body>\n" +
                "</html>";
        return html;*/
    }

    public void html2Pdf(String basePath, String html) throws IOException, DocumentException {
        //预处理html
        html = preRenderHtml(html);
//        String htmlUrl = basePath + "qrcode.html";
//        OutputStream os2 = new BufferedOutputStream(new FileOutputStream(htmlUrl));
//        os2.write(html.getBytes());
//        os2.flush();
//        os2.close();
        //输出位置
       final String baseUrl = basePath + Constants.IMG_BASE_URL;
        String pdfUrl = baseUrl + Constants.PDF_NAME;
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfUrl));

        document.open();

        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);

        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.setImageProvider(new AbstractImageProvider() {
            @Override
            public String getImageRootPath() {
                return baseUrl;
            }
        });

        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, htmlPipeline);

        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new ByteArrayInputStream(html.getBytes()));

        document.close();
    }



}
