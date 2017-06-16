package mx.com.amx.unotv.backoffice.dao;

import mx.com.amx.unotv.backoffice.dto.NotaSmsDTO;
import mx.com.amx.unotv.backoffice.exception.DAOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Qualifier("notasmsDAO")
public class NotaSmsDAO {
	
	private JdbcTemplate jdbcTemplate;
	private Logger logger=Logger.getLogger(NotaSmsDAO.class);
	
	public boolean insertNotaSMS(NotaSmsDTO dto, String id_nota) throws DAOException {
		StringBuffer sql=new StringBuffer();
		Boolean flag=false;
		try {
			sql.append(" INSERT INTO WPDB2INS.UNO_MX_N_SMS ");
			sql.append(" (FC_FECHA, FC_ID_ESTADO, FC_ID_NOTA,  ");
			sql.append(" FC_USUARIO, FD_FECHA_REGISTRO ) ");
			sql.append(" VALUES (? , ? ,? ,? , CURRENT TIMESTAMP) ");
			
			Object [] listObjects={
				dto.getFecha(),
				dto.getId_estado(),
				id_nota,
				dto.getUsuario()
			};
			int rows=jdbcTemplate.update(sql.toString(), listObjects);
			/*int [] rows=jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					NotaSmsDTO n = listNotas.get(i);
					ps.setString(1, n.getFecha());
					ps.setString(2, n.getId_estado());
					ps.setString(3, n.getId_nota() );
					ps.setString(4, n.getUsuario() );
				}
				
				@Override
				public int getBatchSize() {
					return listNotas.size();
				}
			});*/
			
			if(rows>0)
				flag=true;
			
		} catch (Exception e) {
			logger.error("Query: "+sql.toString());
			logger.error("Error insertNotaSMS [DAO]: ",e);
			throw new DAOException(e.getMessage());
		}
		return flag;
	}
	
	public boolean insertHistoricoNotaSMS(NotaSmsDTO dto, String id_nota) throws DAOException {
		StringBuffer sql=new StringBuffer();
		Boolean flag=false;
		try {
			sql.append(" INSERT INTO WPDB2INS.UNO_MX_H_SMS ");
			sql.append(" (FC_FECHA, FC_ID_ESTADO, FC_ID_NOTA,  ");
			sql.append(" FC_USUARIO, FD_FECHA_REGISTRO ) ");
			sql.append(" VALUES (? , ? ,? ,?, CURRENT TIMESTAMP) ");
			Object [] listObjects={
					dto.getFecha(),
					dto.getId_estado(),
					id_nota,
					dto.getUsuario()
				};
				int rows=jdbcTemplate.update(sql.toString(), listObjects);
				
			if(rows>0)
				flag=true;
		} catch (Exception e) {
			logger.error("Query: "+sql.toString());
			logger.error("Error insertNotaSMS [DAO]: ",e);
			throw new DAOException(e.getMessage());
		}
		return flag;
	}
	
	public boolean deleteAllNotaSMS(NotaSmsDTO dto) throws DAOException {
		StringBuffer sql=new StringBuffer();
		Boolean flag=true;
		try {
			sql.append(" DELETE FROM WPDB2INS.UNO_MX_N_SMS ");
			sql.append(" WHERE FC_FECHA=? and FC_ID_ESTADO=? ");
			int row= jdbcTemplate.update(sql.toString(),new Object[]{dto.getFecha(), dto.getId_estado()});
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.error("Query: "+sql.toString());
			logger.error("Error deleteAllNotaSMS [DAO]: ",e);
			throw new DAOException(e.getMessage());
			
		}
		return flag;
	}
	
	public boolean deleteAllNotaHistoricoSMS(NotaSmsDTO dto) throws DAOException {
		StringBuffer sql=new StringBuffer();
		Boolean flag=true;
		try {
			sql.append(" DELETE FROM WPDB2INS.UNO_MX_H_SMS ");
			sql.append(" WHERE FC_FECHA=? and FC_ID_ESTADO=? ");
			int row= jdbcTemplate.update(sql.toString(),new Object[]{dto.getFecha(), dto.getId_estado()});
			if(row>0){
				flag=true;
			}
		} catch (Exception e) {
			logger.error("Query: "+sql.toString());
			logger.error("Error deleteAllNotaSMS [DAO]: ",e);
			throw new DAOException(e.getMessage());
			
		}
		return flag;
	}
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
