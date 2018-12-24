import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import javax.swing.JPanel;

public class Fenetre extends JFrame {
	
	private CardLayout cl = new CardLayout();
	private JPanel content = new JPanel();

	private JPanel containerSearch = new SearchPanel();
	private JPanel containerInsertRow = new InsertPanel();
	private JPanel containerSelect = new SelectPanel();
	private JPanel containerPredQuer = new PredefinedQueriesPanel();
	private JPanel containerDelete = new DeletePanel();
	private JPanel containerUpdate = new UpdatePanel();
	
	
	public static String[] tables = {"ACT", "BIOGRAPHICAL_BOOKS", "BIOGRAPHIES", "CHARACTER",
			"CLIP_GENRE", "CLIP_LANGUAGES", "CLIP_LINKS", "CLIPS","COUNTRIES", "DIRECT", 
			"FILMED_IN", "HAS_GENRE","HAS_LANGUAGE", "LINK_TYPES", "MARRIED", "PEOPLE", 
			"PRODUCE", "RELEASED_IN", "RELEASEDATES", "RUNNINGTIMES", "RUNS_FOR", "WRITE"};
	
	public Fenetre() {

		this.setTitle("Database Interface");
		this.setSize(900, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		JPanel boutonPane = new JPanel();
		JButton buttonNormalSqlQuery = new JButton("Search");

		buttonNormalSqlQuery.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(content, "search");
			}
		});

		JButton buttonInsertRow = new JButton("Insert");

		buttonInsertRow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(content, "insert row");
			}
		});
		
		JButton buttonSelect = new JButton("Select");
		
		buttonSelect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(content, "select");
			}
		});
		
		JButton buttonPredQuer = new JButton("Predefined Queries");
		
		buttonPredQuer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(content, "Predefined Queries");
			}
		});
		
		JButton buttonDelete = new JButton("Delete");
		
		buttonDelete.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(content, "delete");
			}
		});
		
		JButton buttonUpdate = new JButton("Update");
		
		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(content, "update");
			}
		});

		boutonPane.add(buttonNormalSqlQuery);
		boutonPane.add(buttonInsertRow);
		boutonPane.add(buttonSelect);
		boutonPane.add(buttonPredQuer);
		boutonPane.add(buttonDelete);
		boutonPane.add(buttonUpdate);

		content.setLayout(cl);

		content.add(containerSearch, "search");
		content.add(containerInsertRow, "insert row");
		content.add(containerSelect, "select");
		content.add(containerPredQuer, "Predefined Queries");
		content.add(containerDelete, "delete");
		content.add(containerUpdate, "update");

		this.getContentPane().add(boutonPane, BorderLayout.NORTH);
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public static void fillComboWithStrings(JComboBox<String> combo, String[] strings){
		for(String tableName: strings){
			combo.addItem(tableName);
		}
	}
}
