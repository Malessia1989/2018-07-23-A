package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.model.Confine;
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
				
				idMap.put(rs.getString("id"), state);
				result.add(state);
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

	public List<String> getShape(String annoInserito) {
		
		String sql="select s.shape as forma, year(s.datetime) as anno\n" + 
				"from sighting s\n" + 
				"where year(s.datetime)= ? " + 
				"order by anno , forma asc";
		
		List<String> forme = new LinkedList<String>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(annoInserito));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String forma = rs.getString("forma");
				if (!forme.contains(forma))
					forme.add(forma);

			}

			conn.close();
			return forme;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

	}

	public List<Confine> getArcoEPeso( String forma,String anno) {
		
		String sql="select n1.state1 s1 ,n1.state2 s2, count(*) peso " + 
				"from sighting s, neighbor n1 " + 
				"where s.shape = ? and year(s.datetime) =?  " + 
				"and (s.state=n1.state1 or s.state=n1.state2 ) " + 
				"group by n1.state1,n1.state2 ";
		List<Confine> result=new LinkedList<Confine>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, forma);
			st.setInt(2, Integer.parseInt(anno));
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Confine c= new Confine(  rs.getString("s1"), rs.getString("s2"), rs.getInt("peso"));
				result.add(c);
			
				

			}

			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return result;
		
	}

}
