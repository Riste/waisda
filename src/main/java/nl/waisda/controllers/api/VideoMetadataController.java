package nl.waisda.controllers.api;

import nl.waisda.domain.Video;
import nl.waisda.repositories.VideoRepository;
import nl.waisda.validators.VideoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller that provides REST API for managing video metadata.
 */
@Controller
public class VideoMetadataController extends AbstractAPIController {

    @Autowired
    private VideoValidator videoValidator;

    @Autowired
    private VideoRepository videoRepository;

    /**
     * Adds video in the system. There should not be a video with the same fragmentID present already. Otherwise,
     * IllegalArgumentException is thrown. Only admin users can execute this operation.
     * @param video to be added to the system
     */
    @RequestMapping(value = "/api/videos", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void addVideo(@RequestBody() Video video) {
        checkIfAdmin();
        checkValid(videoValidator, video, "video");
        if(videoRepository.existsWithFragmentID(video.getFragmentID()))
            throw new IllegalArgumentException(String.format("Video with fragmentID %s already exists",
                    video.getFragmentID()));
        videoRepository.store(video);
    }

    /**
     * Updates the metadata for an existing video. If the video does not exist in the system already (as identified by
     * fragmentID) IllegalArgumentException is thrown. Only admin users can execute this operation.
     * @param video
     */
    @RequestMapping(value = "/api/videos", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void updateVideo(@RequestBody() Video video) {
        checkIfAdmin();
        checkValid(videoValidator, video, "video");
        if(!videoRepository.existsWithFragmentID(video.getFragmentID()))
            throw new IllegalArgumentException(String.format("Video with fragmentID %s does not exists",
                    video.getFragmentID()));
        Video persisted = videoRepository.findByFragmentID(video.getFragmentID());
        video.setId(persisted.getId());
        videoRepository.store(video);
    }

    /**
     * Enables/disables existing video for tagging in Waisda?. If the video does not exist in the system already (as
     * identified by fragmentID) IllegalArgumentException is thrown. Only admin users can execute this operation.
     * @param command object which encapsulates the fragmentID and enable/disable status
     */
    @RequestMapping(value = "/api/videos/enabled", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void enabled(@RequestBody() EnabledVideoCommand command){
        checkIfAdmin();
        Video video = videoRepository.findByFragmentID(command.getFragmentID());
        if(video == null)
            throw new IllegalArgumentException(String.format("Video with fragmentID %s does not exist",
                    command.getFragmentID()));
        video.setEnabled(command.isEnabled());
        videoRepository.store(video);
    }

}
