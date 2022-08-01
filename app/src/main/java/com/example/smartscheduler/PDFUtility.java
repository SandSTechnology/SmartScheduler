package com.example.smartscheduler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smartscheduler.model.CourseModel;
import com.example.smartscheduler.model.TimeTableWithFacultyModel;
import com.example.smartscheduler.util.TimeSlots;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public final class PDFUtility {
    private static ArrayList<TimeTableWithFacultyModel> list = new ArrayList<>();
    private static final String TAG = PDFUtility.class.getSimpleName();
    private static final Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

    private static final Font FONT_CELL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static final Font FONT_COLUMN = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);

    private static ArrayList<String> TimeSlotList = new ArrayList<>();
    private static ArrayList<CourseModel> CoursesList = new ArrayList<>();

    public static void createPdf(@NonNull Context mContext, OnDocumentClose mCallback, @NonNull String filePath, boolean isPortrait, ArrayList<TimeTableWithFacultyModel> l) throws Exception {
        if (filePath.equals("")) {
            throw new NullPointerException("PDF File Name can't be null or blank. PDF File can't be created");
        }

        File file = new File(filePath);

        //TimeSlot List
        TimeSlotList.add(TimeSlots.SLOT1.toString());
        TimeSlotList.add(TimeSlots.SLOT2.toString());
        TimeSlotList.add(TimeSlots.SLOT3.toString());
        TimeSlotList.add(TimeSlots.SLOT4.toString());
        TimeSlotList.add(TimeSlots.SLOT5.toString());
        TimeSlotList.add(TimeSlots.SLOT6.toString());

        list = l;
        if (file.exists()) {
            file.delete();
            Thread.sleep(50);
        }

        Document document = new Document();
        document.setMargins(24f, 24f, 32f, 32f);
        document.setPageSize(isPortrait ? PageSize.A4 : PageSize.A4.rotate());

        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        pdfWriter.setFullCompression();
        //pdfWriter.setPageEvent(new PageNumeration());

        document.open();

        //setMetaData(document);

        addHeader(mContext, document);
        addEmptyLine(document, 3);

        addDateAndDay(mContext, document);
        //addEmptyLine(document, 3);

        document.add(createDataTable());

        addEmptyLine(document, 2);

        document.add(createCourseTable());

        addEmptyLine(document, 2);

        //TotalInWords(mContext, document);
        //  AddLink(mContext, document);
        // PdfWriter pdfWriter1 = null;
        //onEndPage(mContext, pdfWriter1, document);
        //document.add(createSignBox());

        document.close();

        try {
            pdfWriter.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error While Closing pdfWriter : " + ex);
        }

        if (mCallback != null) {
            mCallback.onPDFDocumentClose(file);
        }
    }

    private static void TotalInWords(Context mContext, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell cell;

        /*MIDDLE TEXT*/
        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(8f);
            cell.setUseAscender(true);

            /*Paragraph temp = new Paragraph("Total in Words : " + cashHistory.getTotalInWords(), new Font(Font.FontFamily.TIMES_ROMAN, 16));
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);*/

            table.addCell(cell);
        }

        document.add(table);
    }

    private static void addDateAndDay(Context mContext, Document document) throws DocumentException {
        /*if (cashHistory.getDate()==null ||cashHistory.getDate().equals(""))
        {
            return;
        }*/
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell cell;

        /*MIDDLE TEXT*/
        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(8f);
            cell.setUseAscender(true);
            Paragraph temp = new Paragraph("");

            /*if (cashHistory.getDate() != null && !cashHistory.getDate().equals("")
                    && cashHistory.getDay() != null && !cashHistory.getDay().equals(""))
            {
                temp = new Paragraph("Date : " + cashHistory.getDate() +","+ cashHistory.getDay() ,new Font(Font.FontFamily.TIMES_ROMAN, 16));
            }
            else if (cashHistory.getDate() != null && !cashHistory.getDate().equals("")
                    && cashHistory.getDay() == null || cashHistory.getDay().equals(""))
            {
                temp = new Paragraph("Date : " + cashHistory.getDate() ,new Font(Font.FontFamily.TIMES_ROMAN, 16));
            }*/
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);

            table.addCell(cell);
        }

        document.add(table);
    }

    private static void AddLink(Context mContext, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);


        PdfPCell cell;

        /*MIDDLE TEXT*/
        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(8f);
            cell.setUseAscender(true);

            /*Anchor anchor = new Anchor(new Phrase(mContext.getString(R.string.appurl), new Font(Font.FontFamily.TIMES_ROMAN, 14)));
            anchor.setReference(mContext.getString(R.string.appurl));
            cell = new PdfPCell(anchor);*/
            cell.setBorder(0);
            cell.setPadding(2f);
            table.addCell(cell);
            PdfWriter writer = null;
            table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 5, writer.getDirectContent());

            // Paragraph temp = new Paragraph(mContext.getString(R.string.appurl), new Font(Font.FontFamily.TIMES_ROMAN, 14));
            // temp.setAlignment(Element.ALIGN_CENTER);
           /* Phrase phrase = new Phrase();
            Chunk chunk = new Chunk("https://play.google.com/store/apps/details?id=com.cash.calculator.gk",new Font(Font.FontFamily.TIMES_ROMAN,14));
            chunk.setAnchor("https://play.google.com/store/apps/details?id=com.cash.calculator.gk");
            phrase.add((Element) chunk);
            Paragraph paragraph = new Paragraph();
            paragraph.add((Element) phrase);
            paragraph.setPaddingTop(-10.0f);
            paragraph.setAlignment(1);
            try {
                document.add(new Paragraph("\n"));
                document.add(paragraph);
            } catch (DocumentException e3) {
                e3.printStackTrace();
            }
            try {
                document.add(new Paragraph("\n"));
            } catch (DocumentException e4) {
                e4.printStackTrace();
            }*/
            //  cell.addElement(temp);

        }

        document.add(table);
    }

    private static void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    public interface OnDocumentClose {
        void onPDFDocumentClose(File file);
    }

    private static void setMetaData(Document document) {
        document.addCreationDate();
        //document.add(new Meta("",""));
        document.addAuthor("");
        document.addCreator("");
        document.addHeader("", "");
    }

    private static void addHeader(Context mContext, Document document) throws Exception {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;

        /*LEFT TOP LOGO
        {
            Drawable d= ContextCompat.getDrawable(mContext, R.drawable.resized_logo);
            Bitmap bmp=((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG,100,stream);

            Image logo=Image.getInstance(stream.toByteArray());
            logo.setWidthPercentage(80);
            logo.scaleToFit(105,55);

            cell = new PdfPCell(logo);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(2f);
            table.addCell(cell);
        }*/

        /*MIDDLE TEXT*/
        {
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(8f);
            cell.setUseAscender(true);

            Paragraph temp = new Paragraph("Timetable", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, new BaseColor(25, 199, 90)));
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            temp = new Paragraph("", FONT_SUBTITLE);
            temp.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(temp);

            table.addCell(cell);
        }

        /* RIGHT TOP LOGO
        {
            PdfPTable logoTable=new PdfPTable(1);
            logoTable.setWidthPercentage(100);
            logoTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            logoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

            Drawable drawable=ContextCompat.getDrawable(mContext, R.drawable.resized_logo);
            Bitmap bmp =((BitmapDrawable)drawable).getBitmap();

            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
            Image logo=Image.getInstance(stream.toByteArray());
            logo.setWidthPercentage(80);
            logo.scaleToFit(38,38);

            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setBorder(PdfPCell.NO_BORDER);

            logoTable.addCell(logoCell);

            logoCell = new PdfPCell(new Phrase("Cash Counter",FONT_CELL));
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setBorder(PdfPCell.NO_BORDER);
            logoCell.setPadding(4f);
            logoTable.addCell(logoCell);

            cell = new PdfPCell(logoTable);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(2f);
            table.addCell(cell);
        }*/

        //Paragraph paragraph=new Paragraph("",new Font(Font.FontFamily.TIMES_ROMAN, 2.0f, Font.NORMAL, BaseColor.BLACK));
        //paragraph.add(table);
        //document.add(paragraph);
        document.add(table);
    }

    private static Element createDataTable() throws DocumentException {
        PdfPTable table1 = new PdfPTable(6);
        table1.setWidthPercentage(100);
        table1.setWidths(new float[]{1f, 1f, 1f, 1f, 1f, 1f});
        table1.setHeaderRows(1);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            cell = new PdfPCell(new Phrase("Time", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Monday", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Tuesday", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Wednesday", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Thursday", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Friday", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);
        }

        float top_bottom_Padding = 8f;
        float left_right_Padding = 4f;
        boolean alternate = false;

        BaseColor lt_gray = new BaseColor(221, 221, 221); //#DDDDDD
        BaseColor cell_color;

        int size = list.size();

        CourseModel courseModel;
        CoursesList.clear();
        for (int subj = 0; subj < size; subj++) {
            courseModel = new CourseModel(list.get(subj).getFaculty().trim().toUpperCase(Locale.ROOT),list.get(subj).getCourse().trim().toUpperCase(Locale.ROOT),"");
            if (!CoursesList.contains(courseModel))
            {
                CoursesList.add(courseModel);
            }
        }

        for (int i = 0; i < 6; i++) {
            String MondaySubject = "---";
            String TuesdaySubject = "---";
            String WednesdaySubject = "---";
            String ThursdaySubject = "---";
            String FridaySubject = "---";

            String MondayROOM = "";
            String TuesdayROOM = "";
            String WednesdayROOM = "";
            String ThursdayROOM = "";
            String FridayROOM = "";

            for (int subj = 0; subj < size; subj++) {
                if (list.get(subj).getTimeslot().equalsIgnoreCase(TimeSlotList.get(i))
                        && list.get(subj).getDay().equalsIgnoreCase("MONDAY"))
                    MondaySubject = list.get(subj).getCourse() + "\n" + "Room # "+ list.get(subj).getRoom()
                            + "\n" + "Block # "+ list.get(subj).getBlock_Num()
                            + "\n" + "Floor # "+ list.get(subj).getFloor_num();

                if (list.get(subj).getTimeslot().equalsIgnoreCase(TimeSlotList.get(i))
                        && list.get(subj).getDay().equalsIgnoreCase("TUESDAY"))
                    TuesdaySubject = list.get(subj).getCourse()+ "\n" + "Room # "+ list.get(subj).getRoom()
                            + "\n" + "Block # "+ list.get(subj).getBlock_Num()
                            + "\n" + "Floor # "+ list.get(subj).getFloor_num();;

                if (list.get(subj).getTimeslot().equalsIgnoreCase(TimeSlotList.get(i))
                        && list.get(subj).getDay().equalsIgnoreCase("WEDNESDAY"))
                    WednesdaySubject = list.get(subj).getCourse()+ "\n" + "Room # "+ list.get(subj).getRoom()
                            + "\n" + "Block # "+ list.get(subj).getBlock_Num()
                            + "\n" + "Floor # "+ list.get(subj).getFloor_num();;

                if (list.get(subj).getTimeslot().equalsIgnoreCase(TimeSlotList.get(i))
                        && list.get(subj).getDay().equalsIgnoreCase("THURSDAY"))
                    ThursdaySubject = list.get(subj).getCourse()+ "\n" + "Room # "+ list.get(subj).getRoom()
                            + "\n" + "Block # "+ list.get(subj).getBlock_Num()
                            + "\n" + "Floor # "+ list.get(subj).getFloor_num();;

                if (list.get(subj).getTimeslot().equalsIgnoreCase(TimeSlotList.get(i))
                        && list.get(subj).getDay().equalsIgnoreCase("FRIDAY"))
                    FridaySubject = list.get(subj).getCourse()+ "\n" + "Room # "+ list.get(subj).getRoom()
                            + "\n" + "Block # "+ list.get(subj).getBlock_Num()
                            + "\n" + "Floor # "+ list.get(subj).getFloor_num();;
            }

            cell_color = alternate ? lt_gray : BaseColor.WHITE;
            //TimeTableWithFacultyModel temp = list.get(i);

            cell = new PdfPCell(new Phrase(TimeSlotList.get(i), FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(MondaySubject, FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(TuesdaySubject, FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(WednesdaySubject, FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(ThursdaySubject, FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(FridaySubject, FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            alternate = !alternate;
        }

        return table1;
    }

    private static Element createCourseTable() throws DocumentException {
        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(100);
        table1.setWidths(new float[]{1f, 1f});
        table1.setHeaderRows(1);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            cell = new PdfPCell(new Phrase("Course Name", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Teacher Name", FONT_TITLE));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4f);
            table1.addCell(cell);
        }

        float top_bottom_Padding = 8f;
        float left_right_Padding = 4f;
        boolean alternate = false;

        BaseColor lt_gray = new BaseColor(221, 221, 221); //#DDDDDD
        BaseColor cell_color;

        int size = CoursesList.size();

        for (int i = 0; i < size; i++) {

            cell_color = alternate ? lt_gray : BaseColor.WHITE;

            cell = new PdfPCell(new Phrase(CoursesList.get(i).getCourse(), FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(CoursesList.get(i).getProfName(), FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            alternate = !alternate;
        }

        return table1;
    }

    private static Element createSignBox() throws DocumentException {
        PdfPTable outerTable = new PdfPTable(1);
        outerTable.setWidthPercentage(100);
        outerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        PdfPTable innerTable = new PdfPTable(2);
        {
            innerTable.setWidthPercentage(100);
            innerTable.setWidths(new float[]{1, 1});
            innerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            //ROW-1 : EMPTY SPACE
            PdfPCell cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(60);
            innerTable.addCell(cell);

            //ROW-2 : EMPTY SPACE
            cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(60);
            innerTable.addCell(cell);

            //ROW-3 : Content Left Aligned
            cell = new PdfPCell();
            Paragraph temp = new Paragraph(new Phrase("Signature of Supervisor", FONT_SUBTITLE));
            cell.addElement(temp);

            temp = new Paragraph(new Phrase("( RAVEESH G S )", FONT_SUBTITLE));
            temp.setPaddingTop(4f);
            temp.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(temp);

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(4f);
            innerTable.addCell(cell);

            //ROW-4 : Content Right Aligned
            cell = new PdfPCell(new Phrase("Signature of Staff ", FONT_SUBTITLE));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(4f);
            innerTable.addCell(cell);
        }

        PdfPCell signRow = new PdfPCell(innerTable);
        signRow.setHorizontalAlignment(Element.ALIGN_LEFT);
        signRow.setBorder(PdfPCell.NO_BORDER);
        signRow.setPadding(4f);

        outerTable.addCell(signRow);

        return outerTable;
    }

    private static Image getImage(byte[] imageByte, boolean isTintingRequired) throws Exception {
        Paint paint = new Paint();
        if (isTintingRequired) {
            paint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN));
        }
        Bitmap input = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(input, 0, 0, paint);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        output.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.setWidthPercentage(80);
        return image;
    }

    private static Image getBarcodeImage(PdfWriter pdfWriter, String barcodeText) {
        Barcode128 barcode = new Barcode128();
        //barcode.setBaseline(-1); //BARCODE TEXT ABOVE
        barcode.setFont(null);
        barcode.setCode(barcodeText);
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setTextAlignment(Element.ALIGN_BASELINE);
        return barcode.createImageWithBarcode(pdfWriter.getDirectContent(), BaseColor.BLACK, null);
    }


}
