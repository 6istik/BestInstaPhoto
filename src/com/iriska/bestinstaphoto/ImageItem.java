package com.iriska.bestinstaphoto;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/** Class to keep Image fields
 * @author iriska
 *
 */
public class ImageItem implements Parcelable, Comparable<ImageItem> {

	private String thumbnail;
	private String source;
	private int likes;

	public ImageItem(String thumbnail, String source, int likes_count) {
		this.thumbnail = thumbnail;
		this.source = source;
		this.likes = likes_count;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
	public int getLikesCount() {
		return likes;
	}

	@Override
	public String toString() {
		return (this.thumbnail);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(thumbnail);
		dest.writeString(source);
		dest.writeInt(likes);
	}

	public static final Parcelable.Creator<ImageItem> CREATOR = new Parcelable.Creator<ImageItem>() {
		public ImageItem createFromParcel(Parcel in) {
			return new ImageItem(in);
		}

		public ImageItem[] newArray(int size) {
			return new ImageItem[size];
		}
	};

	private ImageItem(Parcel parcel) {
		thumbnail = parcel.readString();
		source = parcel.readString();
		likes=parcel.readInt();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ImageItem img) {
		return likes - img.likes;
	}

}