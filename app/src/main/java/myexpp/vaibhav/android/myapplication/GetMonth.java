package myexpp.vaibhav.android.myapplication;

public class GetMonth {
	public String getMonth(int month) {
		if (month == 0)
			return "JANUARY";
		else if (month == 1)
			return "FEBRUARY";
		else if (month == 2)
			return "MARCH";
		else if (month == 3)
			return "APRIL";
		else if (month == 4)
			return "MAY";
		else if (month == 5)
			return "JUNE";
		else if (month == 6)
			return "JULY";
		else if (month == 7)
			return "AUGUST";
		else if (month == 8)
			return "SEPTEMBER";
		else if (month == 9)
			return "OCTOBER";
		else if (month == 10)
			return "NOVEMBER";
		else if (month == 11)
			return "DECEMBER";
		return null;
	}

	public String getMonthInNumeric(String month) {
		if (month.equals("JANUARY"))
			return "01";
		else if (month.equals("FEBRUARY"))
			return "02";
		else if (month.equals("MARCH"))
			return "03";
		else if (month.equals("APRIL"))
			return "04";
		else if (month.equals("MAY"))
			return "05";
		else if (month.equals("JUNE"))
			return "06";
		else if (month.equals("JULY"))
			return "07";
		else if (month.equals("AUGUST"))
			return "08";
		else if (month.equals("SEPTEMBER"))
			return "09";
		else if (month.equals("OCTOBER"))
			return "10";
		else if (month.equals("NOVEMBER"))
			return "11";
		else if (month.equals("DECEMBER"))
			return "12";
		else
			return null;
	}
}
