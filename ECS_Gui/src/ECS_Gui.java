import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ECS_Gui {

    /////////////////////////////////
    //ALL THE DIRECTORIES MUST CHANGE
    /////////////////////////////////
    //START PAGE MAY CHANGE
    /////////////////////////////////
    //INSTRUCTION & ABOUT US PAGES NEED TO BE WRITTEN
    /////////////////////////////////

    public static class startPage extends JFrame implements ActionListener {

        JButton openFile, instructionsButton, aboutButton;

        startPage(){

            setSize(1422, 800);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);


            JLabel background = new JLabel(new ImageIcon("C:\\Users\\alire\\Desktop\\1.JPG"));
            background.setPreferredSize(new Dimension(1422, 800));
            add(background);

            openFile = new JButton(new ImageIcon("C:\\Users\\alire\\Desktop\\openimage2.png"));
            openFile.setBounds(1140, 35, 150, 150);
            openFile.setContentAreaFilled(false);
            openFile.setOpaque(false);
            openFile.setBorderPainted(false);
            openFile.addActionListener(this);

            instructionsButton = new JButton(new ImageIcon("C:\\Users\\alire\\Desktop\\info2.png"));
            instructionsButton.setBounds(1140, 310, 150, 150);
            instructionsButton.setOpaque(false);
            instructionsButton.setContentAreaFilled(false);
            instructionsButton.setBorderPainted(false);

            aboutButton = new JButton(new ImageIcon("C:\\Users\\alire\\Desktop\\about.png"));
            aboutButton.setBounds(1140, 570, 150, 150);
            aboutButton.setOpaque(false);
            aboutButton.setContentAreaFilled(false);
            //aboutButton.setBackground(Color.WHITE);
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

                    ///////////////new page

                }

                else {
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

    public static class mainPage extends JFrame {

        JButton run, load, draw;

        mainPage(){
            



        }


    }

    public static void main(String[] args){

        new startPage();

    }
}
