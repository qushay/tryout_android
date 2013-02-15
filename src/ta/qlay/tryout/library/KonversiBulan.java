package ta.qlay.tryout.library;

public class KonversiBulan {
	public String Konversi(String bulan) {
		String namaBulan = null;
		if (bulan.equals("01")){
			namaBulan="Januari";
		}else if (bulan.equals("02")){
			namaBulan="Februari";
		}else if (bulan.equals("03")){
			namaBulan="Maret";
		}else if (bulan.equals("04")){
			namaBulan="April";
		}else if (bulan.equals("05")){
			namaBulan="Mei";
		}else if (bulan.equals("06")){
			namaBulan="Juni";
		}else if (bulan.equals("07")){
			namaBulan="Juli";
		}else if (bulan.equals("08")){
			namaBulan="Agutus";
		}else if (bulan.equals("09")){
			namaBulan="September";
		}else if (bulan.equals("10")){
			namaBulan="Oktober";
		}else if (bulan.equals("11")){
			namaBulan="November";
		}else if (bulan.equals("12")){
			namaBulan="Desember";
		}
		return namaBulan;
	}
}
