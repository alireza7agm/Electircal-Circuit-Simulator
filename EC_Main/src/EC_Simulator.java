package helloworld;
//hi


import java.io.BufferedWriter;

/////// alireza && amir presents:)))))))))))))

/////// inja har ruz begim be nazeret key tamoom mishe:))

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.lang.Math; 

public class HelloWorld {
//////////////////////////////
    public static class Node{

    	boolean connected_to_ground = false;
        double previous_voltage = 0;
        double voltage = 0;
        
        double double_previous_voltage = 0;
        String name;
        String union;
        ArrayList <String> connected_nodes_list;
		public boolean added;


        Node(String name){
            this.connected_nodes_list = new ArrayList<String>();
            this.name = name;
            this.union = name;

        }

    }


    public static class SuperNode
    {
    	
        ArrayList<Node> nodes;
        ArrayList<VoltageSource> voltage_sources;
        double base_voltage;
        String name;
        double current = 0;
        double previous_current = 0;
        
        SuperNode(Node base_node)
        {
          nodes = new ArrayList<Node>();
          nodes.add(base_node);
          this.name = base_node.name;
          voltage_sources = new ArrayList<VoltageSource>();
          base_voltage = base_node.voltage;
          
        }


//checks all nodes to find the node we're looking for 
///if previous voltage == true, returns previous voltage, else returns current voltage
        double ReturnVoltage(int node_address, boolean previous_voltage)
        {
          if(previous_voltage)
          {
            return this.nodes.get(node_address).previous_voltage;
          }
          return this.nodes.get(node_address).voltage;
          
        }

        int IsNodeHere(String node_name)
        {
            for (int i =0; i<this.nodes.size(); i++)
            {
              if (this.nodes.get(i).name.matches(node_name))
              {
                return i;
              }
            }
            return -1;
        }

        void UndoLastChange()
        {
        	for (Node n : this.nodes)
        	{
        		n.voltage = n.previous_voltage;
        		n.previous_voltage = n.double_previous_voltage;
        		
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
        int super_node_1; 
        int super_node_2;
        int node_1;
        int node_2;
        //in and out supernodes address in the supernodes list of the circuit
        String type;
        String name;
        /////////////////other variables
        boolean added = false;
        
        
        //constructor :
        public Element(String name ,String in_node_name, String out_node_name, double value, String type) {
            this.name = name;
        	this.in_node_name = in_node_name;
            this.out_node_name = out_node_name;
            this.voltage = 0;
            this.previous_current = 0;
            this.current = 0;
            this.value = value;
            this.type = type;
        }

		double return_current(SuperNode superNode, SuperNode superNode2) {
			return 0;
		}

		void update(SuperNode superNode, SuperNode superNode2) {

			
		}
    }


    public static class Resistor extends Element{

        public Resistor(String name, String in, String out, double value) {
            super(name, in, out, value, "R");
        }

        void update(SuperNode in, SuperNode out)
        {
          this.current = -1*(in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))/this.value;
        }

        double return_current(SuperNode in, SuperNode out)
        {
        	
          return  -1*(in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))/this.value;
        }

    }


    public static class Capacitor extends Element{


    	double initial_condition = 0;
        public Capacitor(String name, String in, String out, double value, double initial_condition) {
            super(name, in, out, value, "C");
            this.initial_condition = initial_condition;

        }

        void update(SuperNode in, SuperNode out)
        {
        	
          this.current = -1*this.value*((in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))
            -(in.ReturnVoltage(node_1, true) - out.ReturnVoltage(node_2, true)))/Circuit.dt;
          
        }

        double return_current(SuperNode in, SuperNode out) 
        {

          return -1*this.value*((in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))
            -(in.ReturnVoltage(node_1, true) - out.ReturnVoltage(node_2, true)))/Circuit.dt;
        }

    }


    public static class Inductor extends Element{


        double initial_current;
        public Inductor(String name, String in, String out, double value, double initial_current) {
            super(name, in, out, value, "L");
            this.initial_current = initial_current;
            this.current = initial_current;
        }

        void update(SuperNode in, SuperNode out)
        {
          this.current += -1*(in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))*Circuit.dt/this.value;
        }

        double return_current(SuperNode in, SuperNode out)
        {
          return -1*(this.current + (in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))*Circuit.dt/this.value);
        }



    }


    public static class VoltageSource extends Element{

//i is independent
        char dependency = 'i';
        String dependent_node_1;
        String dependent_node_2;
        String dependent_element;
        int dependent_node_1_address;
        int dependent_node_2_address;
        int dependent_element_address;
        double amplitude = 0;
        double frequency = 0;
        double phase = 0;

        public VoltageSource (String name, String in, String out, double value, double amplitude, double frequency, double phase) {
            super(name, in, out, value, "V");
            dependency = 'i';
            this.frequency = frequency; 
            this.phase = phase;
            this.amplitude = amplitude;
        }

///voltage dependent voltage source
        public VoltageSource (String name, String in, String out, String dependent_node_1, String dependent_node_2, double amplitude)
        {
          super(name, in, out, amplitude, "V");
        		  
          dependency = 'e';
          this.dependent_node_1 = dependent_node_1;
          this.dependent_node_2 = dependent_node_2;
          this.amplitude = amplitude;
        }
//current dependent voltage source
        public VoltageSource (String name, String in, String out, String dependent_element , double amplitude)
        {
        	super(name, in, out, amplitude, "V");
          dependency = 'h';
          this.dependent_element = dependent_element;
          this.amplitude = amplitude;
        }

///////////returns voltage at time t
        void update(SuperNode in, SuperNode out)
        {
              if (dependency == 'i')
              {
                this.voltage = this.value + amplitude*Math.sin(Circuit.time*2*Math.PI*frequency + phase);
              }

         
         
        }
        
        void update()
        {

                this.voltage = this.value + amplitude*Math.sin(Circuit.time*2*Math.PI*frequency + phase);
        }
        
        void update(Element e)
        {
        	this.voltage = this.amplitude * (e.current);
        }

        double ReturnVoltage()
        {
            return this.value + amplitude*Math.sin(Circuit.time*2*Math.PI*frequency + phase);

        }

        double ReturnVoltage(Node in, Node out)
        {
        	  return this.amplitude*(in.voltage - out.voltage);
         
        }
        
        
        double ReturnVoltage(Element e)
        {
        	
        	return this.amplitude * e.current;
        }

    }


    public static class CurrentSource extends Element{

        char dependency = 'i';
        String dependent_node_1;
        String dependent_node_2;
        String dependent_element;
        int dependent_node_1_address;
        int dependent_node_2_address;
        int dependent_element_address;
        double frequency = 0;
        double phase = 0;
        double amplitude = 0;
        public CurrentSource (String name, String in, String out, double value, double amplitude, double frequency, double phase) {
            super(name, in, out, value, "I");
            dependency = 'i';
            this.frequency = frequency; 
            this.phase = phase;
            this.amplitude = amplitude;
        }

///voltage dependent voltage source
        public CurrentSource (String name, String in, String out, String dependent_node_1, String dependent_node_2, double amplitude)
        {
          super(name, in, out, amplitude, "I");
        		  
          dependency = 'g';
          this.dependent_node_1 = dependent_node_1;
          this.dependent_node_2 = dependent_node_2;
          this.amplitude = amplitude;
        }
//current dependent voltage source
        public CurrentSource (String name, String in, String out, String dependent_element , double amplitude)
        {
        	super(name, in, out, amplitude, "I");
          dependency = 'f';
          this.dependent_element = dependent_element;
          this.amplitude = amplitude;
        }


        void update(SuperNode in, SuperNode out)
        {
        	if (this.dependency == 'i')
        	{
        		this.current = this.value + amplitude*Math.sin(Circuit.time*2*Math.PI*frequency + phase); 
        	}


        }
        
        double return_current()
        {
          return this.value + amplitude*Math.sin(Circuit.time*2*Math.PI*frequency + phase); 
        }
        
        double return_current(Element e)
        {
        	return this.amplitude * e.current;
        }

        double return_current(Node dependent_node_1, Node dependent_node_2)
        {
        	return this.amplitude*(dependent_node_1.voltage - dependent_node_2.voltage);
        }
        
    }

    public static class Circuit{

		ArrayList <Node> nodes = new ArrayList<Node>();
        ArrayList <SuperNode> super_nodes = new ArrayList<SuperNode>();
        ArrayList <Element> elements = new ArrayList<Element>();
        ArrayList <ArrayList<String>> super_node_lists = new ArrayList<ArrayList<String>>();
        ArrayList<Node> added_nodes = new ArrayList<Node>();

        public static String filename;
        public static int circuit_check = 0;
        public static double time = 0;
        double dv;
        public static double dt;
        double di;
        double sumOfSquares = 0;
        double sumOfSquares_2 = 0;
        double sumOfSquares_3 = 0;
        public static boolean ac_sources = false;

        public double valueFinder(String value){
            String regex = "\\d+\\.?\\d*";

            Pattern normal = Pattern.compile(regex + ".*");
            Pattern milli = Pattern.compile(regex + "m.*");
            Pattern micro = Pattern.compile(regex + "u.*");
            Pattern nano = Pattern.compile(regex + "n.*");
            Pattern pico = Pattern.compile(regex + "p.*");
            Pattern kilo = Pattern.compile(regex + "k.*", Pattern.CASE_INSENSITIVE);
            Pattern Meg = Pattern.compile(regex + "Meg.*");
            Pattern Gig = Pattern.compile(regex + "Gig.*");

            Matcher p, n, u, m, k, Mega, Giga, none;
            double converted = 0;
            p = pico.matcher(value);
            n = nano.matcher(value);
            u = micro.matcher(value);
            m = milli.matcher(value);
            none = normal.matcher(value);
            k = kilo.matcher(value);
            Mega = Meg.matcher(value);
            Giga = Gig.matcher(value);

            String temp;

            if (p.find()){
                temp = p.group().substring(0, p.group().indexOf("m"));
                converted = Double.parseDouble(temp) * 1e-12;
            }
            else if (n.find()){
                temp = n.group().substring(0, n.group().indexOf("n"));
                converted = Double.parseDouble(temp) * 1e-9;
            }
            else if (u.find()){
                temp = u.group().substring(0, u.group().indexOf("u"));
                converted = Double.parseDouble(temp) * 1e-6;
            }
            else if (m.find()){
                temp = m.group().substring(0, m.group().indexOf("m"));
                converted = Double.parseDouble(temp) * 1e-3;
            }
            else if (none.find()){
            	if (none.group().charAt(none.group().length()-1) > 64)
            	{
                    temp = none.group().substring(0, none.group().length() - 1);
                    converted = Double.parseDouble(temp);
            	}
            	
            	else
            	{
            		converted = Double.parseDouble(none.group());
            	}
            }
            else if (k.find()){
                temp = k.group().substring(0, k.group().indexOf("k") & 32);
                converted = Double.parseDouble(temp) * 1e3;
            }
            else if (Mega.find()){
                temp = Mega.group().substring(0, Mega.group().indexOf("M"));
                converted = Double.parseDouble(temp) * 1e6;
            }
            else if (Giga.find()){
                temp = Giga.group().substring(0, Giga.group().indexOf("G"));
                converted = Double.parseDouble(temp) * 1e9;
            }
            else{
                return -85;
            }

            return converted;
        }

        
        public void circuit_initialize(double dv, double dt, double di)
        {
        	Circuit.dt = dt;
            this.dv = dv;
            this.di = di;
        }
        
        //modifies voltage of the supernode sn by dv
        void ModifyVoltage(SuperNode sn, double dv)
        {
        	
        	ArrayList <Node> modified = new ArrayList<Node> ();
            sn.nodes.get(0).double_previous_voltage = sn.nodes.get(0).previous_voltage;
            sn.nodes.get(0).previous_voltage = sn.nodes.get(0).voltage;
            sn.nodes.get(0).voltage += dv;
        	modified.add(sn.nodes.get(0));
            int counter = 0;
            while (modified.size()<sn.nodes.size() || counter<modified.size())
            {
            	
              Node current_node = modified.get(counter);
              //find nodes connected to current_node
              for (VoltageSource v : sn.voltage_sources)
              {
            	  
                if (v.out_node_name.matches(current_node.name))
                {
                  //if the connected node has not been updated
                  boolean node_modified = false;
                  for (Node n : modified)
                  {
                    if (n.name.matches(v.in_node_name))
                    {
                      node_modified = true;
                    } 
                  }

                  if (!node_modified)
                  {
                	  
                    //find node and modify voltage
                    for (Node n : sn.nodes)
                    {
                      if (n.name.matches(v.in_node_name))
                      {
                    	  
                    	n.double_previous_voltage = n.previous_voltage;
                    	n.previous_voltage = n.voltage;
                        double voltage_difference = 0;
                        if (v.dependency == 'i')
                        {
                        		voltage_difference = v.ReturnVoltage();
                        }
                        //if it's a voltage dependent source
                        else if (v.dependency == 'e')
                        {
                        	voltage_difference = v.ReturnVoltage(this.nodes.get(v.dependent_node_1_address), 
                        			this.nodes.get(v.dependent_node_2_address));
                        }
                        	
                        else if (v.dependency == 'h')
                        {
                        	
                        	voltage_difference = v.ReturnVoltage(this.elements.get(v.dependent_element_address));
                        	System.out.println(v.name+"  : " +this.elements.get(v.dependent_element_address).current+"   "+current_node.voltage);
                        	
                       	}
                        
                        
                       	n.voltage = current_node.voltage - voltage_difference;
                       	
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
                      node_modified = true;
                    } 
                  }

                  if (!node_modified)
                  {
                    //find node and modify voltage
                    for (Node n : sn.nodes)
                    {
                      if (n.name.matches(v.out_node_name))
                      {
                    	n.double_previous_voltage = n.previous_voltage;
                        n.previous_voltage = n.voltage;
                
                        	double voltage_difference = 0;
                        	if (v.dependency == 'i')
                        	{
                        		voltage_difference = v.ReturnVoltage();
                        	}
                        	else if (v.dependency == 'e')
                            {
                            	voltage_difference = v.ReturnVoltage(this.nodes.get(v.dependent_node_1_address), 
                            			this.nodes.get(v.dependent_node_2_address));
                            }
                            	
                            else if (v.dependency == 'h')
                            {
                            	
                            	voltage_difference = v.ReturnVoltage(this.elements.get(v.dependent_element_address));

                            		
                           	}
                        	n.voltage = current_node.voltage + voltage_difference;
                        
                        	modified.add(n);
                        	break;
                      }
                    }
                  }
                }
              }

              counter++;
            }

        }

        //adds the neighbor nodes of the given nodes to the added_nodes list needed for creating the supernodes. if two nodes are
        //connected by voltage sources, sets their unions as equal
        void add_neighbor_nodes(int current_node_address)
        {
          for (Element e: this.elements)
          {
        	  if (!e.added)
        	  {
                  if (e.in_node_name.matches(added_nodes.get(current_node_address).name))
                  {
                    //find the node connected to current node and add it to added_nodes list
                    for (Node n : this.nodes)
                    {
                      if (n.name.matches(e.out_node_name))
                      {
                    	  e.added = true;      
                        if (!n.added)
                        {
                          n.added = true;
                          added_nodes.add(n);
                                              
                        }
                        
                        if (e.type.matches("V"))
                        {
                          n.union = added_nodes.get(current_node_address).union;
                          add_neighbor_nodes(added_nodes.size()-1);
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
                    	  e.added = true;
                        if (!n.added)
                        {
                        	n.added = true;
                          added_nodes.add(n);
                          
                          
                         }
                        
                        if (e.type.matches("V"))
                        {
                          n.union = added_nodes.get(current_node_address).union;
                          add_neighbor_nodes(added_nodes.size()-1);
                         
                        }
                        
                      }
                     
                    }
                  }
            }

        	  
          }


        }
        
        ///creates supernoodes
        public void Init_Circuit()
        {
          
        
          int ground_index = 0;
          //find ground
          
          //////////
          for (int i =0; i<this.nodes.size(); i++)
          {
            if (this.nodes.get(i).name.matches("0"))
            {
              ground_index = i;
            }
          }
          this.added_nodes.add(this.nodes.get(ground_index));
          this.added_nodes.get(0).added = true;
          int counter = 0;
          while (added_nodes.size()<this.nodes.size())
          {
            add_neighbor_nodes(counter);
            counter++;
          }

//create super nodes
          
          ArrayList<String> current_super_nodes = new ArrayList<String>();
          for (Node n : this.added_nodes)
          {
        	  
            if (!current_super_nodes.contains((String) n.union))
            {
              SuperNode sn = new SuperNode(n);
              this.super_nodes.add(sn);
              current_super_nodes.add(n.union);
            }

            else
            {
            	for (SuperNode sn: this.super_nodes)
            	{
            		if (sn.name.matches(n.union))
            		{
            			sn.nodes.add(n);
                        boolean voltage_source_found = false;
                        for (Element e: this.elements)
                        {
                      	  if (e.type.matches("V"))
                      	  {
                      		  for (Node node : sn.nodes)
                      		  {
                      			if (e.in_node_name.matches(node.name))
                        		  {
                        			  if (e.out_node_name.matches(n.name))
                        			  {
                        				  voltage_source_found = true;
                        				  sn.voltage_sources.add((VoltageSource) e);
                        				  break;
                        			  }
                        			  
                        		  }
                      			
                      			
                      			if (e.out_node_name.matches(node.name))
	                      		  {
	                      			  if (e.in_node_name.matches(n.name))
	                      			  {
	                      				  voltage_source_found = true;
	                      				  sn.voltage_sources.add((VoltageSource) e);
	                      				  break;
	                      			  }
	                      		  }
                      		  
                      		  }
	                      		  
                      	  }
                        }
                        
                        if (!voltage_source_found)
                        {
                      	  System.out.println("Voltage Source Not Found");
                        }
                        break;
            			}
            		
            		
            		}
            		
            	}
              //add the voltage source connecting this node to the super node, to the super node!
              
          }
          
          
          int in_dependent_node = -1;
          int out_dependent_node = -1;
          int dependent_element = -1;
          int in_node_address = -1;
          int out_node_address = -1;
          //find supernodes and nodes of elements and add their addresses to the elements for easy access
          //for dependent elements find the index of the dependent nodes or elements and store them in the element object
          for (Element e : this.elements)
          {
        	  in_dependent_node = -1;
              out_dependent_node = -1;
              dependent_element = -1;
              in_node_address = -1;
              out_node_address = -1;

            for (int i = 0 ; i<this.super_nodes.size(); i++)
            {
              in_node_address = this.super_nodes.get(i).IsNodeHere(e.in_node_name);
              if (in_node_address != -1)
              {
                e.super_node_1 = i;
                e.node_1 = in_node_address;
              }
              
              
              out_node_address = this.super_nodes.get(i).IsNodeHere(e.out_node_name);
              if (out_node_address != -1)
              {
                e.super_node_2 = i;
                e.node_2 = out_node_address;

                
              }
              
              
              if (in_node_address != -1 && out_node_address != -1)
              {
                break;
              }
            }
            
            if (e.type.matches("V"))
            {
          	  VoltageSource v = (VoltageSource) e;
          	  
          	  if (v.dependency == 'e')
          	  {
          		  for (int i =0; i<this.nodes.size(); i++)
          		  {
          			  //if dependent_node 1 is not found yet
          			if (in_dependent_node == -1)
            		{
            			  if (this.nodes.get(i).name.matches(v.dependent_node_1))
            			  {
            				  in_dependent_node = i;
            				  v.dependent_node_1_address = in_dependent_node;
            			  }
            		}
            		  
            		if (out_dependent_node == -1)
            		{
            			if (this.nodes.get(i).name.matches(v.dependent_node_2))
            			{
            				out_dependent_node = i;
            				v.dependent_node_2_address = out_dependent_node;
            			}
            		}
            		
            		if (in_dependent_node != -1 && out_dependent_node != -1)
            		{
            			break;
            		}
          		  }
          		  
          		  if (in_dependent_node == -1 || out_dependent_node == -1)
          		  {
          			  System.out.println("Could not find dependent nodes of the voltage source");
          		  }
          		  
          	  }
          	  
          	  else if (v.dependency == 'h')
          	  {
          		  for (int i =0; i<this.elements.size(); i++)
          		  {
          			  if (v.dependent_element.matches(this.elements.get(i).name))
          			  {
          				
          				  dependent_element = i;
          				  v.dependent_element_address = i;
          			
        				 
          				  break;
          			  }
          		  }
          		  
          		  if (dependent_element == -1)
          		  {
          			  System.out.println("Could not find dependent element of the current source");
          		  }
          	  }
          	  
          	  //else check if there are ac sources in the circuit
          	  else
          	  {
          		  if (v.frequency!=0)
          		  {
          			  this.ac_sources = true;
          		  }
          	  }
            }
          
            if (e.type.matches("I"))
            {
          	  CurrentSource s = (CurrentSource) e;
          	  
          	  if (s.dependency == 'g')
          	  {
          		  for (int i =0; i<this.nodes.size(); i++)
          		  {
          			  //if dependent_node 1 is not found yet
          			if (in_dependent_node == -1)
            		{
            			  if (this.nodes.get(i).name.matches(s.dependent_node_1))
            			  {
            				  in_dependent_node = i;
            				  s.dependent_node_1_address = in_dependent_node;
            			  }
            		}
            		  
            		if (out_dependent_node == -1)
            		{
            			if (this.nodes.get(i).name.matches(s.dependent_node_2))
            			{
            				out_dependent_node = i;
            				s.dependent_node_2_address = out_dependent_node;
            			}
            		}
            		
            		if (in_dependent_node != -1 && out_dependent_node != -1)
            		{
            			break;
            		}
          		  }
          		  
          		  if (in_dependent_node == -1 || out_dependent_node == -1)
          		  {
          			  System.out.println("Could not find dependent nodes of the current source");
          		  }
          		  
          	  }

          	  
          	  else if (s.dependency == 'f')
          	  {
          		  for (int i =0; i<this.elements.size(); i++)
          		  {
          			  if (s.dependent_element.matches(this.elements.get(i).name))
          			  {
          				  dependent_element = i;
          				  s.dependent_element_address = i;
          				  break;
          			  }
          		  }
          		  
          		  if (dependent_element == -1)
          		  {
          			  System.out.println("Could not find dependent element of the current source");
          		  }
          	  }
          	  
          	  else
          	  {
          		  if (s.frequency != 0)
          		  {
          			  this.ac_sources = true;
          		  }
          	  }
            
            }
          
          
          }
          
          
          
          for ( SuperNode sn : this.super_nodes)
          {
        	  
            //update voltages of nodes inside the supernodes just created
            ModifyVoltage(sn, 0);

          }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
      	//update the elements using the updated Voltages 
          for (int i =0; i<this.elements.size(); i++)
          {
              this.elements.get(i).update(this.super_nodes.get(this.elements.get(i).super_node_1),
              		this.super_nodes.get(this.elements.get(i).super_node_2));
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

        public void Add_Element(Element e)
        {
        	//if element is not already added
        	boolean new_element = true;
        	for (Element element : this.elements)
        	{
        		if (element.name.matches(e.name))
        		{
        			new_element = false;
        		}
        	}
        	
        	if (new_element)
        	{
        		this.elements.add(e);
        	}
        }

        public int Check_Ground()
        {
        	//check if a ground exists
        	for ( int i=0; i<this.nodes.size(); i++)
        	{
        		Node n = this.nodes.get(i);
        		if (n.name.matches("0"))
        		{
        			return i;
        		}
        	}
        	
        	
        	return -1;
        }
        
        public ArrayList<Integer> Return_Connected_Elements_Addresses(String node_name)
        {

        	ArrayList<Integer> connected_elements = new ArrayList<Integer>();
        	for (int i=0; i<this.elements.size(); i++)
        	{
        		Element e = this.elements.get(i);
        		if (e.in_node_name.matches(node_name) || e.out_node_name.matches(node_name))
        		{
        			connected_elements.add(i);
        		}
        	}
        	return connected_elements;
        }
 
        public int Find_Connected_Elements_Number(int address)
        {
        	int connected_elements_count = 0;
        	String name = this.nodes.get(address).name;
        	for (Element e :this.elements)
        	{
        		if (e.in_node_name.matches(name))
        		{
        			connected_elements_count++;
        		}
        		
        		else if (e.out_node_name.matches(name))
        		{
        			connected_elements_count++;
        		}
        	}
        	
        	return connected_elements_count;
        }
        
        public boolean Check_KVL(ArrayList<Integer> elements_addresses, ArrayList<Integer> nodes_addresses)
        {
        	//if found a voltage source cycle
        	boolean voltage_source_cycle = true;
        	for (Integer i : elements_addresses)
        	{
        		if (!this.elements.get(i).type.matches("V"))
        		{
        			voltage_source_cycle = false;
        		}
        	}
        	
        	if (voltage_source_cycle)
        	{
        		//if there is an ac source in the cycle, return false
        		for (Integer i : elements_addresses)
        		{
        			VoltageSource v = (VoltageSource) this.elements.get(i);
        			if (v.frequency != 0) 
        			{
        				return false;
        			}
        		}
        		
        		
        		double voltage = 0;
        		for (int i =0; i<elements_addresses.size(); i++) 
        		{
        			int address = elements_addresses.get(i);
        			Element e = this.elements.get(address);
        			
        			//if moving from in to out
        			if (this.nodes.get(nodes_addresses.get(i)).name.matches(e.in_node_name))
        			{

        					VoltageSource v = (VoltageSource) e;
        					if (v.dependency == 'i')
        					{
        						voltage += v.ReturnVoltage();
        					}
        					
        					else if (v.dependency == 'e')
        					{
        						voltage += v.ReturnVoltage(this.nodes.get(v.dependent_node_1_address), this.nodes.get(v.dependent_node_2_address));
        					}
        					
        					//if v.dependency == 'h'
        					else
        					{
        						voltage += v.ReturnVoltage(this.elements.get(v.dependent_element_address));
        					}       				

        			}
        			
        			//if moving from out to in
        			else if (this.nodes.get(nodes_addresses.get(i)).name.matches(e.out_node_name))
        			{

        					VoltageSource v = (VoltageSource) e;
        					if (v.dependency == 'i')
        					{
        						voltage -= v.ReturnVoltage();
        					}
        					
        					else if (v.dependency == 'e')
        					{
        						voltage -= v.ReturnVoltage(this.nodes.get(v.dependent_node_1_address), this.nodes.get(v.dependent_node_2_address));
        					}
        					
        					//if v.dependency == 'h'
        					else
        					{
        						voltage -= v.ReturnVoltage(this.elements.get(v.dependent_element_address));
        					}
        				}
        			
        		}
        		
        		//if kvl is true
        		if (voltage == 0)
        		{
        			return true;
        		}
        		
        		else
        		{
        			return false;
        		}
        	}
        	
        	else
        	{
        		double voltage = 0;
        		for (int i =0; i<elements_addresses.size(); i++) 
        		{
        			int address = elements_addresses.get(i);
        			Element e = this.elements.get(address);
        			
        			//if moving from in to out
        			if (this.nodes.get(nodes_addresses.get(i)).name.matches(e.in_node_name))
        			{
        				if (e.type.matches("V"))
        				{
        					VoltageSource v = (VoltageSource) e;
        					if (v.dependency == 'i')
        					{
        						voltage += v.ReturnVoltage();
        					}
        					
        					else if (v.dependency == 'e')
        					{
        						voltage += v.ReturnVoltage(this.nodes.get(v.dependent_node_1_address), this.nodes.get(v.dependent_node_2_address));
        					}
        					
        					//if v.dependency == 'h'
        					else
        					{
        						voltage += v.ReturnVoltage(this.elements.get(v.dependent_element_address));
        					}
        				}
        				
        				else
        				{
        					voltage += this.nodes.get(nodes_addresses.get(i+1)).voltage 
        							- this.added_nodes.get(nodes_addresses.get(i)).voltage;
        				}
        			}
        			
        			//if moving from out to in
        			else if (this.nodes.get(nodes_addresses.get(i)).name.matches(e.out_node_name))
        			{
        				if (e.type.matches("V"))
        				{
        					VoltageSource v = (VoltageSource) e;
        					if (v.dependency == 'i')
        					{
        						voltage -= v.ReturnVoltage();
        					}
        					
        					else if (v.dependency == 'e')
        					{
        						voltage -= v.ReturnVoltage(this.nodes.get(v.dependent_node_1_address), this.nodes.get(v.dependent_node_2_address));
        					}
        					
        					//if v.dependency == 'h'
        					else
        					{
        						voltage -= v.ReturnVoltage(this.elements.get(v.dependent_element_address));
        					}
        				}
        				
        				else
        				{
        					voltage -= this.nodes.get(nodes_addresses.get(i+1)).voltage 
        							- this.added_nodes.get(nodes_addresses.get(i)).voltage;
        				}
        			}
        			
        		}
        		
        		//if kvl is true
        		if (voltage<0.1)
        		{
        			return true;
        		}
        		
        		else
        		{
        			return false;
        		}
        		
        	}
        }
        
        public boolean Check_Cycles(ArrayList<Integer> elements_addresses, ArrayList<Integer> nodes_addresses,
        		boolean found_ground)
        {

        	String node_name = this.nodes.get(nodes_addresses.get(nodes_addresses.size()-1)).name;
        	String starting_node_name = this.nodes.get(nodes_addresses.get(0)).name;
        	ArrayList<Integer> connected_elements = Return_Connected_Elements_Addresses(node_name);
        	
        	for (Integer i : connected_elements)
        	{


        		Element e = this.elements.get(i);

        		//if element is not already in the cycle
				boolean element_already_included = false;
				for (Integer element_address : elements_addresses)
				{
					if (element_address == i)
					{
						element_already_included = true;
						break;
					}
				}
				

				
				//find name of the new node we're adding to the path
        		String new_node;
        		if (e.in_node_name.matches(node_name))
        		{
        			new_node = e.out_node_name;
        		}
        		
        		else
        		{
        			new_node = e.in_node_name;
        		}
        	

        		boolean node_already_included = false;
        		//if new node is not already in the path
        		for (Integer node_address: nodes_addresses)
        		{
        			if (this.nodes.get(node_address).name.matches(new_node))
        			{
        				
        				node_already_included = true;
        				break;
        			}
        		}
	        	
        		
        		////if found a new node and element
        		if (!node_already_included && !element_already_included)
        		{

        			//if the new node is always connected to the first node, 
        			//it cannot be used to make a new path. only check the cycles of that is the case 
        			boolean path_available = false;
        			if (new_node.matches("0"))
	        		{
	        			found_ground = true;
	        		}
	        		
	        		ArrayList<Integer> neighboring_elements_of_new_node = Return_Connected_Elements_Addresses(new_node);
	        		// if new node makes a cycle with the first node of the path and the ground is included, return true 
	            	for (Integer element_address : neighboring_elements_of_new_node)
	            	{

	            		Element element = this.elements.get(element_address);	            		
	            		
	            		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	            		if (element.out_node_name.matches(starting_node_name) || element.in_node_name.matches(starting_node_name))
	            		{
	        				boolean new_element_already_included = false;
	        				if (e.name.matches(element.name))
	        				{
	        					new_element_already_included = true;
	        				}
	            			
	        				//if found a new cycle
	            			if (!new_element_already_included)
	            			{
	            				
	            				//if found a cycle made of voltage sources, return false
	            				boolean found_voltage_source_cycle = true;
	            				//if the new element is a voltage source
	            				if (element.type.matches("V"))
	            				{
	            					//if all the elements in the path are voltage sources
	            					for (Integer integer : elements_addresses)
	            					{
	            						if (!this.elements.get(integer).type.matches("V"))
	            						{
	            							found_voltage_source_cycle = false;
	            						}
	            					}
	            				}
	            				
	            				else
	            				{
	            					found_voltage_source_cycle = false;
	            				}
	            				
	            				if (found_voltage_source_cycle)
	            				{
		                    		
	            					if (nodes_addresses.get(0) == this.Check_Ground())
		            				{
		            					//if found a voltage source cycle with the ground, return error -4
		            					this.circuit_check = -4;
		            					return false;
		            				}
	            					
	            					else
	            					{
	            						///////////////////////check kvl for the voltage source cycle
	            						//make a new path
	            		            	ArrayList<Integer> path_nodes = new ArrayList<Integer>();
	            		            	ArrayList<Integer> path_elements = new ArrayList<Integer>();
	            		            	
	            		            	//copy the existing path
	            		            	for (Integer address : nodes_addresses)
	            		            	{
	            		            		path_nodes.add(address);
	            		            	}
	            		            	
	            		            	for (Integer address : elements_addresses)
	            		            	{
	            		            		path_elements.add(address);
	            		            	}
	            		            	
	            		            	//add the new element and node to the path
	            		            	path_elements.add(i);
	            		            	//find address of the node and add it to the path
	            		            	boolean new_node_found = false;
	            		            	for (int number =0; number<this.nodes.size(); i++)
	            		            	{
	            		            		if (this.nodes.get(number).name.matches(new_node))
	            		            		{
	            		            			new_node_found = true;
	            		            			path_elements.add(number);
	            		            			break;
	            		            		}
	            		            	}
	            		            	
	            		            	if (!new_node_found)
	            		            	{
	            		            		System.out.println("hi");
	            		            	}
	            		            	
	            		            	boolean kvl = Check_KVL(path_elements, path_nodes);
	            						//if found voltage source cycle 
	            		            	if (!kvl)
	            		            	{
	            		            		this.circuit_check = -3;
		            						return false;
	            		            	}
	            						
	            					}
	            				}
	            				
	            				
	            				
	            				else if (found_ground)
	        					{
	            					
	            					return true;
	            				}
	            				
	            				
	            			}		
	            		}	
	            		
	            		else
	            		{

		            		
	            			path_available = true;
	            		}
	            	}
	            	
	            	
	            	if (path_available)
	            	{
	            		
	            		//make a new path
		            	ArrayList<Integer> path_nodes = new ArrayList<Integer>();
		            	ArrayList<Integer> path_elements = new ArrayList<Integer>();
		            	
		            	//current source counter used for finding series current source
		            	int current_source_count = 0;
		            	
		            	//copy the existing path
		            	for (Integer address : nodes_addresses)
		            	{
		            		path_nodes.add(address);
		            	}
		            	
		            	for (Integer address : elements_addresses)
		            	{
		            		if (this.elements.get(address).type.matches("I"))
		            		{
		            			current_source_count++;
		            		}
		            		path_elements.add(address);
		            	}
		            	
		            	if (this.elements.get(i).type.matches("I"))
		            	{
		            		current_source_count++;
		            	}
		            			            	
		            	//add the new element and node to the path
		            	path_elements.add(i);
		            	//find address of the node and add it to the path
		            	boolean new_node_found = false;
		            	for (int number =0; number<this.nodes.size(); i++)
		            	{
		            		if (this.nodes.get(number).name.matches(new_node))
		            		{
		            			new_node_found = true;
		            			path_elements.add(number);
		            			break;
		            		}
		            	}
		            	
		            	if (!new_node_found)
		            	{
		            		System.out.println("new node not found");
		            	}
		            	

		            	//check if there are series current sources
		            	boolean series_path = true;
		            	if (current_source_count>1)
		            	{
		            		//if there is a node connected to multiple branches, break
		            		for (Integer address : nodes_addresses)
		            		{
		            			if (this.Find_Connected_Elements_Number(address)>2)
		            			{
		            				series_path = false;
		            				break;
		            			}
		            		}
		            	}
		            	
		            	//check if the current sources are equal
		            	if (series_path)
		            	{
		            		boolean found_first_current_source = false;
		            		double current = 0;
		            		for (Integer address : elements_addresses)
		            		{
		            			if (this.elements.get(address).type.matches("I"))
		            			{
		            				CurrentSource c = (CurrentSource) this.elements.get(address);
		            				if (!found_first_current_source)
		            				{
		            					//if found an ac current source, return false
		            				
		            					if (c.dependency == 'i')
		            					{
		            						if (c.frequency != 0)
		            						{
		            							this.circuit_check = -2;
		            							break;
		            						}
		            						
		            						else
		            						{
		            							current = c.return_current();
		            						}
		            					}
		            					
		            					else if (c.dependency == 'g')
		            					{
		            						current = c.return_current(this.nodes.get(c.dependent_node_1_address),
		            								this.nodes.get(c.dependent_node_2_address));
		            					}
		            					
		            					//if c.dependency == f
		            					else
		            					{
		            						current = c.return_current(this.elements.get(c.dependent_element_address));
		            					}
		            				}
		            				
		            				else
		            				{
		            					if (c.dependency == 'i')
		            					{
		            						if (c.frequency != 0)
		            						{
		            							this.circuit_check = -2;
		            							break;
		            						}
		            						
		            						else
		            						{
		            							if (current != c.return_current())
		            							{
		            								this.circuit_check = -2;
		            								break;
		            							}
		            						}
		            					}
		            					
		            					else if (c.dependency == 'g')
		            					{
		            						if (current != c.return_current(this.nodes.get(c.dependent_node_1_address),
		            								this.nodes.get(c.dependent_node_2_address)))
		            						{
		            							this.circuit_check = -2;
		            							break;
		            						}
		            					}
		            					
		            					else
		            					{
		            						if (current != c.return_current(this.elements.get(c.dependent_element_address)))
		            						{
		            							this.circuit_check = -2;
		            							break;
		            						}
		            					}
		            				}
		            			}
		            			
		            			
		            		}
		            	}
		            	if (this.circuit_check == -2)
		            	{
		            		return false;
		            	}
		            	
		            	//check the new path
		            	boolean found_cycle = Check_Cycles(path_elements, path_nodes, found_ground);
		            	
		            	//if found a voltage source cycle
		            	if (this.circuit_check == -4 || this.circuit_check == -3 )
		            	{
		            		
		            		return false;
		            	}
		            	
		            	if (found_cycle)
		            	{
		            		return true;
		            	}
	        		}
        		}
	            	
        			
        	}
        	
        	//if there is a floating node, return error -5
        	this.circuit_check = -5;
        	return false;
        }
        
                
        public boolean Check_Circuit()
        {
        	if (Check_Ground()!=-1)
        	{
        		for (int i = 0 ; i < this.nodes.size(); i++)
        		{
        			ArrayList<Integer> elements_addresses = new ArrayList<Integer>();
            		ArrayList<Integer> nodes_addresses = new ArrayList<Integer>();
            		nodes_addresses.add(i);
            		if (i == 0)
            		{
            			if (!Check_Cycles(elements_addresses, nodes_addresses, true))
            			{
            				return false;
            			}
            		}
            		else if (!Check_Cycles(elements_addresses, nodes_addresses,false))
            		{


            			return false;
            		}
        		}
        		
        	}
        	else
        	{
        		return false;
        	}
        	
        	if (this.circuit_check!=0)
        	{
        		return false;
        	}
			return true;
        }

        double Calculate_Sum_of_Squares()
        {
            double sum_of_squares = 0;
            for (SuperNode sn : this.super_nodes)
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
            double current_1 = 0;
            double current_2 = 0;
            double current_3 = 0;
            for (SuperNode sn : this.super_nodes)
            {
              //ground voltage is constant
                if (sn.name.matches("0"))
                {
                	//if the element is connected on both heads to the ground supernode, update its current
                	for (Element e: this.elements)
                	{
                		if (!e.type.matches("V"))
                		{
                			if (this.super_nodes.get(e.super_node_1).name.matches("0"))
                    		{
                    			if (this.super_nodes.get(e.super_node_2).name.matches("0"))
                    			{
                    				e.update(this.super_nodes.get(e.super_node_1), this.super_nodes.get(e.super_node_2));
                    			}
                    		}
                		}
                		
                	}
                    continue;
                }
                current_1 = 0;
                current_2 = 0;
                current_3 = 0;
            	ArrayList<Integer> element_names = new ArrayList<Integer>();
                for (Node n : sn.nodes)
                {
                
                  for (int i =0; i<this.elements.size(); i++)
                  {
                	  Element e = this.elements.get(i);
                    // if element is a voltage source
                    if (e.type.matches("V"))
                    {
                      continue;
                    }
                    
                    if (e.in_node_name.matches(n.name))
                    {
                      //save the names of the elements connected to the supernode we're updating. 
                      //the connected elements will be updated after supernode is updated
                      element_names.add(i);
                      if (e.type.matches("I"))
                      {
                    	 CurrentSource cs = (CurrentSource) e;
                    	 if (cs.dependency == 'f')
                    	 {
                    			 current_1 -= cs.return_current(this.elements.get(cs.dependent_element_address));
                    			ModifyVoltage(sn, dv);
                                  current_2 -= cs.return_current(this.elements.get(cs.dependent_element_address));
                                     //undo voltage changes
                                     sn.UndoLastChange();
                                     ModifyVoltage(sn, dv*-1);
                                     current_3 -= cs.return_current(this.elements.get(cs.dependent_element_address));
                                     //undo changes again
                                     sn.UndoLastChange();
                    		 
                    	 }
                    	 
                    	 else if (cs.dependency == 'i')
                    	 {
                    		 current_1 -= cs.return_current();
            				 ModifyVoltage(sn, dv);
                             current_2 -= cs.return_current();
                             //undo voltage changes
                             sn.UndoLastChange();
                             ModifyVoltage(sn, dv*-1);
                             current_3 -= cs.return_current();
                             //undo changes again
                             sn.UndoLastChange();
                    	 }
                    	 
                    	 ////////////////////////////gg//////////////////////////////////////////
                    	 else if (cs.dependency == 'g')
                    	 {
                    		 current_1 -= cs.return_current(this.nodes.get(cs.dependent_node_1_address),
                    				 this.nodes.get(cs.dependent_node_2_address));
            				 ModifyVoltage(sn, dv);
                             current_2 -= cs.return_current(this.nodes.get(cs.dependent_node_1_address),
                    				 this.nodes.get(cs.dependent_node_2_address));
                             //undo voltage changes
                             sn.UndoLastChange();
                             ModifyVoltage(sn, dv*-1);
                             current_3 -= cs.return_current(this.nodes.get(cs.dependent_node_1_address),
                    				 this.nodes.get(cs.dependent_node_2_address));
                             //undo changes again
                             sn.UndoLastChange(); 
                    	 }
                    	 
                      }
                      
                      else
                      {

                    	  current_1 -= e.return_current(this.super_nodes.get(e.super_node_1), this.super_nodes.get(e.super_node_2));
                    	  
                    	  ModifyVoltage(sn, dv);
                          current_2 -= e.return_current(this.super_nodes.get(e.super_node_1), this.super_nodes.get(e.super_node_2));
                          //undo voltage changes
                          sn.UndoLastChange();
                          ModifyVoltage(sn, dv*-1);
                          current_3 -= e.return_current(this.super_nodes.get(e.super_node_1), this.super_nodes.get(e.super_node_2));
                          //undo changes again
                          sn.UndoLastChange();
                      }
                      
                      
                    } 

                    if (e.out_node_name.matches(n.name))
                    { 
	                    	if (e.type.matches("I"))
	                        {
	                    		CurrentSource cs = (CurrentSource) e;
	                       	 if (cs.dependency == 'f')
	                       	 {
	                       			 current_1 -= cs.return_current(this.elements.get(cs.dependent_element_address));
	                       			ModifyVoltage(sn, dv);
	                                     current_2 -= cs.return_current(this.elements.get(cs.dependent_element_address));
	                                        //undo voltage changes
	                                        sn.UndoLastChange();
	                                        ModifyVoltage(sn, dv*-1);
	                                        current_3 -= cs.return_current(this.elements.get(cs.dependent_element_address));
	                                        //undo changes again
	                                        sn.UndoLastChange();
	                       		 
	                       	 }
	                       	 
	                       	 else if (cs.dependency == 'i')
	                       	 {
	                       		 current_1 -= cs.return_current();
	               				 ModifyVoltage(sn, dv);
	                                current_2 -= cs.return_current();
	                                //undo voltage changes
	                                sn.UndoLastChange();
	                                ModifyVoltage(sn, dv*-1);
	                                current_3 -= cs.return_current();
	                                //undo changes again
	                                sn.UndoLastChange();
	                       	 }
	                       	 
	                       	 ////////////////////////////gg//////////////////////////////////////////
	                       	 else if (cs.dependency == 'g')
	                       	 {
	                       		 current_1 -= cs.return_current(this.nodes.get(cs.dependent_node_1_address),
	                       				 this.nodes.get(cs.dependent_node_2_address));
	               				 ModifyVoltage(sn, dv);
	                                current_2 -= cs.return_current(this.nodes.get(cs.dependent_node_1_address),
	                       				 this.nodes.get(cs.dependent_node_2_address));
	                                //undo voltage changes
	                                sn.UndoLastChange();
	                                ModifyVoltage(sn, dv*-1);
	                                current_3 -= cs.return_current(this.nodes.get(cs.dependent_node_1_address),
	                       				 this.nodes.get(cs.dependent_node_2_address));
	                                //undo changes again
	                                sn.UndoLastChange(); 
	                       	 }
                        
                        
                        }
                    	
                    	else
                    	{
                    		 element_names.add(i);
                             current_1 += e.return_current(this.super_nodes.get(e.super_node_1), this.super_nodes.get(e.super_node_2));
                             ModifyVoltage(sn, dv);
                             current_2 += e.return_current(this.super_nodes.get(e.super_node_1), this.super_nodes.get(e.super_node_2));
                             //undo voltage changes
                             sn.UndoLastChange();
                             ModifyVoltage(sn, dv*-1);
                             current_3 += e.return_current(this.super_nodes.get(e.super_node_1), this.super_nodes.get(e.super_node_2));
                             //undo changes again
                             sn.UndoLastChange();
                    	}
                     
                    }

                  }
                }

                sn.current = current_1;
                double sum_of_squares_1 = Calculate_Sum_of_Squares();
                this.sumOfSquares = sum_of_squares_1;
                //if error is small, don't do anything. error is checked here and not in the beginning because we want to find supernodes currents. this is important especially in the beginning of analysis
                if (sum_of_squares_1 < 0.00001)
                {
                	
                    continue;
                }
                
                sn.current = current_2;
                double sum_of_squares_2 = Calculate_Sum_of_Squares();
                this.sumOfSquares_2 = sum_of_squares_2;
                
                sn.current = current_3;
                double sum_of_squares_3 = Calculate_Sum_of_Squares();
                this.sumOfSquares_3 = sum_of_squares_3;
                
                if (sum_of_squares_1 < sum_of_squares_2 && sum_of_squares_1 < sum_of_squares_3)
                {
                	continue;
                }
                
                else if (sum_of_squares_2 < sum_of_squares_1 && sum_of_squares_2 < sum_of_squares_3)
                {
                	ModifyVoltage(sn, dv*sum_of_squares_1);
                }
                
                else if (sum_of_squares_3 < sum_of_squares_1 && sum_of_squares_3 < sum_of_squares_2)
                {
                	ModifyVoltage(sn, -1*dv*sum_of_squares_1);
                }

                //update the elements using the updated Voltages 
                for (Integer i : element_names)
                {
                    this.elements.get(i).update(this.super_nodes.get(this.elements.get(i).super_node_1),
                    		this.super_nodes.get(this.elements.get(i).super_node_2));
                }
            }

        }
        
        void Analyze(double t)
        {
        	double iterations = t/Circuit.dt;
        	iterations = Math.floor(iterations);
        	ArrayList<File> results = new ArrayList<File>();
        	
        	for (Element e : this.elements)
        	{
        		try {
        			String name = String.format("%s_%s.txt", this.filename,e.name);
        			File result = new File(name);
        			if (result.createNewFile())
        			{
        				results.add(result);
        			}
        		}
        		
        		catch (IOException error)
        		{
        			error.printStackTrace();
        		}
        	}
        	
        	
        	

            
        	for (int i =0; i<iterations; i++)
        	{


        		this.Update_Nodes();
        		for (Element e : this.elements)
        		{
        			String name = String.format("%s.txt", e.name);
        			FileWriter fw;
					try {
						fw = new FileWriter(name, true);
						double current = e.current;
						double voltage = this.super_nodes.get(e.super_node_2).ReturnVoltage(e.node_2, false) 
								- this.super_nodes.get(e.super_node_1).ReturnVoltage(e.node_1, false);
						double power = current * voltage * -1;
						fw.write(String.format("v = %f  i = %f  p = %f%n",voltage , current,  power));
						
						fw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		
        		}	
        		time += dt;
        		
        		
        		//if there are ac source, update the voltages of the nodes
        		if (ac_sources)
        		{
        			for (SuperNode sn : this.super_nodes)
        			{
        				ModifyVoltage(sn, 0);
        			}
        		}
        		
        		
        	}
            
        }
        
        void Show_Results()
        {
        	
        	for (Element e : this.elements)
        	{
        		if (e.name.matches("V1"))
        		{
        			VoltageSource v = (VoltageSource) e;
        			System.out.println("v1: " + v.ReturnVoltage());
        		}
        	}
        	//print node voltages
        	for (Node n : this.nodes)
        	{
        		
        		System.out.println(n.name + "   " + n.union + "   " + n.voltage);
        	}
        	
        	for (SuperNode sn : this.super_nodes)
        	{
        		System.out.println(sn.current);
        	}
        	for (Element e : this.elements)
        	{
        		if (!e.type.matches("V"))
        		{
        			System.out.println(e.name + "   "+ e.node_1 + "   " + e.node_2 + "   " + e.super_node_1 + "   "
        					+ e.super_node_2 + "   " + e.current);
        		}
        	}
        	
        	System.out.println(this.sumOfSquares);
        	System.out.println(this.sumOfSquares_2);
        	System.out.println(this.sumOfSquares_3);
        }
    }

    public static void main(String[] args){

    	String file = "input";
    	String filename = String.format("%s.txt", file);
        File inputFile = new File(filename);

        try {

            Circuit eC = new Circuit();
            eC.filename = file;
            Scanner sc = new Scanner(inputFile);
            ArrayList<String> lines = new ArrayList<>();
            int cnt = 0, checkEnoughVariables = 0;
            double dt = 0, dv = 0, di = 0;
            double analysis_time = 5;
            while (sc.hasNextLine())
            {
                lines.add(sc.nextLine().trim());
                String info[] = lines.get(cnt).split("\\s+");

                if (lines.get(cnt).length() < 4){
                    //
                }
                else if (!lines.get(cnt).substring(0, 5).equals(".tran") && lines.get(cnt).charAt(0) != 'd') {
                	
                    Node in = new Node(info[1]);
                    eC.Add_Node(info[1]);
                    Node out = new Node(info[2]);
                    eC.Add_Node(info[2]);

                	switch (lines.get(cnt).charAt(0)) {

                    case 'R':
                        if (eC.valueFinder(info[3]) != -85){
                            Resistor R = new Resistor(info[0], in.name, out.name, eC.valueFinder(info[3]));
                            eC.Add_Element((Element) R);
                            break;
                        }
                        else {
                            System.out.println("Invalid Value");
                            return;
                        }
                    case 'C':
                        if (eC.valueFinder(info[3]) != -85) {
                            Capacitor C = new Capacitor(info[0], in.name, out.name, eC.valueFinder(info[3]), 0);
                            eC.Add_Element((Element) C);
                            break;
                        }
                        else {
                            System.out.println("Invalid Value");
                            return;
                        }
                    case 'L':
                        if (eC.valueFinder(info[3]) != -85) {
                            Inductor L = new Inductor(info[0], in.name, out.name, eC.valueFinder(info[3]), 0);
                            eC.Add_Element((Element) L);
                            break;
                        }
                        else {
                            System.out.println("Invalid Value");
                            return;
                        }
                    case 'V':
                    	//if its an independent voltage source
                    	if (info.length == 7)
                    	{
                    	    if (eC.valueFinder(info[3]) != -85 && eC.valueFinder(info[4]) != -85
                                    && eC.valueFinder(info[5]) !=-85 && eC.valueFinder(info[6]) != -85) {
                                VoltageSource V = new VoltageSource(info[0], in.name, out.name, eC.valueFinder(info[3]),
                                        eC.valueFinder(info[4]), eC.valueFinder(info[5]), eC.valueFinder(info[6]));
                                eC.Add_Element((Element) V);
                            }
                            else {
                                System.out.println("Invalid Value");
                                return;
                            }
                    	}
                    	
                    	//if its a current dependent element
                    	else if (info.length == 5)
                    	{
                    	    if (eC.valueFinder(info[4]) != 85) {
                                VoltageSource V = new VoltageSource(info[0], in.name, out.name, info[3],
                                        eC.valueFinder(info[4]));
                                eC.Add_Element((Element) V);
                            }
                            else {
                                System.out.println("Invalid Value");
                                return;
                            }
                    	}
                    	
                    	//if it's a voltage dependent voltage source
                    	else if (info.length == 6)
                    	{
                    	    if (eC.valueFinder(info[4]) != -85) {
                                VoltageSource V = new VoltageSource(info[0], in.name, out.name, info[3],
                                        eC.valueFinder(info[4]));
                                eC.Add_Element((Element) V);
                            }
                            else {
                                System.out.println("Invalid Value");
                                return;
                            }
                    	}
                       
                    	else
                    	{
                    		System.out.println("Incorrect Voltage Source Information Format");
                    		return;
                    	}
                        
                        break;
                    case 'I':
                    	//if it's an independent current source
                    	if (info.length == 7)
                    	{
                            if (eC.valueFinder(info[3]) != -85 && eC.valueFinder(info[4]) != -85
                                    && eC.valueFinder(info[5]) !=-85 && eC.valueFinder(info[6]) != -85) {
                                CurrentSource I = new CurrentSource(info[0], in.name, out.name, eC.valueFinder(info[3]),
                                        eC.valueFinder(info[4]), eC.valueFinder(info[5]), eC.valueFinder(info[6]));
                                eC.Add_Element((Element) I);
                            }
                            else {
                                System.out.println("Invalid Value");
                                return;
                            }
                    	}
	                        
                       	//if its a current dependent current source
                    	else if (info.length == 5) {
                            if (eC.valueFinder(info[4]) != -85) {
                                CurrentSource I = new CurrentSource(info[0], in.name, out.name, info[3],
                                        eC.valueFinder(info[4]));
                                eC.Add_Element((Element) I);
                            }
                            else {
                                System.out.println("Invalid Value");
                                return;
                            }
                        }
                    	
                    	//if it's a voltage dependent current source
                    	else if (info.length == 6)
                    	{
                    	    if (eC.valueFinder(info[4]) != -85) {
                                CurrentSource I = new CurrentSource(info[0], in.name, out.name, info[3],
                                        eC.valueFinder(info[4]));
                                eC.Add_Element((Element) I);
                            }
                            else {
                                System.out.println("Invalid Value");
                                return;
                            }
                    	}
                       
                    	else
                    	{
                    		System.out.println("Incorrect Current Source Information Format");
                    		return;
                    	}
                        
                        break;
                        
                    case '*':
                        //comment
                        break;

                    default:
                        System.out.printf("Invalid Syntax : line %d ", cnt);
                        return;

                	}

                }
                
                else
                {
                	if (lines.get(cnt).charAt(0) == 'd')
                	{
                		switch (lines.get(cnt).charAt(1)){
                        case 't': case 'T':
                            dt = eC.valueFinder(info[2]);
                            checkEnoughVariables++;
                            break;
                        case 'v': case 'V':
                            dv = eC.valueFinder(info[2]);
                            checkEnoughVariables++;
                            break;
                        case 'i': case 'I':
                            di = eC.valueFinder(info[2]);
                            checkEnoughVariables++;
                            break;

                        default:
                            System.out.printf("Invalid Syntax : line %d ", cnt);
                            return;
                		}
                	}
                	
                	else if (lines.get(cnt).substring(0, 5).equals(".tran"))
                	{
                		analysis_time = eC.valueFinder(info[1]);
                	}



                    if (checkEnoughVariables == 3){
                            eC.circuit_initialize(dv, dt, di);
                    }
                        
                        /*if (1==2) {
                            //error for not initializing dv,dt,di
                            System.out.printf("Not Enough Information!");
                        }
                         */

                        ///////////////////circuit analysis
                        
                    //loops and updates
                    //other stuff
                }

                cnt++;
            }

            if (checkEnoughVariables != 3){
                System.out.println("Not Enough Information");
                sc.close();
                return;
            }

            sc.close();
            eC.Init_Circuit();
    
            eC.Analyze(analysis_time);
   
            eC.Show_Results();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
