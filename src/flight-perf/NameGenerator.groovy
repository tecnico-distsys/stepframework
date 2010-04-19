/**
 *  Groovy object to generate names
 */

public class NameGenerator {

    def names;
    def surnames;
    def random;

    public NameGenerator(nameFilePath, surnameFilePath, random) {
        initNames(nameFilePath);
        initSurnames(surnameFilePath);
        initRandom(random);
    }


    private void initNames(nameFilePath) {
        def nameFile = new File(nameFilePath);
        assert(nameFile.exists());

        names = fileToArray(nameFile);
    }

    private void initSurnames(surnameFilePath) {
        def surnameFile = new File(surnameFilePath);
        assert(surnameFile.exists());

        surnames = fileToArray(surnameFile);
    }

    private def fileToArray(file) {
        // count lines
        int lineCount = 0;
        file.eachLine{
            lineCount++;
        }

        // allocate array
        def array = new Object[lineCount];

        // fill array with file data
        int i = 0;
        file.eachLine{line ->
            array[i++] = line;
        }

        return array;
    }

    private void initRandom(random) {
        this.random = random;
    }

    /** Generate a random name */
    public def nextName() {
        def i = random.nextInt(names.length);
        def j = random.nextInt(surnames.length);

        return names[i] + " " + surnames[j];
    }

    //
    //  Test
    //
    public static void main(String[] args) throws Exception {
        NameGenerator ng = new NameGenerator("data/names.txt", "data/surnames.txt", new Random());
        for(int i=0; i < 10; i++) {
            println(ng.nextName());
        }
    }

}