package nl.waisda.controllers.api;

import nl.waisda.domain.TagEntry;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.Assert;

/**
 * Data transfer object used by the ExportTagsController. TagDTO represents the information about each tag entry that is
 * exposed to the outside world. It features the tag, the time when the tag was entered from the beginning of the video,
 * the id of the tag, and the id the of the player that entered the tag.
 */
final class TagDTO {

    private final String tag;
    private final long videoTime;
    private final long tagId;
    private final long playerID;

    private TagDTO(String tag, long videoTime, long tagId, long playerID) {
        Assert.notNull(tag);
        this.tag = tag;
        this.videoTime = videoTime;
        this.tagId = tagId;
        this.playerID = playerID;
    }

    public static TagDTO from(TagEntry tagEntry){
        return new TagDTO(tagEntry.getTag(), tagEntry.getGameTime(), tagEntry.getId(), tagEntry.getOwner().getId());
    }

    public String getTag() {
        return tag;
    }

    public long getVideoTime() {
        return videoTime;
    }

    @JsonProperty("tag_id")
    public long getTagId() {
        return tagId;
    }

    @JsonProperty("player_id")
    public long getPlayerID() {
        return playerID;
    }
}
