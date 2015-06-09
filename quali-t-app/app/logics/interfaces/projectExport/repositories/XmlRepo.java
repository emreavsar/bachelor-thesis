package logics.interfaces.projectExport.repositories;

import logics.interfaces.projectExport.models.Instance;
import logics.interfaces.projectExport.models.Project;
import logics.interfaces.projectExport.models.ProjectInitiator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

/**
 * Created by corina on 23.05.2015.
 */
public class XmlRepo {

    public ByteArrayOutputStream projectToXML(Project project) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(Project.class, ProjectInitiator.class, Instance.class);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(project, outStream);

        return outStream;
    }
}
