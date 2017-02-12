package regina;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.tools.DBConnectionManager;
import db.tools.DbException;

/**
 * CRUD is acronym for Create - Read - Update - Delete
 * We will try to make generic functions playing that four role like Hibernate does
 * It's really boring and time consuming to repeat same actions all time instead of mutualise them one for all  
 * @author AJoan
 */
public class CRUD {

	/**
	 * That one was easy to find : push actions don't need to return specific 
	 * results , so i just have to put in parameter the task we want to execute
	 * and that's it   
	 * @param table
	 * @param task
	 * @throws DbException
	 */
	public static void CRUDPush(String task, String caller) throws DbException{ 
		System.out.println("exec="+task);
		try {
			Connection c = DBConnectionManager.getMySQLDBConnection();
			Statement s = c.createStatement();
			s.executeUpdate(task);
			s.close();
			c.close();
		} catch (SQLException e) {//@CRUDPush means from method CRUDPush and ! means that method modify database (it's not a request (POST's Style))
			// so here for example, we have an error from a method called CRUDPush that modify the database state
			throw new DbException("@!CRUDPush/"+caller+" Error : " + e.getMessage());}
	}

	/**
	 * That is much more difficult, we have to return results , but we don't know
	 * the type of the results in advance... 
	 * There are two ways to fix it :
	 * 
	 * 1)With java reflect, invoke the method that needs the results (service method)
	 * it would be fun (especially that I love the java reflexivity)
	 * but invocation are slower than direct call (seen in "Compilation Avancée"(M1)) : 
	 * From Stack Overflow : http://stackoverflow.com/questions/435553/java-reflection-performance
	 * (Because reflection involves types that are dynamically resolved,
	 * certain Java virtual machine optimizations can not be performed.
	 * Consequently, reflective operations have slower performance than their non-reflective counterparts,
	 * and should be avoided in sections of code which are called frequently in performance-sensitive applications.)
	 *   **  **  **  **  **  **  **  **  **  **  **  **  **  **  **  **  **  **
	 * 2)return the resultSet to the caller and the caller close the resultSet, the statement, etc
	 * Not pretty but should works. 
	 * According to oracle, (When a Statement object is closed, its current ResultSet object, if one exists, is also closed.)
	 * So we could just close the associated statement but (Oracle may have problems and you might have to explicitly close the ResultSet.)
	 * http://stackoverflow.com/questions/10602271/automatic-closing-of-result-set-in-java-sql
	 * 
	 * ** 2")We could also possibly try to copy the resultSet, but it seems to be difficult as ResultSet is complex, 
	 *  and the possibilities offered by the resultSet should be reduced restricted a.
	 *  "And given the time allotted for this project I do not have much more time 
	 *  to study in depth the resultSet It's a pity! LOL"
	 * @param table
	 * @param query
	 * @throws DbException
	 */
	public static CSRShuttleBus CRUDPull(String query) throws DbException{ 
		System.out.println("exec="+query);
		try {
			Connection c = DBConnectionManager.getMySQLDBConnection();
			Statement s = c.createStatement();
			return new CSRShuttleBus(c, s, s.executeQuery(query));
			//Delegated lines of code
			//rs.close();
			//s.close();
			//c.close();
		} catch (SQLException e) {throw new DbException(
				//@CRUDPush means from method CRUDPush and ? means that method don't modify database(it's a request (GET's Style))
				// so here for example, we have an error from a method called CRUDPull that don't modify the database state
				"@?CRUDPull Error : " + e.getMessage());}
	}
	
	
	/**
	 * I realized that to just check for a result could also fully 
	 * make generic the code, so I made an Additional function for this specific
	 * case, because we obviously know the result type : BOOLEAN of course
	 * @param query
	 * @return
	 * @throws DbException
	 */
	public static Boolean CRUDCheck(String query, String caller) throws DbException{ 
		System.out.println("exec="+query);
		boolean answer=false;
		try {
			Connection c = DBConnectionManager.getMySQLDBConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(query);
			answer=rs.next();
			rs.close();
			s.close();
			c.close();
			return answer;
		} catch (SQLException e) {throw new DbException(
				//@CRUDPush means from method CRUDPush and ? means that method don't modify database(it's a request (GET's Style))
				// so here for example, we have an error from a method called CRUDCheck that don't modify the database state
				"@?CRUDCheck/"+caller+" Error : "+e.getMessage());}
	}
}
