//ols
package mx.com.amx.unotv.backoffice.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import mx.com.amx.unotv.backoffice.dao.BackOfficeDAO;
import mx.com.amx.unotv.backoffice.dto.CategoriaDTO;
import mx.com.amx.unotv.backoffice.dto.MagazineDTO;
import mx.com.amx.unotv.backoffice.dto.NotaDTO;
import mx.com.amx.unotv.backoffice.dto.ContentDTO;
import mx.com.amx.unotv.backoffice.util.Buscador;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("backOfficeController")
public class BackOfficeController {
	
	private static Logger logger=Logger.getLogger(BackOfficeController.class);
	
	private BackOfficeDAO backOfficeDAO;
	Integer resultadosPorPagina  = 10;
		
	@RequestMapping( value = "getNotasInfiniteLanding" , method=RequestMethod.POST , headers="Accept=application/json" )
	@ResponseBody
	public List < NotaDTO > getNotasInfiniteLanding (@RequestBody String idContenidos, HttpServletResponse  response ) {
		logger.info("===== getNotasInfiniteLanding =====");
		ArrayList<NotaDTO>listNotasInifniteLanding=new ArrayList<NotaDTO>();
		try {
			logger.info("idContenidos: "+idContenidos);
			listNotasInifniteLanding=(ArrayList<NotaDTO>) backOfficeDAO.getNotasInfiniteLanding(idContenidos);
			
		} catch ( Exception e ){
			logger.error("Error getNotasInfiniteLanding[Controller] ",e);
			return Collections.emptyList();
		}	 	
		
		return listNotasInifniteLanding;
	}

	
	@RequestMapping(value={"getNotasRelacionadas"}, method={org.springframework.web.bind.annotation.RequestMethod.POST}, headers={"Accept=application/json"})
	@ResponseBody
	public List<ContentDTO> getNotasRelacionadas (@RequestBody String tags){
		
		ArrayList<ContentDTO> list=new ArrayList<ContentDTO>();
		logger.info("getNotasRelacionadas -- Controller");
		logger.info("Tagas recibidos: "+tags);
		try {
			Buscador buscador=new Buscador();
			list=(ArrayList<ContentDTO>) buscador.getNotasRelacionadas(tags);
			
		} catch (Exception e) {
			
			logger.error(" Error getNotasRelacionadas [Controller] ",e );
		}
		return list;
	}
	/*
	@RequestMapping( value = "generaHTMLHome" , method=RequestMethod.POST , headers="Accept=application/json" )
	@ResponseBody
	public String generaHTMLHome (@RequestBody List<NotaDTO> list ){
		logger.info("generaHTMLHome");
		String respuesta="";
		
		try {
			List<NotaDTO> listaRecibida=list;
			if(listaRecibida != null && listaRecibida.size() > 0){
				OperacionesPrerender prerender=new OperacionesPrerender();
				ParametrosDTO parametrosDTO=prerender.obtenerPropiedades("ambiente.resources.properties");
				if(prerender.createPlantilla(parametrosDTO,(ArrayList<NotaDTO>) listaRecibida,"home")){
					//respuesta="http://m.unotv.com/mobile/landing-sms/"+estadoSeleccionado+ "/"+nombreArchivo+paramEstadoSeleccionado;
					respuesta="HTML ACTUALIZADO CORRECTAMENTE";
				}else{
					respuesta="OCURRIÓ UN ERROR, FAVOR DE CONTACTAR AL ADMINISTRADOR";
				}
				logger.debug("Respuesta: "+respuesta);
			}
			
		} catch ( Exception e ){
			logger.error("Error generaHTMLHome[Controller] ",e);
		}	 	
		
		return respuesta;
	}*/
	
	@RequestMapping( value = "saveNotes" , method=RequestMethod.POST , headers="Accept=application/json" )
	@ResponseBody
	public String saveNotes (@RequestBody MagazineDTO magazineDTO, HttpServletResponse  response) {
		logger.info("===== saveNotes =====");
		String idMagazine, respuesta="", urlExterna;
		try {
			
			
			ArrayList<NotaDTO> listNotas= (ArrayList<NotaDTO>) magazineDTO.getListNotas();
			logger.info("listNotas del magazine DTO: "+listNotas.size());
			
			if(listNotas!=null && listNotas.size()>0){
				idMagazine=listNotas.get(0).getFcNombreMagazine();
				logger.info("idMagazine: "+idMagazine);
				if(backOfficeDAO.deleteAllNoticias(idMagazine) && backOfficeDAO.deleteAllPreOlimpicos(idMagazine)){
					for (int i = 0; i < listNotas.size(); i++) {
						urlExterna="";
						if(listNotas.get(i).getFcNombre().toLowerCase().contains("http://") ||listNotas.get(i).getFcNombre().toLowerCase().contains("https://")){
							urlExterna=listNotas.get(i).getFcNombre();
						}else
							urlExterna="";
						int patrocinado=listNotas.get(i).getFiBanPatrocinio() == null || listNotas.get(i).getFiBanPatrocinio().equals("")?0:Integer.parseInt(listNotas.get(i).getFiBanPatrocinio());
						if(listNotas.get(i).getFcLinkDetalle().contains("olimpicos.clarosports.com"))
							backOfficeDAO.insertNotaMagazinePreOlimpicos(listNotas.get(i).getFcIdContenido(), idMagazine, i, patrocinado,urlExterna);
						else if (listNotas.get(i).getFcImgPrincipal().contains("unotv"))
							backOfficeDAO.insertNotaMagazine(listNotas.get(i).getFcIdContenido(), idMagazine, i, patrocinado,urlExterna);
					}
					respuesta="success";
				}
				
			}
			
		} catch ( Exception e ){
			logger.error("Error saveNotes[Controller] ",e);
			respuesta=e.getMessage();
		}	 	
		
		return respuesta;
	}
	
	@RequestMapping( value = "getNotesPublished" , method=RequestMethod.POST , headers="Accept=application/json" )
	@ResponseBody
	public List < NotaDTO > getNotesPublished (@RequestParam String idMagazine, HttpServletResponse  response ) {
		logger.info("===== getNotesPublished =====");
		ArrayList<NotaDTO>listNotasResponse=new ArrayList<NotaDTO>();
		try {
			logger.info("idMagazine: "+idMagazine);
			
			NotaDTO notaResponse=null;
			ArrayList<NotaDTO>listNotasID=(ArrayList<NotaDTO>) backOfficeDAO.getAllIdsNotesPublished(idMagazine);
			if(listNotasID!=null && listNotasID.size()>0){
					for (int i = 0; i < listNotasID.size(); i++) {
						if(listNotasID.get(i).getFcImgPrincipal().equals("noticias")){
							notaResponse=backOfficeDAO.getNotaMagazine(listNotasID.get(i).getFcIdContenido(), idMagazine);
							
						}else if(listNotasID.get(i).getFcImgPrincipal().equals("olimpicos_pre")){
						//else if(listNotasID.get(i).getFcLinkDetalle().contains("olimpicos.clarosports.com")){
							notaResponse=backOfficeDAO.getNotaMagazinePreOlimpicos(listNotasID.get(i).getFcIdContenido());
						}
						
						listNotasResponse.add(notaResponse);
					}
					logger.debug("ListNotesResponse.Size: "+listNotasResponse.size());
			}
			
		} catch ( Exception e ){
			logger.error("Error getNotesPublished[Controller] ",e);
			return Collections.emptyList();
		}	 	
		
		return listNotasResponse;
	}
	
	@RequestMapping( value = "getTipoSecciones" , method=RequestMethod.GET , headers="Accept=application/json" )
	@ResponseBody
	public List < CategoriaDTO > getTipoSecciones (HttpServletResponse  response ) {

		List < CategoriaDTO > listCategorias = new ArrayList < CategoriaDTO > ();
		
		try {
				logger.info("===== getTipoSecciones =====");
				listCategorias = backOfficeDAO.getTipoSecciones();
				
		} catch ( Exception e ){
			logger.error("Error getTipoSecciones[Controller] ",e);
			listCategorias = Collections.emptyList();
		}	 	
		
		return listCategorias;
	}
	
	@RequestMapping( value = "getSecciones/{fcIdTipoSeccion}" , method=RequestMethod.GET , headers="Accept=application/json" )
	@ResponseBody
	public List < CategoriaDTO > getSecciones ( @PathVariable String fcIdTipoSeccion , HttpServletResponse  response ) {

		List < CategoriaDTO > listCategorias = new ArrayList < CategoriaDTO > ();
		
		try {
				logger.info("===== getSecciones =====");
				listCategorias = backOfficeDAO.getSecciones(fcIdTipoSeccion);
				
		} catch ( Exception e ){
			logger.error("Error getSecciones[Controller] ",e);
			listCategorias = Collections.emptyList();
		}	 	
		
		return listCategorias;
	}
	
	@RequestMapping( value = "getCategorias/{fcIdSeccion}" , method=RequestMethod.GET , headers="Accept=application/json" )
	@ResponseBody
	public List < CategoriaDTO > getCategorias ( @PathVariable String fcIdSeccion , HttpServletResponse  response ) {

		List < CategoriaDTO > listCategorias = new ArrayList < CategoriaDTO > ();
		
		try {
				logger.info("===== getSecciones =====");
				listCategorias = backOfficeDAO.getCategorias(fcIdSeccion);
				
		} catch ( Exception e ){
			logger.error("Error getCategorias[Controller] ",e);
			listCategorias = Collections.emptyList();
		}	 	
		
		return listCategorias;
	}
	
	@RequestMapping( value = "getNotasByCategoria/{fcIdCategoria}/{pagina}" , method=RequestMethod.GET , headers="Accept=application/json" )
	@ResponseBody
	public List < NotaDTO > getNotasByCategoria ( @PathVariable String fcIdCategoria , @PathVariable Integer pagina, HttpServletResponse  response ) {

		List < NotaDTO > listNotas = new ArrayList < NotaDTO > ();
			try {
					logger.info("===== getNotasByCategoria =====");
					Integer numeroPaginas = 0;
					Integer total = 0;
					
					if ( pagina == 0 ){
						total = backOfficeDAO.getTotalNotasByCategoria( fcIdCategoria );
						numeroPaginas = (int)( Math.ceil( (double) total / (double) resultadosPorPagina  )   )   ;
	        			
						response.setHeader("total", String.valueOf( numeroPaginas ) );
						pagina = 1;
					}
					
					listNotas = backOfficeDAO.getNotasByCategoria( fcIdCategoria , pagina , resultadosPorPagina );
					
					
					
			} catch ( Exception e ){
				
				logger.error("Error getNotasByCategoria[Controller] ",e);
				listNotas = Collections.emptyList();
			}
		
			return listNotas;
	}
	/**
	 * @return the backOfficeDAO
	 */
	public BackOfficeDAO getBackOfficeDAO() {
		return backOfficeDAO;
	}

	/**
	 * @param backOfficeDAO the backOfficeDAO to set
	 */
	@Autowired
	public void setBackOfficeDAO(BackOfficeDAO backOfficeDAO) {
		this.backOfficeDAO = backOfficeDAO;
	}
	
	
	
}
