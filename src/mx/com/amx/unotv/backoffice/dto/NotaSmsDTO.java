package mx.com.amx.unotv.backoffice.dto;

import java.util.List;

public class NotaSmsDTO {

	private String fecha;
	private String id_estado;
	private List<String> list_id_nota;
	private String usuario;
	
	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the id_estado
	 */
	public String getId_estado() {
		return id_estado;
	}
	/**
	 * @param id_estado the id_estado to set
	 */
	public void setId_estado(String id_estado) {
		this.id_estado = id_estado;
	}
	/**
	 * @return the list_id_nota
	 */
	public List<String> getList_id_nota() {
		return list_id_nota;
	}
	/**
	 * @param list_id_nota the list_id_nota to set
	 */
	public void setList_id_nota(List<String> list_id_nota) {
		this.list_id_nota = list_id_nota;
	}
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
		
	
	
}
