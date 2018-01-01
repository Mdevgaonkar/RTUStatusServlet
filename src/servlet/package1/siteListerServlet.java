package servlet.package1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class siteListerServlet
 */
@WebServlet("/siteListerServlet")
public class siteListerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public siteListerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		

		Enumeration userNames = request.getParameterNames();
        String user[] = new String[10];
        String param=null;
        int i = 0;
        while (userNames.hasMoreElements()) {
            String userName = (String) userNames.nextElement();
            param=userName;
            //System.out.println(paramName);
            String[] userValues = request.getParameterValues(userName);
            user[i] = userValues[0];
            
 
            //System.out.println(params[i]);
            i++;
 
        }
        
        if (param.matches("gid")) {
        	writeSitesJSON(user, response);
		}	
        else if (param.matches("n")) {
        	writeSiteInfoJSON(user, response);
		}
        else if (param.matches("timeto")) {
        	writeSiteInfoJSONInRange(user, response);
		}
	 

       
		
		
		
	}

	private void writeSiteInfoJSONInRange(String[] user,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String paramName=user[0];
		String id=user[1];
		String from=user[2];
		String to=user[3];
		long longFrom=Long.valueOf(from);
		long longto=Long.valueOf(to);
		
		if (longFrom>longto) {
			long temp;
			temp=longFrom;
			longFrom=longto;
			longto=temp;
		}
		String[] params = new String[]{"oil_pressure", "coolant_temperature", "dg1_fuel_level_per", "dg1_battery_voltage", "coolant_pressure1",
	            "inlet_manfold_temp1", "engine_run_time", "dg_kvah", "number_of_starts", "fuel_used"};
		String table = null;
		if(paramName.matches(params[0]) | paramName.matches(params[1]) | paramName.matches(params[4]) | paramName.matches(params[5]) | paramName.matches(params[6]) | paramName.matches(params[8]) | paramName.matches(params[9])){
			table="RTU_DATA1";
		}else if(paramName.matches(params[2]) | paramName.matches(params[3]) | paramName.matches(params[7]) ){
			table="RTU_DATA";
		}
		

		JSONObject json = new JSONObject();
		//json.put("key","up to1");	
		int count=0;
		String sql1 = "SELECT "+paramName+" , DATA_TIME FROM "+table+" WHERE RTU_ID =? AND DATA_TIME >? AND DATA_TIME <=? ORDER BY DATA_TIME ASC";
			 
			 Connection con = DBconnector.getConnection();
			  
			 try {
					PreparedStatement	ps1 = con.prepareStatement(sql1);
					
					ps1.setString(1, id);
					ps1.setString(2, ""+longFrom);
					ps1.setString(3, ""+longto);
					//json.put("key",ps1.toString());
					ResultSet rs1 = ps1.executeQuery();
					//json.put("key","up to");
					JSONArray arrayJSON=new JSONArray();
					
					
					while(rs1.next())
	            	{
	            		JSONObject temp=new JSONObject();
	            		temp.put("c", count);
	            		temp.put("paramValue", rs1.getString(paramName));
	            		temp.put("paramTime", rs1.getString("DATA_TIME"));
	            		arrayJSON.add(temp);
	            		count++;
						
	            		
	            	}
					
	                
	                json.put("Data Series",arrayJSON);
	               // json.put("key","up to2");
					
					} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
	            
				
	        
		
	}

	private void writeSiteInfoJSON(String[] user, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		JSONObject json = new JSONObject();
		
		 String sql1 = "SELECT * FROM `RTU_DATA1` WHERE RTU_ID =? ORDER BY DATA_TIME DESC LIMIT 1";
		 String sql2 = "SELECT * FROM `RTU_DATA` WHERE RTU_ID =? ORDER BY DATA_TIME DESC LIMIT 1";
	        Connection con = DBconnector.getConnection();
	         
	        
	            
				try {
					PreparedStatement	ps1 = con.prepareStatement(sql1);
					
					ps1.setString(1, user[0]);
					
					ResultSet rs1 = ps1.executeQuery();
					
					JSONArray arrayJSON=new JSONArray();
					JSONObject arrayObject = new JSONObject();
					
					while(rs1.next())
	            	{
	            		
	            		arrayObject.put("oil_pressure", rs1.getString(1));
	            		arrayObject.put("coolant_temperature", rs1.getString(2));
	            		//
	            		//
	            		arrayObject.put("coolant_pressure1", rs1.getString(3));
	            		arrayObject.put("inlet_manfold_temp1", rs1.getString(4));
	            		arrayObject.put("engine_run_time", rs1.getString(5));
	            		//
	            		arrayObject.put("number_of_starts", rs1.getString(6));
	            		arrayObject.put("fuel_used", rs1.getString(7));
	            		arrayObject.put("data_time", rs1.getString(9));
						
					/*	arrayJSON.add(rs1.getString(1));
						arrayJSON.add(rs1.getString(2));
						arrayJSON.add(rs1.getString(3));
						arrayJSON.add(rs1.getString(4));
						arrayJSON.add(rs1.getString(5));
						arrayJSON.add(rs1.getString(6));
						arrayJSON.add(rs1.getString(7));*/
	            		
	            	}
					PreparedStatement	ps2 = con.prepareStatement(sql2);
					ps2.setString(1, user[0]);
					ResultSet rs2 = ps2.executeQuery();
	                while (rs2.next()) {
	                	
	                	arrayObject.put("fuel_level", rs2.getString(1));
	                	arrayObject.put("dg1_battery_voltage", rs2.getString(2));
	                	arrayObject.put("dg_kvah", rs2.getString(3));
	                	arrayJSON.add(arrayObject);
	                	
	                	/*arrayJSON.add(rs2.getString(1));
						arrayJSON.add(rs2.getString(2));
						arrayJSON.add(rs2.getString(3));*/
					}
	                json.put("SiteInfo",arrayJSON);
	                //json.put("info",disp);
	            
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
	         
	        
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        try {
				response.getWriter().write(json.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        

       
		
		
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	public void writeSitesJSON(String[] user, HttpServletResponse response ) {
		JSONObject json = new JSONObject();
		
		 String sql = "SELECT RTU_NAME, RTU_LOCATION, RTU_ID FROM `RTU` WHERE RTU_USER =? AND ACTIVE_FLG =? AND RTU_GROUP_ID =?";
	        Connection con = DBconnector.getConnection();
	         
	        
	            
				try {
					PreparedStatement	ps = con.prepareStatement(sql);
					ps.setString(1, user[0]);
					ps.setString(2, user[1]);
					ps.setString(3, user[2]);
					ResultSet rs2 = ps.executeQuery();
					JSONArray arrayJSON=new JSONArray();
					while(rs2.next())
	            	{
	            		JSONObject arrayObject = new JSONObject();
	            		arrayObject.put("siteName", rs2.getString(1));
	            		arrayObject.put("siteLocation", rs2.getString(2));
	            		arrayObject.put("siteID", rs2.getString(3));
	            		//arrayObject.put("siteNumber", rs2.getString(4));
	            		//arrayObject.put("siteUser", rs2.getString(4));
	            		
	            		arrayJSON.add(arrayObject);
	            	}
	                
	                json.put("Sites",arrayJSON);
	                //json.put("info",disp);
	            
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
	         
	        
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        try {
				response.getWriter().write(json.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
		
	}

}
