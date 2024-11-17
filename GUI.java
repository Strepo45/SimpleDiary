import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.io.FileWriter;

public class GUI implements ActionListener
{
    JFrame window;
    JTextArea textArea;
    JTextField headerEntry;
    JScrollPane scrollPane;
    JPanel savingPanel;
    JMenuBar menuBar;
    JMenu menuFile, menuEdit, menuFormat, menuColor;
    JMenuItem iNew, iOpen, iSave, iSaveAs, iExit;
    JLabel date, saveQuery;
    JButton cancel, save;
    JSeparator separator;
    File entry, openFile = null;
    FileWriter fw;

    String lastSaved, path, text, header;
    ArrayList<File> files;

    public static void main(String[] args) {
        new GUI(); //
    }

    //constructor class
    public GUI()
    {
        createWindow();
        createTextArea();
        createMenuBar();
        createFileMenu();
        createDirectory();
        window.setVisible(true); //set the window visible
    }

    //creates folder in which entries will be saved in
    public void createDirectory()
    {
        path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + "Entries";
        files = new ArrayList<File>();
        files.add(new File (path));
        /*
        Checks if file directory is created
        You should be able to see the file in file explorer upon run

        if (entry.exists()) {
            System.out.println(entry + " already exists");
        } else if (entry.mkdirs()) {
            System.out.println(entry + " was created");
        } else {
            System.out.println(entry + " was not created");
        }*/
    }

    //Method that generates the window for the notepad
    public void createWindow()
    {
        window = new JFrame("Notepad"); //top bar of window text
        window.setSize(800, 600); //size of window
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //window closes when clicking X

    }

    //generates New window
    public void createNewFileWindow()
    {
        //gets text to save first
        text = textArea.getText();

        //creates new panel to replace scroll planel, formats buttons and such
        savingPanel = new JPanel();
        savingPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        window.add(savingPanel);

        saveQuery = new JLabel("New entry name: ");
        saveQuery.setHorizontalAlignment(SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = GridBagConstraints.CENTER;
        c.anchor = GridBagConstraints.PAGE_START;
        savingPanel.add(saveQuery, c);



        headerEntry = new JTextField(10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        headerEntry.setAlignmentY(Component.CENTER_ALIGNMENT);
        savingPanel.add(headerEntry, c);

        save = new JButton("Save");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        save.addActionListener(this);
        savingPanel.add(save, c);

        cancel = new JButton("Cancel");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        cancel.addActionListener(this);
        savingPanel.add(cancel, c);


        window.show();
    }

    //Method that generates the text area for the notepad
    public void createTextArea()
    {
        textArea = new JTextArea();

        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); //creates a general border for window (looks better imo but barely visible)
        window.add(scrollPane);
    }


    //Method that creates a bar at the top of the notepad with
    public void createMenuBar()
    {
        menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);

        menuFile = new JMenu("File");
        menuBar.add(menuFile);

        menuEdit = new JMenu("Edit");
        menuBar.add(menuEdit);

        menuFormat = new JMenu("Format");
        menuBar.add(menuFormat);

        menuColor = new JMenu("Color");
        menuBar.add(menuColor);

        separator = new JSeparator();
        menuBar.add(separator);

        date = new JLabel("Last saved:");
        menuBar.add(date);
    }

    //Method to add & hold the items listed in the file section
    public void createFileMenu()
    {
        iNew = new JMenuItem("New");
        iNew.addActionListener(this);
        menuFile.add(iNew);


        iOpen = new JMenuItem("Open");
        iOpen.addActionListener(this); //makes it so actionListener is listening for actions on this particular button
        menuFile.add(iOpen);

        iSave = new JMenuItem("Save");
        iSave.addActionListener(this); //makes it so actionListener is listening for actions on this particular button
        menuFile.add(iSave);

        iSaveAs = new JMenuItem("Save As");
        iSaveAs.addActionListener(this); //makes it so actionListener is listening for actions on this particular button
        menuFile.add(iSaveAs);

        iExit = new JMenuItem("Exit");
        menuFile.add(iExit);
    }

    @Override
    public void actionPerformed (ActionEvent e){

        //prompt user with a new file-saving window
        //if "new" menubar button pressed.
        if (e.getSource() == iNew)
        {
            window.remove(scrollPane);
            createNewFileWindow();
            window.show();
        }
        //if save button pressed,
        //create a new file
        if(e.getSource() == save)
        {
            //gets new header entry name before closing panel
            header = File.separator + headerEntry.getText();
            path += header;
            //creates new file with header name
            files.add(new File(path));
            //resets openFile so that it may be saved
            //(save does not work if file is null)
            openFile = files.getLast();

            //entry text was already read in createNewFileWindow
            //opens printwriter which writes previously saved text, then closes
            //printwriter
            try
            {
                fw = new FileWriter(files.getLast());
                fw.write(text);
                //date function here is identical to save
                fw.write("Last saved: " + new Date());
                fw.close();

                date.setText("Last saved: " + new Date());
                fw.close();
            }
            catch (IOException ex)
            {
                System.out.println(ex);
                throw new RuntimeException(ex);

            }

            //resets all text variables
            header = "";
            text = "";

            path =  System.getProperty("user.home") + File.separator + "Documents";
            path += File.separator + "Entries";

            //unknown why code must be ordered this way,
            //but will only work in fullscreen if this code is not present.
            window.remove(savingPanel);
            window.add(scrollPane);
            window.pack();
            window.setSize(800, 600); //size of windo
            window.show();

        }
        if(e.getSource() == cancel)
        {
            window.remove(savingPanel);
            window.add(scrollPane);
            window.pack();
            window.setSize(800, 600); //size of window
            window.show();
            //does nothing save wise as user has not prompted such.

        }

        if (e.getSource() == iOpen){ //if Open button is clicked, do the following

            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null); //select file to open
            //int response = fileChooser.showSaveDialog(null); //select file to save
            lastSaved = ""; //resets last saved
            if (response == JFileChooser.APPROVE_OPTION){ //if the button that was clicked inside the fileChooser is Open
                openFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                //clears text so it is not loaded on top of prexisting entry
                textArea.setText("");
                try{
                    FileReader reader = new FileReader(openFile);
                    int data = reader.read();
                    int counter = 0;


                    while(data != -1)
                    {
                        //if last reader reaches last 40 characters of file,
                        //do not print to textArea and rather add to String
                        //of JLabel.
                        //Otherwise, continue writing as normal
                        if (openFile.length() -counter <= 40)
                        {
                            lastSaved += String.valueOf((char)data);
                            data = reader.read();
                            //delete comment to see last 40 printed System.out.println(lastSaved);
                            counter++;
                        }
                        else
                        {
                            textArea.insert(String.valueOf((char)data), counter);
                            data = reader.read();
                            counter++;
                        }
                    }
                    date.setText(lastSaved);
                    reader.close();
                }
                catch(FileNotFoundException notFoundException){
                    System.out.println(notFoundException.getMessage());
                }
                catch(IOException ioException){
                    System.out.println(ioException.getMessage());
                }
            }
        }

        if (e.getSource() == iSave){ //if Save button is clicked, do the following
            String updatedText = textArea.getText();

            try {
                if (openFile == null){
                    textArea.insert("No file currently opened", 0);
                }
                else{
                    FileWriter writer = new FileWriter(openFile);
                    writer.write(updatedText);
                    //below line adds the date to the tail-end of saved file.
                    //it will not appear when file is read (see Open ActionListener func)
                    writer.write("Last saved: " + new Date());
                    writer.close();

                    date.setText("Last saved: " + new Date());

                }

            }
            catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }
        if (e.getSource() == iSaveAs){
            JFileChooser fileChooser = new JFileChooser();
            
        }


    }

}