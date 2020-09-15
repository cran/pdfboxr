package interfaces;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.io.MemoryUsageSetting;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


public class PdfExtractor extends PDFTextStripper {
    public PdfExtractor() throws IOException {}

    public static Boolean adjust_direction = false;
    public static Boolean font_size_in_pt = false;
    public static int page_id = 0;
    public static PDDocument document = null;
    public static MemoryUsageSetting memory_options = null;

    // pid rotate x0 y0  x1  y1
    public static VectorList df = null;
    public static VectorList meta = null;

    public static void set_memory_options (int max_main_memory, String temp_dir) {
        System.setProperty("pdfbox.fontcache", temp_dir);
        if (max_main_memory > 0) {
           // setupMixed(long maxMainMemoryBytes, long maxStorageBytes) 
           memory_options = MemoryUsageSetting.setupMixed((long) max_main_memory * 1024 * 1024, -1).setTempDir(new File(temp_dir));
        } else {
           memory_options = MemoryUsageSetting.setupMainMemoryOnly();
        }
    }

    public static void load(String pdf_file, String password) throws InvalidPasswordException, IOException {
        if ( password.length() > 0 ) {
            document = PDDocument.load( new File(pdf_file), password, memory_options );
        } else {
            document = PDDocument.load( new File(pdf_file), memory_options );
        }
    }

    public static int read_chars(int[] pages, String adj, String pt) throws IOException {
        df = new VectorList();
        meta = new VectorList();

        adjust_direction = Boolean.parseBoolean(adj);
        font_size_in_pt = Boolean.parseBoolean(pt);
        try {
            PDFTextStripper stripper = new PdfExtractor();
            stripper.setSortByPosition( true );
            // The indexing is done as in pdfbox.tools.ExtractText.java => extractPages
            for (int k = 0; k < pages.length; k++) {
                page_id = pages[k];
                PDPage page = document.getPage(page_id - 1);
                meta.append("pid", page_id);
                meta.append("rotation", page.getRotation());
                meta.append("x0", page.getBBox().getLowerLeftX());
                meta.append("y0", page.getBBox().getLowerLeftY());
                meta.append("x1", page.getBBox().getUpperRightX());
                meta.append("y1", page.getBBox().getUpperRightY());

                stripper.setStartPage( page_id );
                stripper.setEndPage( page_id );
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                stripper.writeText(document, dummy);
            }
        } finally {};

        return(0);
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        
        for (TextPosition text : textPositions) {

            df.append("pid", page_id);
            df.append("text", text.getUnicode());
            df.append("font", text.getFont().getName());

            if ( font_size_in_pt ) {
                df.append("size", text.getFontSizeInPt());
            } else {
                df.append("size", text.getFontSize());
            }
            
            if ( adjust_direction ) {
                df.append("x0", text.getXDirAdj());
                df.append("y0", text.getYDirAdj());
                df.append("height", text.getHeightDir());
                df.append("width", text.getWidthDirAdj());
            } else {
                df.append("x0", text.getX());
                df.append("y0", text.getY());
                df.append("height", text.getHeight());
                df.append("width", text.getWidth());
            }

            df.append("x1", text.getEndX());
            df.append("y1", text.getEndY());
            df.append("xscale", text.getXScale());
            df.append("yscale", text.getYScale());
            df.append("rotation", text.getDir());
            df.append("space_width", text.getWidthOfSpace());
        }
    }

    public static int read_text(int[] pages) throws IOException {
        df = new VectorList();
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            for (int k = 0; k < pages.length; k++) {
                page_id = pages[k];
                stripper.setStartPage( page_id );
                stripper.setEndPage( page_id );
                df.append("pid", page_id);
                df.append("text", stripper.getText(document));
            }
        } finally {};

        return(0);
    }

    public static int number_of_pages() {
        int npages = -1;
        try {
            npages = document.getNumberOfPages();
        } finally {};
        return(npages);
    }

    public static PDDocumentInformation document_info() {
        return(document.getDocumentInformation());
    }

    public static float version() {
        return(document.getVersion());
    }

    public static void close_document() throws IOException {
        try {
            document.close();
        } finally {};
    }

    public static IntegerVector test_int() {
        IntegerVector x = new IntegerVector();
        x.add(1);
        x.add(2);
        return x;
    }

    public static NumericVector test_dbl() {
        NumericVector x = new NumericVector();
        x.add(1.1);
        x.add(2.2);
        return x;
    }

    public static CharacterVector test_char() {
        CharacterVector x = new CharacterVector();
        x.add("1");
        x.add("2");
        return x;
    }

    public static VectorList test_vecli() {
        VectorList x = new VectorList();
        x.append("ivec", 0);
        x.append("ivec", 1);
        x.append("cvec", "abc");
        x.append("cvec", "cdf");
        return x;
    }

    public static int test_pages(int[] pages) {
        return pages.length;
    }

}