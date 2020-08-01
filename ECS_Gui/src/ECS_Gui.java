import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.stream.events.Characters;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ECS_Gui {

    /////////////////////////////////
    //ALL THE DIRECTORIES MUST CHANGE
    /////////////////////////////////
    //START PAGE MAY CHANGE
    /////////////////////////////////
    //INSTRUCTION & ABOUT US PAGES NEED TO BE WRITTEN
    /////////////////////////////////

    static Font f1 = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 30);
    static Font f3 = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 100);
    static Font f2 = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 20);
    static Font f4 = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 50);


    public static class startPage extends JFrame implements ActionListener {

        JButton openFile, instructionsButton, aboutButton;

        startPage(){

            setSize(1422, 800);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            //background
            JLabel background = new JLabel(new ImageIcon("C:\\Users\\alire\\Desktop\\1.jpg"));
            background.setPreferredSize(new Dimension(1422, 800));
            add(background);

            //Open file button
            openFile = new JButton(new ImageIcon("C:\\Users\\alire\\Desktop\\openimage2.png"));
            openFile.setBounds(1140, 35, 150, 150);
            openFile.setContentAreaFilled(false);
            openFile.setOpaque(false);
            openFile.setBorderPainted(false);
            openFile.addActionListener(this);

            //Instructions Button
            instructionsButton = new JButton(new ImageIcon("C:\\Users\\alire\\Desktop\\info2.png"));
            instructionsButton.setBounds(1140, 310, 150, 150);
            instructionsButton.setOpaque(false);
            instructionsButton.setContentAreaFilled(false);
            instructionsButton.setBorderPainted(false);


            //About us button
            aboutButton = new JButton(new ImageIcon("C:\\Users\\alire\\Desktop\\about.png"));
            aboutButton.setBounds(1140, 570, 150, 150);
            aboutButton.setOpaque(false);
            aboutButton.setContentAreaFilled(false);
            aboutButton.setBorderPainted(false);


            background.add(aboutButton);
            background.add(instructionsButton);
            background.add(openFile);

            setVisible(true);

        }


        @Override
        public void actionPerformed(ActionEvent e){

            if (e.getSource() == openFile){

                JFileChooser fileChooser = new JFileChooser("C:\\Users\\alire\\Desktop");
                fileChooser.showSaveDialog(null);

                File selectedFile = fileChooser.getSelectedFile();

                if (selectedFile.getName().substring(selectedFile.getName().length() - 3).equalsIgnoreCase("txt")){

                    // main page opens
                    this.dispose();
                    mainPage m = new mainPage(selectedFile);

                }

                else
                {
                    JOptionPane.showMessageDialog(this, "Please Choose a Valid Input",
                            "Invalid File", JOptionPane.ERROR_MESSAGE);
                }


            }

            else if (e.getSource() == instructionsButton){

                //////

            }

            else if (e.getSource() == aboutButton){

                ////////

            }

        }

    }

    public static class mainPage extends JFrame implements ActionListener{

        JButton run, load, draw;
        JTextArea input;
        JLabel inputName, fileName;
        File f;

        mainPage(File file){

            f = file;

            setSize(1422, 800);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            //background
            JLabel background = new JLabel(new ImageIcon("C:\\Users\\alire\\Desktop\\main6.jpg"));
            background.setPreferredSize(new Dimension(1422, 800));
            add(background);


            //run(build) button
            run = new JButton();
            run.setBounds(207,10,108,108);
            run.setContentAreaFilled(false);
            run.setOpaque(false);
            run.setBorderPainted(false);
            run.addActionListener(this);

            //load(save) button
            load = new JButton();
            load.setBounds(651,8,108,110);
            load.setContentAreaFilled(false);
            load.setOpaque(false);
            load.setBorderPainted(false);
            load.addActionListener(this);

            //Draw Button
            draw = new JButton();
            draw.setBounds(1076,10,108,108);
            draw.setContentAreaFilled(false);
            draw.setOpaque(false);
            draw.setBorderPainted(false);
            draw.addActionListener(this);


            background.add(load);
            background.add(draw);
            background.add(run);

            try {

                fileName = new JLabel("Input File Name :");
                fileName.setFont(f2);
                fileName.setForeground(Color.BLACK);
                fileName.setBounds(200,150,200,40);

                inputName = new JLabel(file.getName());
                inputName.setBounds(400, 150, 300, 40);
                inputName.setBackground(Color.RED);
                inputName.setOpaque(true);
                inputName.setHorizontalAlignment(SwingConstants.CENTER);
                inputName.setVerticalAlignment(SwingConstants.CENTER);
                inputName.setForeground(Color.BLACK);
                inputName.setFont(f1);

                background.add(fileName);
                background.add(inputName);

                input = new JTextArea(Files.readString(file.toPath()));
                input.setBounds(200,200,500,450);
                input.setFont(f2);
                input.setBackground(Color.YELLOW);
                input.setForeground(Color.BLACK);
                background.add(input);

            }

            catch (IOException e) {
                e.printStackTrace();
            }

            setVisible(true);

        }



        @Override
        public void actionPerformed(ActionEvent e){

            if (e.getSource() == run){

                this.dispose();
                try {
                    DrawCircuit circuit = new DrawCircuit(f);
                }
                catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }

            else if (e.getSource() == load){

                String newInput = input.getText();

                try {

                    PrintWriter pw = new PrintWriter(f);
                    pw.write(newInput);
                    pw.close();
                    JOptionPane.showMessageDialog(this, "File Successfully Updated",
                            "Updated", JOptionPane.INFORMATION_MESSAGE);

                }
                catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }

            else if (e.getSource() == draw){

                //////////////////

            }

        }

    }

    public static class Point {

        int x;
        int y;
        boolean alreadyConnected;

        Point(int x, int y){

            this.x = x;
            this.y = y;
            this.alreadyConnected = false;

        }

        public void setAlreadyConnected(){

            this.alreadyConnected = true;
        }

    }

    public static class Ground extends Point{
        boolean used = false;

        Ground(int x, int y) {
            super(x, y);
        }

        public void setUsed(){
            this.used = true;
        }

    }

    /*public static class parallelHandling{

        boolean leftUsed = false;
        boolean upUsed = false;
        boolean straightBranch = false;

        public boolean checkUsage(Point positiveNode, Point negativeNode){
            if (positiveNode.alreadyConnected && negativeNode.alreadyConnected)
                return true;
            else
                return false;
        }

        public void checkLeft(Point positiveNode, Point negativeNode){
            leftUsed = true;
        }

        public boolean checkUp(Point positiveNode, Point negativeNode){
            upUsed = true;
        }


    }*/

    public static class Branch{
        Point positiveNode;
        Point negativeNode;
        int timesUsed;

        Branch(Point p, Point n){
            this.positiveNode = p;
            this.negativeNode = n;
            this.timesUsed = 0;
        }


        public boolean equals(Branch branch) {
           if ((this.positiveNode == branch.positiveNode && this.negativeNode == branch.negativeNode)
                   || (this.positiveNode == branch.negativeNode && this.negativeNode == branch.positiveNode)){
               return true;
           }
           else
               return false;
        }

    }


    public static class DrawCircuit extends JFrame {

        Point[] nodes = new Point[31];
        Ground[] groundNodes = new Ground[6];
        JLabel background;
        ArrayList<Branch> circuitBranches = new ArrayList<>();

        DrawCircuit(File input) throws FileNotFoundException {

            setSize(1422, 800);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            this.nodeSet();

            background = new JLabel(new ImageIcon("bgOfRun.jpg"));
            background.setPreferredSize(new Dimension(1422, 800));
            add(background);

            JLabel nameLabel = new JLabel();
            String label = String.format("%s   Circut", input.getName());
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setText(label);
            nameLabel.setBackground(Color.WHITE);
            nameLabel.setForeground(Color.BLACK);
            nameLabel.setOpaque(true);
            nameLabel.setFont(f4);
            nameLabel.setBorder(new LineBorder(Color.BLACK, 4));
            nameLabel.setBounds(450, 10, 500, 70);
            background.add(nameLabel);

            //sets the nodes in background
            for (int i = 0; i < 5; i++) {
                for (int j = i + 1; j < i + 7; j++) {
                    JLabel dot = new JLabel(".");
                    dot.setBackground(Color.BLACK);
                    dot.setFont(f3);
                    dot.setBounds(nodes[5 * i + j].x - 5, nodes[5 * i + j].y - 5, 10, 10);
                    dot.setOpaque(true);
                    background.add(dot);
                }
            }

            //saving each line of the input
            ArrayList<String> lines = new ArrayList<>();
            Scanner sc = new Scanner(input);
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            sc.close();

            for (int i = 0; i < lines.size(); i++) {

                String[] words = lines.get(i).trim().split("\\s");

                if (words[0].charAt(0) == '*' || lines.get(i).trim().equals(".tran")) {
                    //siktir
                } else {

                    //ac/dc sources
                    if (words.length == 7) {

                        if (Integer.parseInt(words[5]) == 0) {
                            this.drawBranch(Integer.parseInt(words[1]), Integer.parseInt(words[2]),
                                    Character.toUpperCase(words[0].charAt(0)), 'D');
                        } else {
                            this.drawBranch(Integer.parseInt(words[1]), Integer.parseInt(words[2]),
                                    Character.toUpperCase(words[0].charAt(0)), 'A');
                        }
                    }
                    //other elements
                    else {
                        this.drawBranch(Integer.parseInt(words[1]), Integer.parseInt(words[2]),
                                Character.toUpperCase(words[0].charAt(0)), ' ');
                    }

                }

            }

            this.drawGround();

            setVisible(true);

        }

        //method to set all the coordinates
        public void nodeSet() {

            nodes[0] = new Point(380, 730);

            for (int i = 0; i < 6; i++) {
                groundNodes[i] = new Ground(380 + 110 * i, 730);
            }

            for (int i = 0; i < 5; i++) {
                for (int j = i + 1; j < i + 7; j++) {
                    /*
                    nodes[5 * i + j].x = 380 + (j - i - 1) * 110;
                    nodes[5 * i + j].y = 70 + (5 - i) * 110;
                     */
                    nodes[5 * i + j] = new Point(380 + (j - i - 1) * 110, 70 + (5 - i) * 110);

                }
            }
        }

        public void drawBranch(int positiveNode, int negativeNode, char type, char AcDc) {
            boolean check = false;
            Branch newBranch = new Branch(nodes[positiveNode], nodes[negativeNode]);
            int index = 0;

            for (int i = 0; i < circuitBranches.size(); i++) {
                if (circuitBranches.get(i).equals(newBranch)) {
                    check = true;
                    circuitBranches.get(i).timesUsed++;
                    index = i;
                    break;
                }
            }

            if (check == true) {
                switch (circuitBranches.get(index).timesUsed){
                    case 1:
                        this.drawParallel(positiveNode, negativeNode, type, AcDc, false);
                        break;

                    case 2:
                        this.drawParallel(positiveNode, negativeNode, type, AcDc, true);
                        break;
                }
            }

            else {

            JLabel element = new JLabel("");
            nodes[positiveNode].setAlreadyConnected();
            nodes[negativeNode].setAlreadyConnected();
            this.circuitBranches.add(newBranch);

            if (positiveNode != 0 && negativeNode != 0) {
                //Horizontal Mode
                if (Math.abs(positiveNode - negativeNode) == 1) {

                    int leftOne = Math.min(positiveNode, negativeNode);

                    //element
                    switch (type) {

                        case 'R':
                            element = new JLabel(new ImageIcon("rx.png"));
                            element.setPreferredSize(new Dimension(110, 53));
                            element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 27, 110, 53);
                            break;

                        case 'L':
                            element = new JLabel(new ImageIcon("lx.png"));
                            element.setPreferredSize(new Dimension(110, 37));
                            element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 19, 110, 37);
                            break;

                        case 'C':
                            element = new JLabel(new ImageIcon("cx.png"));
                            element.setPreferredSize(new Dimension(110, 52));
                            element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 26, 110, 52);
                            break;

                        case 'V':
                            if (positiveNode - negativeNode > 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("vdcR.png"));
                                        element.setPreferredSize(new Dimension(110, 72));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 72);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acx.png"));
                                        element.setPreferredSize(new Dimension(110, 71));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 71);
                                        break;
                                }
                            } else if (positiveNode - negativeNode < 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("vdcL.png"));
                                        element.setPreferredSize(new Dimension(110, 72));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 72);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acx.png"));
                                        element.setPreferredSize(new Dimension(110, 71));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 71);
                                        break;
                                }
                            }

                            break;

                        case 'I':
                            if (positiveNode - negativeNode > 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("IdcR.png"));
                                        element.setPreferredSize(new Dimension(110, 71));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 71);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acx.png"));
                                        element.setPreferredSize(new Dimension(110, 71));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 71);
                                        break;
                                }
                            } else if (positiveNode - negativeNode < 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("IdcL.png"));
                                        element.setPreferredSize(new Dimension(110, 71));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 71);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acx.png"));
                                        element.setPreferredSize(new Dimension(110, 71));
                                        element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 36, 110, 71);
                                        break;
                                }
                            }

                            break;

                        case 'H':
                        case 'E':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("DvR.png"));

                            } else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("DvL.png"));
                            }
                            element.setPreferredSize(new Dimension(110, 80));
                            element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 40, 110, 80);
                            break;

                        case 'G':
                        case 'F':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("DiR.png"));

                            } else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("DiL.png"));
                            }
                            element.setPreferredSize(new Dimension(110, 79));
                            element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 40, 110, 79);
                            break;


                        default:
                            //
                            break;

                    }
                    background.add(element);

                }

                //Vertical Mode
                else if (Math.abs(positiveNode - negativeNode) == 6) {

                    int upOne = Math.max(positiveNode, negativeNode);

                    //element
                    switch (type) {

                        case 'R':
                            element = new JLabel(new ImageIcon("ry.png"));
                            element.setPreferredSize(new Dimension(53, 110));
                            element.setBounds(nodes[upOne].x - 27, nodes[upOne].y, 53, 110);
                            break;

                        case 'L':
                            element = new JLabel(new ImageIcon("ly.png"));
                            element.setPreferredSize(new Dimension(37, 110));
                            element.setBounds(nodes[upOne].x - 19, nodes[upOne].y, 37, 110);
                            break;

                        case 'C':
                            element = new JLabel(new ImageIcon("cy.png"));
                            element.setPreferredSize(new Dimension(52, 110));
                            element.setBounds(nodes[upOne].x - 26, nodes[upOne].y, 52, 110);
                            break;

                        case 'V':
                            if (positiveNode - negativeNode > 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("vdcU.png"));
                                        element.setPreferredSize(new Dimension(72, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 72, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acy.png"));
                                        element.setPreferredSize(new Dimension(71, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                        break;
                                }
                            } else if (positiveNode - negativeNode < 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("vdcD.png"));
                                        element.setPreferredSize(new Dimension(72, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 72, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acy.png"));
                                        element.setPreferredSize(new Dimension(71, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                        break;
                                }
                            }
                            break;

                        case 'I':
                            if (positiveNode - negativeNode > 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("IdcU.png"));
                                        element.setPreferredSize(new Dimension(71, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acy.png"));
                                        element.setPreferredSize(new Dimension(71, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                        break;
                                }
                            } else if (positiveNode - negativeNode < 0) {
                                switch (AcDc) {
                                    case 'D':
                                        element = new JLabel(new ImageIcon("IdcD.png"));
                                        element.setPreferredSize(new Dimension(71, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acy.png"));
                                        element.setPreferredSize(new Dimension(71, 110));
                                        element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                        break;
                                }
                            }
                            break;

                        case 'H':
                        case 'E':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("DvU.png"));
                            } else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("DvD.png"));
                            }
                            element.setPreferredSize(new Dimension(80, 110));
                            element.setBounds(nodes[upOne].x - 40, nodes[upOne].y, 80, 110);
                            break;

                        case 'F':
                        case 'G':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("DiU.png"));
                            } else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("DiD.png"));
                            }
                            element.setPreferredSize(new Dimension(79, 110));
                            element.setBounds(nodes[upOne].x - 40, nodes[upOne].y, 79, 110);
                            break;


                        default:
                            //
                            break;

                    }
                    background.add(element);


                }
            }
            else if (positiveNode == 0 || negativeNode == 0) {
                int upOne = 0;

                if (positiveNode == 0) {
                    upOne = negativeNode;
                } else if (negativeNode == 0) {
                    upOne = positiveNode;
                }

                groundNodes[upOne - 1].setUsed();


                //element
                switch (type) {

                    case 'R':
                        element = new JLabel(new ImageIcon("ry.png"));
                        element.setPreferredSize(new Dimension(53, 110));
                        element.setBounds(nodes[upOne].x - 27, nodes[upOne].y, 53, 110);
                        break;

                    case 'L':
                        element = new JLabel(new ImageIcon("ly.png"));
                        element.setPreferredSize(new Dimension(37, 110));
                        element.setBounds(nodes[upOne].x - 19, nodes[upOne].y, 37, 110);
                        break;

                    case 'C':
                        element = new JLabel(new ImageIcon("cy.png"));
                        element.setPreferredSize(new Dimension(52, 110));
                        element.setBounds(nodes[upOne].x - 26, nodes[upOne].y, 52, 110);
                        break;

                    case 'V':
                        if (negativeNode == 0) {
                            switch (AcDc) {
                                case 'D':
                                    element = new JLabel(new ImageIcon("vdcU.png"));
                                    element.setPreferredSize(new Dimension(72, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 72, 110);
                                    break;

                                case 'A':
                                    element = new JLabel(new ImageIcon("acy.png"));
                                    element.setPreferredSize(new Dimension(71, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                    break;
                            }
                        } else if (positiveNode == 0) {
                            switch (AcDc) {
                                case 'D':
                                    element = new JLabel(new ImageIcon("vdcD.png"));
                                    element.setPreferredSize(new Dimension(72, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 72, 110);
                                    break;

                                case 'A':
                                    element = new JLabel(new ImageIcon("acy.png"));
                                    element.setPreferredSize(new Dimension(71, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                    break;
                            }
                        }
                        break;

                    case 'I':
                        if (negativeNode == 0) {
                            switch (AcDc) {
                                case 'D':
                                    element = new JLabel(new ImageIcon("IdcU.png"));
                                    element.setPreferredSize(new Dimension(71, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                    break;

                                case 'A':
                                    element = new JLabel(new ImageIcon("acy.png"));
                                    element.setPreferredSize(new Dimension(71, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                    break;
                            }
                        } else if (positiveNode == 0) {
                            switch (AcDc) {
                                case 'D':
                                    element = new JLabel(new ImageIcon("IdcD.png"));
                                    element.setPreferredSize(new Dimension(71, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                    break;

                                case 'A':
                                    element = new JLabel(new ImageIcon("acy.png"));
                                    element.setPreferredSize(new Dimension(71, 110));
                                    element.setBounds(nodes[upOne].x - 36, nodes[upOne].y, 71, 110);
                                    break;
                            }
                        }
                        break;

                    case 'H':
                    case 'E':
                        if (positiveNode - negativeNode > 0) {
                            element = new JLabel(new ImageIcon("DvU.png"));
                        } else if (positiveNode - negativeNode < 0) {
                            element = new JLabel(new ImageIcon("DvD.png"));
                        }
                        element.setPreferredSize(new Dimension(80, 110));
                        element.setBounds(nodes[upOne].x - 40, nodes[upOne].y, 80, 110);
                        break;

                    case 'F':
                    case 'G':
                        if (positiveNode - negativeNode > 0) {
                            element = new JLabel(new ImageIcon("DiU.png"));
                        } else if (positiveNode - negativeNode < 0) {
                            element = new JLabel(new ImageIcon("DiD.png"));
                        }
                        element.setPreferredSize(new Dimension(79, 110));
                        element.setBounds(nodes[upOne].x - 40, nodes[upOne].y, 79, 110);
                        break;

                    default:
                        //
                        break;

                }
                background.add(element);

            }


            add(background);
        }

    }

        public void drawGround(){

            for (int i = 0; i < 6; i++){
                if (groundNodes[i].used){
                    JLabel gnd = new JLabel(new ImageIcon("gnd.png"));
                    gnd.setPreferredSize(new Dimension(50, 20));
                    gnd.setBounds(groundNodes[i].x - 27, groundNodes[i].y - 2, 50, 20);
                    background.add(gnd);
                }
            }

        }

        public void drawParallel(int positiveNode, int negativeNode, char type, char AcDc, boolean direction) {
            JLabel element = new JLabel("");
            
                /////////////////////////////Horizontal parallel branch
                if (Math.abs(positiveNode - negativeNode) == 1 && positiveNode != 0 && negativeNode != 0) {

                    int leftOne = Math.min(positiveNode, negativeNode);

                    if (direction == false) {//means that up branch has not used yet
                        switch (type) {

                            case 'R':
                                element = new JLabel(new ImageIcon("rxU.png"));
                                element.setPreferredSize(new Dimension(110, 38));
                                element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 38, 110, 38);
                                break;

                            case 'C':
                                element = new JLabel(new ImageIcon("cxU.png"));
                                element.setPreferredSize(new Dimension(110, 41));
                                element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 41, 110, 41);
                                break;

                            case 'L':
                                element = new JLabel(new ImageIcon("lxU.png"));
                                element.setPreferredSize(new Dimension(110, 32));
                                element.setBounds(nodes[leftOne].x, nodes[leftOne].y - 32, 110, 32);
                                break;
                        }

                    } else {//means that Up branch has used
                        switch (type) {

                            case 'R':
                                element = new JLabel(new ImageIcon("rxD.png"));
                                element.setPreferredSize(new Dimension(110, 38));
                                element.setBounds(nodes[leftOne].x, nodes[leftOne].y, 110, 38);
                                break;

                            case 'C':
                                element = new JLabel(new ImageIcon("cxD.png"));
                                element.setPreferredSize(new Dimension(110, 41));
                                element.setBounds(nodes[leftOne].x, nodes[leftOne].y, 110, 41);
                                break;

                            case 'L':
                                element = new JLabel(new ImageIcon("lxD.png"));
                                element.setPreferredSize(new Dimension(110, 32));
                                element.setBounds(nodes[leftOne].x, nodes[leftOne].y, 110, 32);
                                break;
                        }
                    }

                    background.add(element);
                }

                //////////////////////////////////////Vertical mode
                else if (Math.abs(positiveNode - negativeNode) == 6 || Math.min(positiveNode, negativeNode) == 0) {
                    int upOne = Math.max(positiveNode, negativeNode);
                    
                    if (direction == false) {//means that left branch has not used yet
                        switch (type) {

                            case 'R':
                                element = new JLabel(new ImageIcon("ryL.png"));
                                element.setPreferredSize(new Dimension(38, 110));
                                element.setBounds(nodes[upOne].x - 38, nodes[upOne].y, 38, 110);
                                break;

                            case 'C':
                                element = new JLabel(new ImageIcon("cyL.png"));
                                element.setPreferredSize(new Dimension(41, 110));
                                element.setBounds(nodes[upOne].x - 41, nodes[upOne].y, 41, 110);
                                break;

                            case 'L':
                                element = new JLabel(new ImageIcon("lyL.png"));
                                element.setPreferredSize(new Dimension(32, 110));
                                element.setBounds(nodes[upOne].x - 32, nodes[upOne].y, 32, 110);
                                break;

                            case 'V':

                                switch (AcDc) {

                                    case 'D':
                                        if (upOne == positiveNode) {
                                            element = new JLabel(new ImageIcon("vyLU.png"));
                                        } else {
                                            element = new JLabel(new ImageIcon("vyLD.png"));
                                        }
                                        element.setPreferredSize(new Dimension(50, 110));
                                        element.setBounds(nodes[upOne].x - 50, nodes[upOne].y, 50, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acyL.png"));
                                        element.setPreferredSize(new Dimension(40, 110));
                                        element.setBounds(nodes[upOne].x - 40, nodes[upOne].y, 40, 110);
                                        break;
                                }
                                break;

                            case 'I':
                                switch (AcDc) {

                                    case 'D':
                                        if (upOne == positiveNode) {
                                            element = new JLabel(new ImageIcon("iyLU.png"));
                                        } else {
                                            element = new JLabel(new ImageIcon("iyLD.png"));
                                        }
                                        element.setPreferredSize(new Dimension(50, 110));
                                        element.setBounds(nodes[upOne].x - 50, nodes[upOne].y, 50, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acyL.png"));
                                        element.setPreferredSize(new Dimension(40, 110));
                                        element.setBounds(nodes[upOne].x - 40, nodes[upOne].y, 40, 110);
                                        break;
                                }
                                break;

                            case 'E':
                            case 'H':
                                if (upOne == positiveNode) {
                                    element = new JLabel(new ImageIcon("DvUL.png"));
                                } else {
                                    element = new JLabel(new ImageIcon("DvDL.png"));
                                }
                                element.setPreferredSize(new Dimension(45, 110));
                                element.setBounds(nodes[upOne].x - 45, nodes[upOne].y, 45, 110);
                                break;

                            case 'F':
                            case 'G':
                                if (upOne == positiveNode) {
                                    element = new JLabel(new ImageIcon("DiUL.png"));
                                } else {
                                    element = new JLabel(new ImageIcon("DiDL.png"));
                                }
                                element.setPreferredSize(new Dimension(50, 110));
                                element.setBounds(nodes[upOne].x - 50, nodes[upOne].y, 50, 110);
                                break;

                        }
                    }

                    else {//means that left branch has used
                        switch (type) {

                            case 'R':
                                element = new JLabel(new ImageIcon("ryR.png"));
                                element.setPreferredSize(new Dimension(38, 110));
                                element.setBounds(nodes[upOne].x, nodes[upOne].y, 38, 110);
                                break;

                            case 'C':
                                element = new JLabel(new ImageIcon("cxR.png"));
                                element.setPreferredSize(new Dimension(41, 110));
                                element.setBounds(nodes[upOne].x, nodes[upOne].y, 41, 110);
                                break;

                            case 'L':
                                element = new JLabel(new ImageIcon("lxR.png"));
                                element.setPreferredSize(new Dimension(32, 110));
                                element.setBounds(nodes[upOne].x, nodes[upOne].y, 32, 110);
                                break;
                                
                            case 'V':

                                switch (AcDc){

                                    case 'D':
                                        if (upOne == positiveNode){
                                            element = new JLabel(new ImageIcon("vyRU.png"));
                                        }
                                        else {
                                            element = new JLabel(new ImageIcon("vyRD.png"));
                                        }
                                        element.setPreferredSize(new Dimension(50, 110));
                                        element.setBounds(nodes[upOne].x, nodes[upOne].y, 50, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acyR.png"));
                                        element.setPreferredSize(new Dimension(40, 110));
                                        element.setBounds(nodes[upOne].x, nodes[upOne].y, 40, 110);
                                        break;
                                }
                                break;

                            case 'I':
                                switch (AcDc){

                                    case 'D':
                                        if (upOne == positiveNode){
                                            element = new JLabel(new ImageIcon("iyRU.png"));
                                        }
                                        else {
                                            element = new JLabel(new ImageIcon("iyRD.png"));
                                        }
                                        element.setPreferredSize(new Dimension(50, 110));
                                        element.setBounds(nodes[upOne].x, nodes[upOne].y, 50, 110);
                                        break;

                                    case 'A':
                                        element = new JLabel(new ImageIcon("acyR.png"));
                                        element.setPreferredSize(new Dimension(40, 110));
                                        element.setBounds(nodes[upOne].x, nodes[upOne].y, 40, 110);
                                        break;
                                }
                                break;

                            case 'E': case 'H':
                                if (upOne == positiveNode) {
                                    element = new JLabel(new ImageIcon("DvUR.png"));
                                }
                                else {
                                    element = new JLabel(new ImageIcon("DvDR.png"));
                                }
                                element.setPreferredSize(new Dimension(45, 110));
                                element.setBounds(nodes[upOne].x, nodes[upOne].y, 45, 110);
                                break;

                            case 'F': case 'G':
                                if (upOne == positiveNode) {
                                    element = new JLabel(new ImageIcon("DiUR.png"));
                                }
                                else {
                                    element = new JLabel(new ImageIcon("DiDR.png"));
                                }
                                element.setPreferredSize(new Dimension(50, 110));
                                element.setBounds(nodes[upOne].x, nodes[upOne].y, 50, 110);
                                break;
                        }
                    }
                    background.add(element);
                }

            }

        }

    public static void main(String[] args){

        startPage intro = new startPage();
    }
}
