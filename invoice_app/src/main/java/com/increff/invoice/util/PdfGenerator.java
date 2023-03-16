package com.increff.invoice.util;

import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.SQLOutput;
import java.util.Base64;

@Service
public class PdfGenerator {
    @Value("${xmlFilePath}")
    private String xmlFilePath;

    @Value("${xslFilePath}")
    private String xslFilePath;

    public String xmlToPdfConverter() {
        try {
            System.out.println("Value of xmlFilePath : " + xmlFilePath);
            System.out.println("Value of xslFilePath : " + xslFilePath);
            File xmlfile = new File(xmlFilePath);
            File xsltfile = new File(xslFilePath);

            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            System.out.println("completing block 1");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                Fop fop;
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                System.out.println("completing block 2");
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                Source src = new StreamSource(xmlfile);

                Result res = new SAXResult(fop.getDefaultHandler());
                System.out.println("completing block 3");
                transformer.transform(src, res);
            } catch (FOPException | TransformerException e) {
                e.printStackTrace();
                System.out.println("in catch block, because of exception");
            } finally {
                byte[] pdf = out.toByteArray();
                System.out.println("completing finally block");
                return Base64.getEncoder().encodeToString(pdf);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return "";
    }
}
