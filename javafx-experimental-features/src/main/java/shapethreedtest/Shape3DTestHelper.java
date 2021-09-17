package shapethreedtest;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import org.apache.logging.log4j.Logger;

public final class Shape3DTestHelper {

    private Shape3DTestHelper() {
    }

    public static void logWarningIf3DNotSupported(Logger logger) {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            final var WARNING_MESSAGE = """
                     
                     *************************************************************
                     *    WARNING: common conditional SCENE3D isn't supported    *
                     *************************************************************
                     """;
            logger.error(WARNING_MESSAGE);
        }
    }
}
