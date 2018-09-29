package zegal.ganlen;

import android.content.Context;
import android.content.Intent;

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
            documento = new Document(PageSize.A4, 56f,56f,56f,56f);
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

    public void verPDF()
    {
        context.startActivity(new Intent(context, PDFViewerActivity.class)
                .putExtra("path", pdfFile.getAbsolutePath())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void agregarParrafo(String par)
    {
        try
        {
            parrafo = new Paragraph();
            addChiidP(new Paragraph(par, fTexto));
            parrafo.setSpacingAfter(12);
            documento.add(parrafo);
        }
        catch (Exception e)
        {
            Log.e("agregarParrafo", e.toString());
        }
    }

    private void addChiidP(Paragraph chilParagraph)
    {
        chilParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        parrafo.add(chilParagraph);
    }

    public void addClausula(String cl)
    {
        try
        {
        parrafo = new Paragraph(cl, fClausulas);
        parrafo.setAlignment(Element.ALIGN_CENTER);
        parrafo.setSpacingAfter(12);
        documento.add(parrafo);
        }
        catch (Exception e)
        {
            Log.e("addClausula", e.toString());
        }

    }

    public void crearFirmero(String[] header) {
        try
        {
            parrafo = new Paragraph();
            parrafo.setFont(fTexto);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setSpacingBefore(12);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;

            int indexC = 0;

            while (indexC < header.length) {
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fTexto));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                pdfPCell.setBackgroundColor(BaseColor.WHITE);
                pdfPCell.setBorderColor(BaseColor.WHITE);
                pdfPTable.addCell(pdfPCell);
            }
            parrafo.add(pdfPTable);
            documento.add(parrafo);
        }
        catch (Exception e)
        {
            Log.e("crearFirmero", e.toString());
        }
    }

    public void crearFirmero2(String[] header) {
        try
        {
            parrafo = new Paragraph();
            parrafo.setFont(fTexto);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setSpacingBefore(70);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;

            int indexC = 0;

            while (indexC < header.length) {
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fTexto));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                pdfPCell.setBackgroundColor(BaseColor.WHITE);
                pdfPCell.setUseVariableBorders(true);
                pdfPCell.setBorderColorLeft(BaseColor.WHITE);
                pdfPCell.setBorderColorRight(BaseColor.WHITE);
                pdfPCell.setBorderColorBottom(BaseColor.WHITE);
                if(indexC == 2){
                    pdfPCell.setBorderColorTop(BaseColor.WHITE);
                }
                else{
                    pdfPCell.setBorderColorTop(BaseColor.BLACK);
                }
                pdfPTable.addCell(pdfPCell);
            }
            parrafo.add(pdfPTable);
            documento.add(parrafo);
        }
        catch (Exception e)
        {
            Log.e("crearFirmero", e.toString());
        }
    }

    public void nuevaHoja()
    {
        documento.newPage();
    }

}
