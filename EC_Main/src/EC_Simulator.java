//hi


/////// alireza && amir presents:)))))))))))))

/////// inja har ruz begim be nazeret key tamoom mishe:))

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class EC_Simulator {
//////////////////////////////
    public static class Node{

        double voltage = 0;
        double voltage_1 = 0;
        double voltage_2 = 0;
        double next_voltage_1 = 0;
        double next_voltage_2 = 0;
        double current = 0;
        double previous_current = 0;
        String name;
        String union;
        ArrayList <String> connected_nodes_list;


        Node(String name){
            this.connected_nodes_list = new ArrayList<String>();
            this.name = name;
            this.union = name;

        }

        /*Node (double voltage_1, double voltage_2,
                   double next_voltage_1, double next_voltage_2,
                   double current, double previous_current, String name){

            this.voltage_1 = voltage_1;
            this.voltage_2 = voltage_2;
            this.next_voltage_1 = next_voltage_1;
            this.next_voltage_2 = next_voltage_2;
            this.current = current;
            this.previous_current = previous_current;
            this.name = name;

        }*/
        public void ModifyVoltage(double dv)
        {
            voltage_1 += dv;
            voltage_2 += dv;
        }

    }


    public static class SuperNode
    {
        ArrayList<Node> nodes;
        ArrayList<Element> voltage_sources;
        double base_voltage;
        String name;
        double current = 0;
        double previous_current = 0;

        SuperNode(Node base_node)
        {
          nodes = new ArrayList<Node>();
          voltage_sources = new ArrayList<Element>();
          base_voltage = base_node.voltage;
          
        }


///chekcs the first and the last node of the supernode to see which one is connected to the element
//for added protection, checks all nodes next 
        double ReturnVoltage(String node_name)
        {
          if (this.nodes.get(0).name.matches(node_name))
          {
            return this.nodes.get(0).voltage;
          }
          else if (this.nodes.get(this.nodes.size()-1).name.matches(node_name))
          {
            return this.nodes.get(this.nodes.size()-1).voltage;
          }

          else
          {
            for (node n : nodes)
            {
              if (n.name.matches(node_name))
              {
                return n.voltage;
              }
            }
          }

          return 0;
          
        }
    }


    public static class Element{
        double voltage;
        double previous_current;
        double current;
        double value;
        String in_node_name;
        String out_node_name;
        SuperNode in;
        SuperNode out;
        String type;
        /////////////////other variables

        //constructor :
        public Element(String in_node_name, String out_node_name, double value, String type) {
            this.in_node_name = in_node_name;
            this.out_node_name = out_node_name;
            this.voltage = 0;
            this.previous_current = 0;
            this.current = 0;
            this.value = value;
            this.type = type;
        }


        //getters and setters :
        public double getVoltage() {
            this.voltage = this.value;
            return voltage;
        }

        public void setVoltage(double voltage) {
            this.voltage = voltage;
        }

        public double getCurrent() {
            return current;
        }

        public void setCurrent(double current) {
            this.current = current;
        }
    }


    public static class Resistor extends Element{

        public Resistor(Node in, Node out, double value, String type) {
            super(in, out, value, type);
        }

        void update_resistor(double dv)
        {
            this.current = (in.ReturnVoltage(in_node_name) - out.ReturnVoltage(out_node_name)/this.value;
        }

    }


    public static class Capacitor extends Element{

        public Capacitor(Node in, Node out, double value, String type) {
            super(in, out, value, type);

        }

        void update_capacitor(double dv, double dt)
        {
            this.current = this.value*((in.ReturnVoltage(in_node_name) - out.ReturnVoltage(out_node_name))
            -(in.voltage_2 - out.voltage_1))/dt;
        }

    }


    public static class Inductor extends Element{


        double initial_current;
        public Inductor(Node in, Node out, double value, double initial_current, String type) {
            super(in, out, value, type);
            this.initial_current = initial_current;
            this.current += initial_current;
            this.previous_current += initial_current;
        }

        void update_inductor(double dv, double dt)
        {
            this.previous_current += (in.voltage_2 - out.voltage_1)*dt/this.value;
            this.current += (in.next_voltage_2+dv - out.voltage_1)*dt/this.value;
        }

    }


    public static class VoltageSource extends Element{

        public VoltageSource(Node in, Node out, double value, String type) {
            super(in, out, value, type);
        }

    }


    public static class CurrentSource extends Element{

        public CurrentSource(Node in, Node out, double value, String type) {
            super(in, out, value, type);
        }

    }

    public static class Circuit{

        ArrayList <Node> nodes = new ArrayList<Node>();
        ArrayList <Element> elements = new ArrayList<Element>();
        ArrayList <ArrayList<String>> super_node_lists = new ArrayList<ArrayList<String>>();

        double dv;
        double dt;
        double di;
        double sumOfSquares = 0;

        public void circuit_initialize(double dv, double dt, double di)
        {
            this.dv = dv;
            this.dt = dt;
            this.di = di;
        }

        public void Init_Circuit()
        {

            
        }

        public void Add_Node(String name)
        {
            ////////////change of arguments
            boolean alreadyCreatedNode = false;
            for (Node sn : this.nodes){
                if (sn.name.equals(name))
                {
                    alreadyCreatedNode = true;
                    break;
                }
            }

            if (!alreadyCreatedNode)
            {
                Node n = new Node(name);
                nodes.add(n);
            }
            return;
        }

        public void Add_Element(Node in, Node out, double value, double initialCondition, String type)
        {
            switch (type){
                case "R":
                    Resistor R = new Resistor(in, out, value, type);
                    elements.add(R);
                    break;
                case "C":
                    Capacitor C = new Capacitor(in, out, value, type);
                    elements.add(C);
                    break;
                case "L":
                    Inductor L = new Inductor(in, out, value, initialCondition, type);
                    elements.add(L);
                    break;
                case "V":
                    VoltageSource V = new VoltageSource(in, out, value, type);
                    elements.add(V);
                    break;
                case "I":
                    CurrentSource I = new CurrentSource(in, out, value, type);
                    elements.add(I);
                    break;
            }
            return;
        }

        /*
        public void Add_Element(Element e, Node in, Node out, double value, String type)
        {
            e = new Element(in, out, value, type);
            elements.add(e);
            return;
        }*/

        double Calculate_Sum_of_Squares()
        {
            double sum_of_squares = 0;
            for (Node n : this.nodes)
            {
                sum_of_squares += n.previous_current * n.previous_current;
            }
            return sum_of_squares;
        }

        void Update_Nodes()
        {
            for (Node n : this.nodes)
            {
                if (n.name.matches("gnd") && !n.union.matches(n.name))
                {
                    continue;
                }
                n.current = 0;
                n.previous_current = 0;
                for (Element e : this.elements)
                {
                    if (e.in.name.matches(n.name))
                    {
                        n.current -= e.current;
                        n.previous_current -= e.previous_current;
                    }
                    else if (e.out.name.matches(n.name)){
                        n.current += e.current;
                        n.previous_current += e.previous_current;
                    }
                }

                double sum_of_squares_1 = Calculate_Sum_of_Squares();

                if (sum_of_squares_1 < 0.1)
                {
                    continue;
                }
                double previous_current_of_node = n.previous_current;
                n.previous_current = n.current;
                double sum_of_squares_2 = Calculate_Sum_of_Squares();
                n.previous_current = previous_current_of_node;
                if (sum_of_squares_1 > sum_of_squares_2)
                {
                    n.ModifyVoltage(dv*sum_of_squares_1);
                }

                else if (sum_of_squares_2 < sum_of_squares_1)
                {
                    if (sum_of_squares_2 < 0.1)
                    {
                        continue;
                    }

                    else
                    {
                        n.ModifyVoltage(-1*dv*sum_of_squares_2);
                    }

                }

            }

        }





    }




    public static void main(String[] args){

        File inputFile = new File("input.txt");

        try {

            Circuit eC = new Circuit();

            Scanner sc = new Scanner(inputFile);
            ArrayList<String> lines = new ArrayList<>();
            int cnt = 0, checkEnoughVariables = 0;
            double dt = 0, dv = 0, di = 0;

            while (sc.hasNextLine())
            {
                lines.add(sc.nextLine().trim());
                String info[] = lines.get(cnt).split("\\s");

                if (!lines.get(cnt).substring(0, 5).equals(".tran")) {

                    Node in = new Node(info[1]);
                    eC.Add_Node(info[1]);
                    Node out = new Node(info[2]);
                    eC.Add_Node(info[2]);

                    //error invalid value


                    //error invalid syntax


                    //pico micro ...


                    switch (lines.get(cnt).charAt(0)) {

                        case 'R':
                            eC.Add_Element(out, in, Double.parseDouble(info[3]), 0, "R");
                            break;
                        case 'C':
                            eC.Add_Element(in, out, Double.parseDouble(info[3]), 0, "C");
                            break;
                        case 'L':
                            eC.Add_Element(in, out, Double.parseDouble(info[3]), 0, "L");
                            break;
                        case 'V':
                            eC.Add_Element(out, in, Double.parseDouble(info[3]), 0, "V");
                            break;
                        case 'I':
                            eC.Add_Element(out, in, Double.parseDouble(info[3]), 0, "I");
                            break;
                        case 'd':
                            switch (lines.get(cnt).charAt(1)){
                                case 't':
                                    dt = Double.parseDouble(info[2]);
                                    checkEnoughVariables++;
                                    break;
                                case 'v':
                                    dv = Double.parseDouble(info[2]);
                                    checkEnoughVariables++;
                                    break;
                                case 'i':
                                    di = Double.parseDouble(info[2]);
                                    checkEnoughVariables++;
                                    break;

                                default:
                                    System.out.printf("Invalid Syntax : line %d ", cnt);
                                    break;
                            }

                        case '*':
                            //comment
                            break;

                        default:
                            System.out.printf("Invalid Syntax : line %d ", cnt);
                            break;

                    }

                }

                else
                    {
                        if (checkEnoughVariables == 3){
                            eC.circuit_initialize(dv, dt, di);
                        }
                        else {
                            //error for not initializing dv,dt,di
                            System.out.printf("Not Enough Information!")
                        }
                    //init the circuit
                    //loops and updates
                    //other stuff
                }

                cnt++;
            }
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
