
package py.com.miefirma.docman.schemas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para GetDocumentResponseType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="GetDocumentResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetDocumentRes" type="{http://miefirma.com.py/docman/schemas}DocumentType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDocumentResponseType", propOrder = {
    "getDocumentRes"
})
public class GetDocumentResponseType {

    @XmlElement(name = "GetDocumentRes", required = true)
    protected DocumentType getDocumentRes;

    /**
     * Obtiene el valor de la propiedad getDocumentRes.
     * 
     * @return
     *     possible object is
     *     {@link DocumentType }
     *     
     */
    public DocumentType getGetDocumentRes() {
        return getDocumentRes;
    }

    /**
     * Define el valor de la propiedad getDocumentRes.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentType }
     *     
     */
    public void setGetDocumentRes(DocumentType value) {
        this.getDocumentRes = value;
    }

}
