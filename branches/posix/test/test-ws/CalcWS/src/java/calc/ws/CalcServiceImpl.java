package calc.ws;

import calc.ws.ties.*;

@javax.jws.WebService (endpointInterface="calc.ws.ties.CalcPortType")
public class CalcServiceImpl implements CalcPortType {

    public int sum(int a, int b) {
        System.out.println("sum " + a + " " + b);
        return(a + b);
    }

    public int sub(int a, int b) {
        System.out.println("sub " + a + " " + b);
        return(a - b);
    }

    public int mult(int a, int b) {
        System.out.println("mult " + a + " " + b);
        return(a * b);
    }

    public int intdiv(int a, int b) throws DivideByZero {
        System.out.println("intdiv " + a + " " + b);
        if(b == 0)
            throw new DivideByZero("Can't divide by zero", null);
        return((int)(a / b));
    }

}
