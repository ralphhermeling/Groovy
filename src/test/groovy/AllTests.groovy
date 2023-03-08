import groovy.test.GroovyTestSuite
import junit.framework.Test
import junit.textui.TestRunner

class AllTest {
    static Test suite(){
        def allTests = new GroovyTestSuite()
        allTests.addTestSuite(ComputationsTest.class)
        allTests.addTestSuite(FruitColorMapServiceTest.class)
        allTests.addTestSuite(FruitRecordServiceTest.class)
        return allTests
    }

    static void main(String[] args){
        TestRunner.run(this.suite())
    }
}

