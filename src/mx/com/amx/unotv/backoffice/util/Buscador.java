package mx.com.amx.unotv.backoffice.util;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mx.com.amx.unotv.backoffice.dto.ContentDTO;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Buscador {
private Logger logger=Logger.getLogger(Buscador.class);
	
	public List<ContentDTO> getNotasRelacionadas(String tags) {
		
		//curl http://AMXSVROUT01-1.tmx-internacional.net/s/search?q=epn&site=unotv&access=p&client=unotv&output=xml_no_dtd&proxyreload=1&getfields=*&num=50 -H "X-Target: buscador.unotv.com"
		String urlBuscador="http://AMXSVROUT01-1.tmx-internacional.net/s/search?q=epn&site=unotv&access=p&client=unotv&output=xml_no_dtd&proxyreload=1&getfields=*&num=50";
		logger.info("Inicia getListRelacionadas ");
		logger.info("tags: "+tags);
		logger.info("urlBuscador: "+urlBuscador);		
		
		ArrayList<ContentDTO> list = new ArrayList<ContentDTO>();
		String urlServidor = "";
		String base = "Keywords:$PALABRA$|";
		//String base = "nota_tags:$PALABRA$|";
		ContentDTO contentDTO = null;
		try {
			
			String palabras[] = tags.split("\\,");
			String query = "";
			
			for (String palabra : palabras) {
				palabra = replaceGSA(palabra.trim());
				query += base.replace("$PALABRA$", palabra);
			}
			urlServidor = urlBuscador + query;
			urlServidor = urlServidor.substring(0, urlServidor.length() - 1);
			logger.info("URL GSA: " + urlServidor);
			URL url = new URL(urlServidor);
			URLConnection urlCon = url.openConnection();
			urlCon.setRequestProperty("X-Target", "buscador.unotv.com");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(urlCon.getInputStream());
			doc.getDocumentElement().normalize();

			logger.info("Root element :" +doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("R");
			
			
			logger.info("Recorremos las notas de la busqueda");
			logger.info("Total de notas: "+nList.getLength());
			//Recorremos las notas
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				
				contentDTO = new ContentDTO();
				
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
										
					logger.info("T (Titulo GSA)  : "+eElement.getElementsByTagName("T").item(0).getTextContent());
					logger.info("U (URL Nota)    : "+eElement.getElementsByTagName("U").item(0).getTextContent());					
					NodeList nodeListMT =   eElement.getElementsByTagName("MT");
					contentDTO.setFcUrl(eElement.getElementsByTagName("U").item(0).getTextContent());
					//Obtenemos los metas de la nota:
					logger.info("== Metas de la nota: "+nodeListMT.getLength());
					logger.info("Num metas: "+nodeListMT.getLength());
					for (int i = 0; i < nodeListMT.getLength(); i++) {					
						Node nNodeMeta = nodeListMT.item(i);																
						 NamedNodeMap namedNodeMap = nNodeMeta.getAttributes();						 						
						 contentDTO = obtieneMeta(contentDTO, namedNodeMap.getNamedItem("N").getTextContent().trim(), namedNodeMap.getNamedItem("V").getTextContent().trim());
					}

					list.add(contentDTO);
				
				}
			}
		} catch (Exception e) {
			logger.error("Error getListRelacionadas: ", e);
			return Collections.emptyList();
		}
		return list;
	}
	
	private String replaceGSA(String cadena) {
		String salida = "";
		try {
			
			salida = cadena.replaceAll(" ", "%2520");

			salida = cadena.replaceAll("á", "%C3%A1");
			salida = cadena.replaceAll("é", "%C3%A9");
			salida = cadena.replaceAll("í", "%C3%AD");
			salida = cadena.replaceAll("ó", "%C3%B3");
			salida = cadena.replaceAll("ú", "%C3%BA");

			salida = cadena.replaceAll("Á", "%C3%81");
			salida = cadena.replaceAll("É", "%C3%89");
			salida = cadena.replaceAll("Í", "%C3%8D");
			salida = cadena.replaceAll("Ó", "%C3%93");
			salida = cadena.replaceAll("Ú", "%C3%9A");

			//salida = cadena.replaceAll("?", "%3F");
			//salida = cadena.replaceAll("!", "%21");


		} catch (Exception e) {
			logger.error("Error replace GSA: " + e.getMessage());
		}
		return salida;
	}
	
	/**
	 * Metodo que coloca los tag
	 * */
	private ContentDTO obtieneMeta(ContentDTO contentDTO, String N, String V)
	{
		logger.debug("Inicia obtieneMeta");
		
		try {
			
			if(V == null)
			{
				V="";
			}
			
			//
			if(N.equals("nota_published_time"))
			{
				//contentDTO.setFdFechaPublicacion(V.trim());
			}
			
						
			if(N.equals("nota_modified_time"))
			{
				//contentDTO.setFcIdTipoNota(V.trim());
			}
			
			
			if(N.equals("nota_tipo"))
			{
				contentDTO.setFcIdTipoNota(V.trim());
			}
			
			if(N.equals("nota_img"))
			{
				contentDTO.setFcImgPrincipal(V.trim());
			}
			
			if(N.equals("nota_tipo_seccion"))
			{
				contentDTO.setFcTipoSeccion(V.trim());
			}
			
			if(N.equals("nota_seccion"))
			{
				contentDTO.setFcSeccion(V.trim());
			}
			
			
			if(N.equals("nota_categoria"))
			{
				contentDTO.setFcIdCategoria(V.trim());
			}
			
			
			if(N.equals("nota_tags"))
			{
				contentDTO.setFcTags(V.trim());
			}
									
			if(N.equals("nota_titulo"))
			{
				contentDTO.setFcTitulo(V.trim());
			}
			
			
			
		} catch (Exception e) {
			logger.error("Exception en obtieneMeta");
		}
		
		return contentDTO;		
	}

}
