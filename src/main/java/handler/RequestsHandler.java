package handler;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Set;

public class RequestsHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        System.out.println("Server : Handler : handleMessage()......");

        Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (!isRequest) {

            try {
                SOAPMessage soapMsg = context.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();

                //if no header, add one
                if (soapHeader == null) {
                    soapHeader = soapEnv.addHeader();
                    //throw exception
                    generateSOAPErrMessage(soapMsg, "No SOAP header.");
                }

                System.out.println("Server : " + context.get(MessageContext.WSDL_INTERFACE).toString().replace("{http://web_servies/}", "") + " : " + context.get(MessageContext.WSDL_OPERATION).toString().replace("{http://web_servies/}", "") + "()......");

            } catch (SOAPException e) {
                System.err.println(e);
            }

        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {

        System.out.println("Server : Handler : handleFault()......");

        return true;
    }

    @Override
    public void close(MessageContext context) {
        System.out.println("Server : Handler : close()......");
    }

    @Override
    public Set<QName> getHeaders() {
        System.out.println("Server : Handler : getHeaders()......");
        return null;
    }

    private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
        try {
            SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
            SOAPFault soapFault = soapBody.addFault();
            soapFault.setFaultString(reason);
            throw new SOAPFaultException(soapFault);
        } catch (SOAPException e) {
        }
    }

}
