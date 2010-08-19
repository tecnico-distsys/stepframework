def file = new File("eng_surnames.htm");
assert file.exists();

def o = new PrintStream(new File("eng_surnames.csv"));

int lineNr = 0;
int nameCounter = 0;
file.eachLine{line ->
    lineNr++;

    // ~ creates a Pattern
    // =~ creates a Matcher
    // ==~ tests if String matches the pattern

//<tr><td>1. <a href="http://genealogy.about.com/library/surnames/s/bl_name-SMITH.htm">SMITH</a></td>
//<td>51. <a href="http://genealogy.about.com/library/surnames/m/bl_name-MITCHELL.htm">MITCHELL</a></td></tr>
//<td>86. <a href="http://genealogy.about.com/od/surname_meaning/p/palmer.htm">PALMER</a></td></tr>

    def matcher = (line =~ "(?:<tr>)*<td>.*href.*surname.*>(\\w+).*");
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

o.close();
println "Captured " + nameCounter + " names!";
