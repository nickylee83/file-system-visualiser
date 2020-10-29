package FileSystemVisualizerV2;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class AppRework2{
    public static Path startingPath = getDefaultPath();
    public static String[] fileStats = new String[5];

    public static void main(String[] args){
        
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
    
        }

    public static Path getDefaultPath(){
        if(System.getProperty("os.name") == "Windows"){
            return Paths.get(System.getenv("SystemDrive"));
        }
        else{
            return Paths.get(System.getProperty("user.dir"));
        }
    }

    public static void createGUI(){
        JFrame frame = new JFrame("GridBagLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 900));
        addComponents(frame.getContentPane());
        frame.pack();
        frame.setVisible(true); 
    }

    public static void addComponents(Container pane){//constructs ui
        pane.setLayout(new GridBagLayout());//sets the layout type for swing
        GridBagConstraints c = new GridBagConstraints();//sets the properties for each component as it is added
        JTextField textField = new JTextField(20);//User input field
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(textField, c);  
        
        JButton button = addButton(c, false, "Browse", 0.0, 3.0, 0.0);//browse button
        pane.add(button, c);
        button.addActionListener(e -> selectFile(pane, textField));//attaches the function to be executed when the user clics the browse button. 

        JButton button2 = addButton(c, false, "Start", 0.0, 4.0, 0.0);//browse button
        pane.add(button2, c);
        
        JLabel title = new JLabel("");//main content. html can be written inside a jlabel text.
        title.setText("<html><h1> Drive Summary</h1><p> Total Files Visited: </p> <p></p> <p> Total folders visited: </p> <p></p> <p> Total file size:</p> <p></p> <p> Largest File: </p> <p></p> <p> Execution time: </p> </html>");
        c.gridx = 0;
        c.gridy = 1;
        pane.add(title, c);
       
        
        FlowLayout Flow = new FlowLayout();

        JPanel BoxPanel = new JPanel();
        BoxPanel.setLayout(Flow);
        BoxPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        BoxPanel.setPreferredSize(new Dimension(400, 600));
        c.gridx = 0;
        c.gridy = 2;
        
        c.weighty = 20;
        c.weightx = 0;

        RectTest mainPanel = new RectTest();
        BoxPanel.add(mainPanel);
        button2.addActionListener(e -> runWalker(title, BoxPanel));//attaches the function to be executed after the user clics the start button.
        JScrollPane scrollPane = new JScrollPane(BoxPanel);

        pane.add(scrollPane, c);


    }

    public static JButton addButton(GridBagConstraints cons, boolean shouldfill, String name, double...weights){//varargs(variable arguments) takes any number of arguments as an array
        //GridBagConstraints is mutable so the properties of each object can be modifed on the method without returning to the orignal object
        JButton butt = new JButton(name);//browse button
        if(shouldfill){
            cons.fill = GridBagConstraints.HORIZONTAL;
        }
        cons.weightx = weights[0];
        cons.gridx = (int)weights[1];
        cons.gridy = (int)weights[2];
        return butt;
    }
    
    public static void selectFile(Container panes, JTextField selector) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // chooser file options after selecting folder.
        if (chooser.showOpenDialog(panes) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            startingPath = f.toPath();//sets the starting path to the directory that the user selected
            selector.setText(f.toPath().toString());
        }
    }

    public static void runWalker(JLabel swingElementToUpdate, JPanel bars){
        System.out.println("Starting");
        walker walk = new walker();
        double time = System.currentTimeMillis();
        try{
            Files.walkFileTree(startingPath, walk);
        }catch(Exception e){
            e.printStackTrace();
        }
        double finishTime = System.currentTimeMillis() - time;
        String[] stats = walk.getFileStats();
        stats[4] = Double.valueOf(finishTime).toString();
        fileStats = stats;
        swingElementToUpdate.setText("<html><h1>Drive Summary</h1><p>Total Files Visited: " +fileStats[3] + "</p> <p></p> <p>Total folders visited: " + fileStats[2] + "</p> <p></p> <p>Total file size: " + fileStats[0] + "</p> <p></p> <p>Largest File: " + fileStats[1] +"</p> <p></p> <p>Execution time:" + fileStats[4] +" </p> </html>");
        //updates the text

        //add bars
        walk.getFiles() //get the file list 
            .stream() //stream the list 
            .mapToInt(e -> Integer.parseInt(fileStats[1]) / (int)e.getTotalSpace()) //map the files to size
            .mapToObj(e -> new RectTest(e))//map stream elements to rectangles given the size of the integer of the elemwnt
            .forEach(e -> bars.add(e));//collect every bar into the scroll pane
    }
}
