package org.bhajanbook.service;

public class BhajanLyricsVO extends BhajanTitleVO {
	private String lyrics;

	private String lang;

	private String raaga;

	private String beat;

	private String audioFilePath;

	public BhajanLyricsVO() {
		initialize();
	}

	public String getAudioFilePath() {
		return audioFilePath;
	}

	public String getBeat() {
		return beat;
	}

	public String getLang() {
		return lang;
	}

	public String getLyrics() {
		return lyrics;
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
	}
	public void setAudioFilePath(String audioFilePath) {
		this.audioFilePath = audioFilePath;
	}
	public void setBeat(String beat) {
		this.beat = beat;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
	
	public void setRaaga(String raaga) {
		this.raaga = raaga;
	}
}

