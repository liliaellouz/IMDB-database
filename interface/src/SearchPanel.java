import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class SearchPanel extends JPanel{
	private JButton buttonOk = new JButton("Launch Query");
	private JTextArea result = new JTextArea(10, 50);
	private JScrollPane spResult = new JScrollPane(result);
	private JTextField query = new JTextField(20);
	private JLabel label = new JLabel("Table:");
	JPanel center = new JPanel();
	JPanel east = new JPanel();
	private JComboBox<String> combo = new JComboBox<String>();
	ArrayList<String> columns = new ArrayList<String>();
	ArrayList<Integer> types = new ArrayList<Integer>();
	
	private Connection conn = null;
	
	public static String[] searchTables = {"ACT", "BIOGRAPHICAL_BOOKS", "BIOGRAPHIES", "CHARACTER",
			"CLIP_GENRE", "CLIP_LANGUAGES", "CLIPS","COUNTRIES", "DIRECT", "LINK_TYPES", "MARRIED", "PEOPLE", 
			"PRODUCE", "WRITE", "ALL"};
	
	public SearchPanel(){
		
		setBackground(Color.white);
		setLayout(new BorderLayout());
		east.add(label);
		Fenetre.fillComboWithStrings(combo, searchTables);
		east.add(combo);
		JPanel top = new JPanel();
		top.add(query);
		JPanel bottom = new JPanel();
		
		
		combo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Hello");
			}
		});
		
		buttonOk.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				
				Statement st = null;
				ResultSet rs = null;
				ResultSetMetaData rsmd = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					String keyword = query.getText();
					
					String sql1 = "SELECT * FROM ACT WHERE ACT.addinfos LIKE "+"'%"+keyword+"%'";
					String sql2 = "SELECT * FROM BIOGRAPHICAL_BOOKS WHERE BIOGRAPHICAL_BOOKS.name LIKE "+"'%"+keyword+"%'";
					String sql3 = "SELECT * FROM BIOGRAPHIES WHERE BIOGRAPHIES.fullname LIKE "+"'%"+keyword+"%' "+ 
							"OR BIOGRAPHIES.realname LIKE "+"'%"+keyword+"%' " +"OR BIOGRAPHIES.nickname LIKE "+"'%"+keyword+"%' " + 
							"OR BIOGRAPHIES.dateandplaceofbirth LIKE "+"'%"+keyword+"%' "+"OR BIOGRAPHIES.height LIKE "+"'%"+keyword+"%' " + 
							"OR BIOGRAPHIES.biographer LIKE "+"'%"+keyword+"%' " + "OR BIOGRAPHIES.dateandcauseofdeath LIKE "+"'%"+keyword+"%'";
					String sql4 = "SELECT * FROM CHARACTER WHERE CHARACTER.chars LIKE "+"'%"+keyword+"%'";
					String sql5 = "SELECT * FROM CLIP_GENRE WHERE CLIP_GENRE.genre LIKE "+"'%"+keyword+"%'";
					String sql6 = "SELECT * FROM CLIP_LANGUAGES WHERE CLIP_LANGUAGES.language LIKE "+"'%"+keyword+"%'";
					String sql7 = "SELECT * FROM CLIPS WHERE CLIPS.title LIKE "+"'%"+keyword+"%' " + "OR CLIPS.type LIKE "+"'%"+keyword+"%'";
					String sql8 = "SELECT * FROM COUNTRIES WHERE COUNTRIES.countryname LIKE "+"'%"+keyword+"%'";
					String sql9 = "SELECT * FROM DIRECT WHERE DIRECT.roles LIKE "+"'%"+keyword+"%' " + "OR DIRECT.addinfos LIKE "+"'%"+keyword+"%'";
					String sql10 = "SELECT * FROM LINK_TYPES WHERE LINK_TYPES.linktype LIKE "+"'%"+keyword+"%'";
					String sql11 = "SELECT * FROM MARRIED WHERE MARRIED.name LIKE "+"'%"+keyword+"%' " +
							"OR MARRIED.children LIKE "+"'%"+keyword+"%' " + "OR MARRIED.status LIKE "+"'%"+keyword+"%'";
					String sql12 = "SELECT * FROM PEOPLE WHERE PEOPLE.fullname LIKE "+"'%"+keyword+"%'";
					String sql13 = "SELECT * FROM PRODUCE WHERE PRODUCE.roles LIKE "+"'%"+keyword+"%' " + "OR PRODUCE.addinfos LIKE "+"'%"+keyword+"%'";
					String sql14 = "SELECT * FROM WRITE WHERE WRITE.roles LIKE "+"'%"+keyword+"%' " + "OR WRITE.addinfos LIKE "+"'%"+keyword+"%'";
					String sql15 = "SELECT * FROM " + combo.getSelectedItem();
					
					String request = sql1;
					String resultString = "";
					
					if(combo.getSelectedItem().equals("ALL")) {
						for(int k=1; k<15; k++) {
							try {
								switch (k){
								  	case 1:
									    request = sql1;
										break;
								  	case 2:
								  		request = sql2;
								  		break;
								  	case 3:
								  		request = sql3;
								  		break;
								  	case 4: 
								  		request = sql4;
								  		break;
								  	case 5:
								  		request = sql5;
								  		break;
								  	case 6:
									    request = sql6;
										break;
								  	case 7:
								  		request = sql7;
								  		break;
								  	case 8:
								  		request = sql8;
								  		break;
								  	case 9: 
								  		request = sql9;
								  		break;
								  	case 10:
								  		request = sql10;
								  		break;
								  	case 11:
									    request = sql11;
										break;
								  	case 12:
								  		request = sql12;
								  		break;
								  	case 13:
								  		request = sql13;
								  		break;
								  	case 14: 
								  		request = sql14;
								  		break;
								  	default:
									  System.out.println("error");
								}
								rs = st.executeQuery(request);
								int i = 0;
								rsmd = rs.getMetaData();
								int noColumns = rsmd.getColumnCount();
								String tableName = searchTables[k-1];
								resultString += "Table : " + tableName + "\n";
								while (rs.next() && i < 1000) {
									for(int j = 1; j <= noColumns; ++j){
										resultString += (rs.getString(j) + '\t');
									}
									resultString += '\n';
									++i;
								}
								result.setText(resultString);
							}catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}else {
						rs = st.executeQuery(sql15);
						rsmd = rs.getMetaData();
						int noColumns = rsmd.getColumnCount();
						for(int i = 0; i < noColumns; ++i){
							columns.add(rsmd.getColumnName(i + 1));
							types.add(rsmd.getColumnType(i+1));	
						}
						sql15 += " WHERE ";
						int count = 0;
						for(int i = 0; i < types.size(); ++i){
							if(types.get(i) == Types.VARCHAR || types.get(i) == Types.CHAR) {
								count++;
							}
						}
						for(int i = 0; i < noColumns; ++i){
							count--;
							int type = types.get(i);
							if(type == Types.VARCHAR || type == Types.CHAR) {
								sql15 += columns.get(i)+" LIKE '%"+keyword+"%'";
							}
							if(count>0) {
								sql15 += " OR ";
							}
						}
						rs = st.executeQuery(sql15);
						rsmd = rs.getMetaData();
						noColumns = rsmd.getColumnCount();
						int k = 0;
						resultString = "Table : "+ combo.getSelectedItem()+"\n";
						while (rs.next() && k < 1000) {
							for(int j = 1; j <= noColumns; ++j){
								resultString += (rs.getString(j) + '\t');
							}
							resultString += '\n';
							++k;
						}
						result.setText(resultString);
					}
				} catch (SQLException e) {
					e.printStackTrace();
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
		bottom.add(buttonOk);
		center.add(spResult);
		add(east, BorderLayout.WEST);
		add(top, BorderLayout.NORTH);
		add(bottom, BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);
	}
}
