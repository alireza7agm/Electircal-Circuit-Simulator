import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.nio.file.Files;
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
    static Font f2 = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 20);


    public static class startPage extends JFrame implements ActionListener {

        JButton openFile, instructionsButton, aboutButton;

        startPage(){

            setSize(1422, 800);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            //background
            JLabel background = new JLabel(new ImageIcon("C:\\Users\\alire\\Desktop\\1.JPG"));
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
            JLabel background = new JLabel(new ImageIcon("C:\\Users\\alire\\Desktop\\main6.JPG"));
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

                //////////////////
                this.dispose();


            }

            else if (e.getSource() == load){

                String newInput = input.getText();

                try {

                    PrintWriter pw = new PrintWriter(f);
                    pw.write(newInput);
                    pw.close();

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

    public class Point {

        int x;
        int y;
        boolean alreadyConnected = false;

        Point(int x, int y){

            this.x = x;
            this.y = y;

        }

        public void setAlreadyConnected(){

            this.alreadyConnected = true;
        }

    }

    public class Ground extends Point{
        boolean used = false;

        Ground(int x, int y) {
            super(x, y);
        }

        public void setUsed(){
            this.used = true;
        }

    }

    public class DrawCircuit extends JFrame {

        Point nodes[] = new Point[31];
        Ground groundNodes[] = new Ground[6];
        Graphics g;

        //method to set all the coordinates
        public void nodeSet(){

            nodes[0] = new Point(380, 730);

            for (int i = 0; i < 6; i++){
                groundNodes[i] = new Ground(380 + 110 * i, 730);
            }

            for (int i = 0; i < 5; i++){
                for (int j = i + 1; j < i + 7; j++){
                    /*
                    nodes[5 * i + j].x = 380 + (j - i - 1) * 110;
                    nodes[5 * i + j].y = 70 + (5 - i) * 110;
                     */
                    nodes[5 * i + j] = new Point(380 + (j - i - 1) * 110, 70 + (5 - i) * 110);
                    g.drawOval(nodes[5 * i + j].x, nodes[5 * i + j].y, 5, 5);

                }
            }
        }


        public void drawBranch(int positiveNode, int negativeNode, char type){

            setSize(1422, 800);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JLabel background = new JLabel();
            background.setBounds(361, 160, 700, 600);
            background.setBorder(new LineBorder(Color.BLACK, 5));
            add(background);

            JLabel element = new JLabel("");

            nodes[positiveNode].setAlreadyConnected();
            nodes[negativeNode].setAlreadyConnected();

            if (positiveNode != 0 && negativeNode != 0) {
                //Horizontal Mode
                if (Math.abs(positiveNode - negativeNode) == 1) {

                    //part1 wire
                    g.drawLine(nodes[negativeNode].x, nodes[negativeNode].y, nodes[negativeNode].x + 15, nodes[negativeNode].y);


                    //element
                    switch (type) {

                        case 'R':
                            element = new JLabel(new ImageIcon("rx.png"));
                            element.setPreferredSize(new Dimension(80, 30));
                            break;

                        case 'L':
                            element = new JLabel(new ImageIcon("lx.png"));
                            element.setPreferredSize(new Dimension(80, 27));
                            break;

                        case 'C':
                            element = new JLabel(new ImageIcon("cx.png"));
                            element.setPreferredSize(new Dimension(80, 38));
                            break;

                        case 'V':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("vdcxR.png"));
                                element.setPreferredSize(new Dimension(80, 45));
                            } else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("vdcxL.png"));
                                element.setPreferredSize(new Dimension(80, 45));
                            }
                            break;

                        case 'I':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("IdcxR.png"));
                                element.setPreferredSize(new Dimension(80, 51));
                            } else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("IdcxL.png"));
                                element.setPreferredSize(new Dimension(80, 51));
                            }
                            break;

                        default:
                            //
                            break;

                    }
                    background.add(element);


                    //part2 wire
                    g.drawLine(nodes[positiveNode].x - 15, nodes[positiveNode].y, nodes[positiveNode].x, nodes[positiveNode].y);

                }

                //Vertical Mode
                else if (Math.abs(positiveNode - negativeNode) == 6) {

                    //part1 wire
                    g.drawLine(nodes[negativeNode].x, nodes[negativeNode].y, nodes[negativeNode].x, nodes[negativeNode].y + 15);


                    //element
                    switch (type) {

                        case 'R':
                            element = new JLabel(new ImageIcon("ry.png"));
                            element.setPreferredSize(new Dimension(30, 80));
                            break;

                        case 'L':
                            element = new JLabel(new ImageIcon("ly.png"));
                            element.setPreferredSize(new Dimension(27, 80));
                            break;

                        case 'C':
                            element = new JLabel(new ImageIcon("cy.png"));
                            element.setPreferredSize(new Dimension(38, 80));
                            break;

                        case 'V':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("vdcyU.png"));
                                element.setPreferredSize(new Dimension(45, 80));
                            }
                            else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("vdcyD.png"));
                                element.setPreferredSize(new Dimension(45, 80));
                            }
                            break;

                        case 'I':
                            if (positiveNode - negativeNode > 0) {
                                element = new JLabel(new ImageIcon("IdcyU.png"));
                                element.setPreferredSize(new Dimension(51, 80));
                            }
                            else if (positiveNode - negativeNode < 0) {
                                element = new JLabel(new ImageIcon("IdcyD.png"));
                                element.setPreferredSize(new Dimension(51, 80));
                            }
                            break;

                        default:
                            //
                            break;

                    }
                    background.add(element);


                    //part2 wire
                    g.drawLine(nodes[positiveNode].x, nodes[positiveNode].y - 15, nodes[positiveNode].x, nodes[positiveNode].y);

                }
            }

            else if (positiveNode == 0 || negativeNode == 0){
                int ground = 0;
                int nonGround = 0;

                if (positiveNode == 0) {

                    ground = positiveNode;
                    nonGround = negativeNode;

                }
                else if (negativeNode == 0){

                    ground = negativeNode;
                    nonGround = positiveNode;

                }
                groundNodes[nonGround - 1].setUsed();

                //part1 wire
                g.drawLine(groundNodes[nonGround - 1].x, groundNodes[nonGround - 1].y,
                        groundNodes[nonGround - 1].x, groundNodes[nonGround - 1].y - 15);


                //element
                switch (type) {

                    case 'R':
                        element = new JLabel(new ImageIcon("ry.png"));
                        element.setPreferredSize(new Dimension(30, 80));
                        break;

                    case 'L':
                        element = new JLabel(new ImageIcon("ly.png"));
                        element.setPreferredSize(new Dimension(27, 80));
                        break;

                    case 'C':
                        element = new JLabel(new ImageIcon("cy.png"));
                        element.setPreferredSize(new Dimension(38, 80));
                        break;

                    case 'V':
                        if (negativeNode == 0) {
                            element = new JLabel(new ImageIcon("vdcyU.png"));
                            element.setPreferredSize(new Dimension(45, 80));
                        }
                        else if (positiveNode == 0) {
                            element = new JLabel(new ImageIcon("vdcyD.png"));
                            element.setPreferredSize(new Dimension(45, 80));
                        }
                        break;

                    case 'I':
                        if (negativeNode == 0) {
                            element = new JLabel(new ImageIcon("IdcyU.png"));
                            element.setPreferredSize(new Dimension(51, 80));
                        }
                        else if (positiveNode == 0) {
                            element = new JLabel(new ImageIcon("IdcyD.png"));
                            element.setPreferredSize(new Dimension(51, 80));
                        }
                        break;

                    default:
                        //
                        break;

                }
                background.add(element);


                //part2 wire
                g.drawLine(nodes[nonGround].x, nodes[nonGround].y + 15 , nodes[nonGround].x, nodes[nonGround].y);


            }


            add(background);
            setVisible(true);

        }

    }


    public static void main(String[] args){

        new startPage();

    }
}
