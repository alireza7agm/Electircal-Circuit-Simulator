//hi


/////// alireza && amir presents:)))))))))))))

/////// inja har ruz begim be nazeret key tamoom mishe:))

import java.util.ArrayList;

public class EC_Simulator {

    public static class SuperNode{

        double voltage_1;
        double voltage_2;
        double next_voltage_1;
        double next_voltage_2;
        double current;
        double previous_current;
        String name;

        SuperNode(){


        }

        SuperNode (double voltage_1, double voltage_2,
                   double next_voltage_1, double next_voltage_2,
                   double current, double previous_current, String name){

            this.voltage_1 = voltage_1;
            this.voltage_2 = voltage_2;
            this.next_voltage_1 = next_voltage_1;
            this.next_voltage_2 = next_voltage_2;
            this.current = current;
            this.previous_current = previous_current;
            this.name = name;

        }


    }

    public static class Element{
        double voltage;
        double current;
        double next_current;
        double value;
        SuperNode in;
        SuperNode out;
        /////////////////other variables

        //constructor :
        public Element(SuperNode in, SuperNode out, double value) {
            this.in = in;
            this.out = out;
            this.voltage = 0;
            this.current = 0;
            this.next_current = 0;
            this.value = value;
        }


        //getters and setters :
        public double getVoltage() {
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


    public class Resistor extends Element{

        public Resistor(SuperNode in, SuperNode out, double value) {
            super(in, out, value);
        }

        void update_resistor(double dv)
        {
            this.current = (in.voltage_2 - out.voltage_1)/value;
            this.next_current = (out.voltage_2+dv - in.voltage_1)/value;
        }

    }


    public class Capacitor extends Element{

        public Capacitor(SuperNode in, SuperNode out, double value) {
            super(in, out, value);
        }

        void update_capacitor(double dv, double dt)
        {
            this.current = this.value*((in.next_voltage_2 - out.next_voltage_1)-(in.voltage_2 - out.voltage_1))/dt ;
            this.next_current = this.value*((in.next_voltage_2+dv - out.next_voltage_1)-(in.voltage_2 - out.voltage_1))/dt;
        }

    }


    public class Inductor extends Element{

        public Inductor(SuperNode in, SuperNode out, double value) {
            super(in, out, value);
        }
        ///////////////////////////////////////////////////////////////////////////what is v_p
        /*void update_inductor(double dv, double dt)
        {
            this.current += (in.voltage_2 - out.voltage_1)*dt/this.value;
            this.next_current += (in.voltage_next_2+dv - in.voltage_2)/this.value;
        }*/

    }


    public class VoltageSource extends Element{

        public VoltageSource(SuperNode in, SuperNode out, double value) {
            super(in, out, value);
        }

    }


    public class CurrentSource extends Element{

        public CurrentSource(SuperNode in, SuperNode out, double value) {
            super(in, out, value);
        }

    }

    public class Circuit{
        ArrayList <SuperNode> super_nodes = new ArrayList<SuperNode>();
        ArrayList <Element> elements = new ArrayList<Element>();
        double dv;
        double dt;
        double sumOfSquares = 0;

        Circuit(double dv, double dt)
        {
            this.dv = dv;
            this.dt = dt;
        }

        public void Init_Circuit()
        {
            //initializing the circuit and sumOfSquares
            return;
        }

        public void Add_Node(SuperNode n)
        {
            n = new SuperNode();
            super_nodes.add(n);
            return;
        }

        public void Add_Element(Element e, SuperNode in, SuperNode out, double value)
        {
            e = new Element(in, out, value);
            elements.add(e);
            return;
        }

        double Calculate_Sum_of_Squares()
        {
            double sum_of_squares = 0;
            for (SuperNode n : this.super_nodes)
            {
                sum_of_squares += n.current * n.current;
            }
            return sum_of_squares;
        }

        void Update_Nodes()
        {
            int counter = 0;
            for (SuperNode n : this.super_nodes)
            {
                n.current = 0;
                n.previous_current = 0;
                for (Element e : this.elements)
                {



                }


            }
        }





    }




    public static void main(String[] args){

        System.out.println("-1");

    }
}
