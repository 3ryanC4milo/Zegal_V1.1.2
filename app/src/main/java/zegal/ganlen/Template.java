package zegal.ganlen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Template {

    private Context context;
    private File pdfFile;
    private String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    private Document documento;
    private PdfWriter pdfWriter;
    private Paragraph parrafo;
    private Font fClausulas = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private Font fTexto = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);


    public Template(Context context)
    {
        this.context = context;
    }

    public void abrePDF()
    {
        crearPDF();
        try {
            documento = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(pdfFile));
            documento.open();

        }catch (Exception e){
            Log.e("abrePDF", e.toString());
        }
    }

    private void crearPDF()
    {
        File folder = new File(Environment.getExternalStorageDirectory().toString(),"Contrato_Servicios");

        if(!folder.exists())
            folder.mkdir();

        pdfFile = new File(folder,"contrato_de_servicios_"+pic_name+".pdf");
    }

    public void cierraPDF()
    {
        documento.close();
    }

    public void agregaData(String tit, String tem, String autor)
    {
        documento.addTitle(tit);
        documento.addSubject(tem);
        documento.addAuthor(autor);
    }

    public void agregaClausula(String sub)
    {
        try {
            parrafo = new Paragraph();
            addChildC(new Paragraph(sub, fClausulas));
            parrafo.setSpacingBefore(5);
            parrafo.setSpacingAfter(5);
            documento.add(parrafo);
        } catch (Exception e){
            Log.e("agregaClausula", e.toString());
        }
    }

    private void addChildC(Paragraph childParagraph)
    {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        childParagraph.add(childParagraph);
    }

    public void addPrimer(String pri)
    {
        try{
            parrafo = new Paragraph();
            addChildP(new Paragraph(pri, fTexto));
            parrafo.setSpacingAfter(5);
            documento.add(parrafo);
        } catch (Exception e)
        {
            Log.e("addPrimer", e.toString());
        }
    }

    private void addChildP(Paragraph chiParagraph)
    {
        chiParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        chiParagraph.add(chiParagraph);
    }

    public void addTexto(String pri)
    {
        try{
            parrafo = new Paragraph();
            addChildT(new Paragraph(pri, fTexto));
            parrafo.setSpacingBefore(5);
            parrafo.setSpacingAfter(5);
            documento.add(parrafo);
        } catch (Exception e)
        {
            Log.e("addTexto", e.toString());
        }
    }

    private void addChildT(Paragraph chiParagraph)
    {
        chiParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        chiParagraph.add(chiParagraph);
    }

    public void crearFirmero(String []header)
    {
        try
        {
            parrafo = new Paragraph();
            parrafo.setFont(fTexto);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            parrafo.setSpacingBefore(30);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int indC =0;
            while (indC<header.length)
            {
                pdfPCell = new PdfPCell(new Phrase(header[indC++], fTexto));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setBorderColor(BaseColor.WHITE);
                pdfPCell.setBackgroundColor(BaseColor.WHITE);

            }
            parrafo.setSpacingAfter(70);
            parrafo.add(pdfPTable);
            documento.add(parrafo);

        } catch (Exception e)
        {
            Log.e("crearFirmero", e.toString());
        }
    }

    public void TerminarFirmero(String []header)
    {
        try
        {
            parrafo = new Paragraph();
            parrafo.setFont(fTexto);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int indC =0;
            while (indC<header.length)
            {
                pdfPCell = new PdfPCell(new Phrase(header[indC++], fTexto));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setBorderColorTop(BaseColor.BLACK);
                pdfPCell.setBorderColorLeft(BaseColor.WHITE);
                pdfPCell.setBorderColorBottom(BaseColor.WHITE);
                pdfPCell.setBorderColorRight(BaseColor.WHITE);
                pdfPCell.setBackgroundColor(BaseColor.WHITE);

            }
            parrafo.add(pdfPTable);
            documento.add(parrafo);

        } catch (Exception e)
        {
            Log.e("TerminarFirmero", e.toString());
        }
    }

    public void verPDF()
    {
        context.startActivity(new Intent(context, PDFViewerActivity.class)
        .putExtra("path", pdfFile.getAbsolutePath())
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
