
package py.com.miefirma.docman.schemas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para UploadDocumentRequestType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="UploadDocumentRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UploadDocumentReq" type="{http://miefirma.com.py/docman/schemas}DocumentType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadDocumentRequestType", propOrder = {
    "uploadDocumentReq"
})
public class UploadDocumentRequestType {

    @XmlElement(name = "UploadDocumentReq", required = true)
    protected DocumentType uploadDocumentReq;

    /**
     * Obtiene el valor de la propiedad uploadDocumentReq.
     * 
     * @return
     *     possible object is
     *     {@link DocumentType }
     *     
     */
    public DocumentType getUploadDocumentReq() {
        return uploadDocumentReq;
    }

    /**
     * Define el valor de la propiedad uploadDocumentReq.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentType }
     *     
     */
    public void setUploadDocumentReq(DocumentType value) {
        this.uploadDocumentReq = value;
    }

}
