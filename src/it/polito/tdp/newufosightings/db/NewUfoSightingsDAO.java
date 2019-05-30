package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Confine;
import it.polito.tdp.newufosightings.model.PesiStato;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates(Map<String,State> idMap) {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(idMap.get(rs.getString("id"))== null) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
				idMap.put(state.getId(), state);
			}else {
				result.add(idMap.get(rs.getString("id")));
			}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> getForme(String anno) {
		
		String sql="select distinct shape as forma " + 
				"from sighting s " + 
				"where year(s.datetime)=? " + 
				"order by s.shape asc ";
		
		List<String> shape= new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(anno));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				shape.add(rs.getString("forma"));
				
			}

			conn.close();
			return shape;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	
	}

	public List<Confine> getConfini(Map<String,State> idMap ) {
		String sql="select state1, state2 " + 
				"from neighbor n " + 
				"where n.state1 > n.state2 ";
		
		List<Confine> result= new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				State s1= idMap.get(rs.getString("state1"));
				State s2=idMap.get(rs.getString("state2"));
				
				if(s1 != null && s2 != null) {
					Confine c= new Confine(s1, s2);
					result.add(c);
				}
			
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		
	}

	public List<PesiStato> getPeso(String anno, String forma, Map<String,State> idMap){
		
		String sql="select st.id s1, count(*) as peso\n" + 
				"from sighting s, state st\n" + 
				"where s.shape =? and year(s.datetime)=? and s.state=st.id\n" + 
				"group by st.id\n";
		
		List<PesiStato> result=new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, forma);
			st.setInt(2, Integer.parseInt(anno));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				State s= idMap.get(rs.getString("s1"));
				if (s!= null) {
					PesiStato ps=new PesiStato(s, rs.getDouble("peso"));
					result.add(ps);
				}
				
				
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		

		
	}
	
	

}
