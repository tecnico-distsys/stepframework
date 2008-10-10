package step.test.dot;

import java.util.List;

import step.framework.config.tree.*;


public class DotConfig {

    public String data;

    public DotConfig(String s) {
        data = s;
    }

    public String toString() {
        return "data=" + data;
    }

}
