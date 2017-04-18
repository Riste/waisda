package nl.waisda.controllers.api;

import nl.waisda.domain.TagEntry;
import nl.waisda.domain.Video;
import nl.waisda.repositories.TagEntryRepository;
import nl.waisda.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Controller that provides functionality for exporting the tags collected with Waisda?.
 */
@Controller
public class ExportTagsController extends AbstractAPIController {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TagEntryRepository tagEntryRepository;

    /**
     * Using this method all tags for a specific video can be exported in JSON format. The data being exported can
     * be limited to tag entries after a certain timestamp, enabling efficient sync mechanisms. If a video with the
     * specified fragmentID does not exist IllegalArgumentException is thrown. Only admin users can run this operation.
     * @param fragmentID of the video
     * @param timestamp (optional). All tags entered after the timestamp are returned. If this parameter is null all
     *                  tags entered for the video are returned.
     * @return JSON string representation of the list of TagDTO objects ordered by creation time in ascending order.
     */
    @RequestMapping(value = "/api/videos/tags/export", method = RequestMethod.GET)
    @Transactional
    @ResponseBody
    public String export(@RequestParam() String fragmentID, @RequestParam(required = false) Long timestamp){
        checkIfAdmin();
        Date timestampDate = timestamp == null? null: new Date(timestamp);
        Video video = videoRepository.findByFragmentID(fragmentID);
        if(video == null)
            throw new IllegalArgumentException(String.format("Video with fragmentID %s does not exist", fragmentID));
        List<TagEntry> tagEntries = tagEntryRepository.getTagsForVideoEnteredAfter(video.getId(), timestampDate);
        List<TagDTO> tagDTOs = new ArrayList<TagDTO>(tagEntries.size());
        for(TagEntry tagEntry: tagEntries)
            tagDTOs.add(TagDTO.from(tagEntry));
        return serialize(tagDTOs);
    }



}
