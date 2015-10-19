
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
 *         &lt;element name="GetDocumentRes" type="{http://miefirma.com.py/docman/schemas}GetDocumentResponseType"/>
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
@XmlRootElement(name = "GetDocumentResponse")
public class GetDocumentResponse {

    @XmlElement(name = "GetDocumentRes", required = true)
    protected GetDocumentResponseType getDocumentRes;

    /**
     * Obtiene el valor de la propiedad getDocumentRes.
     * 
     * @return
     *     possible object is
     *     {@link GetDocumentResponseType }
     *     
     */
    public GetDocumentResponseType getGetDocumentRes() {
        return getDocumentRes;
    }

    /**
     * Define el valor de la propiedad getDocumentRes.
     * 
     * @param value
     *     allowed object is
     *     {@link GetDocumentResponseType }
     *     
     */
    public void setGetDocumentRes(GetDocumentResponseType value) {
        this.getDocumentRes = value;
    }

}
