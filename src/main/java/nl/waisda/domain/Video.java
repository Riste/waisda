/*  This file is part of Waisda 

    Copyright (c) 2012 Netherlands Institute for Sound and Vision
    https://github.com/beeldengeluid/waisda
	
    Waisda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Waisda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Waisda.  If not, see <http://www.gnu.org/licenses/>.
*/

package nl.waisda.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;

@Entity
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Basic
	private String title;

	/** Duration in ms. */
	@Column(nullable = false)
	private int duration;

	@Basic
	@Size(max = 1024)
	@Column(length = 1024)
	private String imageUrl;

	@Basic
	private boolean enabled;

	@Formula("(SELECT COUNT(*) FROM Game g WHERE g.video_id = id)")
	private int timesPlayed;

	@Enumerated(EnumType.STRING)
	private PlayerType playerType;

	@Basic
	private String fragmentID;

	/** Start time within episode, in ms. */
	@Basic
	private Integer startTime;

	/** Fragmentenrubriek zoals in MBH dump. */
	private Integer sectionNid;

	@Size(max = 1024)
	@Column(length = 1024)
	private String sourceUrl;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getDuration() {
		return duration;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getTimesPlayed() {
		return timesPlayed;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public String getFragmentID() {
		return fragmentID;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public Integer getSectionNid() {
		return sectionNid;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public String getPrettyDuration() {
		return TagEntry.getFriendlyTime(duration);
	}

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void setFragmentID(String fragmentID) {
        this.fragmentID = fragmentID;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public void setSectionNid(Integer sectionNid) {
        this.sectionNid = sectionNid;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("Video");
        sb.append("{id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", duration=").append(duration);
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", timesPlayed=").append(timesPlayed);
        sb.append(", playerType=").append(playerType);
        sb.append(", fragmentID='").append(fragmentID).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", sectionNid=").append(sectionNid);
        sb.append(", sourceUrl='").append(sourceUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}