import groovy.util.logging.Slf4j
import org.commonjava.maven.ext.core.groovy.GMEBaseScript
import org.commonjava.maven.ext.core.groovy.InvocationPoint
import org.commonjava.maven.ext.core.groovy.InvocationStage
import org.jboss.gm.common.groovy.BaseScript

@InvocationPoint(invocationPoint = InvocationStage.FIRST)
@GMEBaseScript BaseScript gme
@Slf4j
class MyBuild {

    BaseScript gme

    def run() {
        log.info('Running custom script before GME is being run')
    }
}

new MyBuild(gme: gme).run()
