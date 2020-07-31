package helloworld;
//hi


/////// alireza && amir presents:)))))))))))))

/////// inja har ruz begim be nazeret key tamoom mishe:))

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math; 

public class HelloWorld {
//////////////////////////////
    public static class Node{

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

        public Resistor(String name, String in, String out, double value, String type) {
            super(name, in, out, value, type);
        }

        void update(SuperNode in, SuperNode out)
        {
          this.current = (in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))/this.value;
        }

        double return_current(SuperNode in, SuperNode out)
        {
        	
          return  (in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))/this.value;
        }

    }


    public static class Capacitor extends Element{


    	double initial_condition = 0;
        public Capacitor(String name, String in, String out, double value, double initial_condition, String type) {
            super(name, in, out, value, type);
            this.initial_condition = initial_condition;

        }

        void update(SuperNode in, SuperNode out)
        {
          this.current = this.value*((in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))
            -(in.ReturnVoltage(node_1, true) - out.ReturnVoltage(node_2, true)))/Circuit.dt;
        }

        double return_current(SuperNode in, SuperNode out) 
        {
          return this.value*((in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))
            -(in.ReturnVoltage(node_1, true) - out.ReturnVoltage(node_2, true)))/Circuit.dt;
        }

    }


    public static class Inductor extends Element{


        double initial_current;
        public Inductor(String name, String in, String out, double value, double initial_current, String type) {
            super(name, in, out, value, type);
            this.initial_current = initial_current;
            this.current = initial_current;
        }

        void update(SuperNode in, SuperNode out)
        {
          this.current += (in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))*Circuit.dt/this.value;
        }

        double return_current(SuperNode in, SuperNode out)
        {
          return (this.current + (in.ReturnVoltage(node_1, false) - out.ReturnVoltage(node_2, false))*Circuit.dt/this.value);
        }



    }


    public static class VoltageSource extends Element{

//i is independant
        char dependancy = 'i';
        Node dependant_node_1;
        Node dependant_node_2;
        String dependant_element;
        double amplitude = 0;
        double frequency = 0;
        double phase = 0;
        public VoltageSource (String name, String in, String out, double value, String type) {
            super(name, in, out, value, type);
            dependancy = 'i';
        }
        public VoltageSource (String name, String in, String out, double value, double amplitude, double frequency, double phase, String type) {
            super(name, in, out, value, type);
            dependancy = 'i';
            this.frequency = frequency; 
            this.phase = phase;
            this.amplitude = amplitude;
        }

///voltage dependant voltage source
        public VoltageSource (String name, String in, String out, Node dependant_node_1, Node dependant_node_2, double amplitude)
        {
          super(name, in, out, amplitude, "V");
        		  
          dependancy = 'e';
          this.dependant_node_1 = dependant_node_1;
          this.dependant_node_2 = dependant_node_2;
        }
//current dependant voltage source
        public VoltageSource (String name, String in, String out, String dependant_element , double amplitude)
        {
        	super(name, in, out, amplitude, "V");
          dependancy = 'h';
          this.dependant_element = dependant_element;
        }

///////////returns voltage at time t
        void update(SuperNode in, SuperNode out)
        {
              if (dependancy == 'i')
              {
                this.voltage = this.value + amplitude*Math.sin(Circuit.time*2*Math.PI*frequency + phase);
              }

              if (dependancy == 'e')
              {
            	  this.voltage = this.amplitude*(dependant_node_1.voltage - this.dependant_node_2.voltage);
              }
              /*
              else if (dependancy == 'g')
              {
                return this.amplitude * (dependant_node_1 - )
              }            
*/
         
        }
        
        void update(Element e)
        {
        	this.voltage = this.amplitude * (e.current);
        }

        double ReturnVoltage()
        {
          if (dependancy == 'i')
          {
            return this.value + amplitude*Math.sin(Circuit.time*2*Math.PI*frequency + phase);
          }

          if (dependancy == 'e')
          {
        	  return this.amplitude*(dependant_node_1.voltage - this.dependant_node_2.voltage);
          }          
          
          return 0;
        }
        
        double ReturnVoltage(Element e)
        {
        	return this.amplitude * e.current;
        }

    }


    public static class CurrentSource extends Element{

        char dependancy = 'i';
        Node dependant_node_1;
        Node dependant_node_2;
        String dependant_element;
        double frequency = 0;
        double phase = 0;
        double amplitude = 0;
        public CurrentSource (String name, String in, String out, double value, String type) {
            super (name, in, out, value, type);
            this.current = value;
        }

        void update(SuperNode in, SuperNode out)
        {
        	if (this.dependancy == 'i')
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

    }

    public static class Circuit{

		ArrayList <Node> nodes = new ArrayList<Node>();
        ArrayList <SuperNode> super_nodes = new ArrayList<SuperNode>();
        ArrayList <Element> elements = new ArrayList<Element>();
        ArrayList <ArrayList<String>> super_node_lists = new ArrayList<ArrayList<String>>();
        ArrayList<Node> added_nodes = new ArrayList<Node>();

        public static double time;
        double dv;
        public static double dt;
        double di;
        double sumOfSquares = 0;
        double sumOfSquares_2 = 0;
        double sumOfSquares_3 = 0;
        
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
                        if (v.dependancy == 'i')
                        {
                        		voltage_difference = v.ReturnVoltage();
                        }
                        else if (v.dependancy == 'e')
                        {
                        		voltage_difference = v.ReturnVoltage();
                        }
                        	
                        else if (v.dependancy == 'h')
                        {
                        	for (Element e : this.elements)
                        	{
                        		if (e.name.matches(v.dependant_element))
                        		{
                        			voltage_difference = v.ReturnVoltage(e);
                        		}
                        	}
                        		
                       	}
                       	n.voltage = current_node.voltage - voltage_difference;
                       }
                       
                   
                        modified.add(n);
                        break;
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
                        	if (v.dependancy == 'i')
                        	{
                        		voltage_difference = v.ReturnVoltage();
                        	}
                        	else if (v.dependancy == 'e')
                        	{
                        		voltage_difference = v.ReturnVoltage();
                        	}
                        	
                        	else if (v.dependancy == 'h')
                        	{
                        		for (Element e : this.elements)
                        		{
                        			if (e.name.matches(v.dependant_element))
                        			{
                        				voltage_difference = v.ReturnVoltage(e);
                        			}
                        		}
                        		
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

          for ( SuperNode sn : this.super_nodes)
          {
            //update voltages of nodes inside the supernodes just created
            ModifyVoltage(sn, 0);

          }

          boolean found_in_supernode = false;
          boolean found_out_supernode = false;
          int in_node_address = -1;
          int out_node_address = -1;
          //find supernodes and nodes of elements and add their addresses to the elements for easy access
          for (Element e : this.elements)
          {
        	  found_in_supernode = false;
        	  found_out_supernode = false;
            for (int i = 0 ; i<this.super_nodes.size(); i++)
            {
              in_node_address = this.super_nodes.get(i).IsNodeHere(e.in_node_name);
              if (in_node_address != -1)
              {
                e.super_node_1 = i;
                e.node_1 = in_node_address;
                found_in_supernode = true;
             
              }
              
              
              out_node_address = this.super_nodes.get(i).IsNodeHere(e.out_node_name);
              if (out_node_address != -1)
              {
                e.super_node_2 = i;
                e.node_2 = out_node_address;
                found_out_supernode = true;
                
              }

              if (found_in_supernode && found_out_supernode)
              {
                break;
              }
            }
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

        public void Add_Element(String name, String in, String out, double value, double initialCondition, String type)
        {
            switch (type){

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
                if (sn.name.matches("GND"))
                {
                	//if the element is connected on both heads to the ground supernode, update its current
                	for (Element e: this.elements)
                	{
                		if (!e.type.matches("V"))
                		{
                			if (this.super_nodes.get(e.super_node_1).name.matches("GND"))
                    		{
                    			if (this.super_nodes.get(e.super_node_2).name.matches("GND"))
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
                    	 if (cs.dependancy == 'f')
                    	 {
                    		 for (Element element : this.elements)
                    		 {
                    			 if (element.name.matches(cs.dependant_element))
                    			 {
                    				 current_1 -= cs.return_current(element);
                    				 ModifyVoltage(sn, dv);
                                     current_2 -= cs.return_current(element);
                                     //undo voltage changes
                                     sn.UndoLastChange();
                                     ModifyVoltage(sn, dv*-1);
                                     current_3 -= cs.return_current(element);
                                     //undo changes again
                                     sn.UndoLastChange();
                    			 }
                    		 }
                    		 
                    	 }
                    	 
                    	 else if (cs.dependancy == 'i')
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
	                      	 if (cs.dependancy == 'f')
	                      	 {
	                      		 for (Element element : this.elements)
	                      		 {
	                      			 if (element.name.matches(cs.dependant_element))
	                      			 {
	                      				 current_1 += cs.return_current(element);
	                      				 ModifyVoltage(sn, dv);
	                                       current_2 += cs.return_current(element);
	                                       //undo voltage changes
	                                       sn.UndoLastChange();
	                                       ModifyVoltage(sn, dv*-1);
	                                       current_3 += cs.return_current(element);
	                                       //undo changes again
	                                       sn.UndoLastChange();
	                      			 }
	                      		 }
	                      		 
	                      	 }
	                      	 
	                      	 else if (cs.dependancy == 'i')
	                      	 {
	                      		 current_1 += cs.return_current();
	              				 ModifyVoltage(sn, dv);
	                               current_2 += cs.return_current();
	                               //undo voltage changes
	                               sn.UndoLastChange();
	                               ModifyVoltage(sn, dv*-1);
	                               current_3 += cs.return_current();
	                               //undo changes again
	                               sn.UndoLastChange();
	                      	 }
	                      	 
	                 ///////////////////////////////////     	 ////////////////////////////gggg
	                 
	                      	 
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
                if (sum_of_squares_1 < 0.01)
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
        	int iterations = 1;
        	for (int i =0; i<iterations; i++)
        	{

        		this.Update_Nodes();
        		time += dt;

        	}
            
        }
        
        void Show_Results()
        {
        
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
        			System.out.println(e.type + "   "+ e.node_1 + "   " + e.node_2 + "   " + e.super_node_1 + "   "
        					+ e.super_node_2 + "   " + e.current);
        		}
        	}
        	
        	System.out.println(this.sumOfSquares);
        	System.out.println(this.sumOfSquares_2);
        	System.out.println(this.sumOfSquares_3);
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

                if (!lines.get(cnt).substring(0, 5).equals(".tran") && lines.get(cnt).charAt(0) != 'd') {
                	
                    Node in = new Node(info[1]);
                    eC.Add_Node(info[1]);
                    Node out = new Node(info[2]);
                    eC.Add_Node(info[2]);
                    
                	switch (lines.get(cnt).charAt(0)) {

                    case 'R':
                        Resistor R = new Resistor(info[0], in.name, out.name, Double.parseDouble(info[3]),  "R");
                        eC.add(R);
                        break;
                    case 'C':
                        Capacitor C = new Capacitor(info[0], in.name, out.name, Double.parseDouble(info[3]), 0, "C");
                        eC.add(C);
                        break;
                    case 'L':
                        Inductor L = new Inductor(info[0], in.name, out.name, Double.parseDouble(info[3]), 0, "L");
                        eC.add(L);
                        break;
                    case 'V':
                    	//if its an ac voltage source
                    	if (info.length == 7)
                    	{
                    		VoltageSource V = new VoltageSource(info[0], in.name, out.name, Double.parseDouble(info[3]), );
                    	}
                    	else
                    	{
                    		 VoltageSource V = new VoltageSource(info[0], in.name, out.name, Double.parseDouble(info[3]),  "V");
                    	}
                       
                        eC.add(V);
                        break;
                    case 'I':
                        CurrentSource I = new CurrentSource(info[0], in.name, out.name, Double.parseDouble(info[3]), "I");
                        eC.add(I);
                        break;
                        
                    case '*':
                        //comment
                        break;

                    default:
                        System.out.printf("Invalid Syntax : line %d ", cnt);
                        break;
                	
                	}
                    //error invalid value


                    //error invalid syntax


                    //pico micro ...   
                }
                
                else
                {
                	if (lines.get(cnt).charAt(0) == 'd')
                	{
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
            sc.close();
            eC.Init_Circuit();
    
            eC.Analyze(5);
   
            eC.Show_Results();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
