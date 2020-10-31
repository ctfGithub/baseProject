package com.springbootbasepackage.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * xml 工具类
 * @author stl
 * https://www.cnblogs.com/yanghaolie/p/11110991.html
 * https://www.cnblogs.com/jcjssl/p/10166293.html
 */
@Slf4j
public class XmlUtils {

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @return
     */
    public static String bean2XmlWithCreateHead(Object obj) {
        String result = null;
        XMLStreamWriter xmlStreamWriter = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //去掉默认xml头
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(
                    baos, (String) jaxbMarshaller.getProperty(Marshaller.JAXB_ENCODING));
            xmlStreamWriter.writeStartDocument(
                    (String) jaxbMarshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
            jaxbMarshaller.marshal(obj, xmlStreamWriter);
            xmlStreamWriter.writeEndDocument();
            result = new String(baos.toByteArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (xmlStreamWriter != null){
                try {
                    xmlStreamWriter.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @return
     */
    public static String bean2XmlWithCreateHead(Object obj, String encoding) {
        String result = null;
        StringWriter writer = new StringWriter();
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty("jaxb.encoding", encoding);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(
                    baos, (String) jaxbMarshaller.getProperty(Marshaller.JAXB_ENCODING));
            xmlStreamWriter.writeStartDocument(
                    (String) jaxbMarshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
            jaxbMarshaller.marshal(obj, xmlStreamWriter);
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.close();
            return new String(baos.toByteArray());
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * xml转换成JavaBean
     *
     * @param xml
     * @param c
     * @return
     */
    public static <T> T xmlToBean(String xml, Class<T> c) {
        if (StringUtils.isEmpty(xml)){
            return null;
        }
        T t = null;
        StringReader reader = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            reader = new StringReader(xml);
            t = (T) unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if(reader != null){
                reader.close();
            }
        }
        return t;
    }

    /**
     * 格式化
     * @param str
     * @return
     * @throws Exception
     */
    public static String format(String str) {
        XMLWriter writer = null;

        StringWriter out = null;
        try {
            SAXReader reader = new SAXReader();
            // 注释：创建一个串的字符输入流
            StringReader in = new StringReader(str);
            Document doc = reader.read(in);
            // 注释：创建输出格式
            OutputFormat formater = OutputFormat.createPrettyPrint();

            // 注释：创建输出(目标)
            out = new StringWriter();
            // 注释：创建输出流
            writer =new XMLWriter(out, formater);
            // 注释：输出格式化的串到目标中，执行后。格式化后的串保存在out中。
            writer.write(doc);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out.toString();
    }

    public static String getCDATAStr(String str){
        if(StringUtils.isNotEmpty(str)){
            return "<![CDATA["+str+"]]>";
        }
        return str;
    }


}
