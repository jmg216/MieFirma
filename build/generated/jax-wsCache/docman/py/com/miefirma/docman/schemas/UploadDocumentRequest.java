
package py.com.miefirma.docman.schemas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="UploadDocumentReq" type="{http://miefirma.com.py/docman/schemas}UploadDocumentRequestType"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "UploadDocumentRequest")
public class UploadDocumentRequest {

    @XmlElement(name = "UploadDocumentReq", required = true)
    protected UploadDocumentRequestType uploadDocumentReq;

    /**
     * Obtiene el valor de la propiedad uploadDocumentReq.
     * 
     * @return
     *     possible object is
     *     {@link UploadDocumentRequestType }
     *     
     */
    public UploadDocumentRequestType getUploadDocumentReq() {
        return uploadDocumentReq;
    }

    /**
     * Define el valor de la propiedad uploadDocumentReq.
     * 
     * @param value
     *     allowed object is
     *     {@link UploadDocumentRequestType }
     *     
     */
    public void setUploadDocumentReq(UploadDocumentRequestType value) {
        this.uploadDocumentReq = value;
    }

}
