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

    public Download(String title, Long timestamp, Long totalsize, Double downloadedPercent, Long downloadSize, String downoadedPath, String mediaType) {
        this.title = title;
        this.timestamp = timestamp;
        this.totalsize = totalsize;
        this.downloadedPercent = downloadedPercent;
        this.downloadSize = downloadSize;
        this.downoadedPath = downoadedPath;
        this.mediaType = mediaType;
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

}
