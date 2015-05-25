package logics.interfaces.projectExport.repositories;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by corina on 23.05.2015.
 */
public class PdfRepo {

    private static Transformer getTransformer(StreamSource streamSource) {
        // setup the xslt transformer
        TransformerFactoryImpl impl = new TransformerFactoryImpl();

        try {
            return impl.newTransformer(streamSource);

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ByteArrayOutputStream CreatePdf(InputStream inputstream, InputStream xsltfile) {
        // the XML file from which we take the name
        StreamSource source = new StreamSource(inputstream);
        // InputStream source = new ByteArrayInputStream(inputstream.toByteArray());


        // creation of transform source
        StreamSource transformSource = new StreamSource(xsltfile);

        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance();

        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        // to store output
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        Transformer xslfoTransformer;
        try {
            xslfoTransformer = getTransformer(transformSource);
            // Construct fop with desired output format
            Fop fop;
            try {
                fop = fopFactory.newFop
                        (MimeConstants.MIME_PDF, foUserAgent, outStream);
                // Resulting SAX events (the generated FO)
                // must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());

                // Start XSLT transformation and FOP processing
                try {
                    // everything will happen here..
                    xslfoTransformer.transform(source, res);
                    // if you want to get the PDF bytes, use the following code
                    //return outStream.toByteArray();

                    // if you want to save PDF file use the following code
            /*File pdffile = new File("Result.pdf");
            OutputStream out = new java.io.FileOutputStream(pdffile);
                        out = new java.io.BufferedOutputStream(out);
                        FileOutputStream str = new FileOutputStream(pdffile);
                        str.write(outStream.toByteArray());
                        str.close();
                        out.close();*/

                    // to write the content to out put stream
                    return outStream;


                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            } catch (FOPException e) {
                e.printStackTrace();
            }
        } catch (TransformerFactoryConfigurationError e) {
            throw e;
        }
        return null;
    }
}
