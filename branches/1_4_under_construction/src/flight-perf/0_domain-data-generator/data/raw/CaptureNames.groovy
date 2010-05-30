def dir = new File(".");
assert dir.exists()
assert dir.isDirectory()

def o = new PrintStream(new File("eng_names.csv"));
int nameCounter = 0;

    // ~ creates a Pattern
    // =~ creates a Matcher
    // ==~ tests if String matches the pattern

dir.eachFile{ file ->

    if(file.name ==~ "eng_names \\(\\d+\\)\\.htm(l)?") {
        println "Processing " + file.name;

        int lineNr = 0;
        file.eachLine{line ->
            lineNr++;

            //<p><b><a href="/name/zachary">ZACHARY</a></b> &nbsp; <font class="masc">m</font> &nbsp; <font class="info"><a href="/nmc/eng.php" class="usg">English</a></font><br>Usual English form of <a href="/name/zacharias" class="nl">ZACHARIAS</a>... <a href="/name/zachary">[more]</a>

            def matcher = (line =~ "<p><b><a href=\"/name/.*>(\\w+).*");
            if(matcher.matches()) {
                def surname = matcher.group(1);

                def first = surname.substring(0, 1);
                def rest = surname.substring(1, surname.length());
                surname = first + rest.toLowerCase();

                o.println(surname);
                nameCounter++;
            } else {
                //println "Line " + lineNr + " did not match: " + line;
            }

        }

    }

}

o.close();
println "Captured " + nameCounter + " names!";
