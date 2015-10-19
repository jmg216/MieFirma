
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
 *         &lt;element name="GetDocumentReq" type="{http://miefirma.com.py/docman/schemas}GetDocumentRequestType"/>
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
@XmlRootElement(name = "GetDocumentRequest")
public class GetDocumentRequest {

    @XmlElement(name = "GetDocumentReq", required = true)
    protected GetDocumentRequestType getDocumentReq;

    /**
     * Obtiene el valor de la propiedad getDocumentReq.
     * 
     * @return
     *     possible object is
     *     {@link GetDocumentRequestType }
     *     
     */
    public GetDocumentRequestType getGetDocumentReq() {
        return getDocumentReq;
    }

    /**
     * Define el valor de la propiedad getDocumentReq.
     * 
     * @param value
     *     allowed object is
     *     {@link GetDocumentRequestType }
     *     
     */
    public void setGetDocumentReq(GetDocumentRequestType value) {
        this.getDocumentReq = value;
    }

}
