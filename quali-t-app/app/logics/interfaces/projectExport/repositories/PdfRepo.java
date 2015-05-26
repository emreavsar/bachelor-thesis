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

    private Transformer getTransformer(StreamSource streamSource) throws TransformerConfigurationException {
        TransformerFactoryImpl impl = new TransformerFactoryImpl();
        return impl.newTransformer(streamSource);
    }

    public ByteArrayOutputStream createPdf(InputStream inputstream, InputStream xsltfile) throws TransformerException, FOPException {
        StreamSource source = new StreamSource(inputstream);
        StreamSource transformSource = new StreamSource(xsltfile);

        FopFactory fopFactory = FopFactory.newInstance();
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        Transformer xslfoTransformer = getTransformer(transformSource);
        Fop fop = fopFactory.newFop
                (MimeConstants.MIME_PDF, foUserAgent, outStream);
        Result res = new SAXResult(fop.getDefaultHandler());

        xslfoTransformer.transform(source, res);
        return outStream;
    }
}
