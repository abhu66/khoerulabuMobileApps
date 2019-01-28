package com.khoerulabu.pulsaapp.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.khoerulabu.pulsaapp.model.Transaction;

import java.text.DecimalFormat;
import java.util.Objects;

public class TableBuilder {

    public static PdfPTable createTable(Transaction transaction) throws DocumentException {

        // create 6 column table
        PdfPTable table = new PdfPTable(3);

        // set the width of the table to 100% of page
        table.setWidthPercentage(100);

        // set relative columns width
        table.setWidths(new float[]{0.6f,0.1f, 0.8f});

        // ----------------Table Header "Title"----------------
        // font
        Font font = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
        // create header cell
        PdfPCell cell = new PdfPCell(new Phrase("TRANSACTION DETAIL",font));
        // set Column span "1 cell = 3 cells width"
        cell.setColspan(3);
        // set style
        Style.headerCellStyle(cell);
        // add to table
        table.addCell(cell);

        //-----------------Table Cells Label/Value------------------
        DecimalFormat decimal = new DecimalFormat("#,###.00");
        // 1st Row
        table.addCell(createCustomeCell("User id"));
        table.addCell(createSeparatorCell());
        table.addCell(createCustomeCell(String.valueOf(Objects.requireNonNull(transaction.getUsers()).getId())));

        table.addCell(createCustomeCell("Transaction id"));
        table.addCell(createSeparatorCell());
        table.addCell(createCustomeCell(String.valueOf(transaction.getId())));
        // 2nd Row
        table.addCell(createCustomeCell("Phone number"));
        table.addCell(createSeparatorCell());
        table.addCell(createCustomeCell(String.valueOf(transaction.getPhoneNumber())));
        // row
        table.addCell(createCustomeCell("Operator"));
        table.addCell(createSeparatorCell());
        table.addCell(createCustomeCell(Objects.requireNonNull(transaction.getOperator()).getOperatorName()));
        // row
        table.addCell(createCustomeCell("Pulsa"));
        table.addCell(createSeparatorCell());
        table.addCell(createCustomeCell(String.valueOf(Objects.requireNonNull(Objects.requireNonNull(transaction.getVoucher()).getPulsaId()).getPulsa())));

        // row
        table.addCell(createCustomeCell("Price"));
        table.addCell(createSeparatorCell());
        table.addCell(createCustomeCell(decimal.format(transaction.getVoucher().getHargaVoucher())));

        table.addCell(createCustomeCell("Transaction Date"));
        table.addCell(createSeparatorCell());
        table.addCell(createCustomeCell(transaction.getCreatedDate()));

        return table;
    }

    // create cells
    private static PdfPCell createCustomeCell(String text){
        // font
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));

        // set style
        Style.labelCellStyle(cell);
        return cell;
    }

    // create cells sparator eg ":"
    private static PdfPCell createSeparatorCell(){
        // font
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(":",font));

        // set style
        Style.valueCellStyle(cell);
        return cell;
    }
}
