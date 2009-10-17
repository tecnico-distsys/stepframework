package hello;

import java.io.*;
import java.util.List;

import javax.xml.bind.*;

import hello.xsd.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.namespace.QName;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;


public class HelloJaxb {

    private static final String USAGE = "usage: (marshal|unmarshal)...";

    public static void main( String[] args ) throws Exception {

        if(args.length < 1) {
            System.out.println(USAGE);
            return;
        }

        if(args[0].equalsIgnoreCase("marshal")) {
            System.out.print("marshal");
            String outputFile = null;
            if(args.length < 2) {
                System.out.println(" to System.out");
            } else {
                outputFile = args[1];
                System.out.println(" to file " + outputFile);
            }
            marshal(outputFile);

        } else if(args[0].equalsIgnoreCase("unmarshal")) {
            System.out.print("unmarshal");
            String inputFile = null, schemaFile = null;
            if(args.length < 2) {
                System.out.println(", missing input file");
            } else {
                inputFile = args[1];
                System.out.print(" from input file " + inputFile);
                if(args.length < 3) {
                    System.out.println(" with no schema validation");
                } else {
                    schemaFile = args[2];
                    System.out.println(" validating with schema file " + schemaFile);
                }
            }
            unmarshal(inputFile, schemaFile);
        } else {
            System.out.println("Action parameter " + args[0] + " is unknown! " + USAGE);
        }
    }

    private static void marshal(String outputFile) throws JAXBException, FileNotFoundException {

        // create a JAXBContext
        JAXBContext jc = JAXBContext.newInstance( "hello.xsd" );

        // create Java XML-bound objects
        HelloType hello = new HelloType();
        hello.setId(1);
        hello.setName("friend");

        // create XML element (a complex type cannot be instantiated by itself)
        JAXBElement jaxbElementMarshal = new JAXBElement(
            new QName("urn:hello:xsd","hello","h"),
            hello.xsd.HelloType.class,
            hello);

        // create a Marshaller and marshal
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        if(outputFile == null) {
            m.marshal(jaxbElementMarshal, System.out);

        } else {
            OutputStream os = new FileOutputStream(outputFile);
            m.marshal(jaxbElementMarshal, os);
        }

    }

    private static void unmarshal(String inputFile, String schemaFile) throws JAXBException, SAXException {

        // input file must not be null
        if(inputFile == null)
            throw new IllegalArgumentException("inputFile");


        // create a JAXBContext
        JAXBContext jc = JAXBContext.newInstance( "hello.xsd" );

        // create an Unmarshaller
        Unmarshaller u = jc.createUnmarshaller();

        // set schema
        if(schemaFile != null) {
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File(schemaFile));
            u.setSchema(schema);
        }

        // unmarshal, get element and cast to expected type
        Object obj = u.unmarshal(new File(inputFile));
        JAXBElement jaxbElementUnmarshal = (JAXBElement) obj;
        HelloType hello = (HelloType) jaxbElementUnmarshal.getValue();

        // print part of the read information
        System.out.println("hello");
        System.out.println("id= " + hello.getId());
        System.out.println("name= " + hello.getName());

    }

}
