/* Client.java - created on 19 de Abr de 2013, Copyright (c) 2011 The European Library, all rights reserved */
package harvesterUI.server.sru;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

import jakarta.activation.DataHandler;
import javax.xml.namespace.QName;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

import org.w3c.dom.Document;

/**
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 19 de Abr de 2013
 */
public class TestClient {
    static String testUrl="http://127.0.0.1:8888/srurecordupdate/recUpdate";
//    static String testUrl="http://repox2.tel.ulcc.ac.uk/repox/srurecordupdate/recUpdate";
    
    public static void main(String[] args) throws Exception {
//        testUpdateRequest();
        testDeleteRequest();
    }
    
    public static void testUpdateRequest() throws Exception {
        SOAPConnection connection;

        SOAPConnectionFactory connectionFactory =
                    SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
        
        String outString ="";

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            Document soapMessageDom = XmlUtil.parseDomFromFile(new File("example3.xml"));
            SOAPMessage outgoingMessage = SoapUtil.toSOAPMessage(soapMessageDom);
            
            SOAPPart soappart = outgoingMessage.getSOAPPart();
            SOAPEnvelope envelope = soappart.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();
            
//            SOAPElement requestEl=SoapUtil.getFirstChild(body, new QName("http://www.loc.gov/zing/srw/update/", "updateRequest"));
//            if(requestEl==null) {
//                System.out.println("no request found");
//                //TODO send a soap error?
//                return;
//            }
//            SOAPelement actionel=soaputil.getfirstchild(requestel, new qname("http://www.loc.gov/zing/srw/update/", "action"));
//            soapelement recidel=soaputil.getfirstchild(requestel, new qname("http://www.loc.gov/zing/srw/update/", "recordidentifier"));
//            soapelement recel=soaputil.getfirstchild(requestel, new qname("http://www.loc.gov/zing/srw/", "record"));
//
//            action action=actionel==null ? null : action.fromuri(actionel.gettextcontent());
//            string recid= recidel==null ? null : recidel.gettextcontent();
//            
//            soapelement recdatael=soaputil.getfirstchild(recel, new qname("http://www.loc.gov/zing/srw/update/", "recorddata"));
//            
//            soapelement xmlrec=soaputil.getfirstchild(recdatael);
//            
//            system.out.println(action);
//            system.out.println(recid);
            
            
//            SOAPMessage outgoingMessage = messageFactory.createMessage();
//            SOAPPart soappart = outgoingMessage.getSOAPPart();
//            SOAPEnvelope envelope = soappart.getEnvelope();
//            SOAPHeader header = envelope.getHeader();
//            SOAPBody body = envelope.getBody();
//
//            body.addBodyElement(envelope.createName("numberAvailable",
//            "laptops",
//            "http://www.XMLPowerCorp.com")).addTextNode("216");

            URL client = new URL(testUrl);

            outString += "SOAP outgoingMessage sent\n";

            SOAPMessage incomingMessage = connection.
                call(outgoingMessage, client);

            if (incomingMessage != null) {
                ByteArrayOutputStream incomingFile = new ByteArrayOutputStream();
                incomingMessage.writeTo(incomingFile);
                incomingFile.close();
                outString +=
                    "SOAP outgoingMessage received:\n" + new String(incomingFile.toByteArray());
            }

            System.out.println(outString);
        } catch(Throwable e) {
            e.printStackTrace();
        }

    }
    
    
    public static void testDeleteRequest() throws Exception {
        SOAPConnection connection;

        SOAPConnectionFactory connectionFactory =
                    SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
        
        String outString ="";

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            Document soapMessageDom = XmlUtil.parseDomFromFile(new File("example_delete.xml"));
            SOAPMessage outgoingMessage = SoapUtil.toSOAPMessage(soapMessageDom);
            
            SOAPPart soappart = outgoingMessage.getSOAPPart();
            SOAPEnvelope envelope = soappart.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();
            
//            SOAPElement requestEl=SoapUtil.getFirstChild(body, new QName("http://www.loc.gov/zing/srw/update/", "updateRequest"));
//            if(requestEl==null) {
//                System.out.println("no request found");
//                //TODO send a soap error?
//                return;
//            }
//            SOAPelement actionel=soaputil.getfirstchild(requestel, new qname("http://www.loc.gov/zing/srw/update/", "action"));
//            soapelement recidel=soaputil.getfirstchild(requestel, new qname("http://www.loc.gov/zing/srw/update/", "recordidentifier"));
//            soapelement recel=soaputil.getfirstchild(requestel, new qname("http://www.loc.gov/zing/srw/", "record"));
//
//            action action=actionel==null ? null : action.fromuri(actionel.gettextcontent());
//            string recid= recidel==null ? null : recidel.gettextcontent();
//            
//            soapelement recdatael=soaputil.getfirstchild(recel, new qname("http://www.loc.gov/zing/srw/update/", "recorddata"));
//            
//            soapelement xmlrec=soaputil.getfirstchild(recdatael);
//            
//            system.out.println(action);
//            system.out.println(recid);
            
            
//            SOAPMessage outgoingMessage = messageFactory.createMessage();
//            SOAPPart soappart = outgoingMessage.getSOAPPart();
//            SOAPEnvelope envelope = soappart.getEnvelope();
//            SOAPHeader header = envelope.getHeader();
//            SOAPBody body = envelope.getBody();
//
//            body.addBodyElement(envelope.createName("numberAvailable",
//            "laptops",
//            "http://www.XMLPowerCorp.com")).addTextNode("216");

            URL client = new URL(testUrl);

            outString += "SOAP outgoingMessage sent\n";

            SOAPMessage incomingMessage = connection.
                call(outgoingMessage, client);

            if (incomingMessage != null) {
                ByteArrayOutputStream incomingFile = new ByteArrayOutputStream();
                incomingMessage.writeTo(incomingFile);
                incomingFile.close();
                outString +=
                    "SOAP outgoingMessage received:\n" + new String(incomingFile.toByteArray());
            }

            System.out.println(outString);
        } catch(Throwable e) {
            e.printStackTrace();
        }

    }
}
