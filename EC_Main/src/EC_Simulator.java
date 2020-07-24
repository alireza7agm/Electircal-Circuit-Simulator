//hi


/////// alireza && amir presents:)))))))))))))

/////// inja har ruz begim be nazeret key tamoom mishe:))

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math; 

public class EC_Simulator {
//////////////////////////////
    public static class Node{

        double previous voltage = 0;
        double voltage = 0;
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


//checks all nodes to find the node we're looking for 
///if previous voltage == true, returns previous voltage, else returns current voltage
        double ReturnVoltage(String node_name, boolean previous_voltage)
        {
            for (node n : nodes)
            {
              if (n.name.matches(node_name))
              {
                if(previous_voltage)
                {
                  return n.previous_voltage
                }
                return n.voltage;
              }
            }
          return 0;
          
        }

        void ModifyVoltage(double dv)
        {

          ArrayList <Node> modified = new ArrayList<Node> ();
          this.nodes.get(0).voltage += dv;
          modifed.add(this.nodes.get(0));
          int counter = 0;
          while (modified.size()<this.nodes.size() || counter<this.modifed.size())
          {
            Node current_node = this.modifed.get(counter);
            //find nodes connected to current_node
            for (VoltageSource V : this.voltage_sources)
            {
              if (v.out_node_name.matches(current_node.name))
              {
                //if the connected node has not been updated
                boolean node_modified = false;
                for (Node n : modified)
                {
                  if (n.name.matches(v.in_node_name))
                  {
                    modified = true;
                  } 
                }

                if (!modified)
                {
                  //find node and modify voltage
                  for (Node n : this.nodes)
                  {
                    if (n.name.matches(v.in_node_name))
                    {
                      n.previous_voltage = n.voltage;
                      n.voltage = current_node - V.ReturnVoltage();
                      modified.add(n);
                      break;
                    }
                  }
                }

              }

              if (v.in_node_name.matches(current_node.name))
              {
                //if the connected node has not been updated
                boolean node_modified = false;
                for (Node n : modified)
                {
                  if (n.name.matches(v.out_node_name))
                  {
                    modified = true;
                  } 
                }

                if (!modified)
                {
                  //find node and modify voltage
                  for (Node n : this.nodes)
                  {
                    if (n.name.matches(v.out_node_name))
                    {
                      n.previous_voltage = n.voltage;
                      n.voltage = current_node + V.ReturnVoltage();
                      this.modifed.add(n);
                      break;
                    }
                  }
                }
              }
            }

            counter++;
          }

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
    }


    public static class Resistor extends Element{

        public Resistor(String in, String out, double value, String type) {
            super(in, out, value, type);
        }

        void update()
        {
          this.current = (in.ReturnVoltage(in_node_name,false) - out.ReturnVoltage(out_node_name,false)/this.value;
        }

        double return_current()
        {
          return this.current = (in.ReturnVoltage(in_node_name,false) - out.ReturnVoltage(out_node_name,false)/this.value;
        }

    }


    public static class Capacitor extends Element{

        public Capacitor(String in, String out, double value, String type) {
            super(in, out, value, type);

        }

        void update()
        {
          this.current = this.value*((in.ReturnVoltage(in_node_name,false) - out.ReturnVoltage(out_node_name,false))
            -(in.ReturnVoltage(in_node_name,true) - out.ReturnVoltage(out_node_name, true)))/Circuit.dt;
        }

        double return_current() 
        {
          return this.value*((in.ReturnVoltage(in_node_name,false) - out.ReturnVoltage(out_node_name,false))
            -(in.ReturnVoltage(in_node_name,true) - out.ReturnVoltage(out_node_name, true)))/Circuit.dt;
        }

    }


    public static class Inductor extends Element{


        double initial_current;
        public Inductor(String in, String out, double value, double initial_current, String type) {
            super(in, out, value, type);
            this.initial_current = initial_current;
            this.current = initial_current;
        }

        void update()
        {
          this.current += (in.ReturnVoltage(in_node_name) - out.ReturnVoltage(out_node_name))*Circuit.dt/this.value;
        }

        double return_current()
        {
          return (this.current + (in.ReturnVoltage(in_node_name) - out.ReturnVoltage(out_node_name))*Circuit.dt/this.value );
        }



    }


    public static class VoltageSource extends Element{

        double amplitude = 0;
        double frequency = 0;
        double phase = 0;
        public VoltageSource(String in, String out, double value, String type) {
            super(in, out, value, type);
        }
         public VoltageSource(String in, String out, double value, double amplitude, double frequency, double phase, String type) {
            super(in, out, value, type);
            this.frequency = frequency; 
            this.phase = phase;
            this.amplitude = amplitude;
        }
///////////returns voltage at time t
        update()
        {
          this.voltage = voltage;;
        }

        double ReturnVoltage()
        {
          return this.value + amplitude*Math.sine(Circuit.time*2*Math.PI*frequency + phase); 
        }

    }


    public static class CurrentSource extends Element{

        public CurrentSource(String in, String out, double value, String type) {
            super(in, out, value, type);
            this.current = value;
        }

        update()
        {
          this.current = current;
        }
        
        double return_current()
        {
          return this.value + amplitude*Math.sine(Circuit.time*2*Math.PI*frequency + phase); 
        }

    }

    public static class Circuit{

        ArrayList <Node> nodes = new ArrayList<Node>();
        ArrayList <SuperNode> super_nodes = new ArrayList<SuperNode>();
        ArrayList <Element> elements = new ArrayList<Element>();
        ArrayList <ArrayList<String>> super_node_lists = new ArrayList<ArrayList<String>>();
        ArrayList<Node> added_nodes = new ArrayList<Node>();

        double static time;
        double dv;
        double static dt;
        double di;
        double sumOfSquares = 0;

        public void circuit_initialize(double dv, double dt, double di)
        {
            time = 0;
            this.dv = dv;
            this.dt = dt;
            this.di = di;
        }

        void add_neighbor_nodes(int current_node_address)
        {
          for (Element e: this.elements)
          {
            if (e.in_node_name.matches(added_nodes.get(current_node_address).name))
            {
              //find the node connected to current node and add it to added_nodes list
              for (Node n : this.nodes)
              {
                if (n.name.matches(e.out_node_name))
                {
                  if (!n.added)
                  {
                    if (e.type.matches("V"))
                    {
                      n.union = added_nodes.get(current_node_address).union;
                    }
                    n.added = true;
                    added_nodes.add(n);
                  }
                }
               
              }

            }

            if (e.out_node_name.matches(added_nodes.get(current_node_address).name))
            {
              //find the node connected to current node and add it to added_nodes list
              for (Node n : this.nodes)
              {
                if (n.name.matches(e.in_node_name))
                {
                  if (!n.added)
                  {
                    if (e.type.matches("V"))
                    {
                      n.union = added_nodes.get(current_node_address).union;
                    }
                    added = true;
                    added_nodes.add(n);
                  }
                  
                }
               
              }
            }
          }


        }
        public void Init_Circuit()
        {
          
          int ground_index = 0;
          //find ground
          
          //////////
          for (int i =0; i<this.nodes.size(); i++)
          {
            if (this.nodes.get(i).name.matches("GND"))
            {
              ground_index = i;
            }
          }

          this.added_nodes.add(this.nodes.get(i));
          
          int counter = 0;
          while (added_nodes.size()<this.nodes.size() || counter<added_nodes.size())
          {
            add_neighbor_nodes(counter);
            counter++;
          }

//create super nodes
          String current_super_node = "";
          for (Node n : this.added_nodes)
          {
            if (!n.union.matches(current_super_node))
            {
              SuperNode sn = new SuperNode(n.name);
              this.super_nodes.add(sn);
            }

            else
            {
              this.super_nodes.get(super_nodes.size()-1).nodes.add(n);
            }
          }

          for (sn SuperNode : this.super_nodes)
          {
            //update voltages of nodes inside the supernodes just created
            sn.ModifyVoltage(0);
          }

        }

        public void Add_Node(String name)
        {
            ////////////change of arguments
            boolean alreadyCreatedNode = false;
            for (Node n : this.nodes){
                if (n.name.equals(name))
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

        public void Add_Element(String in, String out, double value, double initialCondition, String type)
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
            for (SuperNode sn : this.supe_nodes)
            {
                sum_of_squares += sn.current * sn.current;
            }
            return sum_of_squares;
        }

        void Update_Nodes()
        {
          //current_1 is the current of the supernode without dv voltage increase
          //after finding the current of the supernodes, if current 1 is less than current 2
          //increase voltage by dv (after checking stuff)
            int current_1 = 0;
            int current_2 = 0;
            for (SuperNode sn : this.super_nodes)
            {
              //ground voltage is constant
                if (sn.name.matches("gnd"))
                {
                    continue;
                }
                current_1 = 0;
                current_2 = 0;
                ArrayList<String> element_names = new ArrayList<String>();
                for (node n : sn.nodes)
                {
                  for (Element e: this.elements)
                  {
                    // if element is a voltage source
                    if (e.type.matches('V'))
                    {
                      continue;
                    }
                    
                    if (e.in.matches(n.name))
                    {
                      //save the names of the elements connected to the supernode we're updating. 
                      //the connected elements will be updated after supernode is updated
                      element_names.add(n.name);
                      current_1 -= e.return_current();
                      sn.ModifyVoltage(dv);
                      current_1 -= e.return_current();
                      //undo voltage changes
                      sn.ModifyVoltage(dv*-1);
                    } 

                    if (e.out.matches(n.name))
                    { 
                      element_names.add(n.name);
                      current_1 += e.return_current();
                      sn.ModifyVoltage(dv);
                      current_1 += e.return_current();
                      //undo voltage changes
                      sn.ModifyVoltage(dv*-1);
                    }

                  }
                }

                sn.current = current_1
                double sum_of_squares_1 = Calculate_Sum_of_Squares();

                //if error is small, don't do anything. error is checked here and not in the beginning because we want to find supernodes currents. this is important especially in the beginning of analysis
                if (sum_of_squares_1 < 0.1)
                {
                    continue;
                }
                
                sn.current = current_2;
                double sum_of_squares_2 = Calculate_Sum_of_Squares();
          
                if (sum_of_squares_1 > sum_of_squares_2)
                {
                    sn.ModifyVoltage(dv*sum_of_squares_1);
                }

                else if (sum_of_squares_1 < sum_of_squares_2)
                {
                    if (sum_of_squares_2 < 0.1)
                    {
                        continue;
                    }

                    else
                    {
                        sn.ModifyVoltage(-1*dv*sum_of_squares_2);
                    }

                }

                //update the elements using the updated Voltages 
                for (element e: this.elements)
                {
                  if (element_names.contains(e.name))
                  {
                    e.update_element();
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
