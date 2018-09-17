package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {



	public static void main(String[] args) throws FileNotFoundException, IOException , ParseException{
	
		Set<Logs> firstPersonLogs = new HashSet<Logs>(); 
		Set<Logs> secondPersonLogs = new HashSet<Logs>();
		Set<Logs> thirdPersonLogs = new HashSet<Logs>();
		
		firstPersonLogs = readLogs(args[0], firstPersonLogs);
		secondPersonLogs = readLogs(args[1], secondPersonLogs);
		thirdPersonLogs = readLogs(args[2], thirdPersonLogs);
		
		
		Set<String> favouriteTypeFirst = new HashSet<String>();
		Set<String> favouriteTypeSecond = new HashSet<String>();
		Set<String> favouriteTypeThird = new HashSet<String>();
		
		//1.
		//  select name 
	    //  from firstPersonLogs, secondPersonLogs , thirdPersonLogs
		// where firstPersonLogs.name = secondPersonLogs.name = thirdPersonLogs.name
		//  order by date desc
		
		//2.
		double firstPersonAverageWatchedMovie = persentage(firstPersonLogs);
		double secondPersonAverageWatchedMovie = persentage(secondPersonLogs);
		double thirdPersonAverageWatchedMovie = persentage(thirdPersonLogs);
		
		//3.
		favouriteTypeFirst = favouriteTypes(firstPersonLogs);
		favouriteTypeSecond = favouriteTypes(secondPersonLogs);
		favouriteTypeThird = favouriteTypes(thirdPersonLogs);
		
		//4.
		Set<String> commonTypesTwo = new HashSet<>(); 
		Set<String> commonTypes = findCommonTypes(favouriteTypeFirst,favouriteTypeSecond);
		commonTypes = findCommonTypes(commonTypes,favouriteTypeThird);
		int i = 0; 
		for(String index : commonTypes ) {
			i++;
			if(i<=2) {
				commonTypesTwo.add(index);				
			}
		}
		
		double idealLength = (firstPersonAverageWatchedMovie + secondPersonAverageWatchedMovie + thirdPersonAverageWatchedMovie)/3;
	    int intIdealLenth = (int)idealLength;
	}
			

	public static Set<Logs> readLogs(String path, Set<Logs> personLogs)  throws FileNotFoundException, IOException , ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		
		while ((line = br.readLine()) != null && !line.isEmpty()) { 
			Logs log = new Logs();
			String regex = "^(\\d+)/(\\d+)/(\\d+) (.*) (\\d+)min (\\d+)min (\\S+)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(line);

			StringBuilder result = new StringBuilder();
				while (matcher.find()) {    
					Date date = formatter.parse(matcher.group(0));
					log.setDate(date);
					log.setName(matcher.group(1));
					log.setMinutesWatched(matcher.group(2));
					log.setMinutedDuration(matcher.group(3));
					log.setType(matcher.group(4));
				}
				personLogs.add(log);
		}
		br.close(); 
		return personLogs;
	}
	
	public static double persentage(Set<Logs> personLogs) {
		
		double sum = 0;
		for(Logs log : personLogs) {
			int minutesWatched = Integer.parseInt(log.getMinutesWatched().
										substring(0, log.getMinutesWatched().length() - 3));
			int getMinutedDuration = Integer.parseInt(log.getMinutedDuration().
					substring(0, log.getMinutedDuration().length() - 3));
			
			sum = sum + minutesWatched/getMinutedDuration;
		}
			
		return sum/personLogs.size();
	}
	
	
	public static Set<String> favouriteTypes(Set<Logs> personLogs) {
		
		Set<String> favouriteType = new HashSet<String>();
		for(Logs log : personLogs) {
			int minutesWatched = Integer.parseInt(log.getMinutesWatched().
										substring(0, log.getMinutesWatched().length() - 3));
			int getMinutedDuration = Integer.parseInt(log.getMinutedDuration().
					substring(0, log.getMinutedDuration().length() - 3));
			
			if(minutesWatched/getMinutedDuration > 0.6) {
				
				favouriteType.add(log.getType());
				
			}
			
		}
			
		return favouriteType;
	}
	
	
	public static Set<String> findCommonTypes(Set<String> fistList, Set<String> secondList) {
		Set<String> commonElements = new HashSet<>();
        for(String a : fistList) {
            for(String b : secondList) {
                    if(a.equals(b)) {  
                    //Check if the list already contains the common element
                        if(!commonElements.contains(a)) {
                            //add the common element into the list
                            commonElements.add(a);
                        }
                    }
            }
        }
        return commonElements;
    }
	
	
	
}
