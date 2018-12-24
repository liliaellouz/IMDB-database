
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class PredQuer extends JPanel{

	private JTextArea result = new JTextArea(10, 50);
	private JScrollPane resultScroll = new JScrollPane(result);
	private JLabel label = new JLabel("Queries:");
	private JComboBox<String> combo = new JComboBox<String>();
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private JLabel feedback = new JLabel();
	
	private Connection conn = null;
	
	public static String[] queries = {"QUERY1", "QUERY2", "QUERY3", "QUERY4",
			"QUERY5", "QUERY6", "QUERY7", "QUERY8","QUERY9", "QUERY10", 
			"QUERY11", "QUERY12","QUERY13", "QUERY14", "QUERY15", "QUERY16", 
			"QUERY17", "QUERY18", "QUERY19", "QUERY20"};
	
	public PredQuer(){
		
		add(label);
		Fenetre.fillComboWithStrings(combo, queries);
		add(combo);
		
		combo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("Hello");
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					String query = (String) combo.getSelectedItem();
					String sql = ""; //demander a guiti de m'envoyer les commandes
					switch (query){
					  case "QUERY1":
					    sql = "SELECT c.TITLE, rt.RUNTIME " + 
					    		"FROM RUNS_FOR rf, RUNNINGTIMES rt, CLIPS c, COUNTRIES cn " + 
					    		"WHERE c.CLIPIDS=rf.CLIPID AND rf.RID=rt.RID AND rf.COUNTID=cn.COUNTID " + 
					    		"AND cn.COUNTRYNAME='France' " + 
					    		"ORDER BY rt.RUNTIME DESC " + 
					    		"FETCH FIRST 10 ROWS ONLY";
					    break;
					  case "QUERY2":
						sql = "SELECT cn.COUNTRYNAME, COUNT(r.CLIPID) AS number_of_clips " + 
								"FROM RELEASED_IN r, RELEASEDATES rd, COUNTRIES cn " + 
								"WHERE r.DID=rd.DID AND rd.RELEASEDATE='2001' AND cn.COUNTID=r.COUNTID " + 
								"GROUP BY cn.COUNTRYNAME";
						break;
					  case "QUERY3":
						sql = "SELECT g.GENRE, COUNT(hg.CLIPID) AS number_of_clips " + 
								"FROM CLIP_GENRE g, HAS_GENRE hg, RELEASED_IN r, RELEASEDATES rd, COUNTRIES cn " + 
								"WHERE hg.GID=g.GID AND r.DID=rd.DID AND rd.RELEASEDATE>2013 AND r.COUNTID=cn.COUNTID " + 
								"AND cn.COUNTRYNAME='USA' AND r.CLIPID=hg.CLIPID " + 
								"GROUP BY g.GENRE";
						break;
					  case "QUERY4":
						sql = "SELECT FULLNAME FROM " + 
								"(SELECT p.FULLNAME, COUNT(ac.CLIPIDS) AS number_of_clips " + 
								"FROM PEOPLE p, ACT ac, CLIPS c " + 
								"WHERE p.PID=ac.PID and ac.CLIPIDS=c.CLIPIDS " + 
								"GROUP BY p.FULLNAME " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 1 ROWS ONLY)";
						break;
					  case "QUERY5":
						sql = "SELECT number_of_clips FROM " + 
								"(SELECT d.PID, COUNT(d.CLIPIDS) AS number_of_clips " + 
								"FROM DIRECT d, CLIPS c " + 
								"WHERE d.CLIPIDS=c.CLIPIDS " + 
								"GROUP BY d.PID " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 1 ROWS ONLY)";
						break;
					  case "QUERY6":
						sql = "SELECT PEOPLE.fullname " + 
								"FROM PEOPLE " + 
								"WHERE PEOPLE.pid IN " + 
								"(SELECT pid FROM " + 
								"(SELECT pid, CLIPIDS FROM ACT GROUP BY pid, CLIPIDS " + 
								"UNION ALL " + 
								"SELECT pid, CLIPIDS FROM DIRECT " + 
								"UNION ALL " + 
								"SELECT pid, CLIPIDS FROM PRODUCE " + 
								"UNION ALL " + 
								"SELECT pid, CLIPIDS FROM WRITE) " + 
								"GROUP BY pid, clipids " + 
								"HAVING COUNT(*) > 1)";
						break;
					  case "QUERY7":
						sql = "SELECT language FROM " + 
								"(SELECT cl.language, COUNT(hl.clipid) AS number_of_clips " + 
								"FROM CLIP_LANGUAGES cl, HAS_LANGUAGE hl " + 
								"WHERE cl.lid=hl.lid " + 
								"GROUP BY cl.language " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 10 ROWS ONLY)";
						break;
				      case "QUERY8":
						sql = "SELECT fullname FROM "+ 
								"(SELECT p.fullname, COUNT(ac.clipids) AS number_of_clips " + 
								"FROM PEOPLE p, ACT ac, CLIPS c, HAS_GENRE hg, CLIP_GENRE g " + 
								"WHERE p.PID=ac.PID AND ac.CLIPIDS=c.CLIPIDS " + 
								"AND c.clipids=hg.clipid AND hg.gid=g.gid AND g.GENRE='Fantasy' " + 
								"GROUP BY p.fullname " + 
								"ORDER BY number_of_clips DESC " + 
								"FETCH FIRST 1 ROWS ONLY)";
						break;
					  case "QUERY9":
						sql = "SELECT p.fullname FROM people p " + 
								"WHERE p.pid IN " + 
								"(SELECT pid FROM " + 
								"(SELECT pid,AVG(rank) FROM " + 
								"(SELECT ac.pid,c.CLIPIDS,c.rank, row_number() " + 
								"OVER (PARTITION BY ac.pid ORDER BY c.rank DESC) AS index_clip_per_actor " + 
								"FROM clips c, act ac " + 
								"WHERE ac.CLIPIDS=c.CLIPIDS AND c.votes>100 AND ac.pid IN " + 
								"(SELECT pid FROM " + 
								"(SELECT ac.pid, ac.clipids, COUNT(*) " + 
								"FROM act ac " + 
								"GROUP BY ac.pid, ac.clipids) " + 
								"GROUP BY pid " + 
								"HAVING COUNT(*)>5)) " + 
								"WHERE index_clip_per_actor<4 " + 
								"GROUP BY pid " + 
								"ORDER BY AVG(rank) DESC " + 
								"FETCH FIRST 10 ROWS ONLY))";
						break;
					  case "QUERY10":
						sql = "SELECT decade, AVG(rank) FROM " + 
								"(SELECT clipids, rank, decade, ROW_NUMBER() OVER (PARTITION BY decade ORDER BY rank DESC) AS index_clip_per_decade " + 
								"FROM (SELECT CLIPIDS, rank, floor(year/ 10) * 10 AS decade " + 
								"FROM CLIPS WHERE rank IS NOT NULL AND year IS NOT NULL)) " + 
								"WHERE index_clip_per_decade < 101 " + 
								"GROUP BY decade " + 
								"ORDER BY AVG(rank) DESC";
						break;
					  case "QUERY11":
						sql = "select fullname, year, game_titles from " + 
								"(select year, fullname, game_titles, " + 
								"row_number() over (partition by fullname order by year) as index_year_per_pid " + 
								"from " + 
								"(select c.year, p.fullname, " + 
								"listagg(c.title, ' , ') within group (order by c.title) as game_titles " + 
								"from direct d, clips c, people p " + 
								"where d.clipids=c.CLIPIDS and p.pid=d.pid and c.type='VG' " + 
								"group by p.fullname,c.year)) " + 
								"where index_year_per_pid=1";
						break;
					  case "QUERY12":
						sql = "partie de guiti";
						break;
					  case "QUERY13":
						sql = "partie de guiti";
						break;
					  case "QUERY14":
					    sql = "partie de guiti";
						break;
					  case "QUERY15":
						sql = "partie de guiti";
						break;
					  case "QUERY16":
						sql = "partie de guiti";
						break;
					  case "QUERY17":
						sql = "partie de guiti";
						break;
					  case "QUERY18":
						sql = "partie de guiti";
						break;
					  case "QUERY19":
						sql = "partie de guiti";
						break;
					  case "QUERY20":
						sql = "partie de guiti";
						break;
					  default:
					    System.out.println("error");
					}
					
					ResultSet rs = st.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData();
					int noColumns = rsmd.getColumnCount();
					String resultString = "";
					int i = 0;
					while (rs.next() && i < 10) {
						for(int j = 1; j <= noColumns; ++j){
							resultString += (rs.getString(j) + '\t');
						}
						resultString += '\n';
						++i;
					}

					result.setText(resultString);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						conn.close();
						st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		JPanel center = new JPanel();
		center.add(resultScroll);
		add(center, BorderLayout.CENTER);
		
	}
	
	
}

