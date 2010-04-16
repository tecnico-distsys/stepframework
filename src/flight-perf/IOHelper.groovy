/**
 *  Input Output Helper
 *
 */
class IOHelper {

    static def initI(args) {

        def i = System.in;

        if(args.length >= 1) {
            i = new File(args[0]);
            assert i.exists();
        }

        return i;
    }

    static def initO(args) {

        def o = System.out;

        if(args.length >= 2) {
            o = new PrintStream(new File(args[1]));
        }

        return o;
    }

}
