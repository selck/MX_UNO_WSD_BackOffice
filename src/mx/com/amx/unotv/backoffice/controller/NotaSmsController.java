package mx.com.amx.unotv.backoffice.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;

import mx.com.amx.unotv.backoffice.dao.NotaSmsDAO;
import mx.com.amx.unotv.backoffice.dto.NotaSmsDTO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("notaSmsController")
public class NotaSmsController {
	
	private Logger logger=Logger.getLogger(NotaSmsController.class);
	private NotaSmsDAO notasmsDAO;
	
	/*@RequestMapping(value="insertNotaSMS", method=RequestMethod.POST, headers="Accept=application/json")
	@ResponseBody
	public Boolean insertNotaSMS(@RequestBody NotaSmsDTO[] arrayNotas, HttpServletResponse  response) throws Exception{
		boolean respuesta=false;
		try {
			List<NotaSmsDTO> listNotas = Arrays.asList(arrayNotas);
			respuesta=notasmsDAO.insertNotaSMS(listNotas);
		} catch (Exception e) {
			logger.error("Error insertNotaSMS [Controller]: ",e);
			throw new Exception(e.getMessage());
		}
		return respuesta;
	}*/
	
	@RequestMapping(value="insertNotaSMS", method=RequestMethod.POST, headers="Accept=application/json")
	@ResponseBody
	public Boolean insertNotaSMS(@RequestBody NotaSmsDTO dto,  HttpServletResponse  response) throws Exception{
		boolean respuesta=true;
		try {
			ArrayList<String> list_id_nota=(ArrayList<String>) dto.getList_id_nota();
			if(list_id_nota != null && list_id_nota.size()>0){
				for (String id : list_id_nota) {
					notasmsDAO.insertNotaSMS(dto,id);
				}
			}
		} catch (Exception e) {
			logger.error("Error insertNotaSMS [Controller]: ",e);
			throw new Exception(e.getMessage());
		}
		return respuesta;
	}
	
	@RequestMapping(value="insertHistoricoNotaSMS", method=RequestMethod.POST, headers="Accept=application/json")
	@ResponseBody
	public Boolean insertHistoricoNotaSMS(@RequestBody NotaSmsDTO dto, HttpServletResponse  response) throws Exception{
		boolean respuesta=true;
		try {
			ArrayList<String> list_id_nota=(ArrayList<String>) dto.getList_id_nota();
			
			if(list_id_nota != null && list_id_nota.size()>0){
				for (String id : list_id_nota) {
					notasmsDAO.insertHistoricoNotaSMS(dto,id);
				}
			}
		} catch (Exception e) {
			logger.error("Error insertNotaSMS [Controller]: ",e);
			throw new Exception(e.getMessage());
		}
		return respuesta;
	}
		
	@RequestMapping(value="deleteAllNotaSMS", method=RequestMethod.POST, headers="Accept=application/json")
	@ResponseBody
	public Boolean deleteAllNotaSMS(@RequestBody NotaSmsDTO smsDTO, HttpServletResponse  response) throws Exception{
		boolean respuesta=false;
		try {
			respuesta=notasmsDAO.deleteAllNotaSMS(smsDTO);
		} catch (Exception e) {
			logger.error("Error deleteAllNotaSMS [Controller]: ",e);
			throw new Exception(e.getMessage());
		}
		return respuesta;
	}
	
	@RequestMapping(value="deleteAllNotaHistoricoSMS", method=RequestMethod.POST, headers="Accept=application/json")
	@ResponseBody
	public Boolean deleteAllNotaHistoricoSMS(@RequestBody NotaSmsDTO smsDTO, HttpServletResponse  response) throws Exception{
		boolean respuesta=false;
		try {
			respuesta=notasmsDAO.deleteAllNotaHistoricoSMS(smsDTO);
		} catch (Exception e) {
			logger.error("Error deleteAllNotaHistoricoSMS [Controller]: ",e);
			throw new Exception(e.getMessage());
		}
		return respuesta;
	}
	
	@Autowired
	public void setNotasmsDAO(NotaSmsDAO notasmsDAO) {
		this.notasmsDAO = notasmsDAO;
	}

}
