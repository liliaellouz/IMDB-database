import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class JoinPanel extends JPanel{
	private JoinPanelTable[] tables = new JoinPanelTable[2];
	private JTextArea resultSelect = new JTextArea(10, 50);
	private JScrollPane resultScrollSelect = new JScrollPane(resultSelect);
	private JButton buttonOkSelect = new JButton("Launch query");
	
	private Connection conn = null;
	
	public JoinPanel(){
		
		JPanel tablePanels = new JPanel(new GridLayout(1, 2));
		
		tables[0] = new JoinPanelTable();
		tables[1] = new JoinPanelTable();
		
		JScrollPane maFenetre = new JScrollPane(tablePanels);	
		
		
		tablePanels.add(tables[0]);
		tablePanels.add(tables[1]);
		add(maFenetre);
		add(resultScrollSelect);
		
		buttonOkSelect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				String selectedFields = tables[0].selectedFieldsToString() + ", " + 
										tables[1].selectedFieldsToString();
				String sql = "SELECT " + selectedFields + " FROM " + tables[0].getTableName() +
						", " + tables[1].getTableName() + " ";
				String whereClause = "WHERE " + tables[0].getTableName() + "." + tables[0].getJoinKey() + " = "
						+ tables[1].getTableName() + "." + tables[1].getJoinKey();
				sql += whereClause;
				String whereTable0 = tables[0].whereClause();
				String whereTable1 = tables[1].whereClause();
				if(whereTable0.length() > 0){
					sql += (" AND" + whereTable0);
				}
				if(whereTable1.length() > 0){
					sql += (" AND " + whereTable1);
				}
				
				Statement st = null;
				try {
					conn = SQLHelper.getConn();
					st = conn.createStatement();
					ResultSet rs = st.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData();
					int numberOfColumns = rsmd.getColumnCount();
					int i = 0;
					resultSelect.setText("");
					while(rs.next() && i < 1000){
						for(int j = 1; j <= numberOfColumns; ++j){
							resultSelect.append(rs.getString(j) + '\t');
						}
						resultSelect.append("\n");
						++i;
					}
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
		add(buttonOkSelect);
	}
}
