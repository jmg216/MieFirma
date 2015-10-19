
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
 *         &lt;element name="UploadDocumentRes" type="{http://miefirma.com.py/docman/schemas}UploadDocumentResponseType"/>
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
@XmlRootElement(name = "UploadDocumentResponse")
public class UploadDocumentResponse {

    @XmlElement(name = "UploadDocumentRes", required = true)
    protected UploadDocumentResponseType uploadDocumentRes;

    /**
     * Obtiene el valor de la propiedad uploadDocumentRes.
     * 
     * @return
     *     possible object is
     *     {@link UploadDocumentResponseType }
     *     
     */
    public UploadDocumentResponseType getUploadDocumentRes() {
        return uploadDocumentRes;
    }

    /**
     * Define el valor de la propiedad uploadDocumentRes.
     * 
     * @param value
     *     allowed object is
     *     {@link UploadDocumentResponseType }
     *     
     */
    public void setUploadDocumentRes(UploadDocumentResponseType value) {
        this.uploadDocumentRes = value;
    }

}
