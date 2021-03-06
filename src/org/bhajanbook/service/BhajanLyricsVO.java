package org.bhajanbook.service;

public class BhajanLyricsVO extends BhajanTitleVO {
	private String lyrics;
	private String meaning;
	private String lang;
	private String raaga;
	private String beat;
	private String audioFilePath;
	private String favorite;
	private String shruti;

	// Todo: Add date added.
	public BhajanLyricsVO() {
		initialize();
	}

	public String getAudioFilePath() {
		return audioFilePath;
	}

	public String getBeat() {
		return beat;
	}

	public String getFavorite() {
		return favorite;
	}

	public String getLang() {
		return lang;
	}

	public String getLyrics() {
		return lyrics;
	}
	
	public String getMeaning() {
		return meaning;
	}

	public String getRaaga() {
		return raaga;
	}


	private void initialize() {
		lyrics = "";
		lang = "";
		raaga = "";
		beat = "";
		audioFilePath = "";
		favorite = "";
	}

	public void setAudioFilePath(String audioFilePath) {
		this.audioFilePath = audioFilePath;
	}

	public void setBeat(String beat) {
		this.beat = beat;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public void setRaaga(String raaga) {
		this.raaga = raaga;
	}

}

