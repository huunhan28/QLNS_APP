package com.example.food.feature.pdf;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class PdfUtil {

   public Font bfBold12, bf12, titleFont;
    public BaseColor colorAccent;
    public float fontSize;
    public float valueFontSize;
    public BaseFont fontName;



    public PdfUtil(){

        colorAccent = new BaseColor(0, 153, 204, 255);
        fontSize = 20.0f;
        valueFontSize = 26.0f;
        try {
            fontName = BaseFont.createFont("assets/font/poppins_regular.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 36.0f, Font.NORMAL, BaseColor.BLACK);
        bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(0, 0, 0));
        bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 18);
    }

    public Document createDocument(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }

        try {
            Document document = new Document();
            // save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.setMargins(50f, 5f, 5f, 10f);
            document.addCreationDate();
            document.addAuthor("TNT_HIEN");
            document.addCreator("Hien Nguyen");

            document.close();
            return document;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addNewItem(Document document, String text, int align, Font font) {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public PdfPTable createTableHeader(Document document,
                                  Font header, Font data,
                                  float[] columnWidths,
                                  String[] headers) {
        //create PDF table with the given widths
        PdfPTable table = new PdfPTable(columnWidths);
        // set table width a percentage of the page width
        table.setWidthPercentage(100f);

        //insert column headings

        Arrays.stream(headers)
                .forEach(s -> {
                    insertCell(table, s, Element.ALIGN_CENTER, 1, header);
                });

        table.setHeaderRows(1);
        return table;
    }


    public void printPDF(Context context) {
        PrintManager printManager = (PrintManager)context.getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(context,
                    Common.getAppPath(context) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception e) {
            Log.d("TNT_HIEN:", "" + e.getMessage());
        }
    }

    public void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }
}

