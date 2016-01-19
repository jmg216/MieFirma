package uy.isa.docman.client;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import uy.isa.docman.schemas.DocumentType;
import uy.isa.docman.schemas.GetDocumentRequest;
import uy.isa.docman.schemas.GetDocumentResponse;
import uy.isa.docman.schemas.ObjectFactory;
import uy.isa.docman.schemas.UploadDocumentRequest;
import uy.isa.docman.schemas.UploadDocumentResponse;

public class DocmanClient {

	public static WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

	public static ObjectFactory objectFactory = new ObjectFactory();

	// Metodo para crear request de GetDocument
	public GetDocumentRequest CreateGetDocumentRequest(String id) {
		GetDocumentRequest req = new GetDocumentRequest();
		req.setId(id);
		return req;
	}

	// Metodo para crear request de UploadDocument
	public UploadDocumentRequest CreateUploadDocumentRequest(String Name, String Type, String Source, byte[] file) {
		UploadDocumentRequest request = objectFactory.createUploadDocumentRequest();
		DocumentType doc = objectFactory.createDocumentType();
		doc.setDocument(file);
		request.setDocument(doc);
		return request;
	}

	// Metodo que llama a la operacion GetDocument del servicio
	public GetDocumentResponse GetDocument(GetDocumentRequest getDocRequest) {
		GetDocumentResponse dr = (GetDocumentResponse) webServiceTemplate.marshalSendAndReceive(getDocRequest);
		return dr;
	}

	// Metodo que llama a la operacion UploadDocument del servicio
	public UploadDocumentResponse UploadDocument(UploadDocumentRequest request) {
		UploadDocumentResponse dr = (UploadDocumentResponse) webServiceTemplate.marshalSendAndReceive(request);
		return dr;
	}

	// Devuelve cliente web
	public WebServiceTemplate getWebServiceTemplate() {
		// TODO Auto-generated method stub
		return webServiceTemplate;
	}

	// Especificando direccion del servicio
	public void setDefaultUri(String uri) {
		webServiceTemplate.setDefaultUri(uri);
	}

	// Especificando marshalls
	public void setMarshallerAndBound() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(UploadDocumentRequest.class, UploadDocumentResponse.class,
				GetDocumentRequest.class, GetDocumentResponse.class);

		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
	}

}
