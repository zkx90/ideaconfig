#parse("TestMe macros.groovy")
#set($hasMocks=$MockitoMockBuilder.hasMockable($TESTED_CLASS.fields))
#if($PACKAGE_NAME)
package ${PACKAGE_NAME}
#end
import spock.lang.*
#if($hasMocks)
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
//import static org.mockito.Mockito.*
#end

#parse("File Header.java")
class ${CLASS_NAME}  extends Specification {
#grRenderMockedFields($TESTED_CLASS.fields)
#grRenderTestSubjectInit($TESTED_CLASS,$TestSubjectUtils.hasTestableInstanceMethod($TESTED_CLASS.methods),$hasMocks)
#if($hasMocks)

    def setup() {
        MockitoAnnotations.initMocks(this)
    }
#end
#foreach($method in $TESTED_CLASS.methods)
    #if($TestSubjectUtils.shouldBeTested($method))

    def "#renderTestMethodName($method.name)"() {
#if($MockitoMockBuilder.shouldStub($method,$TESTED_CLASS.fields))
        given:
#grRenderMockStubs($method,$TESTED_CLASS.fields)

    #end
        when:
        #grRenderMethodCall($method,$TESTED_CLASS.name)

        then:
#if($method.hasReturn())        #grRenderAssert($method)#{else}false//todo - validate something
#end
    }
#end
#end
}

#parse("TestMe Footer.java")