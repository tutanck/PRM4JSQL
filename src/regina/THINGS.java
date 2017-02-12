package regina;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import db.tools.DBToolBox;
import db.tools.DbException;
import db.tools.Sprint;

/**
 * @author AJoan
 * With THINGS almost everything is string because of all the SQL business code lines are strings,
 *  so I see no reason why we bothers to types (true mostly for CUD (push actions)
 *  as usual Read is much more difficult to make generic  )
 *  The basic idea is we have earthlings who want to leave the earth to Mars
 *  @see http://www.spacex.com/ (it is a tribute to Elon Musk))
 *  and become Martians. Why ? I don't really know but 
 *  imagine earthlings represent data (from java representation) we have to send
 *  to Mars that represent Database(unknown representation) 
 *  whose we don't know typeSet but at least we know the fields position)
 *  it's enough to put the rocket on the ground ...
 *  We don't have complete representation of database but we have the different locations where we can land
 *  We also know that any solid object heavy enough can land no matters his size, or type, or representation on Mars 
 *  Well that's the same with strings (they are heavy enough to be supported on hearth and on Mars)
 *  So it begin to become easier, we can send Strings understood on JAVA and DB and the DB will automatically convert and accept any String 
 *  in the appropriate DB representation.
 *  The most difficult is not the driveway but the return ...
    ** *** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** ** * ** ** ** ** ** ** ** ** ** ** ** ** ** 
 * Using THINGS allows : 
 * More logical not concerning database in services
 * No need to know SQL CUD (Create Update Delete) syntax (only read operations are up to the programmer)
 * Note : it's a beginning THINGS do not perform yet complexes request or operation in database
 * It works just like a stupid shuttle bus for data transportation  
 */
public class THINGS{


	/**
	 * @DESCRIPTION insert things from the map in the table using SQL insert requests
	 * @param things 
	 * @param table
	 * @param caller
	 * @throws DbException
	 */
	public static void addTHINGS(Map<String,String> things, String table,String caller) throws DbException{ 
		//Task Definition
		String insertPart1="INSERT INTO "+table+" (";
		String insertPart2=") VALUES (";
		Iterator<Map.Entry<String, String>> it = things.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String,String> valuedField= it.next();
			insertPart1+=valuedField.getKey();
			insertPart2+="'"+valuedField.getValue()+"'";
			if(it.hasNext()){insertPart1+=", ";insertPart2+=", ";}
			//it.remove(); // avoids a ConcurrentModificationException
		}
		String task=insertPart1+insertPart2+");";
		System.out.println("task="+task);
		//Task Execution 
		CRUD.CRUDPush(task,caller);
	}


	/**
	 * @DESCRIPTION update things from the map in the table using SQL update request
	 * @param things
	 * @param where
	 * @param table
	 * @param caller
	 * @throws DbException
	 */
	public static void updateTHINGS(Map<String,String> things,Map<String,String> where, String table,String caller) throws DbException{ 
		//Task Definition
		String update="UPDATE "+table+" SET ";
		String[]whr ={" WHERE "};  //array to avoid passing parameters by value

		Iterator<Map.Entry<String, String>> it = things.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String,String> valuedField= it.next();
			update+=valuedField.getKey()+" = '"+valuedField.getValue()+"'";
			if(it.hasNext()){update+=", ";}
			//it.remove(); // avoids a ConcurrentModificationException
		}

		//Need a Quick Job to generate where request's chunk
		Thread sprinter= new Sprint(whr){
			@Override
			public void run() {	
				Iterator<Map.Entry<String, String>> it = where.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String,String> valuedField= it.next();
					whr[0]+=valuedField.getKey()+" = '"+valuedField.getValue()+"'";
					if(it.hasNext()){whr[0]+=" AND ";}
					//it.remove(); // avoids a ConcurrentModificationException
				}
			}
		};sprinter.start();
		try {sprinter.join();} //sync main and parallel (wait end of parallel exec whose you need the result)
		catch (InterruptedException e) {
			throw new DbException("@!updateThings JavaConcurrentError :"+DBToolBox.getStackTrace(e));} 

		String task=update+whr[0]+" ;";
		System.out.println("task="+task);
		//Task Execution 
		CRUD.CRUDPush(task,caller);
	}



	/**
	 * @DESCRIPTION match ONE(yes one not two) or more things together (
	 * 	if the SQL request generated finds all matchings in the table it returns true else false )
	 * Note : if you match just One thing it is equivalent to verify its presence in the table
	 * NB: A thing is literally an entry in the map (<K,V>)
	 * @param where
	 * @param table
	 * @param caller
	 * @throws DbException
	 */
	public static boolean matchTHINGS(Map<String,String> where, String table,String caller) throws DbException{ 
		//Task Execution 
		return CRUD.CRUDCheck(getTHINGS(where,table),caller);
	}


	/**
	 * @DESCRIPTION returns things in the map from the table using SQL select requests 
	 * @param where
	 * @param table
	 * @return
	 */
	public static String getTHINGS(Map<String,String> where,String table){
		String select="SELECT * FROM "+table+" WHERE ";

		Iterator<Map.Entry<String, String>> it = where.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String,String> valuedField= it.next();
			select+=valuedField.getKey()+" = '"+valuedField.getValue()+"'";
			if(it.hasNext()){select+=" AND ";}
			//it.remove(); // avoids a ConcurrentModificationException
		}
		String task =select+" ;";
		System.out.println("task="+task);
		return task;
	}


	/**
	 * @DESCRIPTION remove things from the table using SQL delete requests
	 * @param where
	 * @param table
	 * @return
	 * @throws DbException 
	 */
	public static void removeTHINGS(Map<String,String> where,String table,String caller) throws DbException{
		String delete="DELETE FROM "+table+" WHERE ";

		Iterator<Map.Entry<String, String>> it = where.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String,String> valuedField= it.next();
			delete+=valuedField.getKey()+" = '"+valuedField.getValue()+"'";
			if(it.hasNext()){delete+=" AND ";}
			//it.remove(); // avoids a ConcurrentModificationException
		}
		String task =delete+" ;";
		System.out.println("task="+task);
		//Task Execution 
		CRUD.CRUDPush(task,caller);		
	}


	public static void main(String[] args) throws DbException {
		//static initialiser : 
		final Map<String,String> map=new HashMap<String,String>();
		map.put("uid", "AGDLNBNVJBFDKBNFB651B1DFVKVHTE");
		map.put("lastname", "ANAGBLA");
		map.put("firstname", "Joan");
		//addTHINGS(map,"USERS","THINGS");
		final Map<String,String> map2=new HashMap<String,String>();
		map2.put("pass", "LOL");
		map2.put("email", "joancom");
		map2.put("phone", "0695919815");
		//updateTHINGS(map,map2,"USERS","THINGS");
		matchTHINGS(map2,"USERS","THINGS");
		//removeTHINGS(map2,"TABLE","caller");
	}
}