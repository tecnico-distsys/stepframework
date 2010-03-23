package step.framework.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.*;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.CompositeTable;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;

import step.framework.persistence.PersistenceUtil;

/**
 *  This is the Framework's base service test.<br />
 *  All service unit tests should extend this class or one of its subclasses.<br />
 *  <br />
 *
 */
public class ServiceTest {

    static IDatabaseConnection connection;

    static {
        PersistenceUtil.getSessionFactory().getCurrentSession();
    }

    @BeforeClass
    public static void openConnection() {
        try {
            connection = getConnection();
        } catch (Exception ex) {
            throw new AssertionError(ex);
        }
    }

    @AfterClass
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new AssertionError(ex);
        }
    }

    private static final IDatabaseConnection getConnection()
    throws IOException, ClassNotFoundException, SQLException {
        // load database properties
        Properties buildProperties = new Properties();
        buildProperties.load(
            ServiceTest.class.getResourceAsStream("/dbunit.properties"));

        String dbUrl = buildProperties.getProperty("database.url");
        String dbUsername = buildProperties.getProperty("database.username");
        String dbPassword = buildProperties.getProperty("database.password");

        // create JDBC connection
        Class.forName(buildProperties.getProperty("database.driver"));
        Connection jdbcConnection = DriverManager.getConnection(dbUrl,
        dbUsername, dbPassword);

        // Disable foreign key constraint checking
        // This really depends on the DBMS product... here for MySQL DB
        jdbcConnection.prepareStatement(
            buildProperties.getProperty("database.disableConstraints")).execute();

        // create DbUnit connection
        return new DatabaseConnection(jdbcConnection);
    }

    @Before
    public void loadData() {
        try {
            // initialize your dataset here
            IDataSet dataSet = getDataSet(getSetupDataSetName());
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } catch (Exception ex) {
            throw new AssertionError(ex);
        }
    }

    @After
    public void clearData() {
    }

    protected final IDataSet getDataSet(String filename) throws DataSetException, IOException {
        InputSource inputSource = new InputSource(
            getClass().getResourceAsStream(filename));
        inputSource.setEncoding("UTF-8");
        return new CompositeDataSet(new FlatXmlDataSet(inputSource));
    }

    /**
    * Gets the name of the file containing the setup data set
    *
    * @return A String in the format "/" + relativeClassName + ".xml"
    */
    protected String getSetupDataSetName() {
        String className = getClass().getName();
        return "/" + className.substring(className.lastIndexOf('.') + 1)
            + ".xml";
    }

    /**
     * Gets the name of the file containing the expected result data set.
     *
     * @return A String in the format "/" + relativeClassName + "-result.xml"
     */
    protected String getResultDataSetName() {
        String className = getClass().getName();
        return "/" + className.substring(className.lastIndexOf('.') + 1)
            + "-result.xml";
    }

    protected void assertDatabase(String filename) throws java.lang.AssertionError {
        IDataSet expectedDataSet;
        IDataSet actualDataSet;

        try {
            // get expected result data set (from a flat XML)
            expectedDataSet = getDataSet(filename);

            // get the actual data set (from the DB)
            actualDataSet = connection.createDataSet();
        } catch (Exception ex) {
            throw new AssertionError(ex);
        }

        try {
            // Compare only columns present on expected result
            ITableIterator expectedIterator = expectedDataSet.iterator();
            while (expectedIterator.next()) {
                ITableMetaData metaData = expectedIterator.getTableMetaData();
                ITable actualTable = new CompositeTable(metaData,
                    actualDataSet.getTable(metaData.getTableName()));

                Assertion.assertEquals(expectedIterator.getTable(), actualTable);
            }
        } catch (DatabaseUnitException ex) {
            throw new AssertionError(ex);
        }
    }

}
