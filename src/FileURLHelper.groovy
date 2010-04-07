/**
 *  File URL Helper
 */

class FileURLHelper {
    def getFileURLPrefix() {
        def osName = System.properties.get("os.name");
        def osIsWindows = (osName ==~ /(?i)win.*/);
        // Windows requires one more slash
        return "file://" + (osIsWindows ? "/" : "");
    }

    def toFileURL(path) {
        def file = new File(path);
        assert file.exists();
        
        if(!file.isAbsolute()) {
            path = file.getAbsolutePath();
        }

        // replace \ by /
        def matcher = (path =~ "\\\\");
        path = matcher.replaceAll("/");

        // add prefix
        return new URL(getFileURLPrefix() + path);
    }
}