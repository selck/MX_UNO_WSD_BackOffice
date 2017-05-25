package mx.com.amx.unotv.backoffice.dao;

import java.util.Collections;
import java.util.List;

import mx.com.amx.unotv.backoffice.dto.CategoriaDTO;
import mx.com.amx.unotv.backoffice.dto.NotaDTO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Qualifier("backOfficeDAO")
public class BackOfficeDAO {
	
	private JdbcTemplate jdbcTemplate;
	private Logger logger=Logger.getLogger(BackOfficeDAO.class);
	
	public  List<NotaDTO> getNotasInfiniteLanding ( String idContenidos ) 
	{
		StringBuffer sbQuery = new StringBuffer();
		
		try {
			
			sbQuery.append(" select ");   
			sbQuery.append(" N.FC_ID_TIPO_NOTA as fcIdTipoNota,  ");
			sbQuery.append(" C.FC_DESCRIPCION as fcDescripcionCategoria,  ");
			sbQuery.append(" N.FC_ID_CONTENIDO as fcIdContenido,  ");
			sbQuery.append(" N.FC_TITULO as fcTitulo,  ");
			sbQuery.append(" REPLACE( REPLACE(N.FC_DESCRIPCION, Char(13), ''), Char(10),'')as fcDescripcion,  ");
			sbQuery.append(" N.FC_ID_CATEGORIA as fcIdCategoria,  ");
			sbQuery.append(" CASE ");
			sbQuery.append(" WHEN TS.FC_ID_TIPO_SECCION = 'especiales' ");
			sbQuery.append(" THEN ('/'||COALESCE(TS.FC_ID_TIPO_SECCION,'')||'/'||COALESCE(S.FC_FRIENDLY_URL,'')||'/'||COALESCE(C.FC_FRIENDLY_URL,'')||'/detalle/'|| COALESCE(N.FC_NOMBRE,'')) "); 
			sbQuery.append(" ELSE ");
			sbQuery.append(" ('/'||COALESCE(TS.FC_ID_TIPO_SECCION,'')||'s/'||COALESCE(S.FC_FRIENDLY_URL,'')||'/'||COALESCE(C.FC_FRIENDLY_URL,'')||'/detalle/'|| COALESCE(N.FC_NOMBRE,'')) ");
			sbQuery.append(" END AS fcLinkDetalle, ");  
			//sbQuery.append(" ('http://www.unotv.com/'||COALESCE(TS.FC_ID_TIPO_SECCION,'')||'s/'||COALESCE(S.FC_FRIENDLY_URL,'')||'/'||COALESCE(C.FC_FRIENDLY_URL,'')||'/detalle/'|| COALESCE(N.FC_NOMBRE,'')) as fcLinkDetalle,   ");
			sbQuery.append(" N.FC_NOMBRE as fcNombre, ");
			sbQuery.append(" N.FC_IMAGEN_PRINCIPAL as fcImgPrincipal,  ");
			sbQuery.append(" varchar_FORMAT( N.FD_FECHA_PUBLICACION , 'DD-MM-YYYY') as fcFechaPublicacion ");
			sbQuery.append(" from WPDB2INS.UNO_MX_N_NOTA as N, ");
			sbQuery.append(" WPDB2INS.UNO_MX_C_CATEGORIA C,  ");
			sbQuery.append(" WPDB2INS.UNO_MX_C_SECCION S,  ");
			sbQuery.append(" WPDB2INS.UNO_MX_C_TIPO_SECCION TS  ");
			sbQuery.append(" where  ");
			sbQuery.append(" C.FC_ID_CATEGORIA=N.FC_ID_CATEGORIA  ");
			sbQuery.append(" AND C.FC_ID_SECCION=S.FC_ID_SECCION AND S.FC_ID_TIPO_SECCION=TS.FC_ID_TIPO_SECCION   ");
			sbQuery.append(" AND N.FI_BAN_INFINITO_HOME =1 ");
			sbQuery.append(" AND N.FC_ID_CONTENIDO NOT IN ( "+idContenidos+" ) ");
			sbQuery.append(" order by N.FD_FECHA_PUBLICACION desc ");
			sbQuery.append(" FETCH FIRST 100 ROWS ONLY ");
			

			return jdbcTemplate.query ( sbQuery.toString() ,
					//new Object [] { idContenidos} , 
					new BeanPropertyRowMapper<NotaDTO>( NotaDTO.class) );
			
		} catch (Exception e) {
			
			logger.error(" Error getNotasInfiniteLanding [DAO] ",e );
			logger.error("Error SQL: "+sbQuery);
			return Collections.emptyList();
		}
		
	}
	
	public  NotaDTO getNotaMagazinePreOlimpicos ( String idContenido ) 
	{
		try {
		
			StringBuffer sbQuery = new StringBuffer();	
			
			sbQuery.append(" select    ");
			//sbQuery.append(" S.FC_FRIENDLY_URL as fcIdSeccion, ");
			sbQuery.append(" 'preolimpicos' as fcTabla, ");
			sbQuery.append(" N.FC_ID_TIPO_NOTA as fcIdTipoNota,  ");
			sbQuery.append(" C.FC_DESCRIPCION as fcDescripcionCategoria,  ");
			sbQuery.append(" N.FC_ID_CONTENIDO as fcIdContenido,  ");
			sbQuery.append(" N.FC_TITULO as fcTitulo,  ");
			sbQuery.append(" REPLACE( REPLACE(N.FC_DESCRIPCION, Char(13), ''), Char(10),'')as fcDescripcion,  ");
			sbQuery.append(" N.FC_ID_CATEGORIA as fcIdCategoria,  ");
			//sbQuery.append(" S.FC_ID_SECCION as fcIdSeccion, ");
				
			sbQuery.append(" CASE  ");
			sbQuery.append("    WHEN S.FC_FRIENDLY_URL = 'programas' or S.FC_FRIENDLY_URL = 'capsulas'  ");
			sbQuery.append("       THEN ('http://olimpicos.clarosports.com/'||COALESCE(S.FC_FRIENDLY_URL,'')||'/'||COALESCE(C.FC_ID_CATEGORIA,'')|| '/'|| COALESCE(N.FC_NOMBRE,''))  ");
			sbQuery.append("    ELSE ('http://olimpicos.clarosports.com/'||COALESCE(S.FC_FRIENDLY_URL,'')||'/'|| COALESCE(N.FC_NOMBRE,''))  ");
			sbQuery.append(" END AS fcLinkDetalle,   ");
			sbQuery.append(" CASE  ");
			sbQuery.append(" WHEN INM.FC_URL_EXTERNA is null or INM.FC_URL_EXTERNA = '' THEN N.FC_NOMBRE  ");
			sbQuery.append(" ELSE COALESCE(INM.FC_URL_EXTERNA, '')  ");
			sbQuery.append(" END As fcNombre, ");
			sbQuery.append(" N.FC_IMAGEN_PRINCIPAL as fcImgPrincipal,  ");
			sbQuery.append(" INM.FI_BAN_PATROCINIO as fiBanPatrocinio,  ");
			sbQuery.append(" varchar_FORMAT( N.FD_FECHA_PUBLICACION , 'DD-MM-YYYY') as fcFechaPublicacion  ");
			sbQuery.append(" from WPDB2INS.PRE_OLI_N_NOTA as N,  ");
			sbQuery.append(" WPDB2INS.PRE_OLI_I_NOTA_MAGAZINE INM,  ");
			sbQuery.append(" WPDB2INS.PRE_OLI_C_CATEGORIA C,  ");
			sbQuery.append(" WPDB2INS.PRE_OLI_C_SECCION S ");
			sbQuery.append(" where INM.FC_ID_CONTENIDO=N.FC_ID_CONTENIDO  ");
			sbQuery.append(" AND C.FC_ID_CATEGORIA=N.FC_ID_CATEGORIA  ");
			sbQuery.append(" AND C.FC_ID_SECCION=S.FC_ID_SECCION ");
			sbQuery.append(" AND INM.FC_ID_CONTENIDO= ?  ");
			sbQuery.append(" order by N.FD_FECHA_PUBLICACION desc  ");
			

			List< NotaDTO > list=jdbcTemplate.query ( sbQuery.toString() ,
					new Object [] { idContenido,} , 
					new BeanPropertyRowMapper<NotaDTO>( NotaDTO.class) );
			if(list!=null && list.size()>0)
				return list.get(0);
		} catch (Exception e) {
			
			logger.error(" Error getNotaMagazine [DAO] ",e );
		}
		
		return null;
	}
	public  NotaDTO getNotaMagazine ( String idContenido, String idMagazine ) 
	{
		try {
		
			StringBuffer sbQuery = new StringBuffer();	
			
			sbQuery.append(" select ");  
			sbQuery.append(" 'noticias' as fcTabla, ");
			sbQuery.append(" N.FC_ID_TIPO_NOTA as fcIdTipoNota, ");
			sbQuery.append(" C.FC_DESCRIPCION as fcDescripcionCategoria, ");
			sbQuery.append(" N.FC_ID_CONTENIDO as fcIdContenido, ");
			sbQuery.append(" N.FC_TITULO as fcTitulo, ");
			sbQuery.append(" REPLACE( REPLACE(N.FC_DESCRIPCION, Char(13), ''), Char(10),'')as fcDescripcion, ");
			sbQuery.append(" N.FC_ID_CATEGORIA as fcIdCategoria, ");
			sbQuery.append(" ('http://www.unotv.com/'||COALESCE(TS.FC_ID_TIPO_SECCION,'')||'s/'||COALESCE(S.FC_FRIENDLY_URL,'')||'/'||COALESCE(C.FC_FRIENDLY_URL,'')||'/detalle/'|| COALESCE(N.FC_NOMBRE,'')) as fcLinkDetalle,  ");
			sbQuery.append(" CASE ");
			sbQuery.append(" WHEN INM.FC_URL_EXTERNA is null or INM.FC_URL_EXTERNA = '' THEN N.FC_NOMBRE ");
			sbQuery.append(" ELSE COALESCE(INM.FC_URL_EXTERNA, '') ");
			sbQuery.append(" END As fcNombre,");
			sbQuery.append(" N.FC_IMAGEN_PRINCIPAL as fcImgPrincipal, ");
			sbQuery.append(" INM.FI_BAN_PATROCINIO as fiBanPatrocinio, ");
			sbQuery.append(" varchar_FORMAT( N.FD_FECHA_PUBLICACION , 'DD-MM-YYYY') as fcFechaPublicacion ");
			sbQuery.append(" from WPDB2INS.UNO_MX_N_NOTA as N, ");
			sbQuery.append(" WPDB2INS.UNO_MX_I_NOTA_MAGAZINE INM, ");
			sbQuery.append(" WPDB2INS.UNO_MX_C_CATEGORIA C, ");
			sbQuery.append(" WPDB2INS.UNO_MX_C_SECCION S, ");
			sbQuery.append(" WPDB2INS.UNO_MX_C_TIPO_SECCION TS ");
			sbQuery.append(" where INM.FC_ID_CONTENIDO=N.FC_ID_CONTENIDO ");
			sbQuery.append(" AND C.FC_ID_CATEGORIA=N.FC_ID_CATEGORIA ");
			sbQuery.append(" AND C.FC_ID_SECCION=S.FC_ID_SECCION AND S.FC_ID_TIPO_SECCION=TS.FC_ID_TIPO_SECCION "); 
			sbQuery.append(" AND INM.FC_ID_CONTENIDO= ? ");
			sbQuery.append(" AND INM.FC_ID_MAGAZINE= ? ");
			sbQuery.append(" order by N.FD_FECHA_PUBLICACION desc ");

			List< NotaDTO > list=jdbcTemplate.query ( sbQuery.toString() ,
					new Object [] { idContenido, idMagazine} , 
					new BeanPropertyRowMapper<NotaDTO>( NotaDTO.class) );
			if(list!=null && list.size()>0)
				return list.get(0);
		} catch (Exception e) {
			
			logger.error(" Error getNotaMagazine [DAO] ",e );
		}
		
		return null;
	}
	
	public List<NotaDTO> getAllIdsNotesPublished(String idMagazine){
		StringBuffer sql=new StringBuffer();
		
		try {

			sql.append(" SELECT N.FC_ID_CONTENIDO as fcIdContenido, 'noticias' as fcImgPrincipal, N.FI_ORDEN ");
			sql.append(" FROM WPDB2INS.UNO_MX_I_NOTA_MAGAZINE N ");
			sql.append(" WHERE N.FC_ID_MAGAZINE= ? ");
			sql.append(" UNION  ");
			sql.append(" SELECT P.FC_ID_CONTENIDO as fcIdContenido, 'olimpicos_pre' as fcImgPrincipal, P.FI_ORDEN ");
			sql.append(" FROM WPDB2INS.PRE_OLI_I_NOTA_MAGAZINE P ");
			sql.append(" WHERE P.FC_ID_MAGAZINE= ? ");
			sql.append(" ORDER BY FI_ORDEN ASC ");

			/*sql.append(" SELECT  "); 
			sql.append(" FC_ID_CONTENIDO as fcIdContenido, ");
			sql.append(" FC_TABLA as fcTabla ");
			sql.append(" FROM WPDB2INS.UNO_MX_I_NOTA_MAGAZINE ");
			sql.append(" where FC_ID_MAGAZINE= ? ");
			sql.append(" order by FI_ORDEN ASC ");*/
			
			Object[] args = {idMagazine, idMagazine};
			
			return  ( List< NotaDTO > ) jdbcTemplate.query ( sql.toString() , args, new BeanPropertyRowMapper<NotaDTO>( NotaDTO.class) );
		} catch (Exception e) {
			logger.error("Error getAllIdsNotesPublished [DAO]: ",e);
			logger.error("Query: "+sql.toString());
		}
		return Collections.emptyList();
	}
	
	public boolean deleteAllPreOlimpicos(String idMagazine) {
		StringBuffer sql=new StringBuffer();
		Boolean flag=true;
		try {
			sql.append(" DELETE FROM WPDB2INS.PRE_OLI_I_NOTA_MAGAZINE ");
			sql.append(" WHERE FC_ID_MAGAZINE=? ");
			int row= jdbcTemplate.update(sql.toString(),new Object[]{idMagazine});
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.error("Error deleteAllPreOlimpicos [DAO]: ");
			flag=false;
			logger.error("Query: "+sql.toString());
			logger.error("idMagazine: "+idMagazine);
			logger.error("Error deleteAll [DAO]: ",e);
			
		}
		return flag;
	}
	
	public boolean deleteAllNoticias(String idMagazine) {
		StringBuffer sql=new StringBuffer();
		Boolean flag=true;
		try {
			sql.append(" DELETE FROM WPDB2INS.UNO_MX_I_NOTA_MAGAZINE ");
			sql.append(" WHERE FC_ID_MAGAZINE=? ");
			int row= jdbcTemplate.update(sql.toString(),new Object[]{idMagazine});
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.error("Error deleteAllNoticias [DAO]: ");
			flag=false;
			logger.error("Query: "+sql.toString());
			logger.error("idMagazine: "+idMagazine);
			logger.error("Error deleteAll [DAO]: ",e);
			
		}
		return flag;
	}
	
	public boolean insertNotaMagazine(String idContenido, String idMagazine, int orden, int patrocinio, String UrlExterna) {
		StringBuffer sql=new StringBuffer();
		Boolean flag=false;
		try {
			sql.append(" INSERT INTO WPDB2INS.UNO_MX_I_NOTA_MAGAZINE ( FC_ID_CONTENIDO, FC_ID_MAGAZINE, FD_FECHA_REGISTRO, FI_ORDEN, FI_BAN_PATROCINIO, FC_URL_EXTERNA )");
			sql.append(" values ( ?, ?, CURRENT TIMESTAMP, ?, ?, ?) ");
			 Object []datos= new Object[] { 
					 idContenido,
					 idMagazine,
					 orden,
					 patrocinio,
					 UrlExterna
						};
			int row= jdbcTemplate.update(sql.toString(),datos);
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.debug("Error insertNotaMagazine [DAO]: ");
			logger.error("Query: "+sql.toString());
			logger.error("idContenido: "+idContenido);
			logger.debug("Error insertNotaMagazine [DAO]: ",e);
			
		}
		return flag;
	}
	public boolean insertNotaMagazinePreOlimpicos(String idContenido, String idMagazine, int orden, int patrocinio, String UrlExterna) {
		StringBuffer sql=new StringBuffer();
		Boolean flag=false;
		try {
			sql.append(" INSERT INTO WPDB2INS.PRE_OLI_I_NOTA_MAGAZINE ( FC_ID_CONTENIDO, FC_ID_MAGAZINE, FD_FECHA_REGISTRO, FI_ORDEN, FI_BAN_PATROCINIO, FC_URL_EXTERNA )");
			sql.append(" values ( ?, ?, CURRENT TIMESTAMP, ?, ?, ?) ");
			 Object []datos= new Object[] { 
					 idContenido,
					 idMagazine,
					 orden,
					 patrocinio,
					 UrlExterna
						};
			int row= jdbcTemplate.update(sql.toString(),datos);
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.debug("Error insertNotaMagazine [DAO]: ");
			logger.error("Query: "+sql.toString());
			logger.error("idContenido: "+idContenido);
			logger.debug("Error insertNotaMagazine [DAO]: ",e);
			
		}
		return flag;
	}
	public List < CategoriaDTO > getTipoSecciones () 
	{
		StringBuffer query = new StringBuffer();
		try {
			query.append(" select FC_ID_TIPO_SECCION as fcIdTipoSeccion ");
			query.append(" from WPDB2INS.UNO_MX_C_TIPO_SECCION ");
			query.append(" where FI_ESTATUS=1 ");
			query.append(" order by FC_ID_TIPO_SECCION asc ");
			return  ( List< CategoriaDTO > ) jdbcTemplate.query ( query.toString() , new BeanPropertyRowMapper<CategoriaDTO>( CategoriaDTO.class) );
			
		} catch (Exception e) {
			logger.error("SQL: "+query);
			logger.error(" Error getSecciones [DAO] ",e );
		}
		
		return Collections.emptyList();
	}
	public List < CategoriaDTO > getSecciones (  String fcIdTipoSeccion) 
	{
		StringBuffer query = new StringBuffer();
		try {
			query.append(" select FC_ID_SECCION as fcIdSeccion, FC_FRIENDLY_URL as fcFriendlyURL ");
			query.append(" from WPDB2INS.UNO_MX_C_SECCION ");
			query.append(" where FI_ESTATUS=1 and FC_ID_TIPO_SECCION='"+fcIdTipoSeccion+"' ");
			query.append(" order by FC_ID_SECCION asc ");
			return  ( List< CategoriaDTO > ) jdbcTemplate.query ( query.toString() , new BeanPropertyRowMapper<CategoriaDTO>( CategoriaDTO.class) );
			
		} catch (Exception e) {
			logger.error("SQL: "+query);
			logger.error(" Error getSecciones [DAO] ",e );
		}
		
		return Collections.emptyList();
	}
	
	public List < CategoriaDTO > getCategorias (String fcIdSeccion  ) 
	{
		StringBuffer query = new StringBuffer();
		try {
			query.append(" select FC_ID_CATEGORIA as fcIdCategoria, FC_ID_SECCION as fcIdSeccion, FC_DESCRIPCION as fcDescripcion, FC_FRIENDLY_URL as fcFriendlyURL ");
			query.append(" from WPDB2INS.UNO_MX_C_CATEGORIA ");
			query.append(" where FI_ESTATUS=1 and FC_ID_SECCION='"+fcIdSeccion+"' ");
			query.append(" order by FC_ID_CATEGORIA asc ");
			return  ( List< CategoriaDTO > ) jdbcTemplate.query ( query.toString() , new BeanPropertyRowMapper<CategoriaDTO>( CategoriaDTO.class) );
			
		} catch (Exception e) {
			logger.error("SQL: "+query);
			logger.error(" Error getCategorias [DAO] ",e );
		}
		
		return Collections.emptyList();
	}
	public List < NotaDTO > getNotasByCategoria ( String idCategoria , Integer pagina , Integer resultadosPorPagina ) 
	{
		StringBuffer query = new StringBuffer();
		try {
			
			Integer paginaInicial = ( pagina * resultadosPorPagina ) - ( resultadosPorPagina -1 );
			Integer paginaFinal	  = ( pagina * resultadosPorPagina );  

			
			
			query.append(" SELECT * FROM (  ");
			query.append(" select  ");
			query.append(" 'noticias' as fcTabla, ");
			query.append(" ('http://www.unotv.com/'||COALESCE(TS.FC_ID_TIPO_SECCION,'')||'s/'||COALESCE(S.FC_FRIENDLY_URL,'')||'/'||COALESCE(C.FC_FRIENDLY_URL,'')||'/detalle/'|| COALESCE(N.FC_NOMBRE,'')) as fcLinkDetalle,  ");
			query.append(" N.FC_ID_TIPO_NOTA as fcIdTipoNota, ");
			query.append(" C.FC_DESCRIPCION as fcDescripcionCategoria,");
			query.append(" N.FC_ID_CONTENIDO as fcIdContenido, ");
			query.append(" N.FC_TITULO as fcTitulo, ");
			//query.append(" N.FC_DESCRIPCION as fcDescripcion, ");
			query.append(" REPLACE( REPLACE(N.FC_DESCRIPCION, Char(13), ''), Char(10),'')as fcDescripcion, ");
			query.append(" N.FC_ID_CATEGORIA as fcIdCategoria, ");
			query.append(" N.FC_NOMBRE as fcNombre, ");
			query.append(" N.FC_IMAGEN_PRINCIPAL as fcImgPrincipal, ");
			query.append(" varchar_FORMAT( N.FD_FECHA_PUBLICACION , 'DD-MM-YYYY') as fcFechaPublicacion, ");
			query.append(" ROW_NUMBER() OVER(ORDER BY N.FD_FECHA_PUBLICACION desc) AS rownumber ");
			query.append(" from WPDB2INS.UNO_MX_N_NOTA N, ");
			query.append(" WPDB2INS.UNO_MX_C_CATEGORIA C, ");
			query.append(" WPDB2INS.UNO_MX_C_SECCION S, ");
			query.append(" WPDB2INS.UNO_MX_C_TIPO_SECCION TS  ");
			query.append(" where N.FC_ID_CATEGORIA = ? ");
			query.append(" AND C.FC_ID_CATEGORIA=N.FC_ID_CATEGORIA ");
			query.append(" AND C.FC_ID_SECCION=S.FC_ID_SECCION AND S.FC_ID_TIPO_SECCION=TS.FC_ID_TIPO_SECCION ");
			query.append(" order by N.FD_FECHA_PUBLICACION desc ");
			query.append(" ) AS xxx ");
			query.append(" WHERE rownumber BETWEEN ? AND ?");
			
			return  ( List< NotaDTO > ) jdbcTemplate.query ( query.toString() ,
																	new Object [] { idCategoria, paginaInicial , paginaFinal } , 
																	new BeanPropertyRowMapper<NotaDTO>( NotaDTO.class) );
			
		} catch (Exception e) {
			logger.error("SQL: "+query);
			logger.error("Error getNotasByCategoria [DAO] ",e );
		}
		
		return Collections.emptyList();
	}
	
	public Integer getTotalNotasByCategoria ( String idCategoria  ) 
	{
		StringBuffer sbQuery = new StringBuffer();
		try {
			
			sbQuery.append(" select count( * ) ");					
			sbQuery.append(" from wpdb2ins.UNO_MX_N_NOTA  ");
			sbQuery.append(" where FC_ID_CATEGORIA = ?  ");
			
			return  ( Integer ) jdbcTemplate.queryForInt ( sbQuery.toString() , new Object []{ idCategoria } );
			
		} catch (Exception e) {
			logger.error("SQL: "+sbQuery);
			logger.error(" Error getTotalNotasByCategoria [DAO] ",e );
		}
		
		return 0;
	}
	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
}
