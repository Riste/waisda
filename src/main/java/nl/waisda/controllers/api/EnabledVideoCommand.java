package nl.waisda.controllers.api;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.Assert;


/**
 * Command object used by the VideoMetatadataController. It encapsulates the fragmentID of the video that is acted on
 * and the enable/disable status of the video.
 */
final class EnabledVideoCommand {

    private final String fragmentID;
    private final boolean enabled;

    @JsonCreator
    public EnabledVideoCommand(@JsonProperty("fragmentID") String fragmentID,
                               @JsonProperty("enabled") boolean enabled) {
        Assert.notNull(fragmentID, "fragmentID must not be null");
        this.fragmentID = fragmentID;
        this.enabled = enabled;
    }

    public String getFragmentID() {
        return fragmentID;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
