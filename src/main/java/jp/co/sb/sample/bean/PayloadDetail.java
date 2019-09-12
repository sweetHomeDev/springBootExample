package jp.co.sb.sample.bean;

import lombok.Data;

@Data
public class PayloadDetail {
	public String year;
	public String month;
	public String day;
	public String time;
	public String msn;
	public String pref;
	public String city;
	public String oaza;
	public String aza;
	public String banchi;
	public String go;
	public String kana_pref;
	public String kana_city;
	public String kana_oaza;
	public String kana_aza;
	public String kana_banchi;
	public String kana_go;
	public String kana_building;
	public String error_range;
	public String latitude;
	public String longitude;
}
