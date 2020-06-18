


/////// alireza && amir presents:)))))))))))))

/////// inja har ruz begim be nazeret key tamoom mishe:))

public class EC_Simulator {

    public static class Element{

        private double voltage;
        private double current;
        /////////////////other variables

        //constructor :

        public Element(double voltage, double current) {
            this.voltage = voltage;
            this.current = current;
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

        public Resistor(double voltage, double current) {
            super(voltage, current);
        }

    }




    
    public static void main(String[] args){

        System.out.println("-1");

    }
}
