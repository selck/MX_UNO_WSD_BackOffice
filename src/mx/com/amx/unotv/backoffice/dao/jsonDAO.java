package mx.com.amx.unotv.backoffice.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Qualifier("parametrosDAO")
public class jsonDAO {
	
	private JdbcTemplate jdbcTemplate;
	private Logger logger=Logger.getLogger(jsonDAO.class);
	
	public boolean insertParametro(String idParametro, String valorParametro) {
		StringBuffer sql=new StringBuffer();
		Boolean flag=false;
		try {
			sql.append(" INSERT INTO WPDB2INS.UNO_MX_C_PARAMETROS ");
			sql.append(" (FC_ID_PARAMETRO, FC_VALOR )  ");
			sql.append(" VALUES (? , ?) ");
			
			int row= jdbcTemplate.update(sql.toString(),new Object[]{idParametro, valorParametro});
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.error("Query: "+sql.toString());
			logger.error("Error insertParametro [DAO]: ",e);
		}
		return flag;
	}
	
	public boolean updateParametro(String idParametro, String valorParametro) {
		StringBuffer sql=new StringBuffer();
		Boolean flag=false;
		try {
			sql.append(" UPDATE WPDB2INS.UNO_MX_C_PARAMETROS SET");
			sql.append(" FC_VALOR = ?  ");
			sql.append(" WHERE FC_ID_PARAMETRO=? ");
			
			int row= jdbcTemplate.update(sql.toString(),new Object[]{valorParametro, idParametro});
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.error("Query: "+sql.toString());
			logger.error("Error updateParametro [DAO]: ",e);
		}
		return flag;
	}
}
	