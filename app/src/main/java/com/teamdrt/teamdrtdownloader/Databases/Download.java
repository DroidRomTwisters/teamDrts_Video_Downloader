package com.teamdrt.teamdrtdownloader.Databases;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "download_table")
public class Download {

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "timestamp")
    private  Long timestamp;

    @ColumnInfo(name = "total_size")
    private Long totalsize;

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "downloaded_percent")
    private Double downloadedPercent;

    @ColumnInfo(name ="downloaded_size" )
    private Long downloadSize;

    @ColumnInfo(name = "downloaded_path")
    private String downoadedPath;

    @ColumnInfo(name = "media_type")
    private String mediaType;

    public Download(String title, Long timestamp, Double downloadedPercent) {
        this.title = title;
        this.timestamp = timestamp;
        this.downloadedPercent = downloadedPercent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getTotalsize() {
        return totalsize;
    }

    public Long getId() {
        return id;
    }

    public Double getDownloadedPercent() {
        return downloadedPercent;
    }

    public Long getDownloadSize() {
        return downloadSize;
    }

    public String getDownoadedPath() {
        return downoadedPath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalsize(Long totalsize) {
        this.totalsize = totalsize;
    }

    public void setDownloadedPercent(Double downloadedPercent) {
        this.downloadedPercent = downloadedPercent;
    }

    public void setDownloadSize(Long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public void setDownoadedPath(String downoadedPath) {
        this.downoadedPath = downoadedPath;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
