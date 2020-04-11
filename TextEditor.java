import java.awt.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import java.io.*;
import java.util.*;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterJob;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
                                                        
public class TextEditor implements ActionListener, CaretListener, ChangeListener{
    
    public JTextArea page, lineNumber;
    public JFrame frame;
    public String path=""; 
    JTextField findText, replaceText;
    JLabel statusBar, label;
    JScrollPane pane; JSlider slide;
    int linenum = 1, word=1, columnnum = 1, linenumber, columnnumber;
    String text="", words[];
    public JPanel panel2;
    public TextEditor(){
        //create the main window
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000,600));
    	frame.setLayout(new BorderLayout());
    	frame.setTitle("Editor");
        // create the menubar to hold all menus
    	JMenuBar menuContainer = new JMenuBar();
        //create file menu
    	JMenu fileMenu = new JMenu("File");
        //create menu items under file menu
    	JMenuItem newPage = new JMenuItem("New", 'N');
    	newPage.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	newPage.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                int option = JOptionPane.showConfirmDialog(null, "Do you wish to save?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == 0){
                    saveAsFile();
                    page.setText("");
                }
                else if(option == 1){
                    page.setText("");
                }
                else{

                } 
            }
        });
        fileMenu.add(newPage);

        JMenuItem open = new JMenuItem("Open", 'O');
    	open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	open.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	JFileChooser fopen = new JFileChooser();
		int option = fopen.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION)
                {
                    page.setText("");
                    try{
                        Scanner scan = new Scanner(new FileReader(fopen.getSelectedFile().getPath()));
                        while(scan.hasNext()){
                            page.append(scan.nextLine()+"\n");
                        } 
                        
                        frame.setTitle(fopen.getSelectedFile().getPath() + "- Editor");
                        path = fopen.getSelectedFile().getPath();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });
        fileMenu.add(open);
        
        JMenuItem save = new JMenuItem("Save", 'S');
    	save.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	if (path.equals("")){
            		saveAsFile();
 	          	}
 	          	else{
 	          		saveFile();
 	          	}
            }
        });
        fileMenu.add(save);

        JMenuItem saveAs = new JMenuItem("SaveAs");
    	saveAs.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	saveAsFile();
            }
        });
        fileMenu.add(saveAs);
        //Use separator to add line in-between
        fileMenu.addSeparator();
        JMenuItem pageSetup = new JMenuItem("Page Setup");
    	pageSetup.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	PrinterJob  pj = PrinterJob.getPrinterJob();
                pj.pageDialog(pj.defaultPage());
            }
        });
        fileMenu.add(pageSetup);
        
        JMenuItem print = new JMenuItem("Print", 'P');
        save.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	print.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
               PrinterJob  pj = PrinterJob.getPrinterJob();
               if(pj.printDialog()){
                   try{
                          pj.print();
                   }
                   catch(Exception e){
                            System.out.print(e);
                    }
               }
                
            }
        });
        fileMenu.add(print);
        
        JMenuItem exit = new JMenuItem("Exit");
    	exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	int option = JOptionPane.showConfirmDialog(null, "Do you wish to save?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == 0){
                    if (path.equals("")){
                        saveAsFile();
                    }else{
                       saveFile(); 
                    }
                    System.exit(0);
                }
                else if(option == 1){
                    System.exit(0);
                }
                else{

                } 
            }
        });
        fileMenu.add(exit);
        // add file menu to the main menu
        menuContainer.add(fileMenu);
        //setting up the edit menu
        JMenu editMenu = new JMenu("Edit");
        // creating sub menus under edit menu
        JMenuItem undo = new JMenuItem("Undo", 'Z');
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	undo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	//saveAsFile();
            }
        });
        editMenu.add(undo);
        
        JMenuItem redo = new JMenuItem("Redo", 'Y');
        redo.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	redo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	
            }
        });
        editMenu.add(redo);
        //Use separator to add line in-between
        editMenu.addSeparator();
        JMenuItem copy = new JMenuItem("Copy", 'C');
        copy.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	copy.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	page.copy();
            }
        });
        editMenu.add(copy);
        
        JMenuItem cut = new JMenuItem("cut", 'X');
        cut.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	cut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	page.cut();
            }
        });
        editMenu.add(cut);
        
        JMenuItem paste = new JMenuItem("Paste", 'V');
        paste.setAccelerator(KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	paste.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	page.paste();
            }
        });
        editMenu.add(paste);
        //Use separator to add line in-between
        editMenu.addSeparator();
        JMenuItem delete = new JMenuItem("Delete");
        //redo.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	delete.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	page.setText("");
            }
        });
        editMenu.add(delete);
        
        JMenuItem findRep = new JMenuItem("Find and Replace", 'F');
        findRep.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	findRep.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	try{
                   findAndReplace();
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                
            }
        });
        editMenu.add(findRep);
        
        JMenuItem goTo = new JMenuItem("Go To", 'G');
        goTo.setAccelerator(KeyStroke.getKeyStroke('G', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	goTo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                goToLine();
            }
        });
        editMenu.add(goTo);
        
        JMenuItem selectAll = new JMenuItem("Select All", 'A');
        selectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	selectAll.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	page.selectAll();
            }
        });
        editMenu.add(selectAll);
        // add the edit menu to the main menubar
        menuContainer.add(editMenu);
        // create the format menu
        JMenu format = new JMenu("Format");
        //create the submenus for format menu
        JMenuItem font = new JMenuItem("Font");
        //selectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	font.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
//                JFontChooser fd = new JFontChooser();
//                fd.show();
//                if(fd.getReturnStatus() == fd.RET_OK){
//                       page.setFont(fd.getFont());
//                }
//                fd.dispose();
            }
        });
        format.add(font);
        JMenuItem color = new JMenuItem("Color");
        //selectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	color.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                Color c = JColorChooser.showDialog(frame,"Choose Color",Color.BLACK);        
                JTextArea colorPage = new JTextArea(page.getSelectedText()); 
                colorPage.setSelectedTextColor(c);
                page.replaceSelection(colorPage.getText());
            }
        });
        format.add(color);
        
        final JCheckBoxMenuItem wrap = new JCheckBoxMenuItem("Word wrap");
        //selectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	wrap.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	boolean checkState = wrap.isSelected();
                if (checkState == true){
                    page.setLineWrap(true);
                    page.setWrapStyleWord(true);
                }
                else
                    page.setLineWrap(false);
                    page.setWrapStyleWord(false);
            }
        });
        format.add(wrap);
        // Add format menu to the main menubar
        menuContainer.add(format);
        // create the view menu
        JMenu view = new JMenu("View");
        //create the menus items for format menu
        JMenu zoom = new JMenu("Zoom");
        //create submenu for zoom menu
        JMenuItem zoomIn = new JMenuItem("Zoom In", '+');
        zoomIn.setAccelerator(KeyStroke.getKeyStroke('+', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	zoomIn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	if(slide.getValue() < 200)
                {
                    label.setText("      Zoom level = " + (slide.getValue()+10));
                    slide.setValue(slide.getValue()+10);
                    pane.setPreferredSize(new Dimension(slide.getValue()+10,slide.getValue()+10));
                    lineNumber.setPreferredSize(new Dimension(slide.getValue()+10,slide.getValue()+10));
                    lineNumber.setFont(new Font(null,0,slide.getValue()+10));
                    page.setFont(new Font(null,0,slide.getValue()+10));
                    frame.validate();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Zoom in can not go above maximum zoom level.");
                }
            }
        });
        zoom.add(zoomIn);
        
        JMenuItem zoomOut = new JMenuItem("Zoom Out", '-');
        zoomOut.setAccelerator(KeyStroke.getKeyStroke('-', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	zoomOut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	if(slide.getValue() >= 20){
                    label.setText("      Zoom level = " + (slide.getValue()-10));
                    slide.setValue(slide.getValue()-10);
                    pane.setPreferredSize(new Dimension(slide.getValue()-10,slide.getValue()-10));
                    lineNumber.setPreferredSize(new Dimension(slide.getValue()-10,slide.getValue()-10));
                    lineNumber.setFont(new Font(null,0,slide.getValue()-10));
                    page.setFont(new Font(null,0,slide.getValue()-10));
                    frame.validate();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Zoom out can not go below minimum zoom level.");
                }
            }
        });
        zoom.add(zoomOut);
        
        JMenuItem defaultZoom = new JMenuItem("Default Zoom", 'O');
        defaultZoom.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	defaultZoom.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                slide.setValue(16);
                label.setText("      Zoom level = " + slide.getValue());
                pane.setPreferredSize(new Dimension(slide.getValue(),slide.getValue()));
                lineNumber.setPreferredSize(new Dimension(slide.getValue(),slide.getValue()));
                lineNumber.setFont(new Font(null,0,slide.getValue()));
                page.setFont(new Font(null,0,slide.getValue()));
                frame.validate();
            }
        });
        zoom.add(defaultZoom);
        // Add zoom menu item to view menu
        view.add(zoom);
        
        final JCheckBoxMenuItem status = new JCheckBoxMenuItem("Show Status Bar");
    	status.setSelected(true);
        status.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {          
                boolean checkBar = status.isSelected();
                if(checkBar){                    
                     // Give the status update value  
                    updateStatus(linenum, columnnum, word, text);
                    panel2.add(statusBar);
                    panel2.repaint();
                }
                else{
                    panel2.remove(statusBar);
                    panel2.repaint();
                }
            }
        });
        view.add(status);
        //Add view menu to the menubar
        menuContainer.add(view);
        //create the help menu 
        JMenu help = new JMenu("Help");
        //create the submenus for help menu
        JMenuItem about = new JMenuItem("Get Help");
        //selectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	about.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	JOptionPane.showMessageDialog(null, "Simple text editor like notepad using Java language. It was created by Yemi lead instructor for "
                + "Java class at SQI college of ICT. Copyright of SQI");
            }
        });
        help.add(about);
        
        JMenuItem contact = new JMenuItem("About Editor");
        //selectAll.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
    	contact.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	JOptionPane.showMessageDialog(null, "SQI college of ICT, Opposit Yoako filling station Ilorin road, Ogbomosho, Oyo State, Nigeria.");
            }
        });
        help.add(contact);
        // Add help menu to the menubar
        menuContainer.add(help);
        // add main menu i.e menubar to the main window.
        frame.add(menuContainer, BorderLayout.NORTH);
        // create the main text area for text editor
        lineNumber = new JTextArea("1");
        lineNumber.setBackground(Color.LIGHT_GRAY);
	lineNumber.setEditable(false);
        page = new JTextArea();
        page.setFocusable(true);
        page.addCaretListener(new CaretListener() {
            
            @Override
            public void caretUpdate(CaretEvent ce) {
                JTextArea editArea = (JTextArea)ce.getSource();
                try {
                    text = page.getText();   
                    words = text.split("\\s");
                    word = words.length;
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);
                    columnnum = caretpos - editArea.getLineStartOffset(linenum);
                    linenum += 1;
                    
                    // this will set the line number 
                }
                catch(Exception ex) { }
                
                updateStatus(linenum, columnnum, word, text);
            }
        });
        
        page.getDocument().addDocumentListener(new DocumentListener(){
	public String getText(){
            int caretPosition = page.getDocument().getLength();
            Element root = page.getDocument().getDefaultRootElement();
            String text = "1" + System.getProperty("line.separator");
            for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
		text += i + System.getProperty("line.separator");
            }
            return text;
            }
            @Override
            public void changedUpdate(DocumentEvent de) {
                lineNumber.setText(getText());
            }
 
            @Override
            public void insertUpdate(DocumentEvent de) {
                lineNumber.setText(getText());
            }
 
            @Override
            public void removeUpdate(DocumentEvent de) {
                lineNumber.setText(getText());
            }
 
        });
        pane = new JScrollPane();
        pane.getViewport().add(page);
	pane.setRowHeaderView(lineNumber);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // Create a slider for zooming 
        slide = new JSlider(8, 200, 16);  
        // paint the ticks and tracks 
        slide.setPaintTrack(true); 
        slide.setPaintTicks(true); 
        // set spacing 
        slide.setMajorTickSpacing(50); 
        slide.setMinorTickSpacing(5);
        slide.addChangeListener(new ChangeListener (){
             // if JSlider value is changed 
            @Override
            public void stateChanged(ChangeEvent e) { 
                label.setText("      Zoom level = " + slide.getValue());
                pane.setPreferredSize(new Dimension(slide.getValue(),slide.getValue()));
                lineNumber.setPreferredSize(new Dimension(slide.getValue(),slide.getValue()));
                lineNumber.setFont(new Font(null,0,slide.getValue()));
                page.setFont(new Font(null,0,slide.getValue()));
                frame.validate();
            }
    });
        pane.setPreferredSize(new Dimension(slide.getValue(),slide.getValue()));
        lineNumber.setPreferredSize(new Dimension(slide.getValue(),slide.getValue()));
        lineNumber.setFont(new Font(null,0,slide.getValue()));
        page.setFont(new Font(null,0,slide.getValue()));
        statusBar = new JLabel("Line: " + linenumber + " Column: " + columnnumber +" Characters: "+text.length()+" Words: "+word+ "     "); 
        label = new JLabel("Zoom level: " + slide.getValue());
        panel2 = new JPanel(new BorderLayout());
        panel2.add(label, BorderLayout.CENTER);
        panel2.add(statusBar, BorderLayout.WEST);
        panel2.add(slide, BorderLayout.EAST);
        frame.add(pane, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.SOUTH);
        frame.pack();
	frame.setVisible(true);
    }
    // if JSlider value is changed 
    @Override
    public void stateChanged(ChangeEvent e) { } 
    
    // This helper function updates the status bar with the line number and column number.
    private void updateStatus(int linenumber, int columnnumber, int word, String text) {
        // create and add a status bar here
        statusBar.setText("Line: " + linenumber + " Column: " + columnnumber +" Characters: "+text.length()+" Words: "+word);
    }
    @Override
    public void actionPerformed(ActionEvent ae){
       
    }
    public void saveFile(){
	        try{
	            	BufferedWriter out = new BufferedWriter(new FileWriter(path));
	                out.write(page.getText());
	                out.close();
	        }catch(Exception ex){
	                    JOptionPane.showMessageDialog(null, ex.getMessage());
	                }
    }

    public void saveAsFile(){
    	JFileChooser fsave = new JFileChooser();
		int option = fsave.showSaveDialog(fsave);
		int fileToSave = option;
		JFileChooser fileSave = fsave;
        if(fileToSave == JFileChooser.APPROVE_OPTION){
	        try{
	                BufferedWriter out = new BufferedWriter(new FileWriter(fileSave.getSelectedFile().getPath()));
	                out.write(page.getText());
	                out.close();
	                frame.setTitle(fileSave.getSelectedFile().getPath() + "- OpescoNote");
	                path = fileSave.getSelectedFile().getPath();
	        }catch(Exception ex){
	                    JOptionPane.showMessageDialog(null, ex.getMessage());
	                }
        }
    }
    public void findAndReplace()
    {
        JDialog findR = new JDialog();
	findR.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	findR.setLayout(new GridLayout(3,1));
	findR.setSize(400, 150);
	findR.setLocation(500, 300);
        replaceText = new JTextField("Enter text to replace", 20);
	findText = new JTextField("Enter text to find", 20);	
	findR.setTitle("Find and Replace");
        final JButton find = new JButton("Find");
        find.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	try {
                    String search = findText.getText();
                    int n = page.getText().indexOf(search);
                    page.select(n,n+search.length());
                }
                catch(Exception e1) {
                  JOptionPane.showMessageDialog(null, e1.getMessage());
                }
            }
        });
	final JButton replace = new JButton("Replace");
        replace.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	try {
                    String search = findText.getText();
                    int n = page.getText().indexOf(search);
                    String replacing = replaceText.getText();
                    page.replaceRange(replacing, n, n+search.length());
                }
                catch(Exception e1) {
                  JOptionPane.showMessageDialog(null, e1.getMessage());
                }
            }
        });	
	JPanel panel = new JPanel();
	findR.add(findText);
	findR.add(replaceText);
	panel.add(find);
	panel.add(replace);
	findR.add(panel);
        findR.setVisible(true);
    }
    
    public void goToLine(){
        final JDialog go2 = new JDialog(frame);
        go2.setSize(200, 100);
	go2.setLocation(200, 300);
        JPanel pa = new JPanel(new BorderLayout());
        JLabel lb = new JLabel("Enter line number");
        pa.add(lb, BorderLayout.NORTH);
        final JTextField tf = new JTextField(10);
        pa.add(tf, BorderLayout.CENTER);
        JButton bn = new JButton("Go to line");
        bn.setSize(5, 5);
        bn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	try {
                    String line = tf.getText();
                    page.setCaretPosition(page.getDocument().getDefaultRootElement().getElement(Integer.parseInt(line)-1).getStartOffset());
                    page.requestFocusInWindow();
                    go2.dispose();
                }
                catch(Exception e1) {
                  JOptionPane.showMessageDialog(null, e1.getMessage());
                }
            }
        });
        pa.add(bn, BorderLayout.SOUTH);
        go2.add(pa);
        go2.setVisible(true);
    }
    @Override
    public void caretUpdate(CaretEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public static void main(String[] args) {
	TextEditor ev = new TextEditor();
    }

}
