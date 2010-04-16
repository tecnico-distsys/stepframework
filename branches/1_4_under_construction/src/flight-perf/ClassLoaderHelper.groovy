/**
 *  Class Loader Helper.
 *
 *  Simplifies adding classes and libraries to a class loader.
 *  First add URLs, files, directories, and JAR directories to collection.
 *  Then add to class loader.
 */
class ClassLoaderHelper {

    // default class loader to use
    // (variable cannot be called classLoader)
    static def cLoader = ClassLoaderHelper.classLoader.rootLoader;

    static def addURL(url) {
        assert(url != null);
        cLoader.addURL(url);
    }

    static def addFile(filePath) {
        assert(filePath != null);
        def file = new File(filePath);
        assert(file.exists());
        def url = file.toURI().toURL();
        addURL(url);
    }

    static def addDir(dirPath) {
        addFile(dirPath);
    }

    static def addJarDir(jarDirPath) {
        assert(jarDirPath != null);
        def jarDir = new File(jarDirPath);
        assert(jarDir.exists());
        assert(jarDir.isDirectory());
        
        jarDir.eachFile() { file ->
            if(file.name.endsWith("jar")) {
                addFile(file.path);
            }
        }
    }

}
