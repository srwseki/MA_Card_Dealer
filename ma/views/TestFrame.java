package ma.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import ma.common.Pair;
import ma.common.TestFeedAdapter;
import ma.resources.Resources;

public class TestFrame extends JFrame implements ActionListener, FocusListener, ItemListener {

	private final int[] num = {0,1,2,3,4,5,6,7,8,9};
	private final int BASE = 5;
	
	private JPanel contentPane;
	
	private JTextField textField_deck_1;
	private JTextField textField_deck_2;
	private JTextField textField_deck_3;
	private JTextField textField_deck_4;
	private JTextField textField_deck_5;
	private JTextField textField_deck_6;
	private JTextField textField_deck_7;
	private JTextField textField_deck_8;
	private JTextField textField_deck_9;
	private JTextField textField_deck_0;
	private JTextField[] textField_decks;

	private JTextField textField_cond_1;
	private JTextField textField_cond_2;
	private JTextField textField_cond_3;
	private JTextField textField_cond_4;
	
	private final ButtonGroup buttonGroupDraw = new ButtonGroup();
	
	private JButton btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,btn_17,btn_18;
	private JButton btn_21,btn_22,btn_23,btn_24,btn_25,btn_26,btn_27,btn_28;
	private JButton btn_31,btn_32,btn_33,btn_34,btn_35,btn_36,btn_37,btn_38;
	private JButton btn_41,btn_42,btn_43,btn_44,btn_45,btn_46,btn_47,btn_48;
	private JButton[] btn_cards;
	
	private JButton btn_clear, btn_undo;

	private int last_deck_idx = -1;

	private JRadioButton[] radioButton_draws;

	private JButton button_calc, button_reset;
	
	private List<List<Integer>> result, notResult;
	private List<String> txtTypes;
	private List<String[][]> txtCards;	//4x8 max

	private JCheckBox[] checkBox_conds;
	private JComboBox[] comboBox_conds;
	private JTextField[] textField_conds;
	private int found = 0;
	private int total = 0;
	private double chance = 0.0;
	private JLabel label_total;
	private JLabel label_found;
	private JLabel label_chance;
	private JButton button_calcNext;
	private JComboBox comboBox_plus;
	private int chosen;
	private DefaultListModel includeListModel, excludeListModel;
	private List<List<Integer>> totalResult;
	private int totalDraw;
	private String lang;
	private JComboBox comboBox_class;
	private JLabel[] lbl_types;
	
	private String word_menu_help = "Help";
	private String word_menu_about = "About...";
	private String word_border_card_selection = "Card Selection";
	private String word_label_class = "CLASS:";
	private String word_border_deck = "Card Deck";
	private String word_button_undo = "Clear Last";
	private String word_button_clear = "Clear All";
	private String word_border_condition = "Condition";
	private String word_label_from = "From";
	private String word_label_pick = " pick at least ";
	private String word_label_unit = " card(s)";
	private String word_label_draw = "Draw:";
	private String word_radio_button_0 = "First";
	private String word_label_plus = "Plus";
	private String word_button_calc_next = "Calc Next";
	private String word_button_calc = "Calculate";
	private String word_button_reset = "Reset";
	private String word_border_result = "Results";
	private String word_label_found = "Found: ";
	private String word_label_possible = " from possible ";
	private String word_label_comb = " cominations, chance: ";
	private String word_tab_include = "Include";
	private String word_tab_exclude = "Exclude";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		final String[] params = args;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String lang = "Chinese";
					if(params.length>0){
						lang = params[0];
					}
					TestFrame frame = new TestFrame(lang);
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestFrame(String lang) {
		//setIconImage(Toolkit.getDefaultToolkit().getImage(TestFrame.class.getResource("../resources/icon.png")));
		setTitle("Million Card Dealer ver. 1.0 by Seki");	//read from menu lang file
		
		this.lang = lang;
		this.totalResult = TestFeedAdapter.permute(num, BASE);
		this.total = totalResult.size();
		readWords();
		boolean success = readCards(true);
		if(!success){
			System.out.println("Config file read from package");
			readCards(false);
		}
		initComponents();
		createEvents();

	}
	
	private boolean readCards(boolean tryReadLocal){
		InputStream is = null;
		String fileName = "Card_"+lang+".txt";
		File file = new File(fileName);
		if(tryReadLocal){
			try {
				is = new FileInputStream(file);
				System.out.println("Config file "+file.getAbsolutePath()+" is found, load cards from it.");
			} catch (FileNotFoundException e) {
				System.out.println("Config file "+file.getAbsolutePath()+" is not found, use package file");
				is = null;
			}
		}

		FileWriter fw = null;
		if(is == null){
			is = Resources.class.getResourceAsStream(fileName);
			if(is == null){
				throw new RuntimeException(fileName +" is not found in package!");
			}
			try {
				fw = new FileWriter(file);
			} catch (Exception e1) {
				System.out.println("cannot copy to new file "+file);
			}
		}
		this.txtTypes = new ArrayList<String>();
		this.txtCards = new ArrayList<String[][]>();
		
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		int type = -1;
		int row = 0;
		while(true){

			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
			}
			if(line == null){
				break;
			}

			try {
				if(fw!=null){
					fw.write(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (line.length() == 0){
				continue;
			}else{
			
				try {
					if(fw!=null){
						fw.write("\r\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			if(line.charAt(0) == '[' && line.charAt(line.length()-1) == ']'){
				txtTypes.add(line.substring(1,line.length()-1));
				//System.out.println(line);
				row = 0;
				type++;
				txtCards.add(new String[4][]);
			}else{
				txtCards.get(type)[row] = new String[8+1];
				StringTokenizer st = new StringTokenizer(line);
				int col = 0;
				while(st.hasMoreTokens() && col < 8+1){
					txtCards.get(type)[row][col] = st.nextToken();
					col++;
				}
				//System.out.println(Arrays.toString(txtCards.get(type)[row]));
				row++;
			}
		}
		try {
			is.close();
			System.out.println("Config file access done");
			if(fw!=null){
				fw.close();
				System.out.println("Clone file access done");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return txtCards.size()>0;
	}
	
	private void initFonts(){
		UIManager.put("Button.font", UIManager.getFont("Button.font").deriveFont((float)14.0));
//		UIManager.put("ToggleButton.font", /* font of your liking */);
		UIManager.put("RadioButton.font", UIManager.getFont("RadioButton.font").deriveFont((float)14.0));
//		UIManager.put("CheckBox.font", /* font of your liking */);
//		UIManager.put("ColorChooser.font", /* font of your liking */);
//		UIManager.put("ComboBox.font", /* font of your liking */);
		UIManager.put("Label.font", UIManager.getFont("Button.font").deriveFont((float)14.0));
		UIManager.put("List.font", UIManager.getFont("Button.font").deriveFont((float)12.0));
//		UIManager.put("MenuBar.font", /* font of your liking */);
//		UIManager.put("MenuItem.font", /* font of your liking */);
//		UIManager.put("RadioButtonMenuItem.font", /* font of your liking */);
//		UIManager.put("CheckBoxMenuItem.font", /* font of your liking */);
//		UIManager.put("Menu.font", /* font of your liking */);
//		UIManager.put("PopupMenu.font", /* font of your liking */);
//		UIManager.put("OptionPane.font", /* font of your liking */);
//		UIManager.put("Panel.font", /* font of your liking */);
//		UIManager.put("ProgressBar.font", /* font of your liking */);
//		UIManager.put("ScrollPane.font", /* font of your liking */);
//		UIManager.put("Viewport.font", /* font of your liking */);
		UIManager.put("TabbedPane.font", UIManager.getFont("TabbedPane.font").deriveFont((float)14.0));
//		UIManager.put("Table.font", /* font of your liking */);
//		UIManager.put("TableHeader.font", /* font of your liking */);
//		UIManager.put("TextField.font", /* font of your liking */);
//		UIManager.put("PasswordField.font", /* font of your liking */);
//		UIManager.put("TextArea.font", /* font of your liking */);
//		UIManager.put("TextPane.font", /* font of your liking */);
//		UIManager.put("EditorPane.font", /* font of your liking */);
//		UIManager.put("TitledBorder.font", /* font of your liking */);
//		UIManager.put("ToolBar.font", /* font of your liking */);
//		UIManager.put("ToolTip.font", /* font of your liking */);
//		UIManager.put("Tree.font", /* font of your liking */);
	}

	private void readWords(){
		HashMap<String,String> prop = new HashMap<String,String>();
		String propFileName = "Menu_"+lang+".txt";
		InputStream fis = Resources.class.getResourceAsStream(propFileName);
		if(fis == null){
			throw new RuntimeException(propFileName +" is not found in package!");
		}
		BufferedReader br = null;
		try {
			br= new BufferedReader(new InputStreamReader(fis));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		while(true){
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
			}
			if(line == null){
				break;
			}
			if (line.length() == 0){
				continue;
			}
			int idx = line.indexOf('=');
			if(idx == -1)	continue;
			String key = line.substring(0,idx);
			String value = line.substring(idx+1,line.length());
			prop.put(key, value);
		}
		try {
			fis.close();
			System.out.println("Menu language file access done");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		word_menu_help = prop.get("word_menu_help");
		word_menu_about = prop.get("word_menu_about");
		word_border_card_selection = prop.get("word_border_card_selection");
		word_label_class = prop.get("word_label_class");
		word_border_deck = prop.get("word_border_deck");
		word_button_undo = prop.get("word_button_undo");
		word_button_clear = prop.get("word_button_clear");
		word_border_condition = prop.get("word_border_condition");
		word_label_from = prop.get("word_label_from");
		word_label_pick = prop.get("word_label_pick");
		word_label_unit = prop.get("word_label_unit");
		word_label_draw = prop.get("word_label_draw");
		word_radio_button_0 = prop.get("word_radio_button_0");
		word_label_plus = prop.get("word_label_plus");
		word_button_calc_next = prop.get("word_button_calc_next");
		word_button_calc = prop.get("word_button_calc");
		word_button_reset = prop.get("word_button_reset");
		word_border_result = prop.get("word_border_result");
		word_label_found = prop.get("word_label_found");
		word_label_possible = prop.get("word_label_possible");
		word_label_comb = prop.get("word_label_comb");
		word_tab_exclude = prop.get("word_tab_exclude");
		word_tab_include = prop.get("word_tab_include");


	}
	
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 700);
		
		initFonts();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnOption = new JMenu("Help");
		mnOption.setText(this.word_menu_help);
		menuBar.add(mnOption);
		
		JMenuItem mntmAbout = new JMenuItem("About ...");
		mntmAbout.setText(this.word_menu_about);
		mnOption.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panel_selection = new JPanel();
		TitledBorder tb = new TitledBorder(null, "card selection", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 14), new Color(0, 0, 0));
		panel_selection.setBorder(tb);
		tb.setTitle(this.word_border_card_selection);
		contentPane.add(panel_selection);
		panel_selection.setLayout(new BoxLayout(panel_selection, BoxLayout.Y_AXIS));
		
		JPanel panel_class = new JPanel();
		FlowLayout fl_panel_class = (FlowLayout) panel_class.getLayout();
		fl_panel_class.setAlignment(FlowLayout.LEFT);
		panel_selection.add(panel_class);
		
		JLabel label_class = new JLabel("CLASS:");
		label_class.setText(this.word_label_class);
		label_class.setFont(new Font("SansSerif", Font.PLAIN, 14));
		label_class.setPreferredSize(new Dimension(60, 14));
		panel_class.add(label_class);
		
		comboBox_class = new JComboBox();
		comboBox_class.setFont(new Font("SansSerif", Font.PLAIN, 14));
		String[] typeItems = new String[] {"C1", "C2", "C3", "C4"};
		if(txtTypes.size()>0){
			typeItems = this.txtTypes.toArray(new String[txtTypes.size()]);
		}
		comboBox_class.setModel(new DefaultComboBoxModel(typeItems));
		comboBox_class.addItemListener(this);
		panel_class.add(comboBox_class);
		
		JPanel panel_type_1 = new JPanel();
		FlowLayout fl_panel_type_1 = (FlowLayout) panel_type_1.getLayout();
		fl_panel_type_1.setAlignment(FlowLayout.LEFT);
		panel_selection.add(panel_type_1);
		
		JLabel lbl_type_1 = new JLabel("TYPE1");
		panel_type_1.add(lbl_type_1);
		
		btn_11 = new JButton("card11");
		btn_11.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_11);
		
		btn_12 = new JButton("card12");
		btn_12.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_12);
		
		btn_13 = new JButton("card13");
		btn_13.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_13);
		
		btn_14 = new JButton("card14");
		btn_14.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_14);
		
		btn_15 = new JButton("card15");
		btn_15.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_15);
		
		btn_16 = new JButton("card16");
		btn_16.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_16);
		
		btn_17 = new JButton("card17");
		btn_17.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_17);
		
		btn_18 = new JButton("card18");
		btn_18.setMargin(new Insets(2, 2, 2, 2));
		panel_type_1.add(btn_18);
		
		JPanel panel_type_2 = new JPanel();
		FlowLayout fl_panel_type_2 = (FlowLayout) panel_type_2.getLayout();
		fl_panel_type_2.setAlignment(FlowLayout.LEFT);
		panel_selection.add(panel_type_2);
		
		JLabel lbl_type_2 = new JLabel("TYPE2");
		panel_type_2.add(lbl_type_2);
		
		btn_21 = new JButton("card21");
		btn_21.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_21);
		
		btn_22 = new JButton("card22");
		btn_22.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_22);
		
		btn_23 = new JButton("card23");
		btn_23.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_23);
		
		btn_24 = new JButton("card24");
		btn_24.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_24);
		
		btn_25 = new JButton("card25");
		btn_25.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_25);
		
		btn_26 = new JButton("card26");
		btn_26.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_26);
		
		btn_27 = new JButton("card27");
		btn_27.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_27);
		
		btn_28 = new JButton("card28");
		btn_28.setMargin(new Insets(2, 2, 2, 2));
		panel_type_2.add(btn_28);
		
		JPanel panel_type_3 = new JPanel();
		FlowLayout fl_panel_type_3 = (FlowLayout) panel_type_3.getLayout();
		fl_panel_type_3.setAlignment(FlowLayout.LEFT);
		panel_selection.add(panel_type_3);
		
		JLabel lbl_type_3 = new JLabel("TYPE3");
		panel_type_3.add(lbl_type_3);
		
		btn_31 = new JButton("card31");
		btn_31.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_31);
		
		btn_32 = new JButton("card32");
		btn_32.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_32);
		
		btn_33 = new JButton("card33");
		btn_33.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_33);
		
		btn_34 = new JButton("card34");
		btn_34.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_34);
		
		btn_35 = new JButton("card35");
		btn_35.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_35);
		
		btn_36 = new JButton("card36");
		btn_36.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_36);
		
		btn_37 = new JButton("card37");
		btn_37.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_37);
		
		btn_38 = new JButton("card38");
		btn_38.setMargin(new Insets(2, 2, 2, 2));
		panel_type_3.add(btn_38);
		
		JPanel panel_type_4 = new JPanel();
		FlowLayout fl_panel_type_4 = (FlowLayout) panel_type_4.getLayout();
		fl_panel_type_4.setAlignment(FlowLayout.LEFT);
		panel_selection.add(panel_type_4);
		
		JLabel lbl_type_4 = new JLabel("TYPE4");
		panel_type_4.add(lbl_type_4);
		
		btn_41 = new JButton("card41");
		btn_41.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_41);
		
		btn_42 = new JButton("card42");
		btn_42.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_42);
		
		btn_43 = new JButton("card43");
		btn_43.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_43);
		
		btn_44 = new JButton("card44");
		btn_44.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_44);
		
		btn_45 = new JButton("card45");
		btn_45.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_45);
		
		btn_46 = new JButton("card46");
		btn_46.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_46);
		
		btn_47 = new JButton("card47");
		btn_47.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_47);
		
		btn_48 = new JButton("card48");
		btn_48.setMargin(new Insets(2, 2, 2, 2));
		panel_type_4.add(btn_48);
		
		btn_cards = new JButton[]{
				btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,btn_17,btn_18,
				btn_21,btn_22,btn_23,btn_24,btn_25,btn_26,btn_27,btn_28,
				btn_31,btn_32,btn_33,btn_34,btn_35,btn_36,btn_37,btn_38,
				btn_41,btn_42,btn_43,btn_44,btn_45,btn_46,btn_47,btn_48
				};
		for(JButton btn : btn_cards){
			btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
			btn.setPreferredSize(new Dimension(64, btn.getPreferredSize().height));
			btn.addActionListener(this);
		}
		
		lbl_types = new JLabel[]{
				lbl_type_1,lbl_type_2,lbl_type_3,lbl_type_4
		};
		for(JLabel lbl : lbl_types){
			lbl.setPreferredSize(new Dimension(60, 14));
			lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
		}
		
		setCards();
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		contentPane.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JPanel panel_deck = new JPanel();
		TitledBorder tb_deck = new TitledBorder(null, "card deck", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 14), null);
		tb_deck.setTitle(this.word_border_deck);
		panel_deck.setBorder(tb_deck);
		panel_1.add(panel_deck);
		panel_deck.setLayout(new GridLayout(4, 3, 0, 0));
		
		JPanel panel_deck_1 = new JPanel();
		panel_deck.add(panel_deck_1);
		
		JLabel label = new JLabel("1");
		panel_deck_1.add(label);
		
		textField_deck_1 = new JTextField();
		panel_deck_1.add(textField_deck_1);
		textField_deck_1.setColumns(7);
		
		JPanel panel_deck_2 = new JPanel();
		panel_deck.add(panel_deck_2);
		
		JLabel label_10 = new JLabel("2");
		panel_deck_2.add(label_10);
		
		textField_deck_2 = new JTextField();
		textField_deck_2.setColumns(7);
		panel_deck_2.add(textField_deck_2);
		
		JPanel panel_deck_3 = new JPanel();
		panel_deck.add(panel_deck_3);
		
		JLabel label_11 = new JLabel("3");
		panel_deck_3.add(label_11);
		
		textField_deck_3 = new JTextField();
		textField_deck_3.setColumns(7);
		panel_deck_3.add(textField_deck_3);
		
		JPanel panel_deck_4 = new JPanel();
		panel_deck.add(panel_deck_4);
		
		JLabel label_3 = new JLabel("4");
		panel_deck_4.add(label_3);
		
		textField_deck_4 = new JTextField();
		textField_deck_4.setColumns(7);
		panel_deck_4.add(textField_deck_4);
		
		JPanel panel_deck_5 = new JPanel();
		panel_deck.add(panel_deck_5);
		
		JLabel label_4 = new JLabel("5");
		panel_deck_5.add(label_4);
		
		textField_deck_5 = new JTextField();
		textField_deck_5.setColumns(7);
		panel_deck_5.add(textField_deck_5);
		
		JPanel panel_deck_6 = new JPanel();
		panel_deck.add(panel_deck_6);
		
		JLabel label_5 = new JLabel("6");
		panel_deck_6.add(label_5);
		
		textField_deck_6 = new JTextField();
		textField_deck_6.setColumns(7);
		panel_deck_6.add(textField_deck_6);
		
		JPanel panel_deck_7 = new JPanel();
		panel_deck.add(panel_deck_7);
		
		JLabel label_8 = new JLabel("7");
		panel_deck_7.add(label_8);
		
		textField_deck_7 = new JTextField();
		textField_deck_7.setColumns(7);
		panel_deck_7.add(textField_deck_7);
		
		JPanel panel_deck_8 = new JPanel();
		panel_deck.add(panel_deck_8);
		
		JLabel label_7 = new JLabel("8");
		panel_deck_8.add(label_7);
		
		textField_deck_8 = new JTextField();
		textField_deck_8.setColumns(7);
		panel_deck_8.add(textField_deck_8);
		
		JPanel panel_deck_9 = new JPanel();
		panel_deck.add(panel_deck_9);
		
		JLabel label_6 = new JLabel("9");
		panel_deck_9.add(label_6);
		
		textField_deck_9 = new JTextField();
		textField_deck_9.setColumns(7);
		panel_deck_9.add(textField_deck_9);
		
		JPanel panel_deck_0 = new JPanel();
		panel_deck.add(panel_deck_0);
		
		JLabel label_9 = new JLabel("0");
		panel_deck_0.add(label_9);
		
		textField_deck_0 = new JTextField();
		textField_deck_0.setColumns(7);
		panel_deck_0.add(textField_deck_0);
		
		textField_decks = new JTextField[] { 
				textField_deck_1, textField_deck_2, textField_deck_3, 
				textField_deck_4, textField_deck_5, textField_deck_6, 
				textField_deck_7, textField_deck_8, textField_deck_9,
				textField_deck_0 };
		for(JTextField txtFld : textField_decks){
			txtFld.setFont(new Font("SansSerif", Font.PLAIN, 14));
			txtFld.addFocusListener(this);
		}
		
		JPanel panel_deck_undo = new JPanel();
		panel_deck.add(panel_deck_undo);
		
		btn_undo = new JButton("Clear Last");
		btn_undo.setText(this.word_button_undo);
		btn_undo.setPreferredSize(new Dimension(75, 23));
		btn_undo.setMargin(new Insets(2, 2, 2, 2));
		btn_undo.addActionListener(this);
		panel_deck_undo.add(btn_undo);
		
		JPanel panel_deck_clear = new JPanel();
		panel_deck.add(panel_deck_clear);
		
		btn_clear = new JButton("Clear All");
		btn_clear.setText(this.word_button_clear);
		btn_clear.setPreferredSize(new Dimension(75, 23));
		btn_clear.setMargin(new Insets(2, 2, 2, 2));
		btn_clear.addActionListener(this);
		panel_deck_clear.add(btn_clear);
		
		JPanel panel_cond = new JPanel();
		TitledBorder tb_condition = new TitledBorder(null, "condition", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 14), null);
		tb_condition.setTitle(this.word_border_condition);
		panel_cond.setBorder(tb_condition);
		panel_1.add(panel_cond);
		panel_cond.setLayout(new BoxLayout(panel_cond, BoxLayout.Y_AXIS));
		
		JPanel panel_26 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_26.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_cond.add(panel_26);
		
		JCheckBox checkBox_cond_1 = new JCheckBox("");
		panel_26.add(checkBox_cond_1);
		
		JLabel label_cond_from_1 = new JLabel("From");
		label_cond_from_1.setText(this.word_label_from);
		panel_26.add(label_cond_from_1);
		
		textField_cond_1 = new JTextField();
		PlainDocument doc1 = (PlainDocument)textField_cond_1.getDocument();
		doc1.setDocumentFilter(new MyIntFilter(textField_cond_1, textField_decks));
		textField_cond_1.setText("");
		textField_cond_1.setColumns(7);
		panel_26.add(textField_cond_1);
		
		JLabel label_cond_pick_1 = new JLabel(" pick at least ");
		label_cond_pick_1.setText(this.word_label_pick);
		panel_26.add(label_cond_pick_1);
		
		JComboBox comboBox_cond_1 = new JComboBox();
		comboBox_cond_1.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		panel_26.add(comboBox_cond_1);
		
		JLabel label_cond_unit_1 = new JLabel(" card(s)");
		label_cond_unit_1.setText(this.word_label_unit);
		panel_26.add(label_cond_unit_1);
		
		JPanel panel_27 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_27.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_cond.add(panel_27);
		
		JCheckBox checkBox_cond_2 = new JCheckBox("");
		panel_27.add(checkBox_cond_2);
		
		JLabel label_cond_from_2 = new JLabel("From");
		label_cond_from_2.setText(this.word_label_from);
		panel_27.add(label_cond_from_2);
		
		textField_cond_2 = new JTextField();
		PlainDocument doc2 = (PlainDocument)textField_cond_2.getDocument();
		doc2.setDocumentFilter(new MyIntFilter(textField_cond_2, textField_decks));
		textField_cond_2.setText("");
		textField_cond_2.setColumns(7);
		panel_27.add(textField_cond_2);
		
		JLabel label_cond_pick_2 = new JLabel(" pick at least ");
		label_cond_pick_2.setText(this.word_label_pick);
		panel_27.add(label_cond_pick_2);
		
		JComboBox comboBox_cond_2 = new JComboBox();
		comboBox_cond_2.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		panel_27.add(comboBox_cond_2);
		
		JLabel label_cond_unit_2 = new JLabel(" card(s)");
		label_cond_unit_2.setText(this.word_label_unit);
		panel_27.add(label_cond_unit_2);
		
		JPanel panel_29 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_29.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_cond.add(panel_29);
		
		JCheckBox checkBox_cond_3 = new JCheckBox("");
		panel_29.add(checkBox_cond_3);
		
		JLabel label_cond_from_3 = new JLabel("From");
		label_cond_from_3.setText(this.word_label_from);
		panel_29.add(label_cond_from_3);
		
		textField_cond_3 = new JTextField();
		PlainDocument doc3 = (PlainDocument)textField_cond_3.getDocument();
		doc3.setDocumentFilter(new MyIntFilter(textField_cond_3, textField_decks));
		textField_cond_3.setText("");
		textField_cond_3.setColumns(7);
		panel_29.add(textField_cond_3);
		
		JLabel label_cond_pick_3 = new JLabel(" pick at least ");
		label_cond_pick_3.setText(this.word_label_pick);
		panel_29.add(label_cond_pick_3);
		
		JComboBox comboBox_cond_3 = new JComboBox();
		comboBox_cond_3.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		panel_29.add(comboBox_cond_3);
		
		JLabel label_cond_unit_3 = new JLabel(" card(s)");
		label_cond_unit_3.setText(this.word_label_unit);
		panel_29.add(label_cond_unit_3);
		
		JPanel panel_30 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_30.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_cond.add(panel_30);
		
		JCheckBox checkBox_cond_4 = new JCheckBox("");
		panel_30.add(checkBox_cond_4);
		
		JLabel label_cond_from_4 = new JLabel("From");
		label_cond_from_4.setText(this.word_label_from);
		panel_30.add(label_cond_from_4);
		
		textField_cond_4 = new JTextField();
		textField_cond_4.setToolTipText("tooltip");
		PlainDocument doc4 = (PlainDocument)textField_cond_4.getDocument();
		doc4.setDocumentFilter(new MyIntFilter(textField_cond_4, textField_decks));
		textField_cond_4.setText("");
		textField_cond_4.setColumns(7);
		panel_30.add(textField_cond_4);
		
		JLabel label_cond_pick_4 = new JLabel(" pick at least ");
		label_cond_pick_4.setText(this.word_label_pick);
		panel_30.add(label_cond_pick_4);
		
		JComboBox comboBox_cond_4 = new JComboBox();
		comboBox_cond_4.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		panel_30.add(comboBox_cond_4);
		
		JLabel label_cond_unit_4 = new JLabel(" card(s)");
		label_cond_unit_4.setText(this.word_label_unit);
		panel_30.add(label_cond_unit_4);

		checkBox_conds = new JCheckBox[]{checkBox_cond_1,checkBox_cond_2,checkBox_cond_3,checkBox_cond_4};
		textField_conds = new JTextField[]{textField_cond_1,textField_cond_2,textField_cond_3,textField_cond_4};
		comboBox_conds = new JComboBox[]{comboBox_cond_1,comboBox_cond_2,comboBox_cond_3,comboBox_cond_4};
		
		for(JTextField textField_deck : textField_decks){
			MyDeckFilter deckFilter = new MyDeckFilter(textField_conds, textField_decks);
			PlainDocument pd = (PlainDocument)textField_deck.getDocument();
			pd.setDocumentFilter(deckFilter);
		}

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(null);
		contentPane.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JPanel panel_24 = new JPanel();
		panel_24.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		FlowLayout flowLayout_5 = (FlowLayout) panel_24.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_24);
		
		JLabel lblDraw = new JLabel("Draw: ");
		lblDraw.setText(this.word_label_draw);
		panel_24.add(lblDraw);
		
		JRadioButton radioButton_0 = new JRadioButton("First");
		radioButton_0.setText(this.word_radio_button_0);
		radioButton_0.setSelected(true);
		buttonGroupDraw.add(radioButton_0);
		panel_24.add(radioButton_0);
		
		JRadioButton radioButton_1 = new JRadioButton("1");
		buttonGroupDraw.add(radioButton_1);
		panel_24.add(radioButton_1);
		
		JRadioButton radioButton_2 = new JRadioButton("2");
		buttonGroupDraw.add(radioButton_2);
		panel_24.add(radioButton_2);
		
		JRadioButton radioButton_3 = new JRadioButton("3");
		buttonGroupDraw.add(radioButton_3);
		panel_24.add(radioButton_3);
		
		JRadioButton radioButton_4 = new JRadioButton("4");
		buttonGroupDraw.add(radioButton_4);
		panel_24.add(radioButton_4);
		
		JRadioButton radioButton_5 = new JRadioButton("5");
		buttonGroupDraw.add(radioButton_5);
		panel_24.add(radioButton_5);
		
		JPanel panel_22 = new JPanel();
		panel_22.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.add(panel_22);
		panel_22.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel = new JLabel("Plus:");
		lblNewLabel.setText(this.word_label_plus);
		panel_22.add(lblNewLabel);
		
		comboBox_plus = new JComboBox();
		comboBox_plus.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4"}));
		comboBox_plus.setEnabled(false);
		panel_22.add(comboBox_plus);
		
		button_calcNext = new JButton("Calc Next");
		button_calcNext.setText(word_button_calc_next);
		button_calcNext.addActionListener(this);
		button_calcNext.setEnabled(false);
		panel_22.add(button_calcNext);
		
		this.radioButton_draws = new JRadioButton[]{radioButton_0,radioButton_1,radioButton_2,radioButton_3,radioButton_4,radioButton_5};
		resetCondition();
		
		this.button_calc = new JButton("Calculate");
		button_calc.setText(word_button_calc);
		button_calc.setPreferredSize(new Dimension(65, 23));
		button_calc.setMargin(new Insets(2, 2, 2, 2));
		button_calc.addActionListener(this);
		panel_24.add(button_calc);
		
		this.button_reset = new JButton("Reset");
		button_reset.setText(word_button_reset);
		button_reset.setMargin(new Insets(2, 2, 2, 2));
		button_reset.setPreferredSize(new Dimension(65, 23));
		button_reset.addActionListener(this);
		panel_24.add(button_reset);
		
		JPanel panel_results = new JPanel();
		TitledBorder tb_result = new TitledBorder(null, "Results", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 14), new Color(0, 0, 0));
		tb_result.setTitle(this.word_border_result);
		panel_results.setBorder(tb_result);
		contentPane.add(panel_results);
		panel_results.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_detail = new JPanel();
		FlowLayout fl_panel_detail = (FlowLayout) panel_detail.getLayout();
		fl_panel_detail.setAlignment(FlowLayout.LEFT);
		panel_results.add(panel_detail, BorderLayout.NORTH);
		
		JLabel lblFound = new JLabel("Found: ");
		lblFound.setText(word_label_found);
		panel_detail.add(lblFound);
		
		label_found = new JLabel("0");
		panel_detail.add(label_found);
		
		JLabel lblFromPossible = new JLabel("from possible");
		lblFromPossible.setText(word_label_possible);
		panel_detail.add(lblFromPossible);
		
		label_total = new JLabel("0");
		panel_detail.add(label_total);
		
		JLabel lblCombinations = new JLabel("combinations, chance:");
		lblCombinations.setText(word_label_comb);
		panel_detail.add(lblCombinations);
		
		label_chance = new JLabel("0.00");
		panel_detail.add(label_chance);
		
		JLabel label_percent = new JLabel("%");
		panel_detail.add(label_percent);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_results.add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab(word_tab_include, null, scrollPane, null);
		
		includeListModel = new DefaultListModel();
		JList includeList = new JList(includeListModel);
		scrollPane.setViewportView(includeList);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab(word_tab_exclude, null, scrollPane_1, null);
		
		excludeListModel = new DefaultListModel();
		JList excludeList = new JList(excludeListModel);
		scrollPane_1.setViewportView(excludeList);
		
	}
	

	private void createEvents() {
		// TODO Auto-generated method stub
		
	}
	
	private int getIdxFrom(Object o, Object[] array){
		for(int i=0;i<array.length;i++){
			if(array[i].equals(o)){
				return i;
			}
		}
		return -1;
	}
	
	private void conditionChanged(){
		//source1: card text field changed
		//source2: uncheck condition checkbox
		//source3: checked condition text field changed
		//source4: checked pick number changed
		//source5: draw number changed
		
	}
	
	private void resetCondition(){
		this.radioButton_draws[0].setSelected(true);
		for(int i=0;i<checkBox_conds.length;i++){
			checkBox_conds[i].setSelected(false);
			textField_conds[i].setText("");
			comboBox_conds[i].setSelectedIndex(0);
		}
		checkBox_conds[0].setSelected(true);
		textField_conds[0].setText("1");
		comboBox_plus.setSelectedIndex(0);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO disable calc after 1st run, enable calc if condition changess
		// TODO disable calc next if condition changes
		
		for(JButton btn : this.btn_cards){
			if(e.getSource().equals(btn)){
				setNextCard(btn.getText());
			}
		}
		if(btn_clear.equals(e.getSource())){
			for(int i=0;i<textField_decks.length;i++){
				JTextField textField = textField_decks[i];
				textField.setText("");
			}
			last_deck_idx = -1;
		}
		if(e.getSource().equals(btn_undo)){
			for(int i=textField_decks.length-1;i>=0;i--){
				JTextField textField = textField_decks[i];
				if(textField.getText().trim().length()>0){
					textField.setText("");
					last_deck_idx = i;
					return;
				}
			}
		}
		if(button_reset.equals(e.getSource())){
			resetCondition();
		}
		try {
			if (button_calcNext.equals(e.getSource())) {
				int drawPlus = Integer.valueOf(comboBox_plus.getSelectedItem().toString());
				calculate(result, getDrawNum() + chosen + drawPlus);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						printResult();
					}
				});
			}
			if (button_calc.equals(e.getSource())) {
				result = null;
				button_calcNext.setEnabled(false);
				comboBox_plus.setEnabled(false);
				int draw = getDrawNum();
				calculate(totalResult,draw);
				long start = System.currentTimeMillis();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						printResult();
					}
				});
				long duration = System.currentTimeMillis() - start;
				System.out.println("invokeLater() takes "+duration/1000+" seconds"); 
			}
		} catch (Exception ex) {
			JOptionPane.showInternalMessageDialog(this.contentPane, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
			ex.printStackTrace();
		}
	}

	
	private class ListSwingWorker extends SwingWorker<List<Object>, Void> {
	    
		private List<Object> list;
	    
	    public ListSwingWorker(List<Object> theOriginalList){
	        list = theOriginalList;
	    }

	    @Override
	    public List<Object> doInBackground() {
	        // Do the first opperation on the list
	        // Do the second opperation on the list

	        return null;
	    }

	    @Override
	    public void done() {
	        // Update the GUI with the updated list.
	    }
	}
	
	private int getDrawNum(){
		for(int d=0;d<radioButton_draws.length;d++){
			if(radioButton_draws[d].isSelected()){
				return d;
			}
		}
		return -1;
	}
	
	private void calculate(List<List<Integer>> res, int draw){
		Pair<Set<Integer>, Integer>[] targetGroup = createTargetGroup();
		this.chosen = 0;
		for(Pair<Set<Integer>, Integer> group : targetGroup){
			chosen += group.second;
		}
		List<List<Integer>>[] resPair = TestFeedAdapter.search(res, BASE+draw, targetGroup );
		this.result = resPair[0];
		this.notResult = resPair[1];
		if(result!=null && result.size()>0){
			button_calcNext.setEnabled(true);
			comboBox_plus.setEnabled(true);
		}else{
			button_calcNext.setEnabled(false);
			comboBox_plus.setEnabled(false);
		}
		
		this.found = result.size();
		this.chance = found*100.0/total;
		this.totalDraw = BASE+draw;
		System.out.println("Found: "+result.size()+"/"+res.size()+" , chance: "+chance);	//pick 2 after n draw , from total found res , chance: %
		this.label_found.setText(""+found);
		this.label_total.setText(""+total);
		this.label_chance.setText(String.format("%.2f", chance));
	}
	
	private void printResult() {
		long start = System.currentTimeMillis();
		String[] cards = new String[textField_decks.length];
		for (int t = 0; t < textField_decks.length; t++) {
			cards[t] = textField_decks[t].getText();
		}
		includeListModel.clear();
		for (int i = 0; i < result.size(); i++) {
			List<Integer> line = result.get(i);
			includeListModel.addElement(formatResultLine(line, cards, 0));
		}
		excludeListModel.clear();
		for (int j = 0; j < notResult.size(); j++) {
			List<Integer> line = notResult.get(j);
			excludeListModel.addElement(formatResultLine(line, cards, 0));
		}
		long duration = System.currentTimeMillis() - start;
		System.out.println("printResult() takes " + duration / 1000 + " seconds");
	}
	
	//mode 0: num[card] (default)
	//mode 1: num only
	//mode 2: card only
	private String formatResultLine(List<Integer> line, String[] cards, int mode){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<line.size();i++){
			int cardIdx = line.get(i)-1;
			cardIdx = cardIdx == -1 ? 9 : cardIdx;
			sb.append(line.get(i)+"["+cards[cardIdx]+"]");
			if(i+1 == this.totalDraw){
				sb.append(" || ");
			}else if(i+1<line.size()){
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	private Pair<Set<Integer>, Integer>[] createTargetGroup() {
		int size = 0;
		for(int i=0;i<checkBox_conds.length;i++){
			if(checkBox_conds[i].isSelected()){
				String txt = textField_conds[i].getText();
				if(txt.trim().length()>0){
					size++;
				}
			}
		}
		Pair<Set<Integer>, Integer>[] targetGroup = new Pair[size];
		int s = 0;
		for(int i=0;i<checkBox_conds.length;i++){
			if(checkBox_conds[i].isSelected()){
				int pickNum = Integer.valueOf(comboBox_conds[i].getSelectedItem().toString());
				String nums = textField_conds[i].getText();
				Set<Integer> set = new HashSet<Integer>();
				for(int x=0;x<nums.length();x++){
					Integer card = -1;
					try {
						card = Integer.valueOf(nums.charAt(x)+"");
					} catch (Exception e) {
						throw new RuntimeException("Invalid card selection: "+nums+" , condition #"+(i+1));
					}
					if(set.contains(card)==false){
						set.add(card);
					}else{
						throw new RuntimeException("Duplicate card selection: "+nums+" , condition #"+(i+1));
					}
				}
				if(set.isEmpty()){
					throw new RuntimeException("Empty card selection, condition #"+(i+1));
				}else if(set.size()<pickNum){
					throw new RuntimeException("Cannot pick "+pickNum+" from only "+set.size()+" card selection: "+nums+" , condition #"+(i+1));
				}
				targetGroup[s] = new Pair<Set<Integer>, Integer>(set, pickNum);
				s++;
			}
		}
		return targetGroup;
	}

	private void setNextCard(String card){
		
		int target = -1;
		
		if (last_deck_idx != -1) {
			target = last_deck_idx;
			//textField_decks[last_deck_idx].setText(text);
			if (last_deck_idx + 1 < textField_decks.length) {
				textField_decks[last_deck_idx+1].requestFocus();
			}else{
				textField_decks[last_deck_idx].requestFocus();
			}
		}else{
			for(int i=0;i<textField_decks.length;i++){
				JTextField textField = textField_decks[i];
				if(textField.getText().trim().length()==0){
					target = i;
					if (i + 1 < textField_decks.length) {
						textField_decks[i+1].requestFocus();
					}else{
						textField_decks[i].requestFocus();
					}
					break;
				}
			}
		}
		
		String text = card;
		if(text.length()>0 && text.charAt(text.length()-1)=='*'){
			String token = text.substring(0,text.length()-1);
			List<Integer> nums = new ArrayList(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,0}));
			for (int i = 0; i < textField_decks.length; i++) {
				String value = textField_decks[i].getText().trim();
				int j = 0;
				while(true){
					if(value.equals(token+nums.get(j))){
						nums.remove(j);
						j--;
					}
					j++;
					if (j >= nums.size()) {
						break;
					}
				}
			}
			text = nums.size()>0 ? token+nums.get(0) : null;
		}
		
		if (target >= 0 && text != null) {
			textField_decks[target].setText(text);
		}else{
			//error
		}
		

	}

	public void focusGained(FocusEvent e) {
		for(int i=0;i<textField_decks.length;i++){
			if(e.getSource().equals(textField_decks[i])){
				this.last_deck_idx = i;
				textField_decks[i].select(0, textField_decks[i].getText().length());
				return;
			}
		}
	}

	public void focusLost(FocusEvent e) {
		for(int i=0;i<textField_decks.length;i++){
			if(e.getSource().equals(textField_decks[i])){
				this.last_deck_idx = i;
				textField_decks[i].select(0, 0);
				return;
			}
		}
	}
	
	private void setCards(){
		int idx = comboBox_class.getSelectedIndex();
		String[][] cards = this.txtCards.get(idx);
		for(int r=0;r<cards.length;r++){
			lbl_types[r].setText(cards[r][0]);
			for(int c=1;c<cards[r].length;c++){
				String text = cards[r][c];
				text = text == null ? " " : text;
				btn_cards[r*8+c-1].setText(text);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(this.comboBox_class.equals(e.getSource())){
			setCards();
		}
	}
	
	protected static class MyDeckFilter extends DocumentFilter {
		
		private JTextField[] textField_conds;
		private JTextField[] textField_decks;
		
		public MyDeckFilter(JTextField[] textField_conds, JTextField[] textField_decks) {
			this.textField_conds = textField_conds;
			this.textField_decks = textField_decks;
		}
		
		private void updateTooltipText(){
			//System.out.println("updateTooltipText()");
			for(JTextField textField : textField_conds){
				String tooltip = transformText(textField.getText(), textField_decks);
				//System.out.println(tooltip);
				textField.setToolTipText(tooltip);
			}
		}
		
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {
			super.insertString(fb, offset, string, attr);
			updateTooltipText();
		}
		
		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
			super.replace(fb, offset, length, text, attrs);
			updateTooltipText();
		}
		
		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			super.remove(fb, offset, length);
			updateTooltipText();
		}
		
	}
	
	protected static class MyIntFilter extends DocumentFilter {
		
		private JTextField textField;
		private JTextField[] textField_decks;

		public MyIntFilter(JTextField textField_cond, JTextField[] textField_decks) {
			this.textField = textField_cond;
			this.textField_decks = textField_decks;
		}
		
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.insert(offset, string);

			if (test(sb.toString())) {
				super.insertString(fb, offset, string, attr);
			} else {
				// warn the user and don't allow the insert
			}
		}

		private boolean test(String text) {
			try {
				if (text.equals("")==false) {
					Integer.parseInt(text);
					for (int i = 0; i < text.length() - 1; i++) {
						char c = text.charAt(i);
						for (int j = i + 1; j < text.length(); j++) {
							if (c == text.charAt(j)) {
								return false;
							}
						}
					}
				}
				//TODO:
				this.textField.setToolTipText(transformText(text, textField_decks));
				ToolTipManager.sharedInstance().mouseMoved(
				        new MouseEvent(this.textField, 0, 0, 0, 
				                0, 0, // X-Y of the mouse for the tool tip
				                0, false));
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		
		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);

			if (test(sb.toString())) {
				super.replace(fb, offset, length, text, attrs);
			} else {
				// warn the user and don't allow the insert
			}

		}

		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.delete(offset, offset + length);

			if (test(sb.toString())) {
				super.remove(fb, offset, length);
			} else {
				// warn the user and don't allow the insert
			}

		}
	}
	
	private static String transformText(String text, JTextField[] textField_decks){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			Integer idx = Integer.valueOf(""+text.charAt(i))-1;
			if(idx==-1)	idx = 9;
			String card = textField_decks[idx].getText();
			if(card.trim().length()>0){
				sb.append(card);
			}else{
				sb.append(text.charAt(i));
			}
			if(i<text.length()-1){
				sb.append(", ");
			}
		}
		return sb.toString();
	}

}
