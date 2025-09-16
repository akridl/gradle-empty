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
        // The Gradle wrapper isn't in the expected location for GME to find it.
        // It's too late to move it since Repour has already checked for it.
        // Manually download the updated Gradle wrapper and install it to the
        // path we specified with the '-l' GME option.
        String gradleHome = "/tmp/gradle"
        File wrapperBin = new File(gme.getBaseDir(), "gradlew")
        StringBuilder sbOut = new StringBuilder()
        StringBuilder sbErr = new StringBuilder()
        Process proc = "$wrapperBin.absolutePath -g $gradleHome --version".execute()
        proc.consumeProcessOutput(sbOut, sbErr)
        proc.waitForOrKill(600000)
        if (sbOut.length() > 0) {
            log.info "$sbOut"
        }
        if (sbErr.length() > 0) {
            log.info "$sbErr"
        }
        if (proc.exitValue() != 0) {
            throw new RuntimeException("Failed to download Gradle using wrapper")
        }
        log.info "Downloaded Gradle wrapper"

        // Check that the Gradle wrapper is installed where GME is expecting it
        File wrapperHome = new File("$gradleHome/wrapper/dists/gradle-7.2-bin/2dnblmf4td7x66yl1d74lt32g/gradle-7.2")
        if (!wrapperHome.exists()) {
            throw new RuntimeException("Gradle wrapper home not found at $wrapperHome.absolutePath")
        }
        log.info "Found Gradle wrapper home $wrapperHome.absolutePath"
    }
}

new MyBuild(gme: gme).run()
